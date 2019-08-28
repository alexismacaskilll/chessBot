package experiment;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

import cse332.chess.interfaces.AbstractSearcher;
import cse332.chess.interfaces.Board;
import cse332.chess.interfaces.Evaluator;
import cse332.chess.interfaces.Move;

public class JamboreeSearcher<M extends Move<M>, B extends Board<M, B>> extends
        AbstractSearcher<M, B> {
	
	private static final int DIVIDE_CUTOFF = 2;
	private static final double PERCENTAGE_SEQUENTIAL = 0.5;
	private static final ForkJoinPool POOL = new ForkJoinPool();
	

    public M getBestMove(B board, int myTime, int opTime) {
    	int cutOff = cutoff;
    	BestMove<M> best = jamboree(this.evaluator, board, ply, Integer.MIN_VALUE + 1, Integer.MAX_VALUE, cutOff, POOL);
        return best.move;
    }
    
    
    public M getBestMove(B board, int myTime, int opTime, ForkJoinPool pool) {
    	int cutOff = cutoff;
    	BestMove<M> best = jamboree(this.evaluator, board, ply, Integer.MIN_VALUE + 1, Integer.MAX_VALUE, cutOff, pool);
        return best.move;
    }
    
    static <M extends Move<M>, B extends Board<M, B>> BestMove<M> jamboree(Evaluator<B> evaluator, B board, int depth, int alpha, int beta, int cut, ForkJoinPool pool) {
    	List<M> moves = board.generateMoves();
    	return pool.invoke(new DivideTask<M, B>(depth, board, evaluator, (int) (PERCENTAGE_SEQUENTIAL * moves.size()), moves.size(), moves, cut, alpha, beta, null));
    }
    
    private static class DivideTask<M extends Move<M>, B extends Board<M, B>> extends RecursiveTask<BestMove<M>>  {
        int depth;
        B board;
        Evaluator<B> evaluator;
        int lo;
        int hi;
        List<M> moves;
        int cut;
        int alpha;
        int beta;
        M move;
        
        public DivideTask(int depth, B board, Evaluator<B> evaluator, int lo, int hi, List<M> moves, int cut, int alpha, int beta, M move) {
            this.depth = depth;
            this.board = board;
            this.evaluator = evaluator;
            this.lo = lo;
            this.hi = hi;
            this.moves = moves;
            this.cut = cut;
            this.alpha = alpha;
            this.beta = beta;
            this.move = move;
            
        }
        
        @Override
        protected BestMove<M> compute() {
        	//copy board in children
        	B boardCopy = board;
        	
        	//since we copy board in children, then we haven't applied the move 
        	if (move != null) {
        		//countNode.countPara.increment();
        		boardCopy = boardCopy.copy();
        		boardCopy.applyMove(move);
        		moves = boardCopy.generateMoves(); // were doing this even when we call alpha beta but its not needed. 
        		lo = (int) (PERCENTAGE_SEQUENTIAL * moves.size());
        		hi = moves.size();
        	} 
        		
        	//if its a leaf (stalemate or checkmate)
        	if (moves.isEmpty()) {
        		//do we pass integer min value or alpha
        		BestMove<M> bestMove = new BestMove<M>(Integer.MIN_VALUE);
        		if (boardCopy.inCheck()) {
        			bestMove.value = -evaluator.mate() - depth;
        		} else {
        			bestMove.value = -evaluator.stalemate();
        		}
        		return bestMove; 
        		//do alpha beta if depth is less than cutoff
        	} else if (depth <= cut) { 
        		return AlphaBetaSearcher.alphaBeta(evaluator, boardCopy, depth, alpha, beta);
        	} 
        	BestMove<M> bestMove = new BestMove<M>(alpha);
        	//so we dont do sequential more than we need to, we could use a boolean here maybe. 
        	if (lo == (int) (PERCENTAGE_SEQUENTIAL * moves.size()) && hi == moves.size()) {
        		//doing the compute and updating alpha and beta one after the other, not all at once. 
        		for (int i = 0; i < (int) (PERCENTAGE_SEQUENTIAL * moves.size()); i++) {
        			boardCopy.applyMove(moves.get(i));
        			//countNode.countPara.increment();
        			List<M> nextMove = boardCopy.generateMoves();
        			DivideTask<M, B> task = new DivideTask<M, B>(depth - 1, boardCopy, evaluator, (int)(PERCENTAGE_SEQUENTIAL * nextMove.size()), nextMove.size(), nextMove, cut, -beta, -alpha, null);
        			BestMove<M> currMove = task.compute().negate();
        			boardCopy.undoMove();
       		 		currMove.move = moves.get(i);
       		 
	       		 	if (currMove.value > bestMove.value) {
	       		 		bestMove = currMove;
	       		 		alpha = currMove.value;
	       		 	}
  
	       		 	if (alpha >= beta) {
	       		 		bestMove.value = alpha;
	       		 		return bestMove; 
	       		 	}
        		}
        	}
        	//when we stop dividing and fork sequentially
        	if (hi - lo  <= DIVIDE_CUTOFF ) {
        		if (hi -lo >= 1) {
        			DivideTask<M,B>[] tasks = (DivideTask<M,B>[])new DivideTask[hi - lo];
            		for (int i = lo; i < hi; i++) {
            			//countNode.countPara.increment();
            			tasks[i - lo] = new DivideTask<M, B>(depth - 1, boardCopy, evaluator, 0, 0, null, cut, -beta, -alpha, moves.get(i));	
            			if (i < hi - 1) {
            				tasks[i - lo].fork();
            			}
            		}
            		BestMove<M> currMove = tasks[hi - lo - 1].compute().negate();
        			currMove.move = moves.get(hi - 1);
            		for (int i = lo - 1; i < hi - 1; i++) {
            			if (i != lo - 1) {
            				currMove = tasks[i - lo].join().negate();
            				currMove.move = moves.get(i);
            			}
            			if (currMove.value > bestMove.value) {
                  			 bestMove = currMove;
                  			 alpha = currMove.value;
                  		 }
            			if (alpha >= beta) {
                			 bestMove.value = alpha;
                			 return bestMove; 
                		}
            		}
        		}
        	} else {
        		//divide and conquer
        		int mid = lo + (hi - lo) / 2;
        		DivideTask<M, B> left = new DivideTask<M, B>(depth, boardCopy, evaluator, lo, mid, moves, cut, alpha, beta, null);
        		DivideTask<M, B> right = new DivideTask<M, B>(depth, boardCopy, evaluator, mid, hi, moves, cut, alpha, beta, null);
        		left.fork();
        		BestMove<M> rightResult = right.compute();
        		BestMove<M> leftResult = left.join();
        		BestMove<M> currMove = rightResult;
        		if (rightResult.value < leftResult.value) {
        			currMove= leftResult;
        		} 
        		if (currMove.value > bestMove.value) {
         			 bestMove = currMove;
         			 alpha = currMove.value;
         		 }
    
         		 if (alpha >= beta) {
         			 bestMove.value = alpha;
         			 return bestMove; 
         		 }
        	}
        	return bestMove;
        }
    }
}
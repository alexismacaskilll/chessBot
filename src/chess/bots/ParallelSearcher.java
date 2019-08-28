package chess.bots;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

import cse332.chess.interfaces.AbstractSearcher;
import cse332.chess.interfaces.Board;
import cse332.chess.interfaces.Evaluator;
import cse332.chess.interfaces.Move;


public class ParallelSearcher<M extends Move<M>, B extends Board<M, B>> extends
        AbstractSearcher<M, B> {

	private static final int DIVIDE_CUTOFF = 2;
	private static final ForkJoinPool POOL = new ForkJoinPool();

    public M getBestMove(B board, int myTime, int opTime) {
    	int cutOff = cutoff;
    	return minimax(this.evaluator, board, ply, cutOff).move;
    }
    
    static <M extends Move<M>, B extends Board<M, B>> BestMove<M> minimax(Evaluator<B> evaluator, B board, int depth, int cut) {
    	List<M> moves = board.generateMoves();
    	return POOL.invoke(new DivideTask<M, B>(depth, board, evaluator, 0, moves.size(), moves, cut));
    }
    
    private static class DivideTask<M extends Move<M>, B extends Board<M, B>> extends RecursiveTask<BestMove<M>>  {
        int depth;
        B board;
        Evaluator<B> evaluator;
        int lo;
        int hi;
        List<M> moves;
        int cut;
        
        public DivideTask(int depth, B board, Evaluator<B> evaluator, int lo, int hi, List<M> moves, int cut) {
            this.depth = depth;
            this.board = board;
            this.evaluator = evaluator;
            this.lo = lo;
            this.hi = hi;
            this.moves = moves;
            this.cut = cut;
        }
        
        @Override
        protected BestMove<M> compute() {
        	if (moves.isEmpty()) {
        		BestMove<M> bestMove = new BestMove<M>(Integer.MIN_VALUE);
        		if (board.inCheck()) {
        			bestMove.value = -evaluator.mate() - depth;
        		} else {
        			bestMove.value = -evaluator.stalemate();
        		}
        		return bestMove; 
        	} else if (depth <= cut) {
        		// we no longer search for bestMove with a parallel algorithm, here it is fully
        		// sequential 
        		// why we don't need the checks for stalemate here is because we do it in simple
        		// searcher. 
        		return SimpleSearcher.minimax(evaluator, board, depth);
        	} else if (hi - lo  <= DIVIDE_CUTOFF) {
        		//list of possible board states within lo-hi
        		DivideTask<M,B>[] tasks = (DivideTask<M,B>[])new DivideTask[hi - lo];
        		// hi  - lo <= divide_cutoff, so then we fork times hi - lo times which is <=
        		// divide_cutoff number of threads. 
        		for (int i = lo; i < hi; i++) {
        			B boardCopy = board.copy();	
        			boardCopy.applyMove(moves.get(i));
        			//for each board state, we apply a move then get all the possible moves that 
        			// are possible after applying that move
        			List<M> nextMove = boardCopy.generateMoves();
        			//then we call new divide task with that board with the new move and the new generated moves
        			//it will put the bestMove for that board in tasks[i-lo]
        			//this is calling compute on EVERY children task, but we only need to call on one 
        			tasks[i - lo] = new DivideTask<M, B>(depth - 1, boardCopy, evaluator, 0, nextMove.size(), nextMove,cut);	
        			// then we fork because like the tic tac toe example, you want to 
        			// fork everything except the one thread (the one you can call compute) to do yourself
        			if (i < hi - 1) {
        				tasks[i - lo].fork();
        			}
        		}
        		BestMove<M> bestMove = tasks[hi- lo - 1].compute().negate();
        		bestMove.move = moves.get(hi - 1);
        		for (int i = lo; i < hi - 1; i++) {
        			//then we want to take the bestMove thats in tasks[i - lo], we have to call join
        			//to make sure its actually there / finished parallel task, then we check if its 
        			//better than the best move, if it is we return it. 
        			BestMove<M> currMove = tasks[i - lo].join().negate();
        			currMove.move = moves.get(i);
        			if (currMove.value > bestMove.value) {
        				bestMove = currMove;
        			}
        		}
        		return bestMove;
        	} 
        	int mid = lo + (hi - lo) / 2;
        	DivideTask<M, B> left = new DivideTask<M, B>(depth, board, evaluator, lo, mid, moves, cut);
        	DivideTask<M, B> right = new DivideTask<M, B>(depth, board, evaluator, mid, hi, moves, cut);
        	left.fork();
        	BestMove<M> rightResult = right.compute();
        	BestMove<M> leftResult = left.join();
        	if (rightResult.value > leftResult.value) {
        		return rightResult;
        	} else {
        		return leftResult;
        	}
        }
    } 
}
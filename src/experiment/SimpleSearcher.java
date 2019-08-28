package experiment;

import java.util.List;

import cse332.chess.interfaces.AbstractSearcher;
import cse332.chess.interfaces.Board;
import cse332.chess.interfaces.Evaluator;
import cse332.chess.interfaces.Move;
import cse332.exceptions.NotYetImplementedException;

/**
 * This class should implement the minimax algorithm as described in the
 * assignment handouts.
 */
public class SimpleSearcher<M extends Move<M>, B extends Board<M, B>> extends
        AbstractSearcher<M, B> {

    public M getBestMove(B board, int myTime, int opTime) {
        /* Calculate the best move */
        BestMove<M> best = minimax(this.evaluator, board, ply);
        return best.move;
    }

    static <M extends Move<M>, B extends Board<M, B>> BestMove<M> minimax(Evaluator<B> evaluator, B board, int depth) {
    	if (depth == 0) {
    		return new BestMove<M>(evaluator.eval(board));
    	}
    	BestMove<M> bestMove = new BestMove<M>(Integer.MIN_VALUE);
    	List<M> moves = board.generateMoves();
    	if (moves.isEmpty()) {
    		if (board.inCheck()) {
    			bestMove.value = -evaluator.mate() - depth;
    		} else {
    			bestMove.value = -evaluator.stalemate();
    		}
    		return bestMove; 
    	} 
    	for (M move : moves) {
    		//countNode.count++;
        	//countNode.countPara.increment();
    		 board.applyMove(move);
    		 //countNode.count++;
    		 //countNode.countPara.increment();
    		 BestMove<M> currMove = minimax(evaluator, board, depth - 1).negate();
    		 currMove.move = move;
    		 board.undoMove();
    		 if (currMove.value > bestMove.value) {
    			 bestMove = currMove;
    		 }
    	}
    	return bestMove;

    }
}
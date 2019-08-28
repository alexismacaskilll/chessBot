package experiment;

import java.util.ArrayList;
import java.util.List;

import chess.board.ArrayBoard;
import chess.board.ArrayMove;
import chess.bots.LazySearcher;
import chess.bots.SimpleSearcher;
import chess.game.SimpleEvaluator;
import cse332.chess.interfaces.Move;
import cse332.chess.interfaces.Searcher;

public class TestGame {
    public Searcher<ArrayMove, ArrayBoard> whitePlayer;
    public Searcher<ArrayMove, ArrayBoard> blackPlayer;
    public static final String STARTING_POSITION = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    
    private ArrayBoard board;
    

    public TestGame() {
        setupWhitePlayer(new AlphaBetaSearcher<ArrayMove, ArrayBoard>(), 4, 4);
        setupBlackPlayer(new SimpleSearcher<ArrayMove, ArrayBoard>(), 3, 3);
    }
    
    public List<String> play() {
       this.board = ArrayBoard.FACTORY.create().init(STARTING_POSITION);
       Searcher<ArrayMove, ArrayBoard> currentPlayer = this.blackPlayer;
       
       int turn = 0;
       
       /* Note that this code does NOT check for stalemate... */
       List<String> fens = new ArrayList<String>();
       while (!board.inCheck() || board.generateMoves().size() > 0) {
           currentPlayer = currentPlayer.equals(this.whitePlayer) ? this.blackPlayer : this.whitePlayer;
           fens.add(board.fen());
           this.board.applyMove(currentPlayer.getBestMove(board, 1000, 1000));
           turn++;
       }
       return fens;
    }
    
    public Searcher<ArrayMove, ArrayBoard> setupPlayer(Searcher<ArrayMove, ArrayBoard> searcher, int depth, int cutoff) {
        searcher.setDepth(depth);
        searcher.setCutoff(cutoff);
        searcher.setEvaluator(new SimpleEvaluator());
        return searcher; 
    }
    public void setupWhitePlayer(Searcher<ArrayMove, ArrayBoard> searcher, int depth, int cutoff) {
        this.whitePlayer = setupPlayer(searcher, depth, cutoff);
    }
    public void setupBlackPlayer(Searcher<ArrayMove, ArrayBoard> searcher, int depth, int cutoff) {
        this.blackPlayer = setupPlayer(searcher, depth, cutoff);
    }
}

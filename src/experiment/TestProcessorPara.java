package experiment;

import java.util.List;
import java.util.concurrent.ForkJoinPool;

import chess.board.ArrayBoard;
import chess.board.ArrayMove;
import chess.game.SimpleEvaluator;

public class TestProcessorPara {
	private static final int NUM_TESTS = 20;
	private static final int NUM_WARMUP = 10;
	private static final int cutOff = 3;
	
	public static void main(String[] args) {
		TestGame games = new TestGame();
		List<String> fens = games.play();
		String start = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
		String mid = fens.get(40);
		String end = fens.get(fens.size() - 6);
		
		for (int i = 1; i <=32; i++) {
			System.out.println(i + " " + timing(i, start));
		}
		
		for (int i = 1; i <=32; i++) {
			System.out.println(i + " " + timing(i, mid));
		}
		
		for (int i = 1; i <=32; i++) {
			System.out.println(i + " " + timing(i, end));
		}
	}
	
	private static double timing(int num, String board) {
		ForkJoinPool pool = new ForkJoinPool(num);
		ParallelSearcher<ArrayMove, ArrayBoard> searcher = new ParallelSearcher<ArrayMove, ArrayBoard>();
		searcher.setDepth(5);
        searcher.setCutoff(cutOff);
        searcher.setEvaluator(new SimpleEvaluator());

		double totalTime = 0;
		for (int i = 0; i < NUM_TESTS; i++) {
			long startTime = System.currentTimeMillis();
			searcher.getBestMove(ArrayBoard.FACTORY.create().init(board), 0, 0, pool);
			long endTime = System.currentTimeMillis();
		 	if (NUM_WARMUP <= i) { // Throw away first NUM_WARMUP runs to exclude JVM warmup
		 		totalTime += (endTime - startTime);
		 	}
		}
		
		double averageRuntime = totalTime / (NUM_TESTS - NUM_WARMUP);
		return averageRuntime;
	}
}

package experiment;
import java.util.List;
import java.util.Scanner;

import chess.board.ArrayBoard;
import chess.board.ArrayMove;

public class TestCutOffJamboree {
	private static final int NUM_TESTS = 20;
	private static final int NUM_WARMUP = 10;

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		System.out.print("sequential cutOff: ");
		int cutOff = input.nextInt();
		TestGame games = new TestGame();
		List<String> fens = games.play();
		String start = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
		String mid = fens.get(40);
		String end = fens.get(fens.size() - 6);
		System.out.println("start: " + time(start, cutOff));
		System.out.println("mid: " + time(mid, cutOff));
		System.out.println("end: " + time(end, cutOff));
	}
	
	private static double time(String board, int cutOff) {
		double totalTime = 0;
		for (int i = 0; i < NUM_TESTS; i++) {
			long startTime = System.currentTimeMillis();
			TestStartingPosition.getBestMove(board, new JamboreeSearcher<ArrayMove, ArrayBoard>(), 5, cutOff);
			long endTime = System.currentTimeMillis();
		 	if (NUM_WARMUP <= i) { // Throw away first NUM_WARMUP runs to exclude JVM warmup
		 		totalTime += (endTime - startTime);
		 	}
		}
		double averageRuntime = totalTime / (NUM_TESTS - NUM_WARMUP);
		return averageRuntime;
	}

}

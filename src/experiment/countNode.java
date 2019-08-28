package experiment;

import java.util.List;
import java.util.concurrent.atomic.LongAdder;

import chess.board.ArrayBoard;
import chess.board.ArrayMove;

public class countNode {
	public static long count;
	public static LongAdder countPara;
	private static final int PLY = 5;
    private static final int CUT_OFF = PLY / 2;
	
	public static void main(String[] args) {
		countPara = new LongAdder();
		long sum = 0;
		TestGame games = new TestGame();
		List<String> fens = games.play();
		for (int i = 0; i < fens.size(); i++) {
			count = 0;
			countPara.reset();
			String fen = fens.get(i);
			TestStartingPosition.getBestMove(fen, new SimpleSearcher<ArrayMove, ArrayBoard>(), PLY, CUT_OFF);
			sum = sum + count;
			//sum = sum + countPara.intValue();
			System.out.println(i + " " + count);
		}
		System.out.println((double) sum / fens.size());
	}
}

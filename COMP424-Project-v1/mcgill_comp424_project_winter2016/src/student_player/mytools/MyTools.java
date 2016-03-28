package student_player.mytools;

import java.util.ArrayList;
import java.util.Stack;

import hus.HusBoardState;
import hus.HusMove;

public class MyTools {

	final static int MAX_DEPTH = 1;
	int depth = 0;
	static int alpha = Integer.MIN_VALUE;
	static int beta = Integer.MAX_VALUE;
	
	public static ABNode getMove(HusBoardState board_state, int player_id, int opponent_id, int depth, boolean max) {
		int score = (max) ? alpha : beta;
		HusMove move;
		
		ArrayList<HusMove> moves = board_state.getLegalMoves();

		move = moves.get(0);
		for (int i = 0; i < moves.size(); i++) {
			HusBoardState cloned_board_state = (HusBoardState) board_state.clone();
			cloned_board_state.move(moves.get(i));

			if (depth == MAX_DEPTH) {
				int[][] pits = cloned_board_state.getPits();
				int diff = getPlayerTotalSeeds(pits[player_id]) - getPlayerTotalSeeds(pits[opponent_id]);
				if (max) {
					if (diff > score) {
						score = diff;
						move = moves.get(i);
					}
				} else if (!max) {
					if (diff < alpha) {
						return (new ABNode(moves.get(i), Integer.MIN_VALUE)); // pruning for min nodes
					}
					if (diff < score) {
						score = diff;
						move = moves.get(i);
					}
				}
			} else {
				ABNode node = getMove(cloned_board_state, player_id, opponent_id, depth + 1, !max);
				if (max && node.score > score || !max && node.score < score) {
					score = node.score;
					move = moves.get(i);
				}
//				else if (!max && node.score < score) {
//					score = node.score;
//					move = node.move;
//				}
			}
		}
		
		ABNode ab = new ABNode(move, score);
		
		return ab;
		
	}
	
	public static int getPlayerTotalSeeds(int[] player_pits) {
		int seeds = 0;

        for (int i = 0; i < player_pits.length; i++) {
        	seeds += player_pits[i];
        }
		
		return seeds;
	}
    
    public static class ABNode {
    	public HusMove move;
    	public int score;
    	
    	public ABNode(HusMove move, int score) {
    		this.move = move;
    		this.score = score;
    	}
    }
}

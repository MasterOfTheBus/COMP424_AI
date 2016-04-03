package student_player.mytools;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

import hus.HusBoardState;
import hus.HusMove;

public class MyTools {

	final static int MAX_ROLLOUT_DEPTH = 10;
	final static int ROLLOUTS = 50;
	static int alpha = Integer.MIN_VALUE;
	static int beta = Integer.MAX_VALUE;
	
	public static ABNode getMoveBasic(HusBoardState board_state, int player_id, int opponent_id) {
		return getMoveDepth(board_state, player_id, opponent_id, 0, true, 1);
	}
	
	public static ABNode getMoveDepth(HusBoardState board_state, int player_id, int opponent_id, int depth, boolean max, int max_depth) {
		int score = (max) ? alpha : beta;
		HusMove move;

		ArrayList<HusMove> moves = board_state.getLegalMoves();

		if (moves.size() == 0) {
			// losing situation for a player
			move = null;
		} else {
			move = moves.get(0);
		}
		for (int i = 0; i < moves.size(); i++) {
			HusBoardState cloned_board_state = (HusBoardState) board_state.clone();
			cloned_board_state.move(moves.get(i));

			if (depth == max_depth) {
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
				ABNode node = getMoveDepth(cloned_board_state, player_id, opponent_id, depth + 1, !max, max_depth);
				if (max && node.score > score || !max && node.score < score) {
					score = node.score;
					move = moves.get(i);
				}
			}
		}

		ABNode ab = new ABNode(move, score);

		return ab;

	}

	public static HusMove MonteCarloTreeSearch(HusBoardState board_state, int player_id, int opponent_id) {
		HusMove move = null;
		Random rand = new Random();

		HusBoardState cloned_board_state = (HusBoardState) board_state.clone();

		// TODO: do minimax on the tree first then do rollouts on the leaves
//		ABNode root = getMoveBasic(board_state, player_id, opponent_id);
//		board_state.move(root.move);

		// Basic Monte Carlo Rollouts
		ArrayList<HusMove> moves = cloned_board_state.getLegalMoves();
		int winsPerMove[] = new int[moves.size()];

//		System.out.println(moves.size());

		int mostWins = -1;
		for (int i = 0; i < ROLLOUTS; i++) {
			int moveNum = rand.nextInt(moves.size());
			int winner = rollout(cloned_board_state, moves.get(moveNum));
			winsPerMove[moveNum] += (winner == player_id) ? 1 : 0;
			if (winsPerMove[moveNum] > mostWins) {
				mostWins = winsPerMove[moveNum];
				move = moves.get(moveNum);
			}
		}

		return move;
	}

	public static int rollout(HusBoardState board_state, HusMove move) {
		Random rand = new Random();
		HusBoardState cloned_board_state = (HusBoardState) board_state.clone();
		cloned_board_state.move(move);
//		int depth = 0;
		while (cloned_board_state.getWinner() == HusBoardState.NOBODY/* && depth < MAX_ROLLOUT_DEPTH*/) {
			ArrayList<HusMove> moves = cloned_board_state.getLegalMoves();
			HusMove rand_move = moves.get(rand.nextInt(moves.size())); // TODO: consider back propagation with Upper Confidence Tree

			cloned_board_state.move(rand_move);
//			depth++;
		}

		return cloned_board_state.getWinner();
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

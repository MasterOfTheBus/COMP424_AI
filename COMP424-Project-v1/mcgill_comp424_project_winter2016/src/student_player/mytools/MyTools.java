package student_player.mytools;

import java.util.ArrayList;
import java.util.Random;

import hus.HusBoardState;
import hus.HusMove;

public class MyTools {

	final static int MAX_AB_DEPTH = 6;
	final static int MAX_MINIMAX_DEPTH = 5;
	final static int MAX_ROLLOUT_DEPTH = 10;
	final static int ROLLOUTS = 500;

	// ======================================= Monte Carlo (Not Used) =======================================================
	
	public static HusMove MonteCarloTreeSearch(HusBoardState board_state, int player_id, int opponent_id) {
		HusMove move = null;
		Random rand = new Random();

		HusBoardState cloned_board_state = (HusBoardState) board_state.clone();

		// TODO: use upper confidence trees

		// Basic Monte Carlo Rollouts
		ArrayList<HusMove> moves = cloned_board_state.getLegalMoves();
		int winsPerMove[] = new int[moves.size()];
		
//		int favoredMove = 0;
//		int score = 0;
//		int[][] pits = cloned_board_state.getPits();
//		// favor the move with the best short term outcome
//		for (int i = 0; i < moves.size(); i++) {
//			int diff = getPlayerTotalSeeds(pits[player_id]) - getPlayerTotalSeeds(pits[opponent_id]);
//			if (diff > score) {
//				score = diff;
//				favoredMove = i;
//			}
//		}

		int mostWins = -1;
		for (int i = 0; i < ROLLOUTS; i++) {
			int moveNum = /*(rand.nextInt() < 0.5) ? favoredMove :*/ rand.nextInt(moves.size()); // simple search
			int winner = rollout(cloned_board_state, moves.get(moveNum));
			winsPerMove[moveNum] += (winner == player_id) ? 1 : 0;
			if (winsPerMove[moveNum] > mostWins) {
				mostWins = winsPerMove[moveNum];
				move = moves.get(moveNum);
			}
		}
		
		System.out.println(mostWins);

		return move;
	}
	
//	public static ABNode AB_Search(HusBoardState board_state, int player_id, int opp_id) {
//		return MaxValues(board_state, Float.MIN_VALUE, Float.MAX_VALUE, 0, player_id, opp_id, false);
//	}
	
//	public static ABNode MonteCarlo_Search(HusBoardState board_state, int player_id, int opp_id) {
//		return MaxValues(board_state, Float.MIN_VALUE, Float.MAX_VALUE, 0, player_id, opp_id, true);
//	}
	
	// Used for monte carlo
	public static float evaluate(HusBoardState board_state, int player_id, int opp_id, boolean monte_carlo) {
		if (monte_carlo) {
			Random rand = new Random();
			float total = 50;
			int wins = 0;
			ArrayList<HusMove> moves = board_state.getLegalMoves();
			for (int i = 0; i < total; i++) {
				if (rollout(board_state, moves.get(rand.nextInt(moves.size()))) == player_id) {
					wins++;
				}
			}
			return (wins / total);
		} else {
			int pits[][] = board_state.getPits();
			return (getPlayerTotalSeeds(pits[player_id]) - getPlayerTotalSeeds(pits[opp_id]));
		}
	}

	public static int rollout(HusBoardState board_state, HusMove move) {
		Random rand = new Random();
		HusBoardState cloned_board_state = (HusBoardState) board_state.clone();
		cloned_board_state.move(move);
		while (cloned_board_state.getWinner() == HusBoardState.NOBODY) {
			ArrayList<HusMove> moves = cloned_board_state.getLegalMoves();
			HusMove rand_move = moves.get(rand.nextInt(moves.size())); // TODO: consider back propagation with Upper Confidence Tree

			cloned_board_state.move(rand_move);
		}

		return cloned_board_state.getWinner();
	}
	
	// ====================================================== Minimax (Not Used) ==================================================
	
	public static ABNode MinimaxValue(HusBoardState board_state, int depth, int player_id, int opp_id, boolean max) {
		/* Check for Endgame */
		int winner = board_state.getWinner();
		if (winner == player_id) {
			return new ABNode(null, Float.MAX_VALUE);
		} else if (winner == opp_id) {
			return new ABNode(null, Float.MIN_VALUE);
		}
		
		if (depth >= MAX_MINIMAX_DEPTH) {
			int pits[][] = board_state.getPits();
			ABNode node = new ABNode(null, getPlayerTotalSeeds(pits[player_id]));
			return node;
		}
		
		ArrayList<HusMove> moves = board_state.getLegalMoves();
		ABNode node = new ABNode(null, (max) ? Float.MIN_VALUE : Float.MAX_VALUE);
		for (int i = 0; i < moves.size(); i++) {
			HusBoardState cloned_board_state = (HusBoardState) board_state.clone();
			cloned_board_state.move(moves.get(i));
			ABNode temp_node = MinimaxValue(cloned_board_state, depth+1, player_id, opp_id, !max);
			if ((max && temp_node.score > node.score) || (!max && temp_node.score < node.score)) {
				node.score = temp_node.score;
				node.move = moves.get(i);
			}
		}
		return node;
	}
	
	// =============================================== Alpha Beta Pruning (Final Implementation) ===================================
	
	public static ABNode MaxValues(HusBoardState board_state, float alpha, float beta, int depth, int player_id, int opp_id/*, boolean monte_carlo*/) {
		ABNode node = new ABNode(null, alpha);
		
		/* Check for Endgame */
		int winner = board_state.getWinner();
		if (winner == player_id) {
			node.score = Float.MAX_VALUE;
			return node;
		} else if (winner == opp_id) {
			node.score = Float.MIN_VALUE;
			return node;
		}
		
		/* For Normal Gameplay*/
//		int test_depth = (monte_carlo) ? 2 : MAX_AB_DEPTH;
		if (depth >= MAX_AB_DEPTH/*test_depth*/) {
			int pits[][] = board_state.getPits();
			node = new ABNode(null, getPlayerTotalSeeds(pits[player_id]));
//			node = new ABNode(null, evaluate(board_state, player_id, opp_id, monte_carlo)); // leaving this in leads to timeouts
			return node;
		}
		ArrayList<HusMove> moves = board_state.getLegalMoves();
		for (int i = 0; i < moves.size(); i++) {
			HusBoardState cloned_board_state = (HusBoardState) board_state.clone();
			cloned_board_state.move(moves.get(i));
			ABNode temp_node = MinValues(cloned_board_state, alpha, beta, depth+1, player_id, opp_id/*, monte_carlo*/);
			if (temp_node.score > alpha) {
				alpha = temp_node.score;
				node.move = moves.get(i);
				node.score = alpha;
			}
			if (temp_node.score >= beta) {
				node = new ABNode(moves.get(i), beta);
				return node;
			}
		}
		return node;
	}
	
	public static ABNode MinValues(HusBoardState board_state, float alpha, float beta, int depth, int player_id, int opp_id/*, boolean monte_carlo*/) {
		ABNode node = new ABNode(null, beta);
		
		/* Check for Endgame */
		int winner = board_state.getWinner();
		if (winner == player_id) {
			node.score = Float.MAX_VALUE;
			return node;
		} else if (winner == opp_id) {
			node.score = Float.MIN_VALUE;
			return node;
		}
		
		/* For Normal Gameplay */
//		int test_depth = (monte_carlo) ? 2 : MAX_AB_DEPTH;
		if (depth >= MAX_AB_DEPTH/*test_depth*/) {
			int pits[][] = board_state.getPits();
			node = new ABNode(null, getPlayerTotalSeeds(pits[player_id]));
//			node = new ABNode(null, evaluate(board_state, player_id, opp_id, monte_carlo));
			return node;
		}
		ArrayList<HusMove> moves = board_state.getLegalMoves();
		for (int i = 0; i < moves.size(); i++) {
			HusBoardState cloned_board_state = (HusBoardState) board_state.clone();
			cloned_board_state.move(moves.get(i));
			ABNode temp_node = MaxValues(cloned_board_state, alpha, beta, depth+1, player_id, opp_id/*, monte_carlo*/);
			if (temp_node.score < beta) {
				beta = temp_node.score;
				node.move = moves.get(i);
				node.score = beta;
			}
			if (temp_node.score < alpha) {
				node = new ABNode(moves.get(i), alpha);
				return node;
			}
		}
		return node;
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
    	public float score;
    	
    	public ABNode(HusMove move, float score) {
    		this.move = move;
    		this.score = score;
    	}
    }
}

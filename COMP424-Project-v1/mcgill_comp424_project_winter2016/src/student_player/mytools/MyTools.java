package student_player.mytools;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

import hus.HusBoardState;
import hus.HusMove;

public class MyTools {

	final static int MAX_AB_DEPTH = 6;
	final static int MAX_ROLLOUT_DEPTH = 10;
	final static int ROLLOUTS = 500;

	public static HusMove MonteCarloTreeSearch(HusBoardState board_state, int player_id, int opponent_id) {
		HusMove move = null;
		Random rand = new Random();

		HusBoardState cloned_board_state = (HusBoardState) board_state.clone();

		// TODO: do minimax on the tree first then do rollouts on the leaves
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
	
	public static ABNode MaxValues(HusBoardState board_state, int alpha, int beta, int depth, int player_id, int opp_id) {
		ABNode node = new ABNode(null, alpha);
		if (depth >= MAX_AB_DEPTH) {
			int pits[][] = board_state.getPits();
			int diff = (getPlayerTotalSeeds(pits[player_id]) - getPlayerTotalSeeds(pits[opp_id]));
			node = new ABNode(null, diff);
			return node;
		}
		ArrayList<HusMove> moves = board_state.getLegalMoves();
		for (int i = 0; i < moves.size(); i++) {
			HusBoardState cloned_board_state = (HusBoardState) board_state.clone();
			cloned_board_state.move(moves.get(i));
			ABNode temp_node = MinValues(cloned_board_state, alpha, beta, depth+1, player_id, opp_id);
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
	
	public static ABNode MinValues(HusBoardState board_state, int alpha, int beta, int depth, int player_id, int opp_id) {
		ABNode node = new ABNode(null, beta);
		if (depth >= MAX_AB_DEPTH) {
			int pits[][] = board_state.getPits();
			int diff = (getPlayerTotalSeeds(pits[player_id]) - getPlayerTotalSeeds(pits[opp_id]));
			node = new ABNode(null, diff);
			return node;
		}
		ArrayList<HusMove> moves = board_state.getLegalMoves();
		for (int i = 0; i < moves.size(); i++) {
			HusBoardState cloned_board_state = (HusBoardState) board_state.clone();
			cloned_board_state.move(moves.get(i));
			ABNode temp_node = MaxValues(cloned_board_state, alpha, beta, depth+1, player_id, opp_id);
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
    	public int score;
    	
    	public ABNode(HusMove move, int score) {
    		this.move = move;
    		this.score = score;
    	}
    }
}

package student_player.mytools;

import java.util.ArrayList;
import java.util.Stack;

import hus.HusBoardState;
import hus.HusMove;

public class MyTools {

	final static int MAX_DEPTH = 3;
	
	// create a depth limited tree from given board state
	public static MinMaxNode<HusMove> createMinMaxTree(HusBoardState board_state, int[] my_pits, int[] op_pits) {
		MinMaxNode<HusMove> root = new MinMaxNode<HusMove>(null, 0, true);
	
		// Get the legal moves for the current board state.
        ArrayList<HusMove> moves = board_state.getLegalMoves();
        Stack<MinMaxNode<HusMove>> move_stack = new Stack<MinMaxNode<HusMove>>();
        
        // add to stack
		for (int i = 0; i < moves.size(); i++) {
			MinMaxNode<HusMove> pushNode = root.addChild(moves.get(i));
			move_stack.push(pushNode);
		}
		
		while (!move_stack.isEmpty()) {
			MinMaxNode<HusMove> nextMove = move_stack.pop();
			
			// depth limited
			if (nextMove.depth <= MAX_DEPTH) {
				// get children moves and push to stack as long as we are within the tree's depth
				// We can see the effects of a move like this...
				HusBoardState cloned_board_state = (HusBoardState) board_state.clone();
				cloned_board_state.move(nextMove.data);
			
				ArrayList<HusMove> nextMoves = cloned_board_state.getLegalMoves();
				for (int j = 0; j < nextMoves.size(); j++) {
					MinMaxNode<HusMove> pushNode = nextMove.addChild(nextMoves.get(j));
					move_stack.push(pushNode);
				}
			} else {
				// update the path based on the score
				int myseeds = getPlayerTotalSeeds(my_pits);
				int opseeds = getPlayerTotalSeeds(op_pits);
				nextMove.score = myseeds - opseeds;
				
				updateParentScores(nextMove);
			}
		}
		
		return root;
	}
	
	private static void updateParentScores(MinMaxNode<HusMove> child) {
		while (child != null) {
			if (child.parent.isMax() && child.parent.score < child.score) {
				child.parent.score = child.score;
			} else if (child.parent.isMin() && child.parent.score > child.score) {
				child.parent.score = child.score;
			}
			
			child = child.parent;
		}
	}
	
	public static HusMove getMaxMove(MinMaxNode<HusMove> root) {
		ArrayList<MinMaxNode<HusMove>> children = (ArrayList<MinMaxNode<HusMove>>) root.children;
		double max_score = children.get(0).score;
		MinMaxNode<HusMove> maxMove = children.get(0);
		for (int i = 0; i < children.size(); i++) {
			if (children.get(i).score > max_score) {
				max_score = children.get(i).score;
				maxMove = children.get(i);
			}
		}
		
		return maxMove.data;
	}
	
	public static int getPlayerTotalSeeds(int[] player_pits) {
		int seeds = 0;

        for (int i = 0; i < player_pits.length; i++) {
        	seeds += player_pits[i];
        }
		
		return seeds;
	}
	
    public static double getSomething(){
        return Math.random();
    }
}

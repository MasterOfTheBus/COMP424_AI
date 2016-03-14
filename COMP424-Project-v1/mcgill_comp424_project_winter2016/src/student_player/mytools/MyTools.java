package student_player.mytools;

import java.util.ArrayList;
import java.util.Stack;

import hus.HusBoardState;
import hus.HusMove;

public class MyTools {

	final static int MAX_DEPTH = 3;
	
	// create a depth limited tree from given board state
	public static MinMaxNode<HusMove> createMinMaxTree(HusBoardState board_state) {
		MinMaxNode<HusMove> root = new MinMaxNode<HusMove>(null, 0, true);
//		MinMaxNode<HusMove> node = root;
	
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
			
			// calculate the score
			
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
			}
			
		}
		

//        
//        for (int i = 0; i < moves.size(); i++) {
//        	HusMove move = moves.get(i);
//        	move_stack.push(move);
//        	int current_depth = 0;
//        	while (!move_stack.isEmpty()) {
//        		HusMove nextMove = move_stack.pop();
//        		
//        		// add the move to the tree
//        		// TODO: need to figure out the score and whether min or max
//        		
//        		
//        		// get the next moves
//        		if (current_depth < TREE_DEPTH) {
//        			// We can see the effects of a move like this...
//        			HusBoardState cloned_board_state = (HusBoardState) board_state.clone();
//        			cloned_board_state.move(nextMove);
//        			
//        			ArrayList<HusMove> nextMoves = cloned_board_state.getLegalMoves();
//        			for (int j = 0; j < nextMoves.size(); j++) {
//        				move_stack.push(nextMoves.get(j));
//        			}
//        		}
//        	}
//        }
		
		return root;
	}
	
    public static double getSomething(){
        return Math.random();
    }
}

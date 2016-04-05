package student_player;

import java.util.ArrayList;

import hus.HusBoardState;
import hus.HusPlayer;
import hus.HusMove;
import student_player.mytools.MinMaxNode;
import student_player.mytools.MyTools;

/**
 * 
 * A Hus player that can beat the random player consistently.
 * Use this player as a base to test any improvements to choosing a move
 * 
 * */
public class BaseStudentPlayer extends HusPlayer {
	boolean firstMove = true;
	
    public BaseStudentPlayer() { super("Base Player"); }

    public HusMove chooseMove(HusBoardState board_state)
    {
    	HusMove move;
    	if (firstMove) {
        	ArrayList<HusMove> moves = board_state.getLegalMoves();

    		// for now the first move will be random so that we can avoid making a tree with one level of the same score
    		move = moves.get((int)(Math.random() * moves.size()));
    		firstMove = false;

    	} else {

    		MyTools.ABNode node = MyTools.MaxValues(board_state, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, player_id, opponent_id);
    		move = node.move;
    	}

        return move;
    }
}

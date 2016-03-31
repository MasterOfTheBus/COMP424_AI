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
	
    public BaseStudentPlayer() { super("260507001"); }

    public HusMove chooseMove(HusBoardState board_state)
    {
    	HusMove move;
    	if (firstMove) {
        	ArrayList<HusMove> moves = board_state.getLegalMoves();

    		// for now the first move will be random so that we can avoid making a tree with one level of the same score
    		move = moves.get((int)(Math.random() * moves.size()));
    		firstMove = false;
    		
    		// TODO: should the first move save a tree or some structure?
    	} else {

    		MyTools.ABNode node = MyTools.getMoveBasic(board_state, player_id, opponent_id);
    		move = node.move;
    	}

        return move;
    }
}

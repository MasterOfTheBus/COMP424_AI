package student_player;

import hus.HusBoardState;
import hus.HusPlayer;
import hus.HusMove;
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

//    	MyTools.ABNode node = MyTools.AB_Search(board_state, player_id, opponent_id);
    	MyTools.ABNode node = MyTools.MaxValues(board_state, Float.MIN_VALUE, Float.MAX_VALUE, 0, player_id, opponent_id);
    	move = node.move;

        return move;
    }
}

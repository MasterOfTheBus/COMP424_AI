package student_player;

import hus.HusBoardState;
import hus.HusPlayer;
import hus.HusMove;
import student_player.mytools.MyTools;

/** A Hus player submitted by a student. */
public class StudentPlayer extends HusPlayer {
	boolean firstMove = true;
	
    /** You must modify this constructor to return your student number.
     * This is important, because this is what the code that runs the
     * competition uses to associate you with your agent.
     * The constructor should do nothing else. */
    public StudentPlayer() { super("260507001"); }

    /** This is the primary method that you need to implement.
     * The ``board_state`` object contains the current state of the game,
     * which your agent can use to make decisions. See the class hus.RandomHusPlayer
     * for another example agent. */
    public HusMove chooseMove(HusBoardState board_state)
    {
    	HusMove move;
//    	MyTools.ABNode node = MyTools.MinimaxValue(board_state, 0, player_id, opponent_id, true);
//    	MyTools.ABNode node = MyTools.AB_Search(board_state, player_id, opponent_id);
//    	MyTools.ABNode node = MyTools.MonteCarlo_Search(board_state, player_id, opponent_id);
    	MyTools.ABNode node = MyTools.MaxValues(board_state, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, player_id, opponent_id);
    	move = node.move;

        return move;
    }
}

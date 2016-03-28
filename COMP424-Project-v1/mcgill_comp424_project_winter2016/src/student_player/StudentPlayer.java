package student_player;

import java.util.ArrayList;

import hus.HusBoardState;
import hus.HusPlayer;
import hus.HusMove;
import student_player.mytools.MinMaxNode;
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
    	if (firstMove) {
        	ArrayList<HusMove> moves = board_state.getLegalMoves();

    		// for now the first move will be random so that we can avoid making a tree with one level of the same score
    		move = moves.get((int)(Math.random() * moves.size()));
    		firstMove = false;
    		
    		// TODO: should the first move save a tree or some structure?
    	} else {

    		MyTools.ABNode node = MyTools.getMove(board_state, player_id, opponent_id, 0, true);
    		move = node.move;
    	}

        return move;
    }
}

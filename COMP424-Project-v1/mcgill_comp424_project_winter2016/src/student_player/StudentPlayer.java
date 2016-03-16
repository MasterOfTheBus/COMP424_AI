package student_player;

import java.util.ArrayList;

import hus.HusBoardState;
import hus.HusPlayer;
import hus.HusMove;
import student_player.mytools.MinMaxNode;
import student_player.mytools.MyTools;

/** A Hus player submitted by a student. */
public class StudentPlayer extends HusPlayer {

	
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
        // Get the contents of the pits so we can use it to make decisions.
        int[][] pits = board_state.getPits();

        // Use ``player_id`` and ``opponent_id`` to get my pits and opponent pits.
        int[] my_pits = pits[player_id];
        int[] op_pits = pits[opponent_id];

        MinMaxNode<HusMove> AB_tree = MyTools.createMinMaxTree(board_state, my_pits, op_pits);
        HusMove move = MyTools.getMaxMove(AB_tree);
        
//        ArrayList<HusMove> moves = board_state.getLegalMoves();
//        HusMove move = moves.get(0);

        // But since this is a placeholder algorithm, we won't act on that information.
        return move;
    }
}

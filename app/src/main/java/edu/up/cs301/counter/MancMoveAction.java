package edu.up.cs301.counter;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * A MancMoveAction is an action that is a "move" the game: confirming which hole they want to move
 *
 * @author Shayna Noone
 * @version September 2017
 */
public class MancMoveAction extends GameAction {

    // to satisfy the serializable interface
    private static final long serialVersionUID = 28062013L;

    //whether this move is a confirmed(true)
    private boolean isConfirmed;

    /**
     * Constructor for the CounterMoveAction class.
     *
     * @param player
     *            the player making the move
     * @param isConfirmed
     *            value to initialize this.isConfirmed
     */
    public MancMoveAction(GamePlayer player, boolean isConfirmed) {
        super(player);
        this.isConfirmed = isConfirmed;
    }

    /**
     * getter method, to tell whether the move is confirmed
     *
     * @return
     * 		a boolean that tells whether this move is confirmed
     */
    public boolean isConfirmed() {
        return isConfirmed;

    }
}//class MancMoveAction

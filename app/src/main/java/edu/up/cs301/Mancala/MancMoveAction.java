package edu.up.cs301.Mancala;

import android.graphics.Point;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * A MancMoveAction is an action that is a "move" the game: confirming which hole they want to move
 *
 * @author Shayna Noone
 * @version October 2017
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
     * @param selectedHole
     *
     */
    public MancMoveAction(GamePlayer player,  Point selectedHole) {
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

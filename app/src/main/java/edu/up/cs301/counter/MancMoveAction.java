package edu.up.cs301.counter;

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
    private int row;
    private int col;
    private Boolean isSelected = false;

    /**
     * Constructor for the CounterMoveAction class.
     *
     * @param player
     *            the player making the move
     * @param c
     *            value to initialize this.hole
     */
    public MancMoveAction(GamePlayer player, int c, int r) {
        super(player);
        this.row=r;
        this.col=c;
    }

    /**
     * getter method, to tell whether the move is confirmed
     *
     * @return
     * 		a boolean that tells whether this move is confirmed
     */
    public boolean isSelected() {
        return isSelected;

    }
}//class MancMoveAction

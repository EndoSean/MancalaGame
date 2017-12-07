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
 class MancMoveAction extends GameAction {

    // to satisfy the serializable interface
    private static final long serialVersionUID = 28062013L;

    //Instance variables
    private MancState State;    //State of game at start of move
     int playerNumber;    //the player number of player who created action
    /**
     * Constructor for the MancMoveAction class.
     *
     * @param player
     *            the player making the move
     * @param Current_State
     *            a recent state of the game to copy and send
     * @param playerNum
     *            the number of player how committed the move
     *
     */

     MancMoveAction(GamePlayer player,  MancState Current_State, int playerNum) {
        super(player);
        State = new MancState(Current_State);
        playerNumber= playerNum;
    }

    /**
     * getter method, to tell current state
     *
     * @return
     * 		the current state of the Mancala game
     */
    public MancState getState(){
        return State;
    }
}//class MancMoveAction

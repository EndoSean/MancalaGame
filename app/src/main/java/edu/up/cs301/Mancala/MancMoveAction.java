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
    //private Point selected_Hole;
    private MancState State;
    public int playerNumber;
    /**
     * Constructor for the MancMoveAction class.
     *
     * @param player
     *            the player making the move
     * @param Current_State
     *            a recent state of the game to copy and send
     * @param playerNum
     *
     */
//    public MancMoveAction(GamePlayer player,  Point selected, int playerNum) {
//        super(player);
//        this.selected_Hole = selected;
//        playerNumber= playerNum;
//    }
    public MancMoveAction(GamePlayer player,  MancState Current_State, int playerNum) {
        super(player);
        State = new MancState(Current_State);
        playerNumber= playerNum;
    }

    /**
     * getter method, to tell whether the move is confirmed
     *
     * @return
     * 		the hole the player has selected to make a move
     */
//    public Point getSelected_Hole() {
//        return selected_Hole;
//
//    }

    public MancState getState(){
        return State;
    }
}//class MancMoveAction

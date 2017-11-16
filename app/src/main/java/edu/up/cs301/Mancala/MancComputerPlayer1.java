package edu.up.cs301.Mancala;

import edu.up.cs301.counter.CounterMoveAction;
import edu.up.cs301.game.GameComputerPlayer;
import edu.up.cs301.game.infoMsg.GameInfo;
import edu.up.cs301.game.util.Tickable;

/**
 * A computer-version of a Mancala-player.  Since this is such a simple game,
 * it just sends a randomly selected hole,
 *
 * @author Shayna Noone
 * @author
 * @version November 2017
 */
public class MancComputerPlayer1 extends GameComputerPlayer {

    /**
     * Constructor for objects of class MancComputerPlayer1
     *
     * @param name
     * 		the player's name
     */
    public MancComputerPlayer1(String name) {
        // invoke superclass constructor
        super(name);

    }

    /**
     * callback method--game's state has changed
     *
     * @param info
     * 		the information (presumably containing the game's state)
     */
    @Override
    protected void receiveInfo(GameInfo info) {
        // Do nothing, as we ignore all state in deciding our next move. It
        // depends totally on the timer and random numbers.
    }

}


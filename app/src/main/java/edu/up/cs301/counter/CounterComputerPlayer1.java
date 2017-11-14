package edu.up.cs301.counter;

import edu.up.cs301.game.GameComputerPlayer;
import edu.up.cs301.game.infoMsg.GameInfo;
import edu.up.cs301.game.util.Tickable;
    
/**
 * A computer-version of a counter-player.  Since this is such a simple game,
 * it just sends "+" and "-" commands with equal probability, at an average
 * rate of one per second. 
 * 
 * @author Steven R. Vegdahl
 * @author Andrew M. Nuxoll
 * @version September 2013
 */
public class CounterComputerPlayer1 extends GameComputerPlayer implements Tickable {
	
    /**
     * Constructor for objects of class CounterComputerPlayer1
     * 
     * @param name
     * 		the player's name
     */
	// the most recent game state, as given to us by the CounterLocalGame
	private MancState recentState;

    public CounterComputerPlayer1(String name) {
        // invoke superclass constructor
        super(name);
        
        // start the timer, ticking 20 times per second
        getTimer().setInterval(50);
        getTimer().start();
    }
    
    /**
     * callback method--game's state has changed
     * 
     * @param info
     * 		the information (presumably containing the game's state)
     */
	@Override
	protected void receiveInfo(GameInfo info) {
		if (info instanceof MancState){
		recentState = (MancState)info;
	}

	}
	
	/**
	 * callback method: the timer ticked
	 */
	protected void timerTicked() {
		// 5% of the time, increment or decrement the counter
		int[][] marbArray = recentState.getMarble_Pos();
		int r;
		Boolean canMove = false;
		while(canMove){
			r = (int)Math.random()*6 +1;
			if(marbArray[this.playerNum][r]>0){
				canMove=true;
			}
		}

		// send the move-action to the game
		game.sendAction(new MancMoveAction(this, canMove));
	}

}

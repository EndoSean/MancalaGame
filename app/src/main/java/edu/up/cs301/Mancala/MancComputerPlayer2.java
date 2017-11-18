package edu.up.cs301.Mancala;

import android.app.Activity;
import android.graphics.Point;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import edu.up.cs301.counter.CounterComputerPlayer1;
import edu.up.cs301.counter.CounterState;
import edu.up.cs301.game.GameMainActivity;
import edu.up.cs301.game.R;
import edu.up.cs301.game.infoMsg.GameInfo;
import edu.up.cs301.game.util.Tickable;


/**
* A computer-version of a counter-player.  Since this is such a simple game,
* it just sends "+" and "-" commands with equal probability, at an average
* rate of one per second. This computer player does, however, have an option to
* display the game as it is progressing, so if there is no human player on the
* device, this player will display a GUI that shows the value of the counter
* as the game is being played.
* 
* @author Courtney Cox
* @version November 2017
*/
class MancComputerPlayer2 extends MancComputerPlayer1 implements Tickable {

	/*
	 * instance variables
	 */

	// the most recent game state, as given to us by the CounterLocalGame
	private MancState recentState = null;

	// If this player is running the GUI, the activity (null if the player is
	// not running a GUI).
	private Activity activityForGui = null;

	// If this player is running the GUI, the widget containing the counter's
	// value (otherwise, null);
	private TextView counterValueTextView = null;

	// If this player is running the GUI, the handler for the GUI thread (otherwise
	// null)
	private Handler guiHandler = null;

	/**
	 * constructor
	 *
	 * @param name
	 * 		the player's name
	 */
	MancComputerPlayer2(String name) {
		super(name);
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
		// perform superclass behavior
		super.receiveInfo(info);
		
		Log.i("computer player", "receiving");
		
		// if there is no game, ignore
		if (game == null) {
		}
		else if (info instanceof MancState) {
			// if we indeed have a counter-state, update the GUI
			recentState = (MancState)info;
			updateDisplay();
		}
	}
	protected void timerTicked() {
		if(recentState != null){ //still just making random moves
			int marbles[][] = recentState.getMarble_Pos();

			int max = 5;
			int min = 0;
			int range = max - min + 1;
			Boolean repick = true; //why is this capital Boolean?
			int randPosition=0;
			while(repick) {

				randPosition = (int) (Math.random() * range) + min;
				if (marbles[this.playerNum][randPosition]!= 0){
					repick=false;
				}
			}

			Point compSelected= new Point(this.playerNum,randPosition);

			// send the move-action to the game if it is the computer turn
			if(recentState.getPlayer_Turn()==this.playerNum) {
				sleep(1000);

				game.sendAction(new MancMoveAction(this, compSelected, this.playerNum));


			}}
	}
	
	/** 
	 * sets the counter value in the text view
	 *  */
	private void updateDisplay() {
		// if the guiHandler is available, set the new counter value
		// in the counter-display widget, doing it in the Activity's
		// thread.
		if (guiHandler != null) {

		}
	}
	
	/**
	 * Tells whether we support a GUI
	 * 
	 * @return
	 * 		true because we support a GUI
	 */
	public boolean supportsGui() {
		return true;
	}
	
	/**
	 * callback method--our player has been chosen/rechosen to be the GUI,
	 * called from the GUI thread.
	 * 
	 * @param a
	 * 		the activity under which we are running
	 */
	@Override
	public void setAsGui(GameMainActivity a) {
		
		// remember who our activity is
		this.activityForGui = a;
		
		// remember the handler for the GUI thread
		this.guiHandler = new Handler();
		
		// Load the layout resource for the our GUI's configuration
		activityForGui.setContentView(R.layout.manc_layout);

	}

}

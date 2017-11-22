package edu.up.cs301.Mancala;

import android.app.Activity;
import android.graphics.Point;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import edu.up.cs301.counter.CounterComputerPlayer1;
import edu.up.cs301.counter.CounterState;
import edu.up.cs301.game.GameComputerPlayer;
import edu.up.cs301.game.GameMainActivity;
import edu.up.cs301.game.R;
import edu.up.cs301.game.infoMsg.GameInfo;
import edu.up.cs301.game.util.Tickable;


/**
 * A computer-version of a Mancala-player.  This computer player Determines if selection of any
 * of the six “holes” will result in an additional move
 *
 *1. Go through it’s array of holes
 *
 *2. Determine number of marbles in each hole
 *
 *3. If marbles equals distance from bank select this hole
 *
 *Determines if selection of any of the six “holes” will result in capturing opponents marbles
 *
 *1. Go through it’s array of holes
 *
 *2. Determine number of marbles in each hole
 *
 *3. Determine where the last marble will land
 *
 *4. Check the hole where marble lands
 *
 *5. If that hole is empty and belongs to player select the move.
 *
 *If no special move exists then randomly select one of the six “holes”. This computer
 *player does, however, have an option to display the game as it is progressing, so if
 *there is no human player on the device, this player will display a GUI that shows the
 *value of the counter as the game is being played.
 *
 * @author Courtney Cox
 * @version November 2017
 */
class MancComputerPlayer2 extends GameComputerPlayer implements Tickable {

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
		//super.receiveInfo(info);


		Log.i("computer player", "receiving");

		// if there is no game, ignore
		if (game == null) {
		}
		else if (info instanceof MancState) {
			// if we indeed have a Manc-state, update the GUI
			recentState = (MancState)info;
			updateDisplay();
		}
	}


	/**
	 * sets the counter value in the text view
	 *  */
	private void updateDisplay() {
		// if the guiHandler is available, set the new Mancala value
		// in the manc-display, doing it in the Activity's
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
	/**
	 * Implements the "brains" of the smart computer player.
	 * checks for potential double moves and captures.
	 * add in calculating opponent caputures
	 */
	@Override
	protected void timerTicked() {
		super.timerTicked();
		//checks if there is a game
		if(recentState != null){
			//saves the array with the number of marbles in the holes
			int marbles[][] = recentState.getMarble_Pos();

			Point select= new Point(0,0);
			boolean holeSelect = false;

			//Checks for holes with marbles that will end in the bank
			for(int c=0; c<6;c++){
				int marb = marbles[this.playerNum][c];
				if(marb == 6-c && !holeSelect){
					select=new Point(this.playerNum, c);
					holeSelect=true;
				}
			}

			//If there is no bank ending move, checks for a capture move
			if(!holeSelect){
				//starts at hole one since that is the first hole to pick that can still play on
				// our side
				for(int c=1; c<6;c++ ){
					int marb = marbles[this.playerNum][c];
					if(marb>0 && marb<c+1 && !holeSelect){
						int future = marbles[this.playerNum][c-marb];
						if(future==0){
							select = new Point(this.playerNum, c);
							holeSelect = true;
						}

					}
				}

			}
			//if none of the above options, selects a random move.
			if(!holeSelect) {

				//the holes range:
				int max = 5;
				int min = 0;
				//boolean to see if
				Boolean repick = true;
				int randPosition = 0;
				while (repick) {

					randPosition = (int) (Math.random() * max - min + 1) + min;
					if (marbles[this.playerNum][randPosition] != 0) {
						repick = false;
					}
				}

				select = new Point(this.playerNum, randPosition);
			}
			// send the move-action to the game if it is the computer turn
			if(recentState.getPlayer_Turn()==this.playerNum) {
				sleep(1000);

				game.sendAction(new MancMoveAction(this, select, this.playerNum));


			}
		}

	}
}

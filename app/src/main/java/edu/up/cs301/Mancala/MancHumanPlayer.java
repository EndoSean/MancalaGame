package edu.up.cs301.Mancala;

import android.graphics.Point;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import edu.up.cs301.animation.AnimationSurface;
import edu.up.cs301.game.GameHumanPlayer;
import edu.up.cs301.game.GameMainActivity;
import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.R;
import edu.up.cs301.game.actionMsg.GameAction;
import edu.up.cs301.game.infoMsg.GameInfo;


/**
 * A GUI of a Mancala-player. The GUI displays the current value of the holes and banks,
 * and allows the human player to press a hole in their row to
 * select the marbles and put them into play.
 *
 *
 * @author Courtney Cox
 * @version November 2017
 *
 */

public class MancHumanPlayer extends GameHumanPlayer {//implements View.OnTouchListener {



    /* instance variables */
    //The animator
    private MancalaAnimatorTwo animator;

    // The TextView the displays the current counter value
    private TextView counterValueTextView;

    // the most recent game state, as given to us by the MancLocalGame
    private MancState recentState;

    // the android activity that we are running
    private GameMainActivity myActivity;

    /**
     * constructor
     * @param name
     * 		the player's name
     */
    public MancHumanPlayer(String name) {
        super(name);
    }

    /**
     * Returns the GUI's top view object
     *
     * @return
     * 		the top object in the GUI's view heirarchy
     */
    public View getTopView() {
        return myActivity.findViewById(R.id.top_gui_layout);
    }


    /**
     * callback method when we get a message (e.g., from the game)
     *
     * @param info
     * 		the message
     */
    @Override
    public void receiveInfo(GameInfo info) {
        //if receive mancState object set it as current state and send to animator
        if (info instanceof MancState){
            recentState = (MancState)info;
            animator.setState(recentState);
            animator.setMarbles(this.playerNum);
        }

    }//receiveInfo

    /**
     * callback method--our game has been chosen/rechosen to be the GUI,
     * called from the GUI thread
     *
     * @param activity
     * 		the activity under which we are running
     */
    public void setAsGui(GameMainActivity activity) {

        myActivity = activity;

        // Load the layout resource for our GUI
        activity.setContentView(R.layout.manc_layout);

        AnimationSurface mySurface =
                (AnimationSurface) myActivity.findViewById(R.id.animation_surface);
        animator = new MancalaAnimatorTwo();
        mySurface.setAnimator(animator);

        Display mdisp = myActivity.getWindowManager().getDefaultDisplay();
        Point mdispSize = new Point();
        mdisp.getSize(mdispSize);
        float maxX = (float)mdispSize.x;
        float maxY = (float)mdispSize.y;
        animator.getBounds(maxX,maxY);

        if(recentState == null) {
            recentState = animator.setHoles();
            recentState = animator.setMarbles(this.playerNum);
        }
        else {

            recentState = animator.setMarbles(this.playerNum);
        }//setAsGui

        mySurface.setOnTouchListener(new MancHumanPlayer.onTouchEvent());
    }


    private boolean PostOnTouch(View view, MotionEvent motionEvent) {
        // if we are not yet connected to a game, ignore
        if (game == null) return false;

        // Construct the action and send it to the game
        GameAction action = new MancMoveAction(this, recentState.getSelected_Hole(), this.playerNum);


        game.sendAction(action); // send action to the game
        return true;
    }

    /**
     * onTouch listener
     */
    private class onTouchEvent implements View.OnTouchListener{
        public boolean onTouch(View v, MotionEvent me){
            //sends event to animator
            animator.onTouch(me);
            recentState = animator.getUpdatedState();
            //send to PostOnTouch to handle human side
            return PostOnTouch(v,me);

        }
    }//onTouchEvent

}//MancHumanPlayer

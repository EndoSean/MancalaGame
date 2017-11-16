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


    MancalaAnimator animator;

	/* instance variables */

    // The TextView the displays the current counter value
    private TextView counterValueTextView;

    // the most recent game state, as given to us by the CounterLocalGame
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
        /*

            public void onTouch (View v) - handles touch events.



public void tick(Canvas canvas) - preforms animation

*/
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
     * sets the counter value in the text view
     */
    protected void updateDisplay() {
        // set the text in the appropriate widget

    }

    /**
     * this method gets called when the user clicks the '+' or '-' button. It
     * creates a new CounterMoveAction to return to the parent activity.
     *
     *
     * 		the button that was clicked
     */
    public void onTouch(SurfaceView surface) {
        // if we are not yet connected to a game, ignore
        if (game == null) return;

        // Construct the action and send it to the game
        GameAction action = new MancMoveAction(this, true);

        game.sendAction(action); // send action to the game
    }// onTouch

    /**
     * callback method when we get a message (e.g., from the game)
     *
     * @param info
     * 		the message
     */
    @Override
    public void receiveInfo(GameInfo info) {
        if (info instanceof MancState){
            recentState = (MancState)info;
        }
        //if (recentState != null) {
        //    receiveInfo(info);
        //}
    }

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
        animator = new MancalaAnimator();
        mySurface.setAnimator(animator);

        Display mdisp = myActivity.getWindowManager().getDefaultDisplay();
        Point mdispSize = new Point();
        mdisp.getSize(mdispSize);
        float maxX = (float)mdispSize.x;
        float maxY = (float)mdispSize.y;
        animator.getBounds(maxX,maxY);
        if(recentState == null) {
            recentState = animator.setHoles();
            recentState = animator.setMarbles2();
        }
        else {
            //int[][] Marble_Pos = recentState.getMarble_Pos();
            //Point Hole_Selected = recentState.getSelected_Hole();
            recentState = animator.setMarbles(recentState);
        }


        mySurface.setOnTouchListener(new MancHumanPlayer.onTouchEvent());




    }

    public boolean PostonTouch(View view, MotionEvent motionEvent) {
        // if we are not yet connected to a game, ignore
        if (game == null) return false;

        // Construct the action and send it to the game
        GameAction action = new MancMoveAction(this, true);

        game.sendAction(action); // send action to the game
        return true;
    }

    private class onTouchEvent implements View.OnTouchListener{
        public boolean onTouch(View v, MotionEvent me){
            animator.onTouch(me);
            recentState = animator.getUpdatedState();
            boolean set = PostonTouch(v,me);


            return set;

        }
    }




}

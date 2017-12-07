package edu.up.cs301.Mancala;

import android.graphics.Point;


import edu.up.cs301.game.GameComputerPlayer;
import edu.up.cs301.game.infoMsg.GameInfo;
import edu.up.cs301.game.util.Tickable;

/**
 * A computer-version of a Mancala-player.  Since this is such a simple game,
 * it just sends a randomly selected hole that contains marbles from its row.
 *
 * @author Shayna Noone
 * @author Courtney Cox
 * @version November 2017
 */
class MancComputerPlayer1 extends GameComputerPlayer implements Tickable{

    private MancState recentState;
    private boolean turn;
    /**
     * Constructor for objects of class MancComputerPlayer1
     *
     * @param name
     * 		the player's name
     */
    MancComputerPlayer1(String name) {
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
            turn=false;
            //initializes boolean to keep track of turn
            if(recentState.getPlayer_Turn()==this.playerNum){
                turn=true;
            }
        }
    }

    /**
     * timerTicked is where the computer player selects a viable hole to send to game
     * action when it isn't waiting for its turn.
     */
    protected void timerTicked() {
        //makes sure there is a game in play and it is computer turn
        if(recentState != null && turn){
            //saves the array with the number of marbles in the holes
            int marbles[][] = recentState.getMarble_Pos();

            //the holes range:
            int max = 6;
            int min = 0;
            //boolean to see if
            Boolean repick = true;
            int randPosition=0;
            while(repick) {

                randPosition = (int) (Math.random() * max - min) + min;
                if (marbles[this.playerNum][randPosition]!= 0){
                    repick=false;
                }
            }

            Point compSelected= new Point(this.playerNum,randPosition);

            //sleep for computer "thinking"
            sleep(4000);

           // game.sendAction(new MancMoveAction(this, compSelected, this.playerNum));
            recentState.setSelected_Hole(compSelected);
            recentState.setReset(false);
            game.sendAction(new MancMoveAction(this, recentState, this.playerNum));
            turn=false;


        }
    }

}


package edu.up.cs301.Mancala;

import android.graphics.Point;

import edu.up.cs301.counter.CounterMoveAction;
import edu.up.cs301.game.GameComputerPlayer;
import edu.up.cs301.game.infoMsg.GameInfo;
import edu.up.cs301.game.util.Tickable;

/**
 * A computer-version of a Mancala-player.  Since this is such a simple game,
 * it just sends a randomly selected hole,
 *
 * @author Shayna Noone
 * @author Courtney Cox
 * @version November 2017
 */
class MancComputerPlayer1 extends GameComputerPlayer implements Tickable{

    private MancState recentState;
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
        }
    }
    protected void timerTicked() {
        if(recentState != null){
        int marbles[][] = recentState.getMarble_Pos();

        int max = 5;
        int min = 0;
        int range = max - min + 1;
        Boolean repick = true;
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

    public int getPlayerNum(){return this.playerNum;}

}


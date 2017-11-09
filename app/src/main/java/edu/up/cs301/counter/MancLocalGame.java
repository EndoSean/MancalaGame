package edu.up.cs301.counter;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.LocalGame;
import edu.up.cs301.game.actionMsg.GameAction;
import android.util.Log;

/**
 * A class that represents the state of a game.
 */
public class MancLocalGame extends LocalGame {

    private MancState gameState;


    /**
     * can this player move
     * @return
     * 		true, because all player are always allowed to move at all times,
     * 		as this is a fully asynchronous game
     */
    @Override
//needs to be modified to alernate players*******************************************************************
    protected boolean canMove(int player) {
        return true;
    }

    /**
     * This ctor should be called when a new counter game is started
     */
    public MancLocalGame() {
        // initialize the game state,
        this.gameState = new MancState();
    }

    /**
     * The only type of GameAction that should be sent is MancMoveAction
     */
    @Override
    protected boolean makeMove(GameAction action) {
        Log.i("action", action.getClass().toString());

        if (action instanceof MancMoveAction) {
//not fully edited yet --- I need to know how to get the position that was selected*******************************************

            // cast so that we Java knows it's a CounterMoveAction
            MancMoveAction cma = (MancMoveAction)action;

            // Update the counter values based upon the action
            int[][] marbles = gameState.getMarble_Pos();
            gameState.setMarble_Pos(marbles);

            // denote that this was a legal/successful move
            return true;
        }
        else {
            // denote that this was an illegal move
            return false;
        }
    }//makeMove

    /**
     * send the updated state to a given player
     */
    @Override
    protected void sendUpdatedStateTo(GamePlayer p) {
        // this is a perfect-information game, so we'll make a
        // complete copy of the state to send to the player
        p.sendInfo(new MancState(gameState));

    }//sendUpdatedSate

    /**
     * Check if the game is over. It is over, return a string that tells
     * who the winner(s), if any, are. If the game is not over, return null;
     *
     * @return
     * 		a message that tells who has won the game, or null if the
     * 		game is not over
     */
    @Override
    protected String checkIfGameOver() {

        int player0Score = this.gameState.getPlayer0_Score();
        int player1Score = this.gameState.getPlayer1_Score();
        int[][] marbles = this.gameState.getMarble_Pos();
        int count0=0; //if count not equal to zero then game not over
        int count1 =0;
        for (int i = 0; i < 6; i++) {
           count0 += marbles[0][i];
        }
        for (int i = 0; i < 6; i++) {
            count1 += marbles[1][i];
        }
        if(count0==0||count1==0) {
            if (player0Score > player1Score) {

                return " player0 has won."; // playerNames[0]+" has won."
            } else if (player0Score < player1Score) {

                return " player1 has won."; // playerNames[1]+" has won."
            } else if (player0Score == player1Score) {

                return " Tie ";
            } else {
                // game is still between the two limit: return null, as the game
                // is not yet over
                return null;
            }
        }
        return null;
    }

}

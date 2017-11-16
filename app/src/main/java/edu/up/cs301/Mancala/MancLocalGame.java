package edu.up.cs301.Mancala;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.LocalGame;
import edu.up.cs301.game.actionMsg.GameAction;

import android.graphics.Point;
import android.util.Log;

/**
 * A class that represents the state of a game.
 */
public class MancLocalGame extends LocalGame {

    private MancState gameState;


    /**
     * can this player move
     * @return
     * 		true, if the game is not over and it is that  players turn
     *      false, if one of the above is not true
     */
    @Override
    protected boolean canMove(int player) {

        if(checkIfGameOver()!=null){
            return false;
        }else if(player != gameState.getPlayer_Turn()){
            return false;
        }
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


            // cast so that we Java knows it's a MancMoveAction
            MancMoveAction cma = (MancMoveAction)action;

            int[][] marbles = gameState.getMarble_Pos(); //copy the marble positions array from the gamestate
            Point z = cma.getSelected_Hole(); // get the selected hole from the gameState that recieves the selected hole from the animation
            gameState.setSelected_Hole(z); //updates the game state to recieve the selected hole
            if (z.x == -1 && z.y == -1){
                return false;
            }
            int pos= z.y; // get which number hole we are on based off the selected hole
            int side = z.x; //get the side of the board based off the location of the selected hole
            int numMarb = marbles[side][pos]; //get the number of marbles in the selected hole
            marbles[side][pos]=0; // set the selected hole to be empty

            while(numMarb>0){ //check to make sure that we still have holes to move
                pos++; //go to the next postion in the marbles array

                if(pos == 6){ //check to see if the hole is a goal
                    if(side == gameState.getPlayer_Turn()) { // checks to see if it is the current player's goal
                        marbles[side][pos]++; //add marbles to goal
                        numMarb--; // decrement marbles
                        if(side == 0){ //goes to the other side of the board
                            side =1;
                        }else if(side ==1){
                            side =0;
                        }
                        pos = 0; //start from the beginning position from that side of the board
                    }else if(side != gameState.getPlayer_Turn()){ //else if its not the same goal as the current player then skip that goal and start on the new side
                        if(side == 0){ //goes to the other side of the board
                            side =1;
                        }else if(side ==1){
                            side =0;
                        }
                        pos = 0; //start from the beginning position from that side of the board
                        marbles[side][pos]++; //add marbles to the first position on the new side
                        numMarb--; // decrement marbles
                    }

                }else if(marbles[side][pos]==0 && numMarb==1 && side==gameState.getPlayer_Turn()) { //is the last marble going to land in an empty hole on the current players side
                    if(side ==1){//checks to see which player's turn it is
                        marbles[1][6]+=marbles[0][pos];//adds the marbles from the hole opposite the previously empty hole to the player's goal
                        marbles[0][pos]=0; //sets the hole across from the previously empty hole, empty.
                    }else{
                        marbles[0][6]+=marbles[1][pos];
                        marbles[1][pos]=0;
                    }
                    numMarb--;//that was the last marble, makes numMarb=0
                }else {
                    marbles[side][pos]++; //adds a marble
                    numMarb--; //one less marble to move
                }

            }

            gameState.setMarble_Pos(marbles);
            gameState.setPlayer0_Score(marbles[0][6]);
            gameState.setPlayer1_Score(marbles[1][6]);

            if(pos==6&&numMarb==0 && side == gameState.getPlayer_Turn()){ //checks to see if the last marble landed in a goal of that player
                gameState.setPlayer_Turn(gameState.getPlayer_Turn()); //if it did then its still that player's turn
  /**          }else{
                int currPlayer = gameState.getPlayer_Turn(); // else we are going to switch players
                if(currPlayer==1) {
                    gameState.setPlayer_Turn(0);
                }else{
                    gameState.setPlayer_Turn(1);
                }**/
            }

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

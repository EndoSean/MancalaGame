package edu.up.cs301.Mancala;
import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.LocalGame;
import edu.up.cs301.game.actionMsg.GameAction;

import android.graphics.Point;
import android.util.Log;

import static android.os.SystemClock.sleep;

/**
 * A class that represents the state of a game.
 */
class MancLocalGame extends LocalGame {

    private MancState gameState;


    /**
     * can this player move
     * @return
     * 		true, if the game is not over and it is that  players turn
     *      false, if one of the above is not true
     */
    @Override
    protected boolean canMove(int player) {

        if(checkIfGameOver()!=null){//check if the game is over
            return false;
        }
        //is player turn = player number?
        return player == this.gameState.getPlayer_Turn();
    }

    /**
     * This constructor should be called when a new Mancala game is started
     */
    MancLocalGame() {
        // initialize the game state,
        this.gameState = new MancState();
    }

    /**
     * The only type of GameAction that should be sent is MancMoveAction
     */
    @Override
    protected boolean makeMove(GameAction action) {
        Log.i("action", action.getClass().toString());
        if(gameState.getReset()){
            return false;
        }
        if (action instanceof MancMoveAction ) {


            // cast so that we Java knows it's a MancMoveAction
            MancMoveAction cma = (MancMoveAction)action;

            if(!canMove(cma.playerNumber)){
                return false;
            }


            // endo edit
            MancState Player_State = cma.getState();
            MyPointF z = Player_State.getSelected_Hole();
            gameState = Player_State;
            int[][] marbles = gameState.getMarble_Pos(); //copy the marble positions array from the gamestate
            //Point z = cma.getSelected_Hole(); // get the selected hole from the gameState that recieves the selected hole from the animation

            // Check if Reset
            if(gameState.getReset()){
                this.Reset();
                return true;
            }

            gameState.setSelected_Hole(z); //updates the game state to recieve the selected hole
            int pos= (int) z.y; // get which number hole we are on based off the selected hole
            int side = (int) z.x; //get the side of the board based off the location of the selected hole
            if(z.x ==-1 || z.y==-1){
                return false;
            }
            int numMarb = marbles[side][pos]; //get the number of marbles in the selected hole
            marbles[side][pos]=0; // set the selected hole to be empty

            pos++;
            if(pos>6 && numMarb>0){
                pos=0;
                if(side == 0){ //goes to the other side of the board
                    side =1;
                }else if(side ==1){
                    side =0;
                }//start from the beginning position from that side of the board
            }
            boolean bank =false;
            while(numMarb>0){ //check to make sure that we still have holes to move
                //go to the next postion in the marbles array

                if(pos == 6){ //check to see if the hole is a goal
                    if(side == gameState.getPlayer_Turn()) { // checks to see if it is the current player's goal
                        marbles[side][pos]++; //add marbles to goal


                        numMarb--; // decrement marbles
                        if(numMarb==0){
                            bank=true;
                        }
                        if(side == 0){ //goes to the other side of the board
                            side =1;
                        }else if(side ==1){
                            side =0;
                        }

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

                }else if(marbles[side][pos]==0 && numMarb==1 && side==gameState.getPlayer_Turn() &&
                        side ==1 && marbles[0][5-pos]>0) { //is the last marble going to land in an empty hole on the current players side
                    //checks to see which player's turn it is
                    marbles[1][6]+=marbles[0][5-pos]+1;//adds the marbles from the hole opposite the previously empty hole to the player's goal
                    marbles[0][5-pos]=0; //sets the hole across from the previously empty hole, empty.
                    numMarb--;//that was the last marble, makes numMarb=0
                }else if (marbles[side][pos]==0 && numMarb==1 && side==gameState.getPlayer_Turn()
                        && side==0&& marbles[1][5-pos]>0){
                    marbles[0][6]+=marbles[1][5-pos]+1;
                    marbles[1][5-pos]=0;
                    numMarb--;//that was the last marble, makes numMarb=0
                }else {
                    marbles[side][pos]++; //adds a marble
                    numMarb--; //one less marble to move
                }if(pos==6 && numMarb>0){
                    pos=0;//start from the beginning position from that side of the board
                }else if(numMarb>0){
                    pos++;
                }

            }
            if(numMarb==0 && !bank) {
                // we are going to switch players if the last marb didnt land in the bank
                if (gameState.getPlayer_Turn() == 1) {
                    gameState.setPlayer_Turn(0);
                } else {
                    gameState.setPlayer_Turn(1);
                }
            }
            //update state
            gameState.setMarble_Pos(marbles);
            // endo edit
            gameState.setMarbles(Player_State.getMarbles());
            gameState.setHoles(Player_State.getHoles());
            // end endo edit


            //send updated state to players
            sendUpdatedStateTo(players[1]);
            sendUpdatedStateTo(players[0]);


            // denote that this was a legal/successful move
            return true;
        }
        else {
            // denote that this was an illegal move
            return false;
        }
    }//makeMove

    public void Reset(){

        boolean resetVal = gameState.getReset();
        gameState = new MancState();
        //sets reset to true if reset so animator will update
        gameState.setReset(resetVal);

        //send updated state to players
        sendUpdatedStateTo(players[1]);
        sendUpdatedStateTo(players[0]);
        //set to false after aminator recieves it
        sleep(30);
        gameState.setReset(false);

    }

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


        int[][] marbles = gameState.getMarble_Pos();
        int count0=0; //if count not equal to zero then game not over
        int count1 =0;
        //counts all marbles left in both rows
        for (int i = 0; i < 6; i++) {
            count0 += marbles[0][i];
            count1 += marbles[1][i];
        }
        //if either row equals zero
        if(count0==0||count1==0) {
            //Adds remaining marbles to respective banks
            marbles[1][6]+=count1;
            marbles[0][6]+=count0;
            //clears rows
            for(int c=0; c<6;c++){
                marbles[0][c]=0;
                marbles[1][c]=0;
            }
            //sends updated banks to state
            gameState.setMarble_Pos(marbles);
            //gets new scores
            int player0Score = gameState.getPlayer0_Score();
            int player1Score = gameState.getPlayer1_Score();
            MyPointF hole = new MyPointF(-1,-1);
            gameState.setSelected_Hole(hole);
            sendUpdatedStateTo(players[0]);
            sendUpdatedStateTo(players[1]);
            //if player0 won
            if (player0Score > player1Score) {

                return  playerNames[0]+" has won. Score: " +player0Score+" to "+player1Score;
            } //if player0 lost
            else if (player0Score < player1Score) {

                return  playerNames[1]+" has won. Score: "+ player1Score+ " to " +player0Score;

            } else if (player0Score == player1Score) { //equal scores aka tie

                return " Tie ";
            }
        }
        return null;
    }


}

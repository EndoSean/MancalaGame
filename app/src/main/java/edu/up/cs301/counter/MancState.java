package edu.up.cs301.counter;

import edu.up.cs301.game.infoMsg.GameState;


/**
 * This contains the state for the Counter game. The state consist of simply
 * the value of the counter.
 *
 * @author Steven R. Vegdahl
 * @version July 2013
 */
public class MancState extends GameState {

    // to satisfy Serializable interface
    private static final long serialVersionUID = 7737393762469851826L;


    private int player_Turn;
    private int[][] Marble_Pos = new int[2][7];//keeps track of the number of marbles in each hole and bank
    private int player0_Score;
    private int player1_Score;

    /**
     * constructor, initializing the player turn, marble position array, player's scores.
     * the value to which the counter's value should be initialized
     */
    public MancState() {

        player_Turn = 0;
        player0_Score = 0;
        player1_Score = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 6; j++) {
                Marble_Pos[i][j] = 4;
            }
        }
    }

    /**
     * copy constructor; makes a copy of the original object
     *
     * @param aMancState the object from which the copy should be made
     */
    public MancState(MancState aMancState) {
        this.setPlayer_Turn(aMancState.getPlayer_Turn());
        this.setPlayer0_Score(aMancState.getPlayer0_Score());
        this.setPlayer1_Score(aMancState.getPlayer1_Score());
        Marble_Pos = aMancState.getMarble_Pos();

    }


    public void setPlayer_Turn(int val) {   player_Turn = val;    }

    public int getPlayer_Turn() {   return player_Turn;    }

    public void setPlayer0_Score(int val) { player0_Score = val;    }

    public int getPlayer0_Score() {
        return player0_Score;
    }

    public void setPlayer1_Score(int val) {
        player1_Score = val;
    }

    public int getPlayer1_Score() {
        return player1_Score;
    }

    public void setMarble_Pos(int[][] position) {   Marble_Pos = position;    }

    public int[][] getMarble_Pos() {
        return Marble_Pos;
    }

}
package edu.up.cs301.Mancala;

import android.graphics.Point;

import edu.up.cs301.game.infoMsg.GameState;


/**
 * This contains the state for the Counter game. The state consist of simply
 * the value of the counter.
 *
 * @author Shayna Noone
 * @version October 2017
 */
public class MancState extends GameState {

    // to satisfy Serializable interface
    private static final long serialVersionUID = 7737393762469851826L;


    private int player_Turn;
    private int[][] Marble_Pos = new int[2][7];//keeps track of the number of marbles in each hole and bank
    private int player0_Score;
    private int player1_Score;
    private Point Selected_Hole; //keeps track of the hole that has been selected // Endo
    private Hole[][] holes = new Hole[2][7];        // Keeps track of marble placements GUI
    private Marble[] marbles = new Marble[48];      // Keeps track of marble positions GUI

    /**
     * constructor, initializing the player turn, marble position array, player's scores.
     * the value to which the counter's value should be initialized
     */
    public MancState() {

        player_Turn = 1;
        player0_Score = 0;
        player1_Score = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 6; j++) {
                Marble_Pos[i][j] = 4;
            }
        }
        Selected_Hole = new Point(-1,-1);
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
        this.setMarble_Pos(aMancState.getMarble_Pos());
        this.setSelected_Hole(aMancState.getSelected_Hole());
        this.setHoles(aMancState.getHoles());
        this.setMarbles(aMancState.getMarbles());
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

    public void setMarble_Pos(int[][] position) {
        Marble_Pos = position;
    setPlayer0_Score(Marble_Pos[0][6]);
    setPlayer1_Score(Marble_Pos[1][6]);}

    public int[][] getMarble_Pos() {
        return Marble_Pos;
    }


    // for keeping track of which hole has been selected // Endo
    public void setSelected_Hole(Point Select){
        Selected_Hole = Select;
    }

    public Point getSelected_Hole(){
        return Selected_Hole;
    }

    public void setHoles(Hole[][] in){
        holes = in;
    }

    public Hole[][] getHoles(){
        return holes;
    }

    public void setMarbles(Marble[] in){
        marbles = in;
    }

    public Marble[] getMarbles(){
        return marbles;
    }

}

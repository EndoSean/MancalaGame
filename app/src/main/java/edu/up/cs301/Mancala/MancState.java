package edu.up.cs301.Mancala;

import android.graphics.Point;

import edu.up.cs301.game.infoMsg.GameState;


/**
 * This contains the state for the Mancala game. The state consist of simply
 * the values or the board, player turn, scores and holes.
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

        int[][] marblepos = aMancState.getMarble_Pos();
        int[][] marbleposCopy = new int[2][7];
        for (int i = 0; i < 2; i++) {
            for(int j=0; j< 7; j++){
                marbleposCopy[i][j]=marblepos[i][j];
            }
        }


        this.setMarble_Pos(marbleposCopy);

        this.setSelected_Hole(aMancState.getSelected_Hole());

        Hole[][] holes = aMancState.getHoles();
        Hole[][] holesCopy = new Hole[2][7];
        for (int i = 0; i < 2; i++) {
            for(int j=0; j< 7; j++){
                holesCopy[i][j]=holes[i][j];
            }
        }
        this.setHoles(holesCopy);

        Marble[] marbles = aMancState.getMarbles();
        Marble[] marbleCopy = new Marble[marbles.length];
        for (int i = 0; i < marbles.length; i++) {
            marbleCopy[i] = marbles[i];
        }
        this.setMarbles(marbleCopy);
    }

    /**
     * getter and setter methods as follows
     */
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

    void setMarble_Pos(int[][] position) {
        if(position==null)return;
       // Marble_Pos = position;
        //also calls to update scores with respect to the new array
        for(int i=0; i<position.length;i++){
            for(int j=0; j<position[i].length;j++){
                this.Marble_Pos[i][j]=position[i][j];
            }
        }
        this.setPlayer0_Score(Marble_Pos[0][6]);
        this.setPlayer1_Score(Marble_Pos[1][6]);}

    public int[][] getMarble_Pos() {
        return Marble_Pos;
    }


    // for keeping track of which hole has been selected // Endo
    void setSelected_Hole(Point Select){
         Selected_Hole = Select;
    }

    Point getSelected_Hole(){
        return Selected_Hole;
    }

    void setHoles(Hole[][] in){
        //holes = in;
        for(int i=0; i<in.length;i++){
            for(int j=0; j<in[i].length;j++){
                this.holes[i][j]=in[i][j];
            }
        }
    }

    Hole[][] getHoles(){
        return holes;
    }

    void setMarbles(Marble[] in){
        marbles = in;

    }

    Marble[] getMarbles(){
        return marbles;
    }



}

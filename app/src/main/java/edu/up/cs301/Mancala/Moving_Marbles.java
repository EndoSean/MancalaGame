package edu.up.cs301.Mancala;

/**
 * Created by endo18 on 11/5/2017.
 */

public class Moving_Marbles {
    private int row;
    private int col;
    private Hole Cor_Hole = new Hole();
    //private ArrayList<Marble> hand = new ArrayList<Marble>();

    public Moving_Marbles(int i, int j, Hole hole){
        row = i;
        col = j;
        Cor_Hole = hole;
    }

    public int getRow(){
        return row;
    }

    public int getCol(){
        return col;
    }

    public Hole getHole(){
        return Cor_Hole;
    }
}

package edu.up.cs301.Mancala;

import android.graphics.PointF;

import java.io.Serializable;

/**
 * Created by endo18 on 11/5/2017.
 */

public class Marble implements Serializable{
    private static final long serialVersionUID = 1437393762469851826L;

    private MyPointF location = new MyPointF();
    private int Color;

    /**
     * Constructor of Class Marbles
     *
     * Initializes marble to be at (0,0) with color=0
     */
    public Marble(){
        location.setMyPointF(0,0);
        Color = 0;

    }
//constructor with paramters
    public Marble(MyPointF xy, int color){
        location.setMyPointF(xy.x, xy.y);
        Color = color;

    }

    public void setLocation(MyPointF xy){
        location.setMyPointF(xy.x, xy.y);
    }

    public void setColor(int color){
        Color = color;
    }

    public MyPointF getLocation(){
        return location;
    }

    public int getColor(){
        return Color;
    }


}

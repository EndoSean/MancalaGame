package edu.up.cs301.Mancala;

import android.graphics.PointF;

import java.io.Serializable;

/**
 * Created by endo18 on 11/5/2017.
 */

public class Marble implements Serializable{
    private static final long serialVersionUID = 1437393762469851826L;

    private PointF location = new PointF();
    private int Color;


    public Marble(){
        location.set(0,0);
        Color = 0;

    }

    public Marble(PointF xy, int color, int count){
        location.set(xy);
        Color = color;

    }

    public void setLocation(PointF xy){
        location.set(xy);
    }

    public void setColor(int color){
        Color = color;
    }

    public PointF getLocation(){
        return location;
    }

    public int getColor(){
        return Color;
    }


}

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


    public Marble(){
        location.setMyPointF(0,0);
        Color = 0;

    }

    public Marble(PointF xy, int color, int count){
        location.setMyPointF(xy.x, xy.y);
        Color = color;

    }

    public void setLocation(PointF xy){
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

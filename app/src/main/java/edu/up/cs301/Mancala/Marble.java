package edu.up.cs301.Mancala;

import android.graphics.PointF;

/**
 * Created by endo18 on 11/5/2017.
 */

public class Marble {
    private PointF location = new PointF();
    private int Color;
    private int Id; //may not be needed

    public Marble(){
        location.set(0,0);
        Color = 0;
        Id = 0;
    }

    public Marble(PointF xy, int color, int count){
        location.set(xy);
        Color = color;
        Id = count;
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

    public int getCount(){ return Id;}
}

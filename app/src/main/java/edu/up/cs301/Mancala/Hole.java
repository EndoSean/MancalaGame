package edu.up.cs301.Mancala;

import android.graphics.PointF;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by endo18 on 11/5/2017.
 */

public class Hole implements Serializable{
    private static final long serialVersionUID = 5727393762469851826L;

    private PointF location = new PointF();
    private int Color;
    ArrayList<Integer> marbles_inside = new ArrayList<Integer>();

    public Hole(){
        location.set(0,0);
        Color = 0;
    }

    public Hole(PointF xy, int color){
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

    public void addMarble(int location){
        marbles_inside.add(location);
    }

    public ArrayList<Integer> takeMarbles(){
        ArrayList<Integer> hold = new ArrayList<Integer>();
        for(int x : marbles_inside){
            hold.add(x);
        }
        marbles_inside.clear();
        return hold;
    }

    public int NumberMarbles(){
        return marbles_inside.size();
    }

}

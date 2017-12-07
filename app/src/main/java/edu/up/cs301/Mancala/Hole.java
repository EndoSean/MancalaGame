package edu.up.cs301.Mancala;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by endo18 on 11/5/2017.
 */

public class Hole implements Serializable{
    private static final long serialVersionUID = 5727393762469851826L;

    private MyPointF location = new MyPointF();
    private int Color;
    ArrayList<Integer> marbles_inside = new ArrayList<Integer>();

    public Hole(){
        location.setMyPointF(0,0);
        Color = 0;
    }

    public Hole(MyPointF xy, int color){
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

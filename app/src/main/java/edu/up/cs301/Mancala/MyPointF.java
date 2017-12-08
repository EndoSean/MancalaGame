package edu.up.cs301.Mancala;

import android.graphics.PointF;

import java.io.Serializable;

/**
 * Created by Shayna Noone on 12/6/2017.
 *copy class of PointF to be serializable
 */

public class MyPointF implements Serializable {

    private static final long serialVersionUID = -455565874321004893L;
    public float x;
    public float y;
//empty contstructor
    public MyPointF(){

    }
    //constructer with paramteters
    public MyPointF(float xval, float yval){
        this.x = xval;
        this.y = yval;
    }


    void setMyPointF(float xval, float yval){
        this.x = xval;
        this.y = yval;
    }

}

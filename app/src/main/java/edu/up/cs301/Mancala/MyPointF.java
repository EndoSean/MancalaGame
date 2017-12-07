package edu.up.cs301.Mancala;

import android.graphics.PointF;

import java.io.Serializable;

/**
 * Created by Shayna Noone on 12/6/2017.
 *
 */

public class MyPointF implements Serializable {

    private static final long serialVersionUID = -455565874321004893L;
    public float x;
    public float y;

    public MyPointF(){

    }
    public MyPointF(float xval, float yval){
        this.x = xval;
        this.y = yval;
    }

    //MyPointF get(){
        //return this;
  //  }

    void setMyPointF(float xval, float yval){
        this.x = xval;
        this.y = yval;
    }

}

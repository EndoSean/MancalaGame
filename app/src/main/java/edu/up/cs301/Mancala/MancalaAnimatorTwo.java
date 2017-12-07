package edu.up.cs301.Mancala;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Random;

import edu.up.cs301.animation.Animator;

/**
 * Created by endo18 on 11/5/2017.
 */

public class MancalaAnimatorTwo implements Animator {
    private Paint rectangles = new Paint();
    private float maxX; // X-coordinate bounds
    private float maxY; // Y-coordinate bounds
    private Hole[][] holes = new Hole[2][7];
    private Marble[] marbles = new Marble[48];
    private Paint marble_color = new Paint();
    private Paint Text_Color = new Paint();
    private String opp = "Opponent";
    private String play = "Player";
    private String Opponent_Turn = "Opponent's Turn";
    private String Player_Turn = "Your Turn";
    private int[][] mar_pos = new int[2][7];
    // hold on to the gameState
    private MancState Current_State = new MancState();
    // boolean to know if we should "invert" the board
    // depends on what value the human player is
    boolean invert;

    public MancalaAnimatorTwo(){
        Text_Color.setColor(Color.BLACK);
        Text_Color.setTextSize(50);
        for(int i = 0; i<2; i++) {
            for (int j = 0; j < 7; j++) {
                if (j==6){
                    mar_pos[i][j]=0;
                }
                else {
                    mar_pos[i][j] = 4;
                }
            }
        }
    }

    public void getBounds(float X, float Y){
        maxX = X;
        maxY = Y;
    }

    public MancState setHoles(){
        MancState Initialize_GUI = new MancState();
        PointF hold = new PointF();
        MyPointF myHold = new MyPointF();
        float x_offset;
        float y_offset;


        for (int i = 1; i>=0; i--){
            if(i == 1) {
                for (int j = 0; j < 7; j++) {
                    if(j == 6){
                        x_offset = (float).915*maxX;
                        y_offset = (float).425*maxY;
                        hold.set(x_offset,y_offset);
                        myHold.setMyPointF(hold.x, hold.y);
                        holes[i][j] = new Hole(myHold, Color.BLACK);
                    }
                    else {
                        x_offset = maxX * (float) .12 * j;
                        y_offset = maxY * (float) .45 * i;
                        hold.set(maxX * (float) .2 + x_offset, maxY * (float) .15 + y_offset);
                        myHold.setMyPointF(hold.x, hold.y);
                        holes[i][j] = new Hole(myHold, Color.BLACK);
                    }
                }
            }
            else {
                for (int j = 0; j < 7 ; j++) {
                    if(j == 6){
                        x_offset = (float).085*maxX;
                        y_offset = (float).425*maxY;
                        hold.set(x_offset,y_offset);
                        myHold.setMyPointF(hold.x, hold.y);
                        holes[i][j] = new Hole(myHold, Color.BLACK);
                    }
                    else {
                        x_offset = maxX * (float) .12 * (5 - j);
                        y_offset = maxY * (float) .45 * i;
                        hold.set(maxX * (float) .2 + x_offset, maxY * (float) .15 + y_offset);
                        myHold.setMyPointF(hold.x, hold.y);
                        holes[i][j] = new Hole(myHold, Color.BLACK);
                    }
                }
            }
        }
        Initialize_GUI.setHoles(holes);
        return Initialize_GUI;
    }

    public MancState setMarbles(int player_number){
        mar_pos = Current_State.getMarble_Pos();
        MyPointF hole;
        Hole mediate;
        PointF placement = new PointF();
        Random ran = new Random();

        // check if inverted board is needed
        if (player_number == 0){
            invert = true;
            int[][] mar_hold = new int[2][7];
            for(int r = 0; r<2; r++){
                for(int c = 0; c<7; c++){
                    mar_hold[r][c] = mar_pos[1-r][c];
                }
            }
            mar_pos = mar_hold;
        }
        else{
            invert = false;
        }


        int random;
        // for rotating colors, the count steps through to the next color after a marble is set
        int[] New_Color = {Color.RED,Color.GREEN,Color.BLUE,Color.YELLOW};
        int Color_Count = 0;

        int count = 0;
        for (int i = 0; i<2; i++){
            for (int j = 0; j<7; j++){
                mediate = holes[i][j];
                hole = mediate.getLocation();
                for(int len = 0; len < mar_pos[i][j]; len++){
                    if(j == 6){                                 // set marble positions for the bank

                        if(hole.x<.5*maxX) {
                            placement.set((float) (.04*maxX +Math.random()*(.08*maxX-maxX/75)+maxX/75 ),
                                    (float) (.15*maxY+Math.random()*(.55*maxY-2*maxX/75))+maxX/75);
                        }else{
                            placement.set((float) (.87*maxX  +Math.random()*(.08*maxX-maxX/75)+maxX/75 ),
                                    (float) (.15*maxY+Math.random()*(.55*maxY-2*maxX/75)+maxX/75));
                        }
                        marbles[count] = new Marble(placement, New_Color[Color_Count], count);
                        count++;
                        if (Color_Count == 3){                  //wrap array back around
                            Color_Count = 0;
                        }
                        else {
                            Color_Count++;
                        }

                    }
                    else {                                      // set the marble positions for the holes
                        random = ran.nextInt(4);
                        if (random == 0) {
                            placement.set(hole.x + maxX / (ran.nextInt(150 - 70) + 70), hole.y + maxX / (ran.nextInt(150 - 70) + 70));
                            marbles[count] = new Marble(placement, New_Color[Color_Count], count);
                            count++;
                            if (Color_Count == 3) {              //wrap array back around
                                Color_Count = 0;
                            } else {
                                Color_Count++;
                            }
                        } else if (random == 1) {
                            placement.set(hole.x + maxX / (ran.nextInt(150 - 70) + 70), hole.y - maxX / (ran.nextInt(150 - 70) + 70));
                            marbles[count] = new Marble(placement, New_Color[Color_Count], count);
                            count++;
                            if (Color_Count == 3) {              //wrap array back around
                                Color_Count = 0;
                            } else {
                                Color_Count++;
                            }
                        } else if (random == 2) {
                            placement.set(hole.x - maxX / (ran.nextInt(150 - 70) + 70), hole.y + maxX / (ran.nextInt(150 - 70) + 70));
                            marbles[count] = new Marble(placement, New_Color[Color_Count], count);
                            count++;
                            if (Color_Count == 3) {              //wrap array back around
                                Color_Count = 0;
                            } else {
                                Color_Count++;
                            }
                        } else if (random == 3) {
                            placement.set(hole.x - maxX / (ran.nextInt(150 - 70) + 70), hole.y - maxX / (ran.nextInt(150 - 70) + 70));
                            marbles[count] = new Marble(placement, New_Color[Color_Count], count);
                            count++;
                            if (Color_Count == 3) {              //wrap array back around
                                Color_Count = 0;
                            } else {
                                Color_Count++;
                            }
                        }
                    }
                }
            }
        }
        Current_State.setMarbles(marbles);
        return Current_State;
    }


    /**
     * Interval between animation frames: .03 seconds (i.e., about 33 times
     * per second).
     *
     * @return the time interval between frames, in milliseconds.
     */
    //@Override
    public int interval() {
        return 30;
    }

    /**
     * The background color: black.
     *
     * @return the background color onto which we will draw the image.
     */
    //@Override
    public int backgroundColor() {
        // create/return the background color
        return Color.rgb(140,180,255); // light blue

    }

    public void tick(Canvas g) {
        rectangles.setColor(Color.BLACK);
        g.drawRect((float) .025 * maxX, (float) .015 * maxY, (float) .975 * maxX, (float) .75 * maxY, rectangles);
        rectangles.setColor(0xffA0522D);
        g.drawRect((float) .03 * maxX, (float) .02 * maxY, (float) .97 * maxX, (float) .745 * maxY, rectangles);

        // draw banks
        rectangles.setColor(Color.BLACK);
        g.drawRect((float) .035 * maxX, (float) .145 * maxY, (float) .135 * maxX, (float) .705 * maxY, rectangles);
        g.drawRect((float) .865 * maxX, (float) .145 * maxY, (float) .965 * maxX, (float) .705 * maxY, rectangles);
        rectangles.setColor(Color.WHITE);
        g.drawRect((float) .04 * maxX, (float) .15 * maxY, (float) .13 * maxX, (float) .7 * maxY, rectangles);
        g.drawRect((float) .87 * maxX, (float) .15 * maxY, (float) .96 * maxX, (float) .7 * maxY, rectangles);

        g.drawText(opp, (float) .03 * maxX, (float) .135 * maxY, Text_Color); //Opponent
        g.drawText(play, (float) .86 * maxX, (float) .135 * maxY, Text_Color);//Player

        Text_Color.setTextSize(100);
        if(invert){
            if(Current_State.getPlayer_Turn() == 0){
                g.drawText(Player_Turn, (float) .4 * maxX, (float) .4 * maxY, Text_Color);
            }
            else{
                g.drawText(Opponent_Turn, (float) .35 * maxX, (float) .4 * maxY, Text_Color);
            }
        }
        else{
            if(Current_State.getPlayer_Turn() == 1){
                g.drawText(Player_Turn, (float) .4 * maxX, (float) .4 * maxY, Text_Color);
            }
            else{
                g.drawText(Opponent_Turn, (float) .35 * maxX, (float) .4 * maxY, Text_Color);
            }
        }
        Text_Color.setTextSize(50);


        g.drawText("" + mar_pos[0][6], (float) .03 * maxX, (float) .135 * maxY - 70, Text_Color);
        g.drawText("" + mar_pos[1][6], (float) .86 * maxX, (float) .135 * maxY - 70, Text_Color);

        mar_pos = Current_State.getMarble_Pos();
        // invert board if necessary
        if (invert){
            int[][] mar_hold = new int[2][7];
            for(int r = 0; r<2; r++){
                for(int c = 0; c<7; c++){
                    mar_hold[r][c] = mar_pos[1-r][c];
                }
            }
            mar_pos = mar_hold;
        }

        //draw holes
        MyPointF hold = new MyPointF();
        Hole mediate;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 6; j++) {
                mediate = holes[i][j];

                hold = mediate.getLocation();

                rectangles.setColor(mediate.getColor());
                g.drawCircle(hold.x, hold.y, maxX / 18, rectangles);
                rectangles.setColor(Color.WHITE);
                g.drawCircle(hold.x, hold.y, maxX / 20, rectangles);
                if (i == 0) {
                    g.drawText("" + mar_pos[i][j], hold.x - (float) .01 * maxX, hold.y - (float) .09 * maxY, Text_Color);
                } else {
                    g.drawText("" + mar_pos[i][j], hold.x - (float) .01 * maxX, hold.y + (float) .12 * maxY, Text_Color);
                }
            }
        }

        //draw the marbles
        Marble marble_hold;
        MyPointF location;
        for (int size = 0; size < 48; size++) {
            marble_hold = marbles[size];
            location = marble_hold.getLocation();
            marble_color.setColor(marble_hold.getColor());
            g.drawCircle(location.x, location.y, maxX / 75, marble_color);

        }
    }

    /**
     * Tells that we never pause.
     *
     * @return indication of whether to pause
     */
    //@Override
    public boolean doPause() {
        return false;
    }

    /**
     * Tells that we never stop the animation.
     *
     * @return indication of whether to quit.
     */
    //@Override
    public boolean doQuit() {
        return false;
    }

    /**
     * event method when screen is touched
     */
    //@Override
    //public Point onTouch(MotionEvent event){
    public void onTouch(MotionEvent event) {
        Hole mediate;
        Point Selected_Hole = new Point(-1,-1);
        MyPointF hold;
        PointF inside = new PointF();
        int action = event.getAction();
        float xPos = event.getX();
        float yPos = event.getY();
        for(int i = 0; i<2; i++){
            for(int j = 0; j<6; j++){
                mediate = holes[i][j];
                hold = mediate.getLocation();
                inside.set(xPos-hold.x,yPos-hold.y);
                if(Math.abs(inside.x) < maxX/18 && Math.abs(inside.y) < maxX/18 && action == MotionEvent.ACTION_UP){
                    if(mar_pos[i][j] == 0 || i == 0){ //check to see if any marbles are in the hole or on wrong side
                        mediate.setColor(Color.RED);
                        holes[i][j] = mediate;
                    }
                    else {
                        mediate.setColor(Color.GREEN);
                        holes[i][j] = mediate;
                        if(invert){                     // send the inverted choice
                            Selected_Hole.set(0,j);
                        }
                        else {
                            Selected_Hole.set(i, j);
                        }

                    }
                }
                else{
                    mediate.setColor(Color.BLACK);
                    holes[i][j] = mediate;
                }
            }
        }

        Current_State.setHoles(holes);
        Current_State.setSelected_Hole(Selected_Hole);
    }

    public MancState getUpdatedState(){
        return Current_State;
    }
    public void setState(MancState state) {
        Current_State = state;
    }

}

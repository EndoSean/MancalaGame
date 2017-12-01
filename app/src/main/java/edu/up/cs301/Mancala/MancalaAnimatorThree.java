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

public class MancalaAnimatorThree implements Animator {
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

    Point Selected_Hole = new Point();
    boolean Same_Hole = false;
    boolean initialized = false;                                // Keeps track if board has been initialized
    private float xvel = 10;
    private float yvel = 10;
    ArrayList<Point> Receiving_Holes = new ArrayList<Point>();      // used to reference holes while marble is moving
    ArrayList<Integer> moving;                                      // keeps track of marbles in motion

    public MancalaAnimatorThree(){
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
        float x_offset;
        float y_offset;


        for (int i = 1; i>=0; i--){
            if(i == 1) {
                for (int j = 0; j < 7; j++) {
                    if(j == 6){
                        x_offset = (float).915*maxX;
                        y_offset = (float).425*maxY;
                        hold.set(x_offset,y_offset);
                        holes[i][j] = new Hole(hold, Color.BLACK);
                    }
                    else {
                        x_offset = maxX * (float) .12 * j;
                        y_offset = maxY * (float) .45 * i;
                        hold.set(maxX * (float) .2 + x_offset, maxY * (float) .15 + y_offset);
                        holes[i][j] = new Hole(hold, Color.BLACK);
                    }
                }
            }
            else {
                for (int j = 0; j < 7 ; j++) {
                    if(j == 6){
                        x_offset = (float).085*maxX;
                        y_offset = (float).425*maxY;
                        hold.set(x_offset,y_offset);
                        holes[i][j] = new Hole(hold, Color.BLACK);
                    }
                    else {
                        x_offset = maxX * (float) .12 * (5 - j);
                        y_offset = maxY * (float) .45 * i;
                        hold.set(maxX * (float) .2 + x_offset, maxY * (float) .15 + y_offset);
                        holes[i][j] = new Hole(hold, Color.BLACK);
                    }
                }
            }
        }
        Initialize_GUI.setHoles(holes);
        return Initialize_GUI;
    }

    public MancState setMarbles(int player_number) {
        mar_pos = Current_State.getMarble_Pos();
        PointF hole;
        Hole mediate;
        PointF placement = new PointF();
        Random ran = new Random();

//        // check if its the starting arrangement
//        boolean begin = true;
//        for(int i = 0; i<2; i++){
//            for(int j = 0; j<6; j++) {
//                if(mar_pos[i][j] == 4){
//                    // begin = true;
//                }
//                else{
//                    begin = false;
//                }
//            }
//        }
        // Initial Setup
        if(!initialized) {
            int count = 0;
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 6; j++) {
                    ///////////hole = holes[i][j];
                    mediate = holes[i][j];
                    hole = mediate.getLocation();
                    placement.set(hole.x + maxX / 75, hole.y);
                    marbles[count] = new Marble(placement, Color.RED, count);
                    mediate.addMarble(count);
                    count++;
                    placement.set(hole.x, hole.y + maxX / 75);
                    marbles[count] = new Marble(placement, Color.GREEN, count);
                    mediate.addMarble(count);
                    count++;
                    placement.set(hole.x - maxX / 75, hole.y);
                    marbles[count] = new Marble(placement, Color.BLUE, count);
                    mediate.addMarble(count);
                    count++;
                    placement.set(hole.x, hole.y - maxX / 75);
                    marbles[count] = new Marble(placement, Color.YELLOW, count);
                    mediate.addMarble(count);
                    count++;
                    holes[i][j] = mediate;
                }
            }
            Current_State.setHoles(holes);
            Current_State.setMarbles(marbles);
            initialized = true;
            return Current_State;
        }


        // check if inverted board is needed
        if (player_number == 0) {
            invert = true;
            int[][] mar_hold = new int[2][7];
            for (int r = 0; r < 2; r++) {
                for (int c = 0; c < 7; c++) {
                    mar_hold[r][c] = mar_pos[1 - r][c];
                }
            }
            mar_pos = mar_hold;
        } else {
            invert = false;
        }

        if(Selected_Hole == Current_State.getSelected_Hole()){
            Same_Hole = true;
        }
        else {
            Selected_Hole = Current_State.getSelected_Hole();
        }

        // sets up marbles to be moved
        // updates variables responsible for moving marbles
        if (Selected_Hole.x == -1) {
            return Current_State;
        }
        int x = Selected_Hole.x;
        int y = Selected_Hole.y;
        Hole mediate2 = holes[x][y];             // holds the new state of holes to by copied
        Hole secondary;
        //ArrayList<Integer> moving;


        if(!Same_Hole) {
            moving = mediate2.takeMarbles();         //takes marbles and clears hole
        }
        holes[x][y] = mediate2;                  // update hole
        int size = moving.size();               // how many marbles are being moved
        //int overflow = 0;
        Point holding;
        //Marble marble;
        for (int a = 1; a <= size; a++) {           // for the number of marbles
            if ((a + y) % 7 == 0) {/////////////////////////////////////////////
                if (x == 1) {
                    x = 0;
                } else {
                    x = 1;
                }
                //overflow = 7 - (a+y);/////////////////////////////////////////////////////
                y = -a;                          // begin index at 0 again
                secondary = holes[x][y + a];
                holding = new Point(x, y + a);                  // used to reference receiving holes
                Receiving_Holes.add(holding);
                secondary.addMarble(moving.get(a - 1));         // add the marble to the new hole
                holes[x][y + a] = secondary;
            } else {
                secondary = holes[x][y + a];
                holding = new Point(x, y + a);
                Receiving_Holes.add(holding);
                secondary.addMarble(moving.get(a - 1));
                holes[x][y + a] = secondary;
            }

        }
        Current_State.setHoles(holes);
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
        PointF hold = new PointF();
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

        //draw marbles
        Marble marble_hold = new Marble();
        PointF location = new PointF();
        Point New_Location = new Point();
        Hole New_Hole;
        PointF vector = new PointF();
        PointF Marble_Location;
        float magnitude;
        PointF check = new PointF();                       //check to see if marble is inside hole

        //make adjustments to the marbles that are moving
        if(moving != null) {
            if (moving.size() != 0) {                                   // if there is something to move
                boolean[] truth_table = new boolean[moving.size()];     // tells when to stop updating the marbles
                for (int length = 0; length < moving.size(); length++) {
                    truth_table[length] = true;                         // set to "need to move"
                }
                //int row;
                //int col;
                for (int inc = 0; inc < moving.size(); inc++) {
                    marble_hold = marbles[moving.get(inc)];             // get the marble
                    Marble_Location = marble_hold.getLocation();        // get marble location
                    New_Location = Receiving_Holes.get(inc);            // location of hole in array
                    New_Hole = holes[New_Location.x][New_Location.y];   // get the corresponding hole

                    // stop the moving of the marbles by removing the marble from arraylist
                    location = New_Hole.getLocation();
                    check.set(Marble_Location.x - location.x, Marble_Location.y - location.y);
                    if (Math.abs(check.x) < maxX / 50 && Math.abs(check.y) < maxX / 50) {
                        truth_table[inc] = false;                       //signifies to be deleted
                    } else {
                        location = New_Hole.getLocation();
                        vector = new PointF(location.x - Marble_Location.x, location.y - Marble_Location.y);
                        magnitude = (float) Math.sqrt(vector.x * vector.x + vector.y * vector.y);
                        vector.set(vector.x / magnitude, vector.y / magnitude);
                        vector.set(Marble_Location.x + vector.x * xvel, Marble_Location.y + vector.y * yvel);
                        marble_hold.setLocation(vector);
                        marbles[moving.get(inc)] = marble_hold;
                    }
                }
                for (int len = 0; len < truth_table.length; len++) {         //remove element if done moving
                    if (!truth_table[len]) {
                        moving.remove(len);
                        Receiving_Holes.remove(len);
                        if (len == 1){
                            Same_Hole = false;
                        }
                    }
                }
            }
        }

        //draw the marbles
       // Marble marble_hold;
        //PointF location;
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
        PointF hold;
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
                        //Is_Moving = true;
                        //To_Move = mediate;
                    }
                }
                else{
                    mediate.setColor(Color.BLACK);
                    holes[i][j] = mediate;
                }
            }
        }
        //Current_State.setMarbles(marbles);
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


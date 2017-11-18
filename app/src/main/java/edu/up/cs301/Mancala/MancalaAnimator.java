package edu.up.cs301.Mancala;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.view.MotionEvent;

import java.util.ArrayList;

import edu.up.cs301.animation.Animator;

/**
 * Created by endo18 on 11/5/2017.
 */

public class MancalaAnimator implements Animator {

    private Paint rectangles = new Paint();
    private float maxX; // X-coordinate bounds
    private float maxY; // Y-coordinate bounds
    //////private PointF[][] holes = new PointF[2][6];
    private Hole[][] holes = new Hole[2][7];
    private Marble[] marbles = new Marble[48];
    private Paint marble_color = new Paint();
    private Paint Text_Color = new Paint();
    //private boolean Is_Moving = false;
    //private Hole To_Move = new Hole();

    //dummy text for now
    private String opp = "Opponent";
    private String play = "Player";
    private int[][] mar_pos = new int[2][7];

    //velocity of marbles
    private float xvel;
    private float yvel;

    // hold on to the gameState
    private MancState Current_State = new MancState();

    ArrayList<Point> Receiving_Holes = new ArrayList<Point>();      // used to reference holes while marble is moving
    ArrayList<Integer> moving;                                      // keeps track of marbles in motion


    public MancalaAnimator(){
        Text_Color.setColor(Color.BLACK);
        Text_Color.setTextSize(50);
        for(int i = 0; i<2; i++) {
            for (int j = 0; j < 7; j++) {
                if (j==6){
                }
                else {
                    mar_pos[i][j] = 4;
                }
            }
        }
        xvel = 20;
        yvel = 20;
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
                        hold.set(maxX * (float) .2 + x_offset, maxY * (float) .15 + y_offset);
                        holes[i][j] = new Hole(hold, Color.BLACK);
                    }
                    else {
                        x_offset = maxX * (float) .12 * j;
                        y_offset = maxY * (float) .45 * i;
                        hold.set(maxX * (float) .2 + x_offset, maxY * (float) .15 + y_offset);
                        //////holes[i][j] = new PointF(hold.x,hold.y);
                        holes[i][j] = new Hole(hold, Color.BLACK);
                    }
                }
            }
            else {
                for (int j = 0; j < 7 ; j++) {
                    if(j == 6){
                        x_offset = (float).085*maxX;
                        y_offset = (float).425*maxY;
                        hold.set(maxX * (float) .2 + x_offset, maxY * (float) .15 + y_offset);
                        holes[i][j] = new Hole(hold, Color.BLACK);
                    }
                    else {
                        x_offset = maxX * (float) .12 * (5 - j);
                        y_offset = maxY * (float) .45 * i;
                        hold.set(maxX * (float) .2 + x_offset, maxY * (float) .15 + y_offset);
                        //////holes[i][j] = new PointF(hold.x,hold.y);
                        holes[i][j] = new Hole(hold, Color.BLACK);
                    }
                }
            }
        }
        Initialize_GUI.setHoles(holes);
        return Initialize_GUI;
    }

    //set marble positions based on Marble_Pos
    public MancState setMarbles(MancState CurrentState){
        // Update from gameState
        Current_State = CurrentState;
        mar_pos = CurrentState.getMarble_Pos();


        //check if its the starting arrangement
        boolean begin = true;
        for(int i = 0; i<2; i++){
            for(int j = 0; j<6; j++) {
                if(mar_pos[i][j] == 4){
                   // begin = true;
                }
                else{
                    begin = false;
                }
            }
        }
        if(begin){
        //run initialize
            Current_State = this.setMarbles2();
        }
        else {
            //run changes based on Selected_Hole
            //clear previous values


            holes = Current_State.getHoles();
            marbles = Current_State.getMarbles();
            mar_pos = Current_State.getMarble_Pos();
            Point Selected_Hole = Current_State.getSelected_Hole();


            // sets up marbles to be moved
            // updates variables responsible for moving marbles
            if (Selected_Hole.x == -1) {
                return CurrentState;
            }
            int x = Selected_Hole.x;
            int y = Selected_Hole.y;
            Hole mediate = holes[x][y];             // holds the new state of holes to by copied
            Hole secondary;
            //ArrayList<Marble> moving;
            moving = mediate.takeMarbles();         //takes marbles and clears hole
            holes[x][y] = mediate;                  // update hole
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
        }
        return Current_State;

    }

    public MancState setMarbles2(){
        MancState Initialize_GUI = new MancState();
        PointF hole = new PointF();
        Hole mediate;
        PointF placement = new PointF();
        //int counti = 0;
        //int countj = 0;
        int count = 0;
        for (int i = 0; i<2; i++){
            for (int j = 0; j<6; j++){
                ///////////hole = holes[i][j];
                mediate = holes[i][j];
                hole = mediate.getLocation();
                placement.set(hole.x+maxX/75,hole.y);
                marbles[count] = new Marble(placement,Color.RED,count);
                mediate.addMarble(count);
                count++;
                placement.set(hole.x, hole.y+maxX/75);
                marbles[count] = new Marble(placement,Color.GREEN,count);
                mediate.addMarble(count);
                count++;
                placement.set(hole.x-maxX/75,hole.y);
                marbles[count] = new Marble(placement,Color.BLUE,count);
                mediate.addMarble(count);
                count++;
                placement.set(hole.x,hole.y-maxX/75);
                marbles[count] = new Marble(placement,Color.YELLOW,count);
                mediate.addMarble(count);
                count++;
                holes[i][j] = mediate;
            }
        }
        Initialize_GUI.setMarbles(marbles);
        return Initialize_GUI;
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
        //return Color.BLACK; // returns Black
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

        g.drawText(""+mar_pos[0][6], (float) .03 * maxX, (float) .135 * maxY-70, Text_Color);
        g.drawText(""+mar_pos[1][6], (float) .86 * maxX, (float) .135 * maxY-70, Text_Color);
        //draw holes
        PointF hold = new PointF();
        Hole mediate;
        for (int i = 0; i <2; i++) {
            for (int j = 0; j < 6; j++) {
                mediate = holes[i][j];
                //////hold = holes[i][j];
                hold = mediate.getLocation();
                ///////rectangles.setColor(Color.BLACK);
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
                    check.set(Marble_Location.x - hold.x, Marble_Location.y - hold.y);
                    if (Math.abs(check.x) < maxX / 10 && Math.abs(check.y) < maxX / 10) {
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
                    }
                }
            }
        }

        //Updates holes to account for change in marbles from action
        //holes marble array is never accessed and may be redundant or we can reduce the number of
        //marble arrays to be exclusively in the holes class and not have several different ones to navigate
        mar_pos = Current_State.getMarble_Pos();
        for(int r=0; r<2;r++ ){
            for(int c=0; c<6; c++){
                ArrayList tempHole = holes[r][c].takeMarbles();
                if(mar_pos[r][c] < holes[r][c].marbles_inside.size()){ //if the current pos contains more marbles
                    // than the hole says it does, then remove the last in the array
                    int dif= tempHole.size()-mar_pos[r][c];
                    for(int d=0; d<dif; d++) {
                        tempHole.remove(tempHole.size()-1);
                    }
                    holes[r][c].marbles_inside=tempHole;
                }
                while(mar_pos[r][c] > holes[r][c].marbles_inside.size()){
                    holes[r][c].addMarble(1); //unsure what location to add to

                }
            }
        }

        //Locations are never updated for marbles array even though mar_pos is changed.
        //Might consider randomizing location of marbles to be contained within hole since who
        // wants to specify 48 location if perchance all the marbles are put in one bank?

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
                        Selected_Hole.set(i,j);
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
        Current_State.setMarbles(marbles);
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

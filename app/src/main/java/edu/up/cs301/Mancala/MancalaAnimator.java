package edu.up.cs301.Mancala;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.view.MotionEvent;

import java.util.ArrayList;

import edu.up.cs301.animation.Animator;

import static android.os.SystemClock.sleep;


/**
 * Created by endo18 on 11/27/2017.
 */

public class MancalaAnimator implements Animator {
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

    MyPointF Selected_Hole = new MyPointF();
    boolean Currently_Moving = false;                           // Halts setMarbles until marbles are done moving
    boolean Same_Hole = false;                                  // To avoid overwrites due to weird multi-threading
    boolean initialized = false;                                // Keeps track if board has been initialized
    // Speed of the marble animations (does not the speed of 25)
    private float xvel = 25;
    private float yvel = 25;

    ArrayList<Point> Receiving_Holes = new ArrayList<Point>();      // used to reference holes while marble is moving
    ArrayList<Integer> moving;                                      // keeps track of marbles in motion
    ArrayList<Point> Receiving_Holes2 = new ArrayList<Point>();      // used to reference holes while marble is moving
    ArrayList<Integer> moving2;                                      // keeps track of marbles in motion
    boolean wait = false;                                       // Tells the animation to wait for setMarbles to finish
    boolean capture = false;
    private String Capture_Text = "Captured!!!";
    boolean start = true;

    public MancalaAnimator() {
        Text_Color.setColor(Color.BLACK);
        Text_Color.setTextSize(50);
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 7; j++) {
                if (j == 6) {
                    mar_pos[i][j] = 0;
                } else {
                    mar_pos[i][j] = 4;
                }
            }
        }
    }

    public void getBounds(float X, float Y) {
        maxX = X;
        maxY = Y;
    }

    public MancState setHoles() {
        MancState Initialize_GUI = new MancState();
        MyPointF hold = new MyPointF();
        float x_offset;
        float y_offset;

        for (int i = 1; i >= 0; i--) {
            if (i == 1) {
                for (int j = 0; j < 7; j++) {
                    if (j == 6) {
                        x_offset = (float) .915 * maxX;
                        y_offset = (float) .425 * maxY;
                        hold.setMyPointF(x_offset, y_offset);
                        holes[i][j] = new Hole(hold, Color.BLACK);
                    } else {
                        x_offset = maxX * (float) .12 * j;
                        y_offset = maxY * (float) .45 * i;
                        hold.setMyPointF(maxX * (float) .2 + x_offset, maxY * (float) .15 + y_offset);
                        holes[i][j] = new Hole(hold, Color.BLACK);
                    }
                }
            } else {
                for (int j = 0; j < 7; j++) {
                    if (j == 6) {
                        x_offset = (float) .085 * maxX;
                        y_offset = (float) .425 * maxY;
                        hold.setMyPointF(x_offset, y_offset);
                        holes[i][j] = new Hole(hold, Color.BLACK);
                    } else {
                        x_offset = maxX * (float) .12 * (5 - j);
                        y_offset = maxY * (float) .45 * i;
                        hold.setMyPointF(maxX * (float) .2 + x_offset, maxY * (float) .15 + y_offset);
                        holes[i][j] = new Hole(hold, Color.BLACK);
                    }
                }
            }
        }
        Initialize_GUI.setHoles(holes);
        return Initialize_GUI;
    }

    public synchronized MancState setMarbles(int player_number) {
        wait = true; // halt the animation
        mar_pos = Current_State.getMarble_Pos();
        MyPointF hole;
        Hole mediate;
        MyPointF placement = new MyPointF();


        // Check end game status
        if ((mar_pos[0][6] + mar_pos[1][6]) == 48){
            this.endGame(player_number);
            Current_State.setHoles(holes);
            Current_State.setMarbles(marbles);
            Receiving_Holes.clear();
            moving.clear();
            wait = false;
            return Current_State;
        }

        // Initial Setup
        if (!initialized || Current_State.getReset()) {
            int count = 0;
            setHoles();
            marbles = new Marble[48];
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 6; j++) {
                    mediate = holes[i][j];
                    hole = mediate.getLocation();
                    placement.setMyPointF(hole.x + maxX / 75, hole.y);
                    marbles[count] = new Marble(placement, Color.RED);
                    mediate.addMarble(count);
                    count++;
                    placement.setMyPointF(hole.x, hole.y + maxX / 75);
                    marbles[count] = new Marble(placement, Color.GREEN);
                    mediate.addMarble(count);
                    count++;
                    placement.setMyPointF(hole.x - maxX / 75, hole.y);
                    marbles[count] = new Marble(placement, Color.BLUE);
                    mediate.addMarble(count);
                    count++;
                    placement.setMyPointF(hole.x, hole.y - maxX / 75);
                    marbles[count] = new Marble(placement, Color.YELLOW);
                    mediate.addMarble(count);
                    count++;
                    holes[i][j] = mediate;
                }
            }
            Current_State.setHoles(holes);
            Current_State.setMarbles(marbles);
            Current_State.setReset(false);
            initialized = true;
            wait = false;
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

        // prevents the overwriting of moving variable
        if (Selected_Hole == Current_State.getSelected_Hole()) {
            Same_Hole = true;
        } else {
            Selected_Hole = Current_State.getSelected_Hole();
        }
        // sets up marbles to be moved
        // updates variables responsible for moving marbles
        if (Selected_Hole.x == -1) {
            wait = false;
            return Current_State;
        }
        // Inversion of board
        int x;
        int y;
        if (invert) {
            if (Selected_Hole.x == 0) {
                x = 1;
            } else {
                x = 0;
            }
            y = (int)Selected_Hole.y;
        } else {
            x = (int)Selected_Hole.x;
            y = (int)Selected_Hole.y;
        }
        Hole mediate2 = holes[x][y];             // holds the new state of holes to by copied
        Hole secondary;


        if (!Same_Hole) {
            moving = mediate2.takeMarbles();         //takes marbles and clears hole

            holes[x][y] = mediate2;                  // update hole
            int player = x;                         // keep track of whose side
            int size = moving.size();               // how many marbles are being moved

            Point holding;

            for (int a = 1; a <= size; a++) {           // for the number of marbles
                if ((a + y) % 7 == 0) {
                    if (x == 1) {
                        x = 0;
                    } else {
                        x = 1;
                    }
                    y = -a;                          // begin index at 0 again
                    if (a == size) {                         // last marble
                        if (player == x && (y + a != 6) && mar_pos[x][y + a] == 0) {  // last marble is on same side
                            secondary = holes[x][y+a];
                            holding = new Point(x, y+a);
                            Receiving_Holes.add(holding);
                            secondary.addMarble(moving.get(a - 1));         // add the marble to the bank
                            holes[x][y+a] = secondary;
                            int other;
                            if (x == 0) {                                      // flip sides
                                other = 1;
                            } else {
                                other = 0;
                            }

                            Hole mediate4 = holes[x][y+a];
                            ArrayList<Integer> temp = mediate4.takeMarbles();
                            holes[x][y+a] = mediate4;

                            Hole mediate3 = holes[other][5 - (y + a)];             // holds the new state of holes to by copied
                            Hole secondary2;
                            if (mediate3.NumberMarbles() != 0) {
                                moving2 = mediate3.takeMarbles();         //takes marbles and clears hole
                                moving2.add(temp.get(0));
                                temp.clear();
                                holes[other][5 - (y + a)] = mediate3;                  // update hole
                                int size2 = moving2.size();               // how many marbles are being moved
                                Point holding2;
                                for (int a2 = 1; a2 <= size2; a2++) {           // for the number of marbles
                                    secondary2 = holes[x][6];
                                    holding2 = new Point(x, 6);                  // bank
                                    Receiving_Holes2.add(holding2);
                                    secondary2.addMarble(moving2.get(a2 - 1));         // add the marble to the bank
                                    holes[x][6] = secondary2;
                                }
                                capture = true;
                            }

                        } else {
                            secondary = holes[x][y + a];
                            holding = new Point(x, y + a);                  // used to reference receiving holes
                            Receiving_Holes.add(holding);
                            secondary.addMarble(moving.get(a - 1));         // add the marble to the new hole
                            holes[x][y + a] = secondary;
                        }
                    } else {
                        secondary = holes[x][y + a];
                        holding = new Point(x, y + a);                  // used to reference receiving holes
                        Receiving_Holes.add(holding);
                        secondary.addMarble(moving.get(a - 1));         // add the marble to the new hole
                        holes[x][y + a] = secondary;
                    }
                } else {
                    if (x != player && y + a == 6) {
                        y = -a;                          // begin index at 0 again
                        if (x == 1) {
                            x = 0;
                        } else {
                            x = 1;
                        }
                        secondary = holes[x][y + a];
                        holding = new Point(x, y + a);
                        Receiving_Holes.add(holding);
                        secondary.addMarble(moving.get(a - 1));
                        holes[x][y + a] = secondary;
                    } else {
                        if (a == size) {                         // last marble
                            if (player == x && (y + a != 6) && mar_pos[x][y + a] == 0) {  // last marble is on same side
                                secondary = holes[x][y+a];
                                holding = new Point(x, y+a);
                                Receiving_Holes.add(holding);
                                secondary.addMarble(moving.get(a - 1));         // add the marble to the bank
                                holes[x][y+a] = secondary;
                                int other;
                                if (x == 0) {                                      // flip sides
                                    other = 1;
                                } else {
                                    other = 0;
                                }

                                Hole mediate4 = holes[x][y+a];
                                ArrayList<Integer> temp = mediate4.takeMarbles();
                                holes[x][y+a] = mediate4;

                                Hole mediate3 = holes[other][5 - (y + a)];             // holds the new state of holes to by copied
                                Hole secondary2;
                                if (mediate3.NumberMarbles() != 0) {
                                    moving2 = mediate3.takeMarbles();         //takes marbles and clears hole
                                    moving2.add(temp.get(0));
                                    temp.clear();
                                    holes[other][5 - (y + a)] = mediate3;                  // update hole
                                    int size2 = moving2.size();               // how many marbles are being moved
                                    Point holding2;
                                    for (int a2 = 1; a2 <= size2; a2++) {           // for the number of marbles
                                        secondary2 = holes[x][6];
                                        holding2 = new Point(x, 6);                  // bank
                                        Receiving_Holes2.add(holding2);
                                        secondary2.addMarble(moving2.get(a2 - 1));         // add the marble to the bank
                                        holes[x][6] = secondary2;
                                    }
                                    capture = true;
                                }
                            } else {
                                secondary = holes[x][y + a];
                                holding = new Point(x, y + a);
                                Receiving_Holes.add(holding);
                                secondary.addMarble(moving.get(a - 1));
                                holes[x][y + a] = secondary;
                            }
                        } else {
                            secondary = holes[x][y + a];
                            holding = new Point(x, y + a);
                            Receiving_Holes.add(holding);
                            secondary.addMarble(moving.get(a - 1));
                            holes[x][y + a] = secondary;
                        }
                    }
                }

            }
        }

        Current_State.setHoles(holes);
        Current_State.setMarbles(marbles);
        wait = false;
        return Current_State;
    }


    public void endGame(int player_number){              // draw marbles in their banks
        MyPointF hole;
        Hole mediate;
        MyPointF placement = new MyPointF();
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
        // for rotating colors, the count steps through to the next color after a marble is set
        int[] New_Color = {Color.RED,Color.GREEN,Color.BLUE,Color.YELLOW};
        int Color_Count = 0;
        int count = 0;
        for (int i = 0; i<2; i++) {
            mediate = holes[i][6];
            hole = mediate.getLocation();
            for (int len = 0; len < mar_pos[i][6]; len++) {
                if (hole.x < .5 * maxX) {
                    placement.setMyPointF((float) (.04 * maxX + Math.random() * (.08 * maxX - maxX / 75) + maxX / 75),
                            (float) (.15 * maxY + Math.random() * (.55 * maxY - 2 * maxX / 75)) + maxX / 75);
                } else {
                    placement.setMyPointF((float) (.87 * maxX + Math.random() * (.08 * maxX - maxX / 75) + maxX / 75),
                            (float) (.15 * maxY + Math.random() * (.55 * maxY - 2 * maxX / 75) + maxX / 75));
                }
                marbles[count] = new Marble(placement, New_Color[Color_Count]);
                count++;
                if (Color_Count == 3) {                  //wrap array back around
                    Color_Count = 0;
                } else {
                    Color_Count++;
                }
            }

        }
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
        return Color.rgb(140, 180, 255); // light blue

    }

    public void tick(Canvas g) {
        // Draw the board
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

        // Text above the banks
        g.drawText(opp, (float) .03 * maxX, (float) .135 * maxY, Text_Color); //Opponent
        g.drawText(play, (float) .86 * maxX, (float) .135 * maxY, Text_Color);//Player

        // Reset Button
        rectangles.setColor(Color.BLACK);
        g.drawRect((float) .85 * maxX, (float) .80 * maxY, (float) .94 * maxX, (float) .89 * maxY, rectangles);
        rectangles.setColor(Color.RED);
        g.drawRect((float) .855 * maxX, (float) .805 * maxY, (float) .935 * maxX, (float) .885 * maxY, rectangles);
        Text_Color.setTextSize(50);
        g.drawText("Reset",(float) .86 * maxX,(float) .85 * maxY, Text_Color);

        Text_Color.setTextSize(100);
        if (invert) {
            if (Current_State.getPlayer_Turn() == 0) {
                g.drawText(Player_Turn, (float) .4 * maxX, (float) .4 * maxY, Text_Color);
            } else {
                g.drawText(Opponent_Turn, (float) .35 * maxX, (float) .4 * maxY, Text_Color);
            }
        } else {
            if (Current_State.getPlayer_Turn() == 1) {
                g.drawText(Player_Turn, (float) .4 * maxX, (float) .4 * maxY, Text_Color);
            } else {
                g.drawText(Opponent_Turn, (float) .35 * maxX, (float) .4 * maxY, Text_Color);
            }
        }
        if(capture){
            g.drawText(Capture_Text, (float) .4 * maxX, (float) .85 * maxY, Text_Color);
        }

        Text_Color.setTextSize(50);


        g.drawText("" + mar_pos[0][6], (float) .03 * maxX, (float) .135 * maxY - 70, Text_Color);
        g.drawText("" + mar_pos[1][6], (float) .86 * maxX, (float) .135 * maxY - 70, Text_Color);

        mar_pos = Current_State.getMarble_Pos();
        // invert board if necessary
        if (invert) {
            int[][] mar_hold = new int[2][7];
            for (int r = 0; r < 2; r++) {
                for (int c = 0; c < 7; c++) {
                    mar_hold[r][c] = mar_pos[1 - r][c];
                }
            }
            mar_pos = mar_hold;
        }

        //draw holes
        MyPointF hold;
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
        Marble marble_hold;
        MyPointF location;
        Point New_Location;
        Hole New_Hole;
        MyPointF vector;
        MyPointF Marble_Location;
        float magnitude;
        MyPointF check = new MyPointF();                       //check to see if marble is inside hole

        //make adjustments to the marbles that are moving
        if (!wait) {
            if (moving != null) {
                if (moving.size() != 0) {                                   // if there is something to move
                    Currently_Moving = true;
                    ArrayList<Boolean> truth_table = new ArrayList<Boolean>();
                    for (int length = 0; length < moving.size(); length++){
                        truth_table.add(true);
                    }
                    for (int inc = 0; inc < moving.size(); inc++) {
                        marble_hold = marbles[moving.get(inc)];             // get the marble
                        Marble_Location = marble_hold.getLocation();        // get marble location
                        New_Location = Receiving_Holes.get(inc);            // location of hole in array
                        New_Hole = holes[New_Location.x][New_Location.y];   // get the corresponding hole

                        // stop the moving of the marbles by removing the marble from arraylist
                        location = New_Hole.getLocation();
                        check.setMyPointF(Marble_Location.x - location.x, Marble_Location.y - location.y);
                        double ran = 3*Math.random(); //Cox edit to space out marbles a bit more
                        if(location.y==.425 * maxY && Marble_Location.y > (maxY *.145) &&
                                Marble_Location.y< maxY*.705 && Math.abs(check.x)<(maxX / 20)- (1+ran)*(maxX/75)){
                            truth_table.set(inc,false);
                        }
                        else if (Math.abs(check.x) < (maxX / 20)- (1+ran)*(maxX/75) &&
                                Math.abs(check.y) < (maxX / 20)- (1+ran)*(maxX/75)) {
                            truth_table.set(inc,false);
                        } else {
                            location = New_Hole.getLocation();
                            vector = new MyPointF(location.x - Marble_Location.x, location.y - Marble_Location.y);
                            magnitude = (float) Math.sqrt(vector.x * vector.x + vector.y * vector.y);
                            vector.setMyPointF(vector.x / magnitude, vector.y / magnitude);
                            vector.setMyPointF(Marble_Location.x + vector.x * xvel, Marble_Location.y + vector.y * yvel);
                            marble_hold.setLocation(vector);
                            marbles[moving.get(inc)] = marble_hold;
                        }
                    }
                    for (int len = 0; len < truth_table.size(); len++) {         //remove element if done moving
                        if(!truth_table.get(len)){
                            moving.remove(len);
                            Receiving_Holes.remove(len);
                            truth_table.remove(len);
                            len--;
                        }
                    }
                }
            }
        }

        if (moving != null) {
            if (moving.size() == 0) {
                Same_Hole = false;
                moving.clear();
                Receiving_Holes.clear();
                start = false;
            }
        }
        if (!start && capture) {
            if (moving2 != null) {
                if (moving2.size() != 0) {                                   // if there is something to move
                    Currently_Moving = true;
                    ArrayList<Boolean> truth_table = new ArrayList<Boolean>();
                    for (int length = 0; length < moving2.size(); length++){
                        truth_table.add(true);
                    }
                    for (int inc = 0; inc < moving2.size(); inc++) {
                        marble_hold = marbles[moving2.get(inc)];             // get the marble
                        Marble_Location = marble_hold.getLocation();        // get marble location
                        New_Location = Receiving_Holes2.get(inc);            // location of hole in array
                        New_Hole = holes[New_Location.x][New_Location.y];   // get the corresponding hole

                        // stop the moving of the marbles by removing the marble from arraylist
                        location = New_Hole.getLocation();
                        check.setMyPointF(Marble_Location.x - location.x, Marble_Location.y - location.y);
                        double ran = 3*Math.random(); //Cox edit to space out marbles a bit more
                        if(location.y==.425 * maxY && Marble_Location.y > (maxY *.145) &&
                                Marble_Location.y< maxY*.705 && Math.abs(check.x)<(maxX / 20)- (1+ran)*(maxX/75)){
                            truth_table.set(inc,false);
                        }
                        else if (Math.abs(check.x) < (maxX / 20)- (1+ran)*(maxX/75) &&
                                Math.abs(check.y) < (maxX / 20)- (1+ran)*(maxX/75)) {
                            truth_table.set(inc,false);
                        } else {
                            location = New_Hole.getLocation();
                            vector = new MyPointF(location.x - Marble_Location.x, location.y - Marble_Location.y);
                            magnitude = (float) Math.sqrt(vector.x * vector.x + vector.y * vector.y);
                            vector.setMyPointF(vector.x / magnitude, vector.y / magnitude);
                            vector.setMyPointF(Marble_Location.x + vector.x * xvel, Marble_Location.y + vector.y * yvel);
                            marble_hold.setLocation(vector);
                            marbles[moving2.get(inc)] = marble_hold;
                        }
                    }
                    for (int len = 0; len < truth_table.size(); len++) {         //remove element if done moving
                        if(!truth_table.get(len)){
                            moving2.remove(len);
                            Receiving_Holes2.remove(len);
                            truth_table.remove(len);
                            len--;
                        }
                    }
                }
            }
        }

        //draw the marbles
        for (int size = 0; size < 48; size++) {
            marble_hold = marbles[size];
            if(marble_hold == null){
                break;
            }
            location = marble_hold.getLocation();
            marble_color.setColor(marble_hold.getColor());
            g.drawCircle(location.x, location.y, maxX / 75, marble_color);

        }
        if (moving != null) {
            if(moving2 == null && moving.size()==0){
                Currently_Moving = false;
            }
            else if(moving2 != null){
                if (moving2.size() == 0 && moving.size()==0) {
                    Currently_Moving = false;
                    Same_Hole = false;
                    moving2.clear();
                    Receiving_Holes2.clear();
                    capture = false;
                    start = true;
                }
            }
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
        //sleep(50);
        MyPointF Selected_Hole = new MyPointF(-1, -1);
        if(Currently_Moving || wait){
            Current_State.setHoles(holes);
            Current_State.setSelected_Hole(Selected_Hole);
            return;
        }
        Hole mediate;
        MyPointF hold;
        MyPointF inside = new MyPointF();
        int action = event.getAction();
        float xPos = event.getX();
        float yPos = event.getY();

        // Check Reset Button
        RectF reset = new RectF((float) .85 * maxX, (float) .80 * maxY, (float) .94 * maxX, (float) .89 * maxY);
        if (reset.contains(xPos,yPos)){
            Current_State.setReset(true);
            return;
        }

        // Check to see what hole has been touched
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 6; j++) {
                mediate = holes[i][j];
                hold = mediate.getLocation();
                inside.setMyPointF(xPos - hold.x, yPos - hold.y);
                if (Math.abs(inside.x) < maxX / 18 && Math.abs(inside.y) < maxX / 18 && action == MotionEvent.ACTION_UP) {
                    if (mar_pos[i][j] == 0 || i == 0) { //check to see if any marbles are in the hole or on wrong side
                        mediate.setColor(Color.RED);
                        holes[i][j] = mediate;
                    } else {
                        mediate.setColor(Color.GREEN);
                        holes[i][j] = mediate;
                        if (invert) {                     // send the inverted choice
                            Selected_Hole.setMyPointF(0, j);
                        } else {
                            Selected_Hole.setMyPointF(i, j);
                        }
                    }
                } else {
                    mediate.setColor(Color.BLACK);
                    holes[i][j] = mediate;
                }
            }
        }
        Current_State.setHoles(holes);
        Current_State.setSelected_Hole(Selected_Hole);
    }

    public MancState getUpdatedState() {
        return Current_State;
    }

    public void setState(MancState state) {
        Current_State = state;
    }

}


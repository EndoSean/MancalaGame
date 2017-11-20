package edu.up.cs301.Mancala;

import android.graphics.Point;

import org.junit.Test;

import edu.up.cs301.game.GamePlayer;

import static org.junit.Assert.*;

/**
 * Created by coxco19 on 11/19/2017.
 */
public class MancMoveActionTest {
    @Test
    public void getSelected_Hole() throws Exception {
        MancComputerPlayer1 p = new MancComputerPlayer1("me");
        Point testP = new Point(0,0);
        MancMoveAction test = new MancMoveAction(p,testP,0);
        assertEquals(testP,test.getSelected_Hole());

    }

}
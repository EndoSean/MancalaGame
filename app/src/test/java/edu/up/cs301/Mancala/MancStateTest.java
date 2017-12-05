package edu.up.cs301.Mancala;

import org.junit.Test;

import edu.up.cs301.Mancala.MancState;

import static org.junit.Assert.*;

/**
 * Created by endo18 on 11/9/2017.
 */
public class MancStateTest {
    @Test
    public void setPlayer_Turn() throws Exception {
        MancState test = new MancState();
        test.setPlayer_Turn(3);
        assertEquals(3, test.getPlayer_Turn());
        test.setPlayer_Turn(-1);
        assertEquals(-1,test.getPlayer_Turn());


    }

    @Test
    public void getPlayer_Turn() throws Exception {

    }

    @Test
    public void setPlayer0_Score() throws Exception {

    }

    @Test
    public void getPlayer0_Score() throws Exception {

    }

    @Test
    public void setPlayer1_Score() throws Exception {

    }

    @Test
    public void getPlayer1_Score() throws Exception {

    }

    @Test
    public void setMarble_Pos() throws Exception {
        MancState test = new MancState();
        int[][] testArray = new int[7][3];
        //test.setMarble_Pos(testArray);


    }

    @Test
    public void getMarble_Pos() throws Exception {

    }

    @Test
    public void testMancState(){
        MancState test = new MancState();
        test.setPlayer0_Score(1);
        test.setPlayer1_Score(5);
        test.setPlayer_Turn(1);
        MancState copy = new MancState(test);
        //assertEquals(copy.getPlayer0_Score(),1);
        //assertEquals(copy.getPlayer1_Score(),5);
        //assertEquals(copy.getPlayer_Turn(),1);

        MancState test2 = new MancState();
        test.setPlayer0_Score(-1);
        test.setPlayer1_Score(-5);
        test.setPlayer_Turn(-1);
        MancState copy2 = new MancState(test);
        //assertEquals(copy2.getPlayer0_Score(),-1);
        //assertEquals(copy2.getPlayer1_Score(),-5);
        //assertEquals(copy2.getPlayer_Turn(),-1);
    }

}
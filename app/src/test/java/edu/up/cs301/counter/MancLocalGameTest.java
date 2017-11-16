package edu.up.cs301.counter;

import org.junit.Test;

import edu.up.cs301.Mancala.MancLocalGame;
import edu.up.cs301.Mancala.MancState;

import static org.junit.Assert.*;

/**
 * Created by endo18 on 11/9/2017.
 */
public class MancLocalGameTest {
    @Test
    public void canMove() throws Exception {

    }

    @Test
    public void makeMove() throws Exception {

    }

    @Test
    public void sendUpdatedStateTo() throws Exception {

    }

    @Test
    public void checkIfGameOver() throws Exception {

    }

    @Test
    public void testCanMove(){
        MancLocalGame testing = new MancLocalGame();
        //MancState test = new MancState();
        int player = 1;
        boolean thing = testing.canMove(player);
        assertTrue(thing);

        MancLocalGame testing2 = new MancLocalGame();
        //MancState test = new MancState();
        int player2 = -1;
        boolean thing2 = testing2.canMove(player2);
        assertTrue(thing);
    }

    @Test
    public void testCheckIfGameOver(){
        MancLocalGame test = new MancLocalGame();
        MancState testcase = new MancState();
        int[][] end = new int[2][7];
        for(int i=0; i<2; i++){
            for(int j=0; j<7; j++){
                if(i==1){
                    end[i][j] = 0;
                }
                else {
                    end[i][j] = 1;
                }
            }
        }
        testcase.setMarble_Pos(end);
        String thing = test.checkIfGameOver();
        assertEquals(thing, "player1 has won.");

    }

    /**
    @Test
    public void testMakeMove(){
        MancLocalGame test = new MancLocalGame();
        GamePlayer player = new GamePlayer();
        boolean confirm = false;
        MancMoveAction action = new MancMoveAction(player,confirm);
        //action.confirm(1,18);
        boolean thing = test.makeMove(action);
        assertTrue(thing);

        CounterHumanPlayer player2 = new CounterHumanPlayer();
        boolean confirm2 = false;
        CounterMoveAction action = new CounterMoveAction(player2,confirm2);
        assertTrue(!thing);

    }
    **/

}
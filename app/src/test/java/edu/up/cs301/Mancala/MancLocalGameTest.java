/*package edu.up.cs301.Mancala;

import android.graphics.Point;

import org.junit.Test;

import edu.up.cs301.Mancala.MancLocalGame;
import edu.up.cs301.Mancala.MancState;
import edu.up.cs301.game.GamePlayer;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.*;

/**
 * Created by endo18 on 11/9/2017.

public class MancLocalGameTest {
    @Test
    public void canMove() throws Exception {
        MancLocalGame testing = new MancLocalGame();

        int player = 1;
        boolean thing = testing.canMove(player);
        assertTrue(thing);

        MancLocalGame testing2 = new MancLocalGame();

        int player2 = -1;
        boolean thing2 = testing2.canMove(player2);
        assertTrue(thing);

    }

    @Test
    public void makeMove() throws Exception {

    }

    @Test
    public void sendUpdatedStateTo() throws Exception {

    }

    @Test
    public void checkIfGameOver() throws Exception {
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
        assertTrue(thing.contains(" has won. Score: 1"));

    }


    @Test
    public void testMakeMove(){
        MancLocalGame test = new MancLocalGame();
        MancHumanPlayer player = new MancHumanPlayer("too");

        MancMoveAction action = new MancMoveAction(player,new Point(1,2),1);

        boolean thing = test.makeMove(action);
        assertTrue(thing);

        MancHumanPlayer player2 = new MancHumanPlayer("name");
        boolean confirm2 = false;
        MancMoveAction action2 = new MancMoveAction(player2,new Point(-1,3),1);
        assertTrue(!test.makeMove(action2));

    }

}*/
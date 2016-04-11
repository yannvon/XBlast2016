package ch.epfl.xblast.personal.server;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.SubCell;
import ch.epfl.xblast.server.Player;
import ch.epfl.xblast.server.Player.DirectedPosition;
import ch.epfl.xblast.server.Player.LifeState;
import ch.epfl.xblast.server.Player.LifeState.State;
import ch.epfl.xblast.server.Ticks;

/**
 * JUnit test for the Player.java class.
 * 
 * @author Loïc Vandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 *
 */
public class PlayerTest {

    @Test
    public void secondConstructorTest() {
        Cell position = new Cell(3,5);
        
        //LifeState ls = new LifeState(3,State.DEAD);
        
        //--- PLAYER ALIVE ---
        Player p1 = new Player(PlayerID.PLAYER_1, 3, position, 2, 4);
        
        //small tests to begin with
        assertEquals(Direction.S, p1.direction());
        assertEquals(4, p1.bombRange());
        assertEquals(3, p1.lives());
        assertEquals(2, p1.maxBombs());
        assertEquals(PlayerID.PLAYER_1, p1.id());
        assertEquals(State.INVULNERABLE, p1.lifeState().state());
        assertEquals(position, p1.position().containingCell());
        
        // test directed position
        int testDuration = 101;
        Sq<DirectedPosition> seqToTest = p1.directedPositions();
        SubCell sub = SubCell.centralSubCellOf(position);

        for(int i = 0; i <= testDuration; i++){
            assertEquals(sub, seqToTest.head().position());
            assertEquals(Direction.S, seqToTest.head().direction());
            seqToTest = seqToTest.tail();
        }
        
        //test life state
        Sq<LifeState> lsSeq = p1.lifeStates();
        for(int i = 0; i < Ticks.PLAYER_INVULNERABLE_TICKS; i++){
            assertEquals(State.INVULNERABLE, lsSeq.head().state());
            assertEquals(3, lsSeq.head().lives());
            lsSeq = lsSeq.tail();
        }
        
        for(int i = 0; i <= testDuration; i++){
            assertEquals(State.VULNERABLE, lsSeq.head().state());
            assertEquals(3, lsSeq.head().lives());
            lsSeq = lsSeq.tail();
        }
    }

    @Test
    public void movingTest(){
        Sq<DirectedPosition> toTest = DirectedPosition.moving(new DirectedPosition(new SubCell(24,25), Direction.N));
        
        int testDuration = 1001;
        
        for(int i = 0; i < testDuration; i++){
            assertEquals(new SubCell(24,25-i),toTest.head().position());
            toTest = toTest.tail();
        }
    }
    
    @Test
    public void stoppedTest(){
        Sq<DirectedPosition> toTest = DirectedPosition.stopped(new DirectedPosition(new SubCell(24,25), Direction.N));
        
        int testDuration = 1001;
        
        for(int i = 0; i < testDuration; i++){
            assertEquals(new SubCell(24,25),toTest.head().position());
            toTest = toTest.tail();
        }
    }
    
    @Test
    public void statesForNextLifeTest(){
        
        //PLAYER STILL ALIVE
        Cell position = new Cell(5,3);
        Player p1 = new Player(PlayerID.PLAYER_1, 3, position, 2, 4);
        
        int testDuration = 101;
        Sq<LifeState> lsToTest = p1.statesForNextLife();
        
        
        for(int i = 0; i < Ticks.PLAYER_DYING_TICKS; i++){
            assertEquals(State.DYING, lsToTest.head().state());
            assertEquals(3, lsToTest.head().lives());
            lsToTest = lsToTest.tail();
        }
        
        
        for(int i = 0; i < Ticks.PLAYER_INVULNERABLE_TICKS; i++){
            assertEquals(State.INVULNERABLE, lsToTest.head().state());
            assertEquals(2, lsToTest.head().lives());
            lsToTest = lsToTest.tail();
        }
        
        for(int i = 0; i <= testDuration; i++){
            assertEquals(State.VULNERABLE, lsToTest.head().state());
            assertEquals(2, lsToTest.head().lives());
            lsToTest = lsToTest.tail();
        }
        
        //PLAYER KILLED
        
        Player pK = new Player(PlayerID.PLAYER_1, 1, position, 2, 4);
        
        Sq<LifeState> lsToTestK = pK.statesForNextLife();
        
        for(int i = 0; i < Ticks.PLAYER_DYING_TICKS; i++){
            assertEquals(State.DYING, lsToTestK.head().state());
            assertEquals(1, lsToTestK.head().lives());
            lsToTestK = lsToTestK.tail();
        }
        
        
        for(int i = 0; i <= testDuration; i++){
            assertEquals(State.DEAD, lsToTestK.head().state());
            assertEquals(0, lsToTestK.head().lives());
            lsToTestK = lsToTestK.tail();
        }
        
        //PLAYER DEAD
        
        Player pD = new Player(PlayerID.PLAYER_1, 0, position, 2, 4);
        
        Sq<LifeState> lsToTestD = pD.statesForNextLife();
        
        for(int i = 0; i < Ticks.PLAYER_DYING_TICKS; i++){
            assertEquals(State.DYING, lsToTestD.head().state());
            assertEquals(0, lsToTestD.head().lives());
            lsToTestD = lsToTestD.tail();
        }
        
        for(int i = 0; i <= testDuration; i++){
            assertEquals(State.DEAD, lsToTestD.head().state());
            assertEquals(0, lsToTestD.head().lives());
            lsToTestD = lsToTestD.tail();
        }
    }
    
    //Test the error handling of Player constructor
    
    @Test(expected = java.lang.NullPointerException.class)
    public void ConstructorId() {
        Cell position = new Cell(3,5);
        Player p = new Player(null, 3, position, 2, 4);
    }
    @Test(expected = java.lang.IllegalArgumentException.class)
    public void ConstructorLives() {
        Cell position = new Cell(3,5);
        Player p = new Player(PlayerID.PLAYER_1, -3, position, 2, 4);
    }
    @Test(expected = java.lang.NullPointerException.class)
    public void ConstructorPosition() {
        Player p = new Player(PlayerID.PLAYER_1, 3, null, 2, 4);
    }
    @Test(expected = java.lang.IllegalArgumentException.class)
    public void ConstructorMaxBombs() {
        Cell position = new Cell(3,5);
        Player p = new Player(PlayerID.PLAYER_1, 3, position, -3, 4);
    }
    @Test(expected = java.lang.IllegalArgumentException.class)
    public void ConstructorRange() {
        Cell position = new Cell(3,5);
        Player p = new Player(PlayerID.PLAYER_1, 3, position, 2, -3);
    }
    

}

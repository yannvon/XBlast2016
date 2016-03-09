package ch.epfl.xblast.server;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerID;

/**
 * JUnit test for the Bomb.java class.
 * 
 * @author Loïc Vandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 *
 */
public class Bombtest {

    /**
     * test if the sequence of fuseLengths is the expected sequence
     */
    @Test
    public void constructor2Test() {
        int fusel=5;
        Bomb a= new Bomb(PlayerID.PLAYER_1,new Cell(0,0),fusel,5);
       
        
        Sq<Integer> fuseLast=a.fuseLengths();
        int i=fusel;
        while(!fuseLast.isEmpty()){
           assertEquals(i,(int)fuseLast.head());
           fuseLast =fuseLast.tail();
           i--;
        }
        
        
    }
    
    /**
     * test if the constructor return an exception with an negative fuselength
     */
    @Test(expected= java.lang.IllegalArgumentException.class)
    public void negativeFuseLengthTest() {
        int fusel=-5;
        Bomb a= new Bomb(PlayerID.PLAYER_1,new Cell(0,0),fusel,5);
       
        
        Sq<Integer> fuseLast=a.fuseLengths();
        int i=fusel;
        while(!fuseLast.isEmpty()){
           assertEquals(i,(int)fuseLast.head());
           fuseLast =fuseLast.tail();
           i--;
        }
    }

    /**
     * test if the sequence stop at 1
     */
    @Test(expected= java.util.NoSuchElementException.class)
    public void fuseLengthsSize() {
        int fusel=5;
        Bomb a= new Bomb(PlayerID.PLAYER_1,new Cell(0,0),fusel,5);
       
        Sq<Integer> fuseLast=a.fuseLengths();
        for(int i=0;i<fusel;i++){
           fuseLast =fuseLast.tail();
        }
        
        fuseLast.head();
    }
    
    /**
     * test if the explosion contain the expected cells
     */
    @Test
    public void explosionTest() {
        int range=4;
        Bomb a= new Bomb(PlayerID.PLAYER_1,new Cell(0,0),4,range);
       
        List<Sq<Sq<Cell>>> explosion = a.explosion();
        for(int i=0;i<explosion.size();i++){
            Sq<Sq<Cell>> arm =explosion.get(i);

            while(!arm.isEmpty()){
                Cell cel=a.position();
                Sq<Cell> part= arm.head();
                while(!part.isEmpty()){
                    
                    assertEquals(cel,part.head());
                   
                    cel=cel.neighbor(Direction.values()[i]);
                    part=part.tail();
                }
                arm=arm.tail();

            }
        }
    }
    
}

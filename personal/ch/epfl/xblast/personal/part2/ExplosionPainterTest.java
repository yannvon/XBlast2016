/*
 *	Author:      Yann Vonlanthen
 *	Date:        19.04.2016
 */

package ch.epfl.xblast.personal.part2;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.server.Bomb;
import ch.epfl.xblast.server.ExplosionPainter;
import ch.epfl.xblast.server.Ticks;

public class ExplosionPainterTest {

    @Test
    public void byteForBlastWorking() {
        assertEquals(0b1111, ExplosionPainter.byteForBlast(true, true, true, true), 1E-10);
        assertEquals(0b1110, ExplosionPainter.byteForBlast(true, true, true, false), 1E-10);
        assertEquals(0b1100, ExplosionPainter.byteForBlast(true, true, false, false), 1E-10);
        assertEquals(0b1000, ExplosionPainter.byteForBlast(true, false, false, false), 1E-10);
        assertEquals(0b0000, ExplosionPainter.byteForBlast(false, false, false, false), 1E-10);
        assertEquals(0b0100, ExplosionPainter.byteForBlast(false, true, false, false), 1E-10);
        assertEquals(0b1101, ExplosionPainter.byteForBlast(true, true, false, true), 1E-10);
        assertEquals(0b1011, ExplosionPainter.byteForBlast(true, false, true, true), 1E-10);
    }
    
    
    @Test
    public void byteForBombWorking() {
        Cell c= new Cell(0,0);
        PlayerID p=PlayerID.PLAYER_1;
        
        assertEquals(20,ExplosionPainter.byteForBomb(new Bomb(p, c, 10000001, 0)));
        assertEquals(20,ExplosionPainter.byteForBomb(new Bomb(p, c, 50, 0)));
        assertEquals(20,ExplosionPainter.byteForBomb(new Bomb(p, c, 267, 0)));
        assertEquals(20,ExplosionPainter.byteForBomb(new Bomb(p, c, 3, 0)));
        assertEquals(20,ExplosionPainter.byteForBomb(new Bomb(p, c, 5, 0)));
        assertEquals(20,ExplosionPainter.byteForBomb(new Bomb(p, c, 6, 0)));
        assertEquals(20,ExplosionPainter.byteForBomb(new Bomb(p, c, 7, 0)));
        assertEquals(20,ExplosionPainter.byteForBomb(new Bomb(p, c, 9, 0)));
        assertEquals(20,ExplosionPainter.byteForBomb(new Bomb(p, c, 10, 0)));
        
        for(int i=1;i<Ticks.TOTAL_TICKS; i*=2){
            assertEquals(21,ExplosionPainter.byteForBomb(new Bomb(p, c, i, 0)));
        }
        
    }

}

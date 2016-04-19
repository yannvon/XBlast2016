/*
 *	Author:      Yann Vonlanthen
 *	Date:        19.04.2016
 */

package ch.epfl.xblast.personal.part2;

import static org.junit.Assert.*;

import org.junit.Test;

import ch.epfl.xblast.server.ExplosionPainter;

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

}

package ch.epfl.xblast.personal.part2;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.server.Block;
import ch.epfl.xblast.server.Board;
import ch.epfl.xblast.server.BoardPainter;

public class BoardPainterTest {
    

    private final static Block __ = Block.FREE;
    private final static Block XX = Block.INDESTRUCTIBLE_WALL;
    private final static Block xx = Block.DESTRUCTIBLE_WALL;
    private final static Block b = Block.BONUS_BOMB;
    private final static Block r = Block.BONUS_RANGE;
    private final static Block cc = Block.CRUMBLING_WALL;
    
    private Board board =Board.ofQuadrantNWBlocksWalled(
            Arrays.asList(Arrays.asList(__, __, XX, xx, b, r, cc),
                    Arrays.asList(__, __, __, __, __, __, __),
                    Arrays.asList(__, __, __, __, __, __, __),
                    Arrays.asList(__, __, __, __, __, __, __),
                    Arrays.asList(__, __, __, __, __, __, __),
                    Arrays.asList(__, __, __, __, __, __, __)));
    
    @Test
    public void ClassicByteForCellTest() {
        BoardPainter painter = BoardPainter.classicBoardPainter();
        
        assertEquals(1,painter.byteForCell(board,new Cell(1,1)));
        assertEquals(0,painter.byteForCell(board,new Cell(2,1)));
        assertEquals(2,painter.byteForCell(board,new Cell(3,1)));
        assertEquals(3,painter.byteForCell(board,new Cell(4,1)));
        assertEquals(5,painter.byteForCell(board,new Cell(5,1)));
        assertEquals(6,painter.byteForCell(board,new Cell(6,1)));
        assertEquals(4,painter.byteForCell(board,new Cell(7,1)));
        
    }
    
        

}

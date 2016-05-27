package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.RunLengthEncoder;

/**
 * This public, final and non intantiable class offers a unique static method
 * that is capable of serializing a GameState.
 * 
 * @author Loïc Vandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 */
public final class GameStateSerializer {
    
    /*
     * Constants
     */
    private static final double REMAINING_TIME_COMPRESSION = 2;

    /**
     * Private Constructor: non instantiable class.
     */
    private GameStateSerializer() {}

    /**
     * Given a BoardPainter and a GameState this method returns a serialized
     * version of the GameState.
     * 
     * @param boardPainter
     *            that is used to represent the board
     * @param gameState
     *            that has to be serialized
     * @return a list of bytes representing the serialized GameState
     */
    public static List<Byte> serialize(BoardPainter boardPainter,
            GameState gameState) {

        /*
         * SERIALIZING BOARD
         */
        List<Byte> serializedBoard = new ArrayList<>();

        for (Cell c : Cell.SPIRAL_ORDER)
            serializedBoard.add(boardPainter.byteForCell(gameState.board(), c));

        // encode board
        serializedBoard = RunLengthEncoder.encode(serializedBoard);

        /*
         * SERIALIZING EXPLOSIONS & BOMBS
         */
        List<Byte> serialisedExplosions = new ArrayList<>();
        Map<Cell, Bomb> bombedCells = gameState.bombedCells();
        Set<Cell> blastedCells = gameState.blastedCells();

        for (Cell c : Cell.ROW_MAJOR_ORDER) {
            
            // --- don't display anything on non-free blocks.
            if (!gameState.board().blockAt(c).isFree())
                serialisedExplosions.add(ExplosionPainter.BYTE_FOR_EMPTY);
            // --- display a bomb
            else if (bombedCells.containsKey(c))
                serialisedExplosions
                        .add(ExplosionPainter.byteForBomb(bombedCells.get(c)));
            // --- display a blast
            else if (blastedCells.contains(c)) {
                serialisedExplosions.add(ExplosionPainter.byteForBlast(
                        blastedCells.contains(c.neighbor(Direction.N)),
                        blastedCells.contains(c.neighbor(Direction.E)),
                        blastedCells.contains(c.neighbor(Direction.S)),
                        blastedCells.contains(c.neighbor(Direction.W))));
            }
            // --- empty byte for block without explosions & bombs
            else
                serialisedExplosions.add(ExplosionPainter.BYTE_FOR_EMPTY);
        }

        // encode explosions
        serialisedExplosions = RunLengthEncoder.encode(serialisedExplosions);

        /*
         * SERIALIZNG PLAYERS
         */
        List<Byte> serialisedPlayers = new ArrayList<>();
        for (Player p : gameState.players()) {
            serialisedPlayers.add((byte) p.lives());
            serialisedPlayers.add((byte) p.position().x());
            serialisedPlayers.add((byte) p.position().y());
            serialisedPlayers
                    .add(PlayerPainter.byteForPlayer(gameState.ticks(), p));
        }

        /*
         * SERIALIZING REMAINING TIME
         */
        byte serialisedTime = (byte) 
                Math.ceil(gameState.remainingTime() / REMAINING_TIME_COMPRESSION);

        /*
         * CONSTRUCT OUTPUT
         * 
         * Add the size of the variable length lists in front of them.
         */
        List<Byte> output = new ArrayList<>();
        
        // --- add serialized board
        output.add((byte) serializedBoard.size());
        output.addAll(serializedBoard);
        
        // --- add serialized explosions
        output.add((byte) serialisedExplosions.size());
        output.addAll(serialisedExplosions);
        
        // --- add serialized players
        output.addAll(serialisedPlayers);
        
        // --- add serialized time
        output.add(serialisedTime);
        
        return Collections.unmodifiableList(output);
    }
}

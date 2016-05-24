package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.RunLengthEncoder;
import ch.epfl.xblast.SubCell;

/**
 * This public, final and non intantiable class offers a unique static method
 * that is capable of serializing a GameState.
 * 
 * @author Loïc Vandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 */
public final class GameStateSerializer {
    private GameStateSerializer(){}

    /**
     * Given a BoardPainter and a GameState this method returns a serialized
     * version of the GameState.
     * 
     * @param boardPainter
     *            that is used
     * @param gameState
     *            that has to be serialized
     * @return a list of bytes representing the serialized GameState
     */
    public static List<Byte> serialize(BoardPainter boardPainter,GameState gameState){

        /*
         * SERIALIZING BOARD
         */
        List<Byte> serialisedBoard = new ArrayList<>();

        for (Cell c : Cell.SPIRAL_ORDER)
            serialisedBoard.add(boardPainter.byteForCell(gameState.board(), c));

        // encode board
        serialisedBoard = RunLengthEncoder.encode(serialisedBoard);

        /*
         * SERIALIZING EXPLOSIONS & BOMBS
         */
        List<Byte> serialisedExplosions = new ArrayList<>();
        Map<Cell, Bomb> bombedCells = gameState.bombedCells();
        Set<Cell> blastedCells = gameState.blastedCells();

        for (Cell c : Cell.ROW_MAJOR_ORDER) {
            if (!gameState.board().blockAt(c).isFree())
                serialisedExplosions.add(ExplosionPainter.BYTE_FOR_EMPTY);
            else if (bombedCells.containsKey(c))
                serialisedExplosions
                        .add(ExplosionPainter.byteForBomb(bombedCells.get(c)));
            else if (blastedCells.contains(c)) {
                serialisedExplosions.add(ExplosionPainter.byteForBlast(
                        blastedCells.contains(c.neighbor(Direction.N)),
                        blastedCells.contains(c.neighbor(Direction.E)),
                        blastedCells.contains(c.neighbor(Direction.S)),
                        blastedCells.contains(c.neighbor(Direction.W))));
            } else
                serialisedExplosions.add(ExplosionPainter.BYTE_FOR_EMPTY);
        }

        // encode explosions
        serialisedExplosions = RunLengthEncoder.encode(serialisedExplosions);

        /*
         * SERIALIZING MOVING BOMBS
         */
        List<Byte> serialisedMovingBombs = new ArrayList<>();
        Map<SubCell,MovingBomb> movingsBombs = gameState.movingBombsSubCells();
        for(Map.Entry<SubCell, MovingBomb> e : movingsBombs.entrySet()){
            serialisedMovingBombs.add(ExplosionPainter.byteForBomb(e.getValue()));
            serialisedMovingBombs.add((byte)e.getKey().x());
            serialisedMovingBombs.add((byte)e.getKey().y());
        }
        
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
        byte serialisedTime = (byte) Math.ceil(gameState.remainingTime() / 2);

        /*
         * CONSTRUCT OUTPUT
         */
        List<Byte> output = new ArrayList<>();
        output.add((byte) serialisedBoard.size());
        output.addAll(serialisedBoard);
        output.add((byte) serialisedExplosions.size());
        output.addAll(serialisedExplosions);
        output.add((byte)serialisedMovingBombs.size());
        output.addAll(serialisedMovingBombs);
        output.addAll(serialisedPlayers);
        output.add(serialisedTime);
        return output;
    }
}

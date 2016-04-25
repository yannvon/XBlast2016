package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.RunLengthEncoder;

/**
 * non instantiable class TODO
 * 
 * @author Loïc Vandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 *
 */
public final class GameStateSerializer {

    private GameStateSerializer(){}
    
    public static List<Byte> serialize(BoardPainter boardPainter,GameState game){
        /*
         * BOARD
         */
        List<Byte> serialisedBoard = new ArrayList<>();

        for (Cell c : Cell.SPIRAL_ORDER)
            serialisedBoard.add(boardPainter.byteForCell(game.board(), c));

        // encode board
        serialisedBoard = RunLengthEncoder.encode(serialisedBoard);

        /*
         * EXPLOSIONS & BOMBS
         */
        List<Byte> serialisedExplosions = new ArrayList<>();
        Map<Cell, Bomb> bombedCells = game.bombedCells();
        Set<Cell> blastedCells = game.blastedCells();

        for (Cell c : Cell.ROW_MAJOR_ORDER) {

            if (!game.board().blockAt(c).isFree())
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
         * PLAYERS
         */
        List<Byte> serialisedPlayers = new ArrayList<>();
        for (Player p : game.players()) {
            serialisedPlayers.add((byte) p.lives());
            serialisedPlayers.add((byte) p.position().x());
            serialisedPlayers.add((byte) p.position().y());
            serialisedPlayers.add(PlayerPainter.byteForPlayer(game.ticks(), p));
        }

        /*
         * REMAINING TIME
         */
        byte serialisedTime = (byte) Math.ceil(game.remainingTime() / 2);

        /*
         * Construct output
         */
        List<Byte> output = new ArrayList<>();

        output.add((byte) serialisedBoard.size());
        output.addAll(serialisedBoard);
        output.add((byte) serialisedExplosions.size());
        output.addAll(serialisedExplosions);
        output.addAll(serialisedPlayers);
        output.add(serialisedTime);

        return output;
    }
}

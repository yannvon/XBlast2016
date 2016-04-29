package ch.epfl.xblast.client;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.RunLengthEncoder;
import ch.epfl.xblast.SubCell;
import ch.epfl.xblast.client.GameState.Player;

/**
 * Non-instanciable class offering static method to deserialize the GameSate to
 * the client.
 * 
 * @author Loïc Vandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 *
 */
public final class GameStateDeserializer {
    private GameStateDeserializer() {
    }

    /*
     * Constants
     */
    private static final int PLAYER_BYTES_LENGTH = PlayerID.values().length * 4; // FIXME
                                                                                 // utile?
    private static final ImageCollection BLOCK_COLLECTION = 
                                         new ImageCollection("block");
    private static final ImageCollection EXPLOSION_COLLECTION = 
                                         new ImageCollection("player");
    private static final ImageCollection PLAYER_COLLECTION = 
                                         new ImageCollection("player");
    private static final ImageCollection SCORE_COLLECTION = 
                                         new ImageCollection("score");
    private static final int MIDDLE_GAP = 8;
    private static final int TIMELINE_LENGTH = 60;
    private static final byte TEXT_MIDDLE = 10;
    private static final byte TEXT_RIGHT = 11;
    private static final byte TILE_VOID = 12;
    private static final byte LED_ON = 20;
    private static final byte LED_OFF = 21;

    /**
     * @param serialized
     * @return
     */
    public static GameState deserializeGameState(List<Byte> serialized) {
        /*
         * Get Sublists Indices
         */
        int lastIndex = serialized.size() - 1;
        int boardDelimiter = serialized.get(0) + 1;
        int explosionDelimiter = serialized.get(boardDelimiter) + boardDelimiter
                + 1;

        /*
         * Deserialize Board
         */
        List<Image> deBoard = deserializeBoard(
                serialized.subList(1, boardDelimiter));

        /*
         * Deserialize explosions and bombs
         */
        List<Image> deExplosions = deserializedExplosions(
                serialized.subList(boardDelimiter + 1, explosionDelimiter));

        /*
         * Deserialize Players
         */
        List<Player> dePlayers = deserializedPlayers(
                serialized.subList(explosionDelimiter, lastIndex));

        /*
         * Construct Score line
         */
        List<Image> scoreLine = constructScoreLine(dePlayers);

        /*
         * Construct Time line
         */
        List<Image> timeLine = constructTimeLine(serialized.get(lastIndex));

        return new GameState(dePlayers, deBoard, deExplosions, scoreLine,
                timeLine);
    }

    /**
     * Additional method that decode the list of byte and construct the imaged board
     * @param encodedBoard
     * @return the list of image that represent the board
     */
    private static List<Image> deserializeBoard(List<Byte> encodedBoard) {
        List<Byte> decodedBoard = RunLengthEncoder.decode(encodedBoard);

        Iterator<Byte> boardIterator = decodedBoard.iterator();
        Iterator<Cell> spiralIterator = Cell.SPIRAL_ORDER.iterator();
        Image[] boardRepresentation = new Image[Cell.COUNT];
        while (boardIterator.hasNext()) {
            boardRepresentation[spiralIterator.next()
                    .rowMajorIndex()] = BLOCK_COLLECTION
                            .image(boardIterator.next());
        }
        return Arrays.asList(boardRepresentation); // FIXME
    }

    /**
     * @param encodedExplosions
     * @return
     */
    private static List<Image> deserializedExplosions(
            List<Byte> encodedExplosions) {
        List<Byte> decodedExplosions = RunLengthEncoder
                .decode(encodedExplosions);
        List<Image> explosionsRepresentation = new ArrayList<>();

        for (Byte b : decodedExplosions)
            explosionsRepresentation.add(EXPLOSION_COLLECTION.imageOrNull(b));

        return explosionsRepresentation;
    }

    /**
     * @param encodedPlayers
     * @return
     */
    private static List<Player> deserializedPlayers(List<Byte> encodedPlayers) {
        List<Player> players = new ArrayList<>();
        Iterator<Byte> encIt = encodedPlayers.iterator();
        for (int i = 0; i < 4; i++) {
            int lives = Byte.toUnsignedInt(encIt.next());
            SubCell pos = new SubCell(Byte.toUnsignedInt(encIt.next()),
                    Byte.toUnsignedInt(encIt.next()));
            Image image = PLAYER_COLLECTION.imageOrNull(encIt.next());
            players.add(new Player(PlayerID.values()[i], lives, pos, image));
        }
        return players;
    }

    /**
     * @param dePlayers
     * @return
     */
    private static List<Image> constructScoreLine(List<Player> dePlayers) {
        List<Image> scoreLine = new ArrayList<>();
        for (Player p : dePlayers) {
            int id = p.playerId().ordinal();
            // add void tiles in the centre of the score line
            if (id == 2) {
                scoreLine.addAll(Collections.nCopies(MIDDLE_GAP,
                        SCORE_COLLECTION.image((byte) TILE_VOID)));
            }
            byte imageNumber = (byte) (id * 2 + ((p.lives() > 0) ? 1 : 0));
            scoreLine.add(SCORE_COLLECTION.image(imageNumber));
            scoreLine.add(SCORE_COLLECTION.image(TEXT_MIDDLE));
            scoreLine.add(SCORE_COLLECTION.image(TEXT_RIGHT));
        }
        return scoreLine;
    }

    /**
     * @param time
     * @return
     */
    private static List<Image> constructTimeLine(Byte time) {
        List<Image> scoreLine = new ArrayList<>();
        scoreLine.addAll(
                Collections.nCopies(time, SCORE_COLLECTION.image(LED_ON)));
        scoreLine.addAll(Collections.nCopies(TIMELINE_LENGTH - time,
                SCORE_COLLECTION.image(LED_OFF)));
        return scoreLine;
    }
}

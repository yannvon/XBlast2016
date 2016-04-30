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
 * Non-instantiable class offering one static method that allows the client to deserialize a GameSate.
 * This class in some way does the opposite class of GameStateSerializer.
 * 
 * @author Loïc Vandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 *
 */
public final class GameStateDeserializer {
    private GameStateDeserializer() {}

    /*
     * ImageCollections used to retrieve the images.
     */
    private static final ImageCollection BLOCK_COLLECTION = new ImageCollection(
            "block");
    private static final ImageCollection EXPLOSION_COLLECTION = new ImageCollection(
            "player");
    private static final ImageCollection PLAYER_COLLECTION = new ImageCollection(
            "player");
    private static final ImageCollection SCORE_COLLECTION = new ImageCollection(
            "score");
    /*
     * Constants
     */
    private static final int MIDDLE_GAP_LENGTH = 8;
    private static final int TIMELINE_LENGTH = 60;
    private static final int TEXT_MIDDLE = 10;  //FIXME int or byte?
    private static final int TEXT_RIGHT = 11;
    private static final int TILE_VOID = 12;
    private static final int LED_ON = 20;
    private static final int LED_OFF = 21;

    /**
     * Static method that given a serialised GameState (list of bytes) can
     * compute and return the corresponding GameState.
     * 
     * @param serialized
     *            List of bytes that represent a GameState
     * @return the GameState corresponding to the received list of bytes
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
         * Deserialize Explosions and Bombs
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
     * Additional static method that decodes the list of bytes corresponding to
     * a serialized board, decodes it and returns a list of images corresponding
     * to every block. The original list is ordered following the spiral order,
     * but this method returns a list ordered in reading order.
     * 
     * @param encodedBoard
     *            list of bytes representing the encoded board in spiral order
     * @return the list of images that represent the board in reading order
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
        return Arrays.asList(boardRepresentation); // FIXME directly use List?
    }

    /**
     * Additional static method that given a list of bytes representing the
     * serialized Explosions and Bombs, decodes it and returns a list of the
     * corresponding images.
     * 
     * @param encodedExplosions
     *            list of bytes representing serialized Explosions and Bombs in
     *            reading order
     * @return list of images in reading order
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
     * Additional static method in charge of deserializing the players. The
     * given list contains 4 bytes for each player, representing their amount of
     * lives, their position (on the x and y axis) and their corresponding
     * image. These bytes have to be interpreted as unsigned bytes! The method
     * returns a list containing the according players (client version).
     * 
     * @param encodedPlayers
     *            list of bytes representing the players, to be interpreted as
     *            unsigned bytes
     * @return a list of the corresponding players
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
     * Additional static method in charge of constructing the ScoreLine. It
     * takes the previously deserialized players as argument and is in charge of
     * correctly filling a list with the images displaying the correct images
     * that represent the players on the Score line. The representation of the
     * amount of lives left is not done here.
     * 
     * @param dePlayers
     *            list of the deserialized players for the current tick
     * @return list of images corresponding that represent the scoreline from
     *         left to right
     */
    private static List<Image> constructScoreLine(List<Player> dePlayers) {
        List<Image> scoreLine = new ArrayList<>();
        for (Player p : dePlayers) {
            int id = p.id().ordinal();
            // add void tiles in the centre of the score line
            if (id == 2) {
                scoreLine.addAll(Collections.nCopies(MIDDLE_GAP_LENGTH,
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
     * Additional static method in charge of constructing the TimeLine given a
     * byte value. The byte value corresponds to half of the time left, which
     * exactly matches the amount of Led's of the TimeLine that have to be ON:
     * the game lasts a maximum of 120 while there are 60 Led's to be displayed.
     * 
     * @param time
     *            byte representing the amount of leds that have to be on
     * @return list of 60 images of on/off leds representing the TimeLine
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

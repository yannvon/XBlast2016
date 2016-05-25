package ch.epfl.xblast.client;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import ch.epfl.xblast.ArgumentChecker;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.SubCell;

/**
 * This immutable class represents a GameState from the client point of view. It
 * contains only the information absolutely required to print the correct
 * GameState to the screen.
 * 
 * @author Loïc Vandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 * 
 */
public final class GameState {

    /**
     * This immutable, statically embedded class represents a Player from the
     * point of view of the client. Therefore it is way less sophisticated than
     * the server version.
     */
    public static final class Player {
        
        /*
         * Attributes
         * 
         * Note: We assume that an Image is immutable, even if it is not really.
         */
        private final PlayerID id;
        private final int lives;
        private final SubCell position;
        private final Image image;

        /**
         * Constructs a player with the values passed as arguments.
         * 
         * @param playerId
         *            id of the player
         * @param lives
         *            amount of lives the player has
         * @param position
         *            SubCell the player is currently at
         * @param image
         *            representing the player
         * @throws NullPointerException
         *             if playerId or position was null
         * @throws IllegalArgumentException
         *             if lives was negative
         */
        public Player(PlayerID playerId, int lives, SubCell position, Image image) {
            this.id = Objects.requireNonNull(playerId);
            this.lives = ArgumentChecker.requireNonNegative(lives);
            this.position = Objects.requireNonNull(position);
            // a player can have a null image (if he is dead for example)
            this.image = image;
        }

        /**
         * Returns the id of the player.
         * 
         * @return PlayerID corresponding to the player
         */
        public PlayerID id() {
            return id;
        }

        /**
         * Returns the number of lives the player has.
         * 
         * @return number of lives the player has
         */
        public int lives() {
            return lives;
        }

        /**
         * Returns the current position of the player.
         * 
         * @return the position of the player
         */
        public SubCell position() {
            return position;
        }

        /**
         * Returns the image of the player.
         * 
         * @return the image of the player
         */
        public Image image() {
            return image;
        }
    }
    
    /*
     * Constants 
     */
    private final static int NUMBER_OF_PLAYERS = PlayerID.values().length;

    /*
     * Attributes
     */
    private final List<Player> players;
    private final List<Image> board;
    private final List<Image> explosions;
    private final List<Image> scoreLine;
    private final List<Image> timeLine;
    
    /**
     * Constructs a GameState given the five lists that define a GameState in
     * the clients perspective.
     * 
     * @param players
     *            list of all players
     * @param board
     *            list of the block images, ordered according to the reading
     *            order!
     * @param explosions
     *            list of the images of the explosions and bombs in reading
     *            order, contains null if a cell does not host a bomb or an
     *            explosion.
     * @param scoreLine
     *            list of the images representing the ScoreLine.
     * @param timeLine
     *            list of the images representing the time line (60 LED's).
     * @throws NullPointerException
     *             if one of the arguments is null
     * @throws IllegalArgumentException
     *             if one of the lists does not have the correct size
     */
    public GameState(List<Player> players, List<Image> board,
            List<Image> explosions, List<Image> scoreLine, List<Image> timeLine) {

        // 1) verify that all the lists have the correct size and that they are
        //    not null
        if (players.size() != NUMBER_OF_PLAYERS
                || board.size() != Cell.COUNT 
                || explosions.size() != Cell.COUNT
                || timeLine.size() != GameStateDeserializer.TIMELINE_LENGTH
                || scoreLine.size() != GameStateDeserializer.SCORELINE_LENGTH)
            throw new IllegalArgumentException("Incorrect list size.");
    
        
        this.players = Collections.unmodifiableList(
                new ArrayList<>(players));
        this.board = Collections.unmodifiableList(
                new ArrayList<>(board));
        this.explosions = Collections.unmodifiableList(
                new ArrayList<>(explosions));
        this.scoreLine = Collections.unmodifiableList(
                new ArrayList<>(scoreLine));
        this.timeLine = Collections.unmodifiableList(
                new ArrayList<>(timeLine));
    }

    /**
     * Returns the list of players.
     * 
     * @return list of players
     */
    public List<Player> players() {
        return players;
    }

    /**
     * Returns the list of images used to represent the board.
     * 
     * @return the board images
     */
    public List<Image> board() {
        return board;
    }

    /**
     * Returns the list of images used to represent the bombs and blasts.
     * 
     * @return the explosions images
     */
    public List<Image> explosions() {
        return explosions;
    }

    /**
     * Returns the list of images used to represent the ScorLine.
     * 
     * @return the scoreLine images
     */
    public List<Image> scoreLine() {
        return scoreLine;
    }

    /**
     * Returns the list of images used to represent the TimeLine.
     * 
     * @return the timeLine images
     */
    public List<Image> timeLine() {
        return timeLine;
    }
}

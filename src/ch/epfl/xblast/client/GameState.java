package ch.epfl.xblast.client;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import ch.epfl.xblast.ArgumentChecker;
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
     * 
     */
    public static final class Player {

        /*
         * Attributes
         */
        private final PlayerID id;
        private final int lives;
        private final SubCell position;
        private final Image image;

        /**
         * Constructs a player.
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
            this.image = image;// TODO requireNonNull?
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
    }

    /*
     * Attributes
     */
    private final List<Player> players;
    private final List<Image> board;
    private final List<Image> explosions;
    private final List<Image> scores;
    private final List<Image> time;

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
     *            order, contains null if a block doesn not have any of them.
     * @param scores
     *            list of the images representing the score line
     * @param time
     *            list of the images representing the time line (60 LED's)
     * @throws NullPointerException
     *             if one of the arguments was null
     */
    public GameState(List<Player> players, List<Image> board,
            List<Image> explosions, List<Image> scores, List<Image> time) {
        this.players = Collections.unmodifiableList(
                new ArrayList<>(Objects.requireNonNull(players)));
        this.board = Collections.unmodifiableList(
                new ArrayList<>(Objects.requireNonNull(board)));
        this.explosions = Collections.unmodifiableList(
                new ArrayList<>(Objects.requireNonNull(explosions)));
        this.scores = Collections.unmodifiableList(
                new ArrayList<>(Objects.requireNonNull(scores)));
        this.time = Collections.unmodifiableList(
                new ArrayList<>(Objects.requireNonNull(time)));
    }
    
    
    //FIXME DELETE LATER-----------
    @Override
    /**
     * @param other
     * @return
     */
    public boolean equals(Object that){
        
        return (that instanceof GameState)
                && players.equals(((GameState) that).players)
                && board.equals(((GameState) that).board)
                && explosions.equals(((GameState) that).explosions)
                && scores.equals(((GameState) that).scores)
                && time.equals(((GameState) that).time);
    }
}

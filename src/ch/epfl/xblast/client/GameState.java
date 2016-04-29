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
 * @author Loïc Vandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 *
 */
public final class GameState {

    public static final class Player {
        /*
         * Attributes
         */
        private final PlayerID playerId;
        private final int lives;
        private final SubCell position;
        private final Image image;

        public Player(PlayerID playerId, int lives, SubCell position, Image image) {
            this.playerId = Objects.requireNonNull(playerId);
            this.lives = ArgumentChecker.requireNonNegative(lives);
            this.position = Objects.requireNonNull(position);
            this.image = image;// TODO requireNonNull?
        }

        /**
         * TODO additional
         * @return the playerId
         */
        public PlayerID playerId() {
            return playerId;
        }

        /**
         * @return the lives
         */
        public int lives() {
            return lives;
        }

        /**
         * @return the position
         */
        public SubCell position() {
            return position;
        }
    }

    /*
     * Attributes
     */
    private final List<Player> players;
    private final List<Image> board, explosions, scores, time;

    /**
     * @param players
     * @param board
     *            TODO precise reading order
     * @param explosions
     * @param scores
     * @param time
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

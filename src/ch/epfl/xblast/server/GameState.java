package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.ArgumentChecker;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;

public final class GameState {

    // ATRIBUTES
    private final int ticks;
    private final Board board;
    private final List<Player> players;
    private final List<Bomb> bombs;
    private final List<Sq<Sq<Cell>>> explosions;
    private final List<Sq<Cell>> blasts;

    /**
     * Principal constructor
     * 
     * @param ticks
     * @param board
     * @param players
     * @param bombs
     * @param explosion
     * @param blasts
     * 
     * @throws IllegalArgumentException
     *             if the number of players is not 4 or if the ticks is
     *             negative.
     * @throws NullPointerException
     *             if one object is null.
     */
    public GameState(int ticks, Board board, List<Player> players,
            List<Bomb> bombs, List<Sq<Sq<Cell>>> explosion,
            List<Sq<Cell>> blasts) {

        this.ticks = ArgumentChecker.requireNonNegative(ticks);
        int nbPlayers = players.size();
        if (nbPlayers != 4) {
            throw new IllegalArgumentException(
                    "the Game requires 4 players instead of " + nbPlayers);
        }
        this.board = Objects.requireNonNull(board);

        this.players = Collections.unmodifiableList(
                new ArrayList<>(Objects.requireNonNull(players)));
        this.explosions = Collections.unmodifiableList(
                new ArrayList<>(Objects.requireNonNull(explosion)));
        this.bombs = Collections.unmodifiableList(
                new ArrayList<>(Objects.requireNonNull(bombs)));
        this.blasts = Collections.unmodifiableList(
                new ArrayList<>(Objects.requireNonNull(blasts)));
    }

    /**
     * Initial constructor: create a game with initials values: ticks:0 empties
     * Lists of Bomb, explosion and blast
     * 
     * @param board
     * @param players
     * 
     * @throws IllegalArgumentException
     *             if the number of players is not 4 or if the ticks is
     *             negative.
     */
    public GameState(Board board, List<Player> players) {
        this(0, board, players, new ArrayList<Bomb>(), //
                new ArrayList<Sq<Sq<Cell>>>(), // FIXME Array or Linked
                new ArrayList<Sq<Cell>>()); //
    }

    /**
     * Getter of ticks
     * 
     * @return actual ticks
     */
    public int ticks() {
        return ticks;
    }

    /**
     * Check if the game is Over: lasted 2 minutes or have 1 player left
     * 
     * @return true if the game is over
     */
    public boolean isGameOver() {
        if (ticks >= Ticks.TOTAL_TICKS) {
            return true;
        }
        return alivePlayers().size() >= 1;

    }

    /**
     * @return the remaining time in seconds
     */
    public double remainingTime() {
        return ((double) Ticks.TOTAL_TICKS - ticks) / Ticks.TICKS_PER_SECOND;
    }

    /**
     * check if the game have a winner an return it
     * 
     * @return an Optional containing the winner PlayerID if there is one.
     *         Else return an empty Optional
     */
    public Optional<PlayerID> winner() {
        List<Player> alivePlayers = alivePlayers();
        if (alivePlayers.size() == 1) {
            return Optional.of(alivePlayers.get(0).id());
        }
        return Optional.empty();
    }

    /**
     * Getter for Board
     * @return the actual Board 
     */
    public Board board() {
        return board;
    }

    /**
     * Getter for Players
     * @return a List containing all the players
     */
    public List<Player> players() {
        return players;
    }

    /**
     * Getter for alive players
     * @return a List containing the alive players only
     */
    public List<Player> alivePlayers() {
        List<Player> alivePlayers = new ArrayList<>();
        for (Player p : players) {
            if (p.isAlive()) {
                alivePlayers.add(p);
            }
        }
        return alivePlayers;
    }
    // TODO other methods (next())

    /*
     * private methods
     */

    /**
     * private method to predict the List of blasts for the next Tick
     * @param blasts0
     * @param board0
     * @param explosions0
     * @return the next List of Blasts
     */
    private List<Sq<Cell>> nextBlasts(List<Sq<Cell>> blasts0, Board board0,
            List<Sq<Sq<Cell>>> explosions0) {
        List<Sq<Cell>> blasts1 = new ArrayList<>(); // FIXME right choice?

        // add existing blasts
        for (Sq<Cell> blast : blasts0) {
            Sq<Cell> newBlast = blast.tail();

            if (board0.blockAt(blast.head()).isFree() && !newBlast.isEmpty()) {
                blasts1.add(newBlast);
            }
        }

        // add new blasts
        for (Sq<Sq<Cell>> arm : explosions0) {
            blasts1.add(arm.head());
        }

        return blasts1;
    }
}

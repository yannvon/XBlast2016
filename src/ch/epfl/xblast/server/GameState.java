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

    // Attributes
    private final int ticks;
    private final Board board;
    private final List<Player> players;
    private final List<Bomb> bombs;
    private final List<Sq<Sq<Cell>>> explosions;
    private final List<Sq<Cell>> blasts;

    /**
     * Principal constructor of a GameState.
     * 
     * @param ticks
     *            amount of ticks passed
     * @param board
     *            board to play on
     * @param players
     *            that participate
     * @param bombs
     *            that are already placed
     * @param explosion
     *            current explosions
     * @param blasts
     *            current blasts
     * @throws IllegalArgumentException
     *             if the number of players is not 4 or if the ticks value is
     *             negative.
     * @throws NullPointerException
     *             if one of the objects is null.
     */
    public GameState(int ticks, Board board, List<Player> players,
            List<Bomb> bombs, List<Sq<Sq<Cell>>> explosion, List<Sq<Cell>> blasts) {
        
        //1) check ticks, players and board requirements
        this.ticks = ArgumentChecker.requireNonNegative(ticks);
        int nbPlayers = players.size();
        if (nbPlayers != 4) {
            throw new IllegalArgumentException(
                    "The Game requires 4 players instead of " + nbPlayers);
        }
        this.board = Objects.requireNonNull(board);
        
        //2) copy lists and save an unmodifiable view of them
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
     * Initial constructor, creates a game with the initial values that
     * represent a fresh game. Ticks is set to 0 and the lists of bombs,
     * explosion and blasts are empty.
     * 
     * @param board
     *            to play on
     * @param players
     *            that participate
     * 
     * @throws IllegalArgumentException
     *             if the number of players is not 4 or if the value of ticks is
     *             negative.
     */
    public GameState(Board board, List<Player> players) {
        this(0, board, players, new ArrayList<Bomb>(),  //
                new ArrayList<Sq<Sq<Cell>>>(),          // FIXME Array or Linked
                new ArrayList<Sq<Cell>>());             //
    }

    /**
     * Returns the amount of ticks that have already been played.
     * 
     * @return the number of passed ticks
     */
    public int ticks() {
        return ticks;
    }

    /**
     * Check if the game is over: either 2 minutes are over or there is only one
     * player left.
     * 
     * @return true if the game is over, false otherwise
     */
    public boolean isGameOver() {
        return (ticks >= Ticks.TOTAL_TICKS || alivePlayers().size() <= 1);
    }

    /**
     * Returns the remaining time in seconds.
     * 
     * @return the remaining time in seconds
     */
    public double remainingTime() {
        return ((double) Ticks.TOTAL_TICKS - ticks) / Ticks.TICKS_PER_SECOND;
    }

    /**
     * Check whether the game has a winner. If so, return it.
     * 
     * @return an Optional containing the winner PlayerID if there is one,
     *          otherwise an empty Optional
     */
    public Optional<PlayerID> winner() {
        List<Player> alivePlayers = alivePlayers();
        return alivePlayers.size() == 1 ? Optional.of(alivePlayers.get(0).id())
                : Optional.empty();

//        if (alivePlayers.size() == 1) {                   FIXME plus propre que ca non?
//            return Optional.of(alivePlayers.get(0).id());
//        }
//        return Optional.empty();
        }

    /**
     * Returns the game Board.
     * 
     * @return the current Board 
     */
    public Board board() {
        return board;
    }

    /**
     * Returns a List containing all players participating.
     * 
     * @return a List containing all the players
     */
    public List<Player> players() {
        return players;
    }

    /**
     * Returns a List containing all currently alive players only.
     * 
     * @return a List containing the players that are alive
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
     * SUUPLEMENTARY METHOD: Calculate the List of blasts for the next Tick
     * 
     * @param blasts0
     *            current blasts
     * @param board0
     *            current game board
     * @param explosions0
     *            current explosions
     * @return the List of Blasts for the next Tick
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

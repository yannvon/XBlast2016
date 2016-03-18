package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.ArgumentChecker;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.Lists;
import ch.epfl.xblast.PlayerID;

public final class GameState {

    // Static attributes
    private static final List<List<PlayerID>> playerPermutation = Collections
            .unmodifiableList(Lists
                    .<PlayerID> permutations(Arrays.asList(PlayerID.values())));
    // FIXME ArrayList or LinkedList?
    private static final Random RANDOM = new Random(2016);
    private static final Block[] BONUS_GENERATOR = {Block.BONUS_BOMB,Block.BONUS_RANGE,Block.FREE};
    
    
    // Instance attributes
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
            List<Bomb> bombs, List<Sq<Sq<Cell>>> explosion,
            List<Sq<Cell>> blasts) {

        // 1) check ticks, players and board requirements
        this.ticks = ArgumentChecker.requireNonNegative(ticks);
        int nbPlayers = players.size();
        if (nbPlayers != 4) {
            throw new IllegalArgumentException(
                    "The Game requires 4 players instead of " + nbPlayers);
        }
        this.board = Objects.requireNonNull(board);

        // 2) copy lists and save an unmodifiable view of them
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
        this(0, board, players, new ArrayList<Bomb>(), //
                new ArrayList<Sq<Sq<Cell>>>(), // FIXME Array or Linked
                new ArrayList<Sq<Cell>>()); //
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
     *         otherwise an empty Optional
     */
    public Optional<PlayerID> winner() {
        List<Player> alivePlayers = alivePlayers();
        return alivePlayers.size() == 1 ? Optional.of(alivePlayers.get(0).id())
                : Optional.empty();
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

    /**
     * @return
     */
    public Map<Cell, Bomb> bombedCells() {
        Map<Cell, Bomb> bombedCells = new HashMap<>();
        for (Bomb bomb : bombs) {
            bombedCells.put(bomb.position(), bomb);
        }
        return bombedCells;
    }

    /**
     * @return
     */
    public Set<Cell> blastedCells() {
        Set<Cell> blastedCells = new HashSet<>();
        for (Sq<Cell> blast : blasts) {
            blastedCells.add(blast.head());
        }
        return blastedCells;
    }
    
    //Overload 
    public Set<Cell> blastedCells(List<Sq<Cell>> blasts) {
        Set<Cell> blastedCells = new HashSet<>();
        for (Sq<Cell> blast : blasts) {
            blastedCells.add(blast.head());
        }
        return blastedCells;
    }

    public GameState next(Map<PlayerID, Optional<Direction>> speedChangeEvents,
            Set<PlayerID> bombDrpEvents) {
        
        //--- EVOLUTION ORDER ---
        // 1) blasts
        List<Sq<Cell>> blasts1 = nextBlasts(blasts, board, explosions);
        
        
        //prepare consumed bonuses and player bonuses
        Set<Cell> consumedBonuses = new HashSet<>();
        Map<PlayerID, Bonus> playerBonuses = new HashMap<>();
        
        for (Player pl : players) {
            Cell plPosition = pl.position().containingCell();
            Block blockAtPosition = board.blockAt(plPosition);
            if (blockAtPosition.isBonus() && pl.position().isCentral()) {
                consumedBonuses.add(plPosition);
                playerBonuses.put(pl.id(), blockAtPosition.associatedBonus());
            }
        }
        
        
        // 2) board
        Board board1 = nextBoard(board, consumedBonuses, blastedCells(blasts1));   //FIXME assistant told me that.. (?)
        // 3) explosion
        List<Sq<Sq<Cell>>> explosions1 = nextExplosions(explosions);
        // 4) bombs
        // 4.1) a new bomb appears
        List<Bomb> newlyDroppedBombs = newlyDroppedBombs(, bombDropEvents, bombs0) //sort player list!!
        
        // 4.2) bombs explode (disappear) and create 4 new explosions
        // 4.3) bombs that are blasted explode immediately
        // 5) players
        List<Player> players1 = nextPlayers(players, playerBonuses, bombedCells1, board1, blastedCells1, speedChangeEvents);

        return null;
    }

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
    private static List<Sq<Cell>> nextBlasts(List<Sq<Cell>> blasts0, Board board0,
            List<Sq<Sq<Cell>>> explosions0) {
        List<Sq<Cell>> blasts1 = new ArrayList<>(); // FIXME right choice?

        // add existing blasts
        for (Sq<Cell> blast : blasts0) {
            Sq<Cell> newBlast = blast.tail();

            if (board0.blockAt(blast.head()).isFree() && !newBlast.isEmpty()){ 
                blasts1.add(newBlast);
            }
        }
        // add new blasts
        for (Sq<Sq<Cell>> arm : explosions0) {
            blasts1.add(arm.head());
        }
        return blasts1;
    }
    
    

    /**
     * @param board0
     * @param consumedBonuses
     * @param blastedCells1
     * @return
     */
    private static Board nextBoard(Board board0, Set<Cell> consumedBonuses,
            Set<Cell> blastedCells1) {
        List<Sq<Block>> board1 = new ArrayList<>();
        List<Cell> allCells = Cell.ROW_MAJOR_ORDER;
        
        for (Cell currentCell : allCells) {
            Sq<Block> blocks = board0.blocksAt(currentCell);
            Block head = blocks.head();

            if (consumedBonuses.contains(currentCell)) {
                board1.add(Sq.constant(Block.FREE));
            } else if (head.isBonus() && blastedCells1.contains(currentCell)) {

                Sq<Block> newBonusSq = blocks.tail()
                        .limit(Ticks.BONUS_DISAPPEARING_TICKS);
                newBonusSq = newBonusSq.concat(Sq.constant(Block.FREE));
                board1.add(newBonusSq);

            } else if (head == Block.DESTRUCTIBLE_WALL
                    || blastedCells1.contains(currentCell)) {

                Sq<Block> newCrumblingWallSq = Sq.repeat(
                        Ticks.WALL_CRUMBLING_TICKS, Block.DESTRUCTIBLE_WALL);

                Block randomBonus = BONUS_GENERATOR[RANDOM
                        .nextInt(BONUS_GENERATOR.length)];
                newCrumblingWallSq = newCrumblingWallSq
                        .concat(Sq.constant(randomBonus));
                board1.add(newCrumblingWallSq);

            } else {
                board1.add(blocks.tail());
            }

        }
        return new Board(board1);
    }
    
    /**
     * @param players0
     * @param playerBonuses
     * @param bombedCells1
     * @param board1
     * @param blastedCells1
     * @param speedChangeEvents
     * @return
     */
    private static List<Player> nextPlayers(List<Player> players0,
            Map<PlayerID, Bonus> playerBonuses, Set<Cell> bombedCells1,
            Board board1, Set<Cell> blastedCells1,
            Map<PlayerID, Optional<Direction>> speedChangeEvents) {
        //TODO
        return players0;
    }
    
    /**
     * Calculates the explosions for the next GameState according to the current one.
     * 
     * @param explosions0
     * @return
     */
    private static List<Sq<Sq<Cell>>> nextExplosions(List<Sq<Sq<Cell>>> explosions0){
        List<Sq<Sq<Cell>>> explosions1 = new ArrayList<>();  //FIXME choice?
        for(Sq<Sq<Cell>> explosionArm : explosions0){
            Sq<Sq<Cell>> newExplosionArm = explosionArm.tail();
            
            if(!newExplosionArm.isEmpty()){
                explosions1.add(newExplosionArm);
            }
        }
        return explosions1;
    }
    
    /**
     * Returns the list of newly dropped bombs, given the current players, the
     * events of dropping bombs and the currently dropped bombs.
     * 
     * @param players0 the list players, in the correct order! (respect playerPermutation!)
     * @param bombDropEvents
     * @param bombs0
     * @return
     */
    private static List<Bomb> newlyDroppedBombs(List<Player> players0,
            Set<PlayerID> bombDropEvents, List<Bomb> bombs0) {
        
        Set<Cell> placedBombs = new HashSet<>();
        for(Bomb b : bombs0){
            placedBombs.add(b.position());
        }
        
        List<Bomb> newlyDroppedBombs = new ArrayList<>();
        
        // for every player, check if he wants to drop a bomb, if so check all other conditions
        for(Player p : players0){
            
            Cell position = p.position().containingCell();
            
            // 1) 
            boolean wantsToDrop = bombDropEvents.contains(p.id());
            // 2) 
            boolean isAlive = p.isAlive();
            // 3) 
            boolean locationFree = !placedBombs.contains(position);
            // 4)
            int placedBombsNb = 0;
            for(Bomb b : bombs0){
                if(b.ownerId() == p.id()){
                    ++placedBombsNb;
                }
            }
            boolean canAddBomb = placedBombsNb < p.maxBombs();
            
            if(wantsToDrop && isAlive && locationFree && canAddBomb){
                placedBombs.add(position);
                newlyDroppedBombs.add(p.newBomb());
            }
        }
        return newlyDroppedBombs;
        
    }
}

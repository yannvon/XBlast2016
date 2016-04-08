package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
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
import ch.epfl.xblast.SubCell;
import ch.epfl.xblast.server.Player.DirectedPosition;
import ch.epfl.xblast.server.Player.LifeState;
import ch.epfl.xblast.server.Player.LifeState.State;

/**
 * This class represents a current state of a game.
 * 
 * @author Loïc Vandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 *
 */
public final class GameState {

    // Static attributes
    private static final int ALLOWED_DISTANCE_TO_BOMB = 6;
    private static final List<List<PlayerID>> PLAYER_PERMUTATION = Collections
            .unmodifiableList(Lists.<PlayerID> permutations(Arrays.asList(PlayerID.values())));
    private static final Random RANDOM = new Random(2016);
    private static final Block[] BONUS_GENERATOR = { Block.BONUS_BOMB,
            Block.BONUS_RANGE, Block.FREE };

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
     *            players that participate
     * @param bombs
     *            bombs that are already placed
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

        // 1) copy lists and save an unmodifiable view of them
        this.players = Collections.unmodifiableList(
                new ArrayList<>(Objects.requireNonNull(players)));
        this.explosions = Collections.unmodifiableList(
                new ArrayList<>(Objects.requireNonNull(explosion)));
        this.bombs = Collections.unmodifiableList(
                new ArrayList<>(Objects.requireNonNull(bombs)));
        this.blasts = Collections.unmodifiableList(
                new ArrayList<>(Objects.requireNonNull(blasts)));
        
        // 2) check ticks, players and board requirements
        this.ticks = ArgumentChecker.requireNonNegative(ticks);
        int nbPlayers = players.size();
        if (nbPlayers != 4) {
            throw new IllegalArgumentException(
                    "The Game requires 4 players instead of " + nbPlayers);
        }
        this.board = Objects.requireNonNull(board);
    }

    /**
     * Initial constructor, creates a game with the initial values that
     * represent a fresh game. Ticks is set to 0 and the lists of bombs,
     * explosion and blasts are empty.
     * 
     * @param board
     *            board on which the game is played on
     * @param players
     *            players that participate
     * 
     * @throws IllegalArgumentException
     *             if the number of players is not 4 or if the value of ticks is
     *             negative.
     */
    public GameState(Board board, List<Player> players) {
        this(0, 
             board, 
             players,
             Collections.emptyList(),
             Collections.emptyList(),
             Collections.emptyList());
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
        return (ticks > Ticks.TOTAL_TICKS || alivePlayers().size() <= 1);
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
        return alivePlayers.size() == 1 ? 
                Optional.of(alivePlayers.get(0).id()) : Optional.empty();
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
     * Returns a map that associates the bombs to the Cells they occupy.
     * 
     * @return a map associating the bombs to their cell
     */
    public Map<Cell, Bomb> bombedCells() {
        return bombedCells(bombs);
    }

    /**
     * Returns a set with all Cells on which there is at least one blast
     * 
     * @return set with all blasted Cells
     */
    public Set<Cell> blastedCells() {
        return blastedCells(blasts);
    }

    /**
     * Returns the GameState at the following Tick, according to the current
     * GameState and the events that happened.
     * 
     * @param speedChangeEvents
     *            mapping between all the player that want to change direction
     *            and the respective speedChange.
     * @param bombDrpEvents
     *            set of players that want to add a bomb
     * @return GameState of the next tick
     */
    public GameState next(Map<PlayerID, Optional<Direction>> speedChangeEvents,
            Set<PlayerID> bombDrpEvents) {

        // Declare and fill consumedBonuses and playerBonuses (used later)
        Set<Cell> consumedBonuses = new HashSet<>();
        Map<PlayerID, Bonus> playerBonuses = new HashMap<>();
        List<Player> sortedPlayers = sortedPlayers();

        for (Player pl : sortedPlayers) {
            Cell plPosition = pl.position().containingCell();
            Block blockAtPosition = board.blockAt(plPosition);
            if (blockAtPosition.isBonus() && pl.position().isCentral() && !consumedBonuses.contains(plPosition)) {
                consumedBonuses.add(plPosition);
                playerBonuses.put(pl.id(), blockAtPosition.associatedBonus());
            }
        }

        // --- EVOLUTION ORDER ---

        // 1) evolve blasts
        List<Sq<Cell>> blasts1 = nextBlasts(blasts, board, explosions);
        // 1.1) using the evolved blast we call our custom private method that
        // returns a set of all blasted cells.
        Set<Cell> blastedCells1 = blastedCells(blasts1);

        // 2) evolve board
        Board board1 = nextBoard(board, consumedBonuses, blastedCells1);

        // 3) evolve explosion
        List<Sq<Sq<Cell>>> explosions1 = nextExplosions(explosions);

        // 4) evolve bombs
        List<Bomb> bombs0 = new ArrayList<>(bombs);
        List<Bomb> bombs1 = new ArrayList<>();

        // 4.1) add all newly dropped bombs (using sortedPlayers method to
        // resolve conflicts)
        bombs0.addAll(newlyDroppedBombs(sortedPlayers, bombDrpEvents, bombs));

        // 4.2) every bomb either explodes (and disappears) or evolves (fuse-1).
        for (Bomb b : bombs0) {

            Sq<Integer> newFuse = b.fuseLengths().tail();
            // if the fuse has burned out or the bomb was hit by a blast it
            // explodes and disappears
            if (newFuse.isEmpty() || blastedCells1.contains(b.position())) {
                explosions1.addAll(b.explosion());
            }
            // otherwise only the fuse gets shorter
            else {
                bombs1.add(new Bomb(b.ownerId(), b.position(), newFuse, b.range()));
            }
        }

        // 5) players
        List<Player> players1 = nextPlayers(players, playerBonuses,
                bombedCells(bombs1).keySet(), board1, blastedCells1,
                speedChangeEvents);

        // 6) construct and return the new GameStates
        return new GameState(ticks + 1, board1, players1, bombs1, explosions1, blasts1);
    }

    /*
     * private methods
     */

    /**
     * Computes the List of blasts for the next Tick.
     * 
     * @param blasts0
     *            current blasts
     * @param board0
     *            current game board
     * @param explosions0
     *            current explosions
     * @return the List of Blasts for the next Tick
     */
    private static List<Sq<Cell>> nextBlasts(List<Sq<Cell>> blasts0,
            Board board0, List<Sq<Sq<Cell>>> explosions0) {
        List<Sq<Cell>> blasts1 = new ArrayList<>();

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

    /**
     * Calculates the state of the next Board according to the current Board,
     * the consumed bonuses and the new blasts.
     * 
     * @param board0
     *            current Board
     * @param consumedBonuses
     *            bonus that were consumed by the players
     * @param blastedCells1
     *            Cells that got hit by a blast
     * @return Board at the next Tick
     */
    private static Board nextBoard(Board board0, Set<Cell> consumedBonuses,
            Set<Cell> blastedCells1) {

        /*
         * 1) create list of block sequences that will be used to create 
         *    new board
         */
        List<Sq<Block>> board1 = new ArrayList<>();

        /*
         * 2) go through every cell
         */
        List<Cell> allCells = Cell.ROW_MAJOR_ORDER;

        for (Cell currentCell : allCells) {
            Sq<Block> blocks = board0.blocksAt(currentCell);
            Block head = blocks.head();

            /*
             * 2.1) if current Cell contains a bonus that was consumed,
             *      the Block becomes free.
             */
            if (consumedBonuses.contains(currentCell)) {
                board1.add(Sq.constant(Block.FREE));
            }
            
            /*
             * 2.2) if current Cell is a bonus and was blasted, it disappears.
             *      (we also handle the case where a already disappearing 
             *      bonus gets blasted again)
             */
            else if (head.isBonus() && blastedCells1.contains(currentCell)) {

                Sq<Block> newBonusSq = 
                        blocks.tail().limit(Ticks.BONUS_DISAPPEARING_TICKS);
                newBonusSq = newBonusSq.concat(Sq.constant(Block.FREE));
                board1.add(newBonusSq);
            }
            
            /*
             * 2.3) if current Cell was a destructible wall and got blasted,
             *      generate a sequence that will maybe make a bonus appear.
             */
            else if (head == Block.DESTRUCTIBLE_WALL
                    && blastedCells1.contains(currentCell)) {

                Sq<Block> newCrumblingWallSq = 
                        Sq.repeat(Ticks.WALL_CRUMBLING_TICKS, Block.CRUMBLING_WALL);

                // use the bonus generator array to get a random bonus.
                Block randomBonus = 
                        BONUS_GENERATOR[RANDOM.nextInt(BONUS_GENERATOR.length)];
                newCrumblingWallSq = 
                        newCrumblingWallSq.concat(Sq.constant(randomBonus));
                board1.add(newCrumblingWallSq);
            }
            
            /*
             * 2.4) if none of the above was the case for the current cell
             *      evolve it by taking its tail.
             */
            else {
                board1.add(blocks.tail());
            }
        }
        return new Board(board1);
    }

    /**
     * Computes the explosions for the next GameState according to the current
     * ones. This method doesn't add any explosions. It merely evolves existing
     * ones.
     * 
     * @param explosions0
     *            list of current explosions
     * @return list of all explosions at the next Tick
     */
    private static List<Sq<Sq<Cell>>> nextExplosions(
            List<Sq<Sq<Cell>>> explosions0) {
        List<Sq<Sq<Cell>>> explosions1 = new ArrayList<>();
        
        // Every explosion is evolved as long as it isn't empty
        for (Sq<Sq<Cell>> explosionArm : explosions0) {
            Sq<Sq<Cell>> newExplosionArm = explosionArm.tail();
            if (!newExplosionArm.isEmpty()) {
                explosions1.add(newExplosionArm);
            }
        }
        return explosions1;
    }

    /**
     * Returns the list of newly dropped bombs, given the current players, the
     * events of players that want to drop bombs and the currently placed bombs.
     * 
     * @param players0
     *            the list of players, in the correct order (using
     *            sortedPlayers)
     * @param bombDropEvents
     *            events of players wanting to drop bombs
     * @param bombs0
     *            bombs that are currently placed
     * @return a list of all the newly dropped bombs
     */
    private static List<Bomb> newlyDroppedBombs(List<Player> players0,
            Set<PlayerID> bombDropEvents, List<Bomb> bombs0) {

        // Create a set containing all currently placed bombs
        Set<Cell> placedBombs = new HashSet<>(bombedCells(bombs0).keySet());

        // Declare list of the newly dropped bombs (output)
        List<Bomb> newlyDroppedBombs = new ArrayList<>();

        // For every player (in priority order) check if he will drop a bomb
        for (Player p : players0) {
            Cell position = p.position().containingCell();

            // 1) Player wants to drop a bomb
            boolean wantsToDrop = bombDropEvents.contains(p.id());
            // 2) Player is alive
            boolean isAlive = p.isAlive();
            // 3) The current location is free
            boolean locationFree = !placedBombs.contains(position);
            // 4) The player hasn't used all his bombs
            int placedBombsNb = 0;
            for (Bomb b : bombs0) {
                if (b.ownerId() == p.id()) {
                    ++placedBombsNb;
                }
            }
            boolean canAddBomb = placedBombsNb < p.maxBombs();

            // if every condition is satisfied the player drops a bomb
            if (wantsToDrop && isAlive && locationFree && canAddBomb) {
                placedBombs.add(position);
                newlyDroppedBombs.add(p.newBomb());
            }
        }
        return newlyDroppedBombs;
    }
        
    
    /**
     * Method in charge of evolving the Players according to what happens around
     * them and what commands they give.
     * 
     * @param players0
     *            (unordered) list of all players
     * @param playerBonuses
     *            mapping playerID's to consumed bonuses
     * @param bombedCells1
     *            set of all cells containing a bomb
     * @param board1
     *            board on which the game is played on
     * @param blastedCells1
     *            cell that contain a blast
     * @param speedChangeEvents
     *            events of players wanting to change their speed (direction)
     * @return list containing all players of the following tick
     */
    private static List<Player> nextPlayers(List<Player> players0,
            Map<PlayerID, Bonus> playerBonuses, Set<Cell> bombedCells1,
            Board board1, Set<Cell> blastedCells1,
            Map<PlayerID, Optional<Direction>> speedChangeEvents) {

        /*
         * 1) move the players by first creating new DirectedPosition Sq 
         *      and then evolving it.
         */
        List<Player> movedPlayers = 
                nextMovedPlayers(players0, board1, bombedCells1, speedChangeEvents);
        
        /*
         * 2) change the lifeState sequence of the players 
         *      according to their new position.
         */
        List<Player> newStatePlayers = nextEvolvedStatePlayers(movedPlayers, blastedCells1);
        
        /*
         * 3) add potential picked up bonuses to the players
         */
        List<Player> players1 = nextUpgradedPlayers(newStatePlayers,playerBonuses);
        
        return players1;
    }

    /**
     * ADDITIONAL METHOD: Computes the new DirectedPosition sequence for every
     * player according to the speedChangeEvents and evolves the (potentially)
     * new sequences if the player fulfils all conditions to move.
     * 
     * @param speedChangeEvents
     *            mapping between all the player that want to change direction
     *            and the respective speedChange.
     * @param bombedCells1
     *            set of all cells that host a bomb
     * @param players0
     *            list of all players in their current state
     * @param board1
     *            board of the next tick
     * @return a list of the moved players for the next step
     */
    private static List<Player> nextMovedPlayers(List<Player> players0,
            Board board1, Set<Cell> bombedCells1,
            Map<PlayerID, Optional<Direction>> speedChangeEvents) {

        List<Player> players1 = new ArrayList<>();
        for (Player p : players0) {
            PlayerID id = p.id();

            /*
             * STEP 1 Computation of the new Directed Position sequence 
             *        - if the player does not want to move nothing is changed here.
             */
            Sq<DirectedPosition> directedPositions1 = speedChangeEvents.containsKey(id) ? 
                            constructDPSq(p, speedChangeEvents.get(id)) : p.directedPositions();

            /*
             *  STEP 2 Evolution of Directed Position sequence
             *         - multiple criteria have to be met:
             */
                            
            // defining some useful variables
            SubCell currentSubCell = p.position();
            Cell nextCell = directedPositions1.tail()
                    .findFirst(d -> d.position().isCentral()).position()
                    .containingCell();
            Block nextBlock = board1.blockAt(nextCell);
            SubCell nextSubCell = directedPositions1.tail().head().position();

            // evaluation of the different logic expressions
            boolean movingTowardsCentral = 
                    currentSubCell.distanceToCentral() > nextSubCell.distanceToCentral();
            boolean canMove = p.lifeState().canMove();
            boolean blockedByWall = currentSubCell.isCentral()
                    && !nextBlock.canHostPlayer();
            boolean blockedByBomb = bombedCells1
                    .contains(currentSubCell.containingCell())
                    && currentSubCell
                            .distanceToCentral() == ALLOWED_DISTANCE_TO_BOMB
                    && movingTowardsCentral;

            // if all criteria is met the player can move
            if (canMove && !blockedByWall && !blockedByBomb) {
                directedPositions1 = directedPositions1.tail();
            }

            /*
             * STEP 3 add the new moved player to the list
             */
            players1.add(new Player(p.id(), p.lifeStates(), directedPositions1,
                    p.maxBombs(), p.bombRange()));
        }
        return players1;
    }
    
    /**
     * ADDITIONAL METHOD for nextPlayers. Constructs a sequence of DirectedPositions
     * according to where the player wants to go.
     * 
     * @param p
     *            player that wants to move
     * @param speedChangeEvent
     *            Direction where the player wants to go, empty if he wants to stop
     *            
     * @return a sequence of DirectedPosition that describes the players path
     */
    private static Sq<DirectedPosition> constructDPSq(Player p,
            Optional<Direction> speedChange) {
    
        /*
         * 1) compute direction where player where player wants to go
         */
        Direction d = speedChange.orElse(p.direction());
        
        /*
         * 2) a player can immediately go back or continue in same direction
         */
        if (speedChange.isPresent() && d.isParallelTo(p.direction())) {
            return DirectedPosition.moving(new DirectedPosition(p.position(),d));
        }
    
        /*
         * 3) if a player wants to do fancy stuff wait until next central cell
         */
        else {
            Sq<DirectedPosition> sq = p.directedPositions();
            // find nearest central SubCell
            SubCell nextCentral = sq.findFirst(t -> t.position().isCentral()).position();
    
            /*
             * 3.1) compute first part of sequence
             */
            Sq<DirectedPosition> dp1 = 
                    sq.takeWhile(t -> !t.position().isCentral());
            /*
             *  3.2) compute second part: the player either stays at the central
             *       SubCell or takes a turn.
             */
            //
            Sq<DirectedPosition> dp2 = speedChange.isPresent()
                    ? DirectedPosition
                            .moving(new DirectedPosition(nextCentral, d))
                    : DirectedPosition
                            .stopped(new DirectedPosition(nextCentral, d));
    
            return dp1.concat(dp2);
        }
    }

    /**
     * ADDITIONAL FUNCTION: determines the new sequence of LifeStates for each
     * player
     * 
     * @param movedPlayers
     *            list of all players that have an evolved DirectedPosition
     *            sequence
     * @param blastedCells1
     *            set containing all blasted Cells of the next Tick
     *
     * @return the list of players with the new LifeState sequence for the next
     *         step
     */
    private static List<Player> nextEvolvedStatePlayers(List<Player> movedPlayers,
            Set<Cell> blastedCells1) {
        
        List<Player> newStatePlayer = new ArrayList<>();
        for (Player p : movedPlayers) {
            
            boolean blasted = blastedCells1
                    .contains(p.position().containingCell());
            boolean vulnerable = p.lifeState().state() == State.VULNERABLE;

            Sq<LifeState> lifeStates1 = (blasted && vulnerable)
                    ? p.statesForNextLife() : p.lifeStates().tail();

            newStatePlayer.add(new Player(p.id(), lifeStates1,
                    p.directedPositions(), p.maxBombs(), p.bombRange()));
        }        
        return newStatePlayer;
    }

    /**
     * ADDITIONAL FUNCTION: Computes the list of players with changed attributes
     * if they picked up a bonus.
     * 
     * @param newStatePlayers
     *            list of players that have a new sequence of DirectedPositions
     *            and LifeStates
     * @param playerBonuses
     *            mapping playerID's to consumed bonuses
     * 
     * @return the list of the players with applied bonuses
     */
    private static List<Player> nextUpgradedPlayers(
            List<Player> newStatePlayers, Map<PlayerID, Bonus> playerBonuses) {

        List<Player> players1 = new ArrayList<>();
        
        for (Player p : newStatePlayers) {
            players1.add(playerBonuses.containsKey(p.id())
                    ? playerBonuses.get(p.id()).applyTo(p) : p);
        }
        return players1;
    }

    /**
     * OVERLOAD: returns a set with all cells on which there is a blast. The
     * list of blasts is given as parameter.
     * 
     * @param blasts
     *            list of all blasts
     * @return set with all blasted Cells
     */
    private static Set<Cell> blastedCells(List<Sq<Cell>> blasts) {

        Set<Cell> blastedCells = new HashSet<>();
        for (Sq<Cell> blast : blasts) {
            blastedCells.add(blast.head());
        }
        return blastedCells;
    }

    /**
     * OVERLOAD: returns a map that associates the bombs to the Cells they
     * occupy. The list of bombs is given as parameter.
     * 
     * @param bombs
     *            list of bombs that will be converted to a map
     * @return a map associating the bombs to their cell
     */
    private static Map<Cell, Bomb> bombedCells(List<Bomb> bombs) {

        Map<Cell, Bomb> bombedCells = new HashMap<>();
        for (Bomb bomb : bombs) {
            bombedCells.put(bomb.position(), bomb);
        }
        return bombedCells;
    }

    /**
     * HELPER METHOD: Returns a sorted version of the list of players according to current
     * permutation that defines who has the priority in case of a conflict.
     * 
     * @return a list of players ordered by priority
     */
    private List<Player> sortedPlayers() {
        List<Player> sortedPlayers = new ArrayList<>();

        // get permutation that is currently valid
        List<PlayerID> idSorted = PLAYER_PERMUTATION
                .get(ticks % PLAYER_PERMUTATION.size());

        // create a map that associates the playerID to every players
        Map<PlayerID, Player> pMap = new HashMap<>();
        for (Player p : players) {
            pMap.put(p.id(), p);
        }

        // sort players according to current permutation
        for (PlayerID id : idSorted) {
            sortedPlayers.add(pMap.get(id));
        }
        return sortedPlayers;
    }
}
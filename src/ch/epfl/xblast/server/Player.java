package ch.epfl.xblast.server;

import java.util.Objects;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.ArgumentChecker;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.SubCell;

/**
 * A player being characterised by a multitude of attributes. This is an
 * immutable class.
 * 
 * @author Loïc Vandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 *
 */
public final class Player {

    /**
     * LifeState of the Player. (Represents the amount of lives and the players
     * current state)
     */
    public static final class LifeState {

        /**
         * An enumeration of all possible states that the player can be in.
         */
        public enum State {
            INVULNERABLE, VULNERABLE, DYING, DEAD;
        }

        // Attributes
        private final int lives;
        private final State state;

        /**
         * Sole constructor of a LifeState.
         * 
         * @param lives
         *            amount of lives
         * @param State
         *            state of the player upon construction
         * @throws IllegalArgumentException
         *             if lives is negative
         * @throws NullPointerException
         *             if state is null
         */
        public LifeState(int lives, State state) {
            this.lives = ArgumentChecker.requireNonNegative(lives);
            this.state = Objects.requireNonNull(state);
        }

        /**
         * Returns the amount of lives the player has.
         * 
         * @return amount of lives
         */
        public int lives() {
            return lives;
        }

        /**
         * Return current state of the player.
         * 
         * @return state of the player
         */
        public State state() {
            return state;
        }

        /**
         * Determines if the player can move or not.
         * 
         * @return true if the player is allowed to move, false otherwise
         */
        public boolean canMove() {//FIXME
            switch (state) {
            case VULNERABLE:
            case INVULNERABLE:
                return true;
            default:
                return false;
            }
        }

    }

    /**
     * A directed position is another attribute of a player. It consists of a
     * direction and a position.
     */
    public static final class DirectedPosition {

        /**
         * Static method that returns an infinite sequence of the given directed
         * position.
         * 
         * @param p
         *            directed position
         * @return a infinite sequence of that directed position
         */
        public static Sq<DirectedPosition> stopped(DirectedPosition p) {
            return Sq.constant(p);
        }

        /**
         * Static method that returns an infinite sequence representing the
         * Player moving forward towards a given direction.
         * 
         * @param p
         *            directed Position
         * @return Sequence of directed position representing the player moving
         *         forwards
         */
        public static Sq<DirectedPosition> moving(DirectedPosition p) {
            return Sq.iterate(p,
                    pos -> new DirectedPosition(
                            pos.position.neighbor(pos.direction),
                            pos.direction));
        }

        // Attributes
        private final SubCell position;
        private final Direction direction;

        /**
         * Constructs a directed position with given parameters.
         * 
         * @param position
         *            SubCell position of player
         * @param direction
         *            Direction towards which player is heading
         * @throws NullPointerException
         *             if one of the parameters is null
         */
        public DirectedPosition(SubCell position, Direction direction) {
            this.position = Objects.requireNonNull(position);
            this.direction = Objects.requireNonNull(direction);
        }

        /**
         * Returns the position of the directed position.
         * 
         * @return position
         */
        public SubCell position() {
            return position;
        }

        /**
         * Returns the direction of the directed position.
         * 
         * @return direction
         */
        public Direction direction() {
            return direction;
        }

        /**
         * Returns a new directed position with same direction but new position.
         * 
         * @param newPosition
         * @return DirectedPosition with changed position but same direction
         */
        public DirectedPosition withPosition(SubCell newPosition) {
            return new DirectedPosition(newPosition, direction);
        }

        /**
         * Returns a new directed position with same position as this but new
         * direction.
         * 
         * @param newDirection
         * @return DirectedPosition with changed direction but same position
         */
        public DirectedPosition withDirection(Direction newDirection) {
            return new DirectedPosition(position, newDirection);
        }
    }

    // Attributes
    private final PlayerID id;
    private final Sq<LifeState> lifeStates;
    private final Sq<DirectedPosition> directedPos;
    private final int maxBombs;
    private final int bombRange;

    /**
     * Constructs a player from given arguments.
     * 
     * @param id
     *            id of the player
     * @param lifeStates
     *            life state of the new player
     * @param directedPos
     *            directedPosition of the player
     * @param maxBombs
     *            maximal number of bombs the player can drop
     * @param bombRange
     *            range of the players bombs
     * @throws NullPointerException
     *             if one of the first three arguments is null.
     * @throws IllegalArgumentException
     *             if one of the last three arguments is negative.
     */
    public Player(PlayerID id, Sq<LifeState> lifeStates,
            Sq<DirectedPosition> directedPos, int maxBombs, int bombRange) {
        
        this.id = Objects.requireNonNull(id);
        this.lifeStates = Objects.requireNonNull(lifeStates);
        this.directedPos = Objects.requireNonNull(directedPos);
        this.maxBombs = ArgumentChecker.requireNonNegative(maxBombs);
        this.bombRange = ArgumentChecker.requireNonNegative(bombRange);
    }

    /**
     * Constructs a player from given arguments. This constructor doesn't take a
     * directed position and a sequence of LifeStates, but only the amount of
     * lives and the Cell the player will be in.(The newly constructed player
     * will face south)
     * 
     * @param id
     *            id of the player
     * @param lives
     *            amount of lives the new player will have
     * @param position
     *            Cell in which the player will be located
     * @param maxBombs
     *            maximal number of bombs the player can drop
     * @param bombRange
     *            range of the players bombs
     * @throws NullPointerException
     *             if id or position is null
     * @throws IllegalArgumentException
     *             if lives, bombRange or maxBombs are negative
     */
    public Player(PlayerID id, int lives, Cell position, int maxBombs,
            int bombRange) {
        this(id, 
             lifeStateSqCreation(ArgumentChecker.requireNonNegative(lives)),
             DirectedPosition.stopped(new DirectedPosition(SubCell.centralSubCellOf(position), Direction.S)),//FIXME requireNonNull?
             maxBombs,
             bombRange);
    }

    /**
     * Returns the id of the player.
     * 
     * @return id of the player
     */
    public PlayerID id() {
        return id;
    }

    /**
     * Returns the sequence of the LifeState couples (number of lives and state)
     * of the player.
     * 
     * @return the sequence of LifeStates
     */
    public Sq<LifeState> lifeStates() {
        return lifeStates;
    }

    /**
     * Returns the current LifeState couple (number of lives and state) the
     * player is in.
     * 
     * @return current lifeState
     */
    public LifeState lifeState() {
        return lifeStates.head();
    }

    /**
     * Returns the sequence of LifeStates for the next life. (The player will go
     * into DYING state and then either be dead forever, or he will be
     * INVULNERABLE for a moment and then start all over again but with one life
     * less)
     * 
     * @return the appropriate sequence of LifeStates for the players next life
     */
    public Sq<LifeState> statesForNextLife() {
        // creating a sequence of LifeStates where the player is dying
        Sq<LifeState> dying = Sq.repeat(Ticks.PLAYER_DYING_TICKS,
                new LifeState(lives(), LifeState.State.DYING));

        // calling lifeStateSqCreation to add the appropriate sequence
        // (either DEAD or INVULNERABLE followed by VULNERABLE with one life
        // less)
        return dying.concat(lifeStateSqCreation(lives() - 1));
    }

    /**
     * Returns the current number of remaining lives the player has.
     * 
     * @return number of lives the player currently has
     */
    public int lives() {
        return lifeState().lives();
    }

    /**
     * Determines if the player is alive or not.
     * 
     * @return true if the player is alive, false otherwise
     */
    public boolean isAlive() {
        return lives() > 0;
    }

    /**
     * Getter for directedPosition sequence of the player.
     * 
     * @return the sequence of directed positions the player has
     */
    public Sq<DirectedPosition> directedPositions() {
        return directedPos;
    }

    /**
     * Returns the current position of the player.
     * 
     * @return current position
     */
    public SubCell position() {
        return directedPos.head().position();
    }

    /**
     * Returns the direction towards which the player is currently facing.
     * 
     * @return the direction towards which the player is looking
     */
    public Direction direction() {
        return directedPos.head().direction();
    }

    /**
     * Returns the maximal amount of bombs that the player can drop.
     * 
     * @return maximal bomb drop amount
     */
    public int maxBombs() {
        return maxBombs;
    }

    /**
     * Returns a new player that is completely identical except for the maximal
     * amount of bombs that he can drop.
     * 
     * @param newMaxBombs
     *            new amount of bombs that the player can drop
     * @return almost identical player but with a new maxBomb value
     */
    public Player withMaxBombs(int newMaxBombs) {
        return new Player(id, lifeStates, directedPos, newMaxBombs, bombRange);
    }

    /**
     * Returns the range of the explosions produced by this players bombs
     * 
     * @return range of the players bombs
     */
    public int bombRange() {
        return bombRange;
    }

    /**
     * Returns a new player that is completely identical except for the range of
     * the bombs that he can drop.
     * 
     * @param newBombRange
     *            new range of the explosions produces by the bombs that the
     *            player can drop
     * @return almost identical player but with a new bombRange value
     */
    public Player withBombRange(int newBombRange) {
        return new Player(id, lifeStates, directedPos, maxBombs, newBombRange);
    }

    /**
     * Returns a bomb, placed on the players current location. The fuseLength of
     * the bomb is defined by the constant BOMB_FUSE_TICKS.
     * 
     * @return a bomb placed by the player on his current location (Cell)
     */
    public Bomb newBomb() {
        return new Bomb(id, position().containingCell(), Ticks.BOMB_FUSE_TICKS,bombRange);
    }

    /**
     * SUPPLEMENTARY METHOD: given an amount of lives this method will create
     * the appropriate sequence of lifeStates. If the amount of lives is
     * non-positive the returned sequence will consist of the state DEAD,
     * otherwise the INVULNERABLE state will anticipate an infinite state of
     * VULNERABLE)
     * 
     * @param lives
     *            amount of lives remaining
     * @return sequence of liveStates according to input value
     */
    private static Sq<LifeState> lifeStateSqCreation(int lives) {
        if (lives <= 0) {
            // Player is dead.
            return Sq.constant(new LifeState(0, LifeState.State.DEAD));
        } else {
            // Player has at least on life left.
            return Sq
                    .repeat(Ticks.PLAYER_INVULNERABLE_TICKS,
                            new LifeState(lives, LifeState.State.INVULNERABLE))
                    .concat(Sq.constant(
                            new LifeState(lives, LifeState.State.VULNERABLE)));
        }
    }
}
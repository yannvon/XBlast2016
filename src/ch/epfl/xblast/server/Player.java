package ch.epfl.xblast.server;

import java.util.Objects;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.ArgumentChecker;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.SubCell;

/**
 * @author Loïc Vandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 *
 */
public final class Player {

    /**
     *
     */
    public static final class LifeState {

        /**
         *
         */
        public enum State {
            INVULNERABLE, VULNERABLE, DYING, DEAD;

        }

        // ATTRIBUTES
        private int lives;
        private State state;

        /**
         * @param lives
         * @param State
         * @throws IllegalArgumentException
         * @throws NullPointerException
         */
        public LifeState(int lives, State State) {
            this.lives = ArgumentChecker.requireNonNegative(lives);
            this.state = Objects.requireNonNull(state);
        }

        /**
         * @return
         */
        public int lives() {
            return lives;
        }

        /**
         * @return
         */
        public State state() {
            return state;
        }

        /**
         * @return
         */
        public boolean canMove() {
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
     *
     */
    public static final class DirectedPosition {

        /**
         * @param p
         * @return
         */
        public static Sq<DirectedPosition> stopped(DirectedPosition p) {
            return Sq.constant(p);
        }

        /**
         * @param p
         * @return
         */
        public static Sq<DirectedPosition> moving(DirectedPosition p) {
            return Sq.iterate(p,
                    pos -> new DirectedPosition(
                            pos.position.neighbor(pos.direction),
                            pos.direction));
        }

        // ATTRIBUTES
        private SubCell position;
        private Direction direction;

        /**
         * @param position
         * @param direction
         * @throws NullPointerException
         */
        public DirectedPosition(SubCell position, Direction direction) {
            this.position = Objects.requireNonNull(position);
            this.direction = Objects.requireNonNull(direction);
        }

        /**
         * @return
         */
        public SubCell position() {
            return position;
        }

        /**
         * @return
         */
        public Direction direction() {
            return direction;
        }

        /**
         * @param newPosition
         * @return
         */
        public DirectedPosition withPosition(SubCell newPosition) {
            return new DirectedPosition(newPosition, direction);
        }

        /**
         * @param newDirection
         * @return
         */
        public DirectedPosition withDirection(Direction newDirection) {
            return new DirectedPosition(position, newDirection);
        }
    }

    // Attributes
    private PlayerID id;
    private Sq<LifeState> lifeStates;
    private Sq<DirectedPosition> directedPos;
    private int maxBombs;
    private int bombRange;

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
     *            maximal number of bombs
     * @param bombRange
     *            bomb range
     * @throws IllegalArgumentException
     *             if one of the first three arguments is null.
     * @throws NullPointerException
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
     * Constructor
     * @param id
     * @param lives
     * @param position
     * @param maxBombs
     * @param bombRange
     * @throws //TODO
     */
    public Player(PlayerID id, int lives, Cell position, int maxBombs, int bombRange) {
        this(id, 
                lifeStateSqCreation(lives),
                DirectedPosition.stopped(new DirectedPosition(SubCell.centralSubCellOf(position), Direction.S)),
                maxBombs, 
                bombRange);
    }
    
    /**
     * @return
     */
    public PlayerID id(){
        return id;
    }
    
    public Sq<LifeState> lifeStates(){
        return lifeStates;
    }
    
    /**
     * @return
     */
    public LifeState lifeState(){
        return lifeStates.head();
    }
    
    /**
     * @return
     */
    public Sq<LifeState> statesForNextLife(){
        return Sq.repeat(Ticks.PLAYER_DYING_TICKS, new LifeState(lives(), LifeState.State.DYING)).concat(lifeStateSqCreation(lives() - 1));
    }
    
    /**
     * @return
     */
    public int lives(){
        return lifeState().lives();
    }
    
    /**
     * @return
     */
    public boolean isAlive(){
        return lives() > 0;
    }
    
    /**
     * Getter for directedPos
     * @return
     */
    public Sq<DirectedPosition> directedPositions(){
        return directedPos;
    }
    
    /**
     * @return
     */
    public SubCell position(){
        return directedPos.head().position();
    }
    
    /**
     * @return
     */
    public Direction direction(){
        return directedPos.head().direction();
    }
    
    /**
     * @return
     */
    public int maxBombs(){
        return maxBombs;
    }
    
    /**
     * @param newMaxBombs
     * @return
     */
    public Player withMaxBombs(int newMaxBombs){
        return new Player(id, lifeStates, directedPos, newMaxBombs, bombRange);
    }
    
    
    /**
     * SUPPLEMENTARY METHOD
     *  TODO
     * @param lives
     * @return
     */
    private static Sq<LifeState> lifeStateSqCreation(int lives){
        if(lives <= 0){
            return Sq.constant(new LifeState(0, LifeState.State.DEAD));
        }else{
            return Sq.repeat(Ticks.PLAYER_INVULNERABLE_TICKS,new LifeState(lives, LifeState.State.INVULNERABLE)).concat(Sq.constant(new LifeState(lives, LifeState.State.VULNERABLE)));
        }
    }

}
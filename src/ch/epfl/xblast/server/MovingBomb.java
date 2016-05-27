package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.SubCell;
import ch.epfl.xblast.server.Player.DirectedPosition;

/**
 * Immutable class that represent a moving bomb (kicked by a player)
 * 
 * @author Loïc Vandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 *
 */
public final class MovingBomb {
    /*
     * Attributes
     */
    private final Bomb bomb;
    private final Sq<DirectedPosition> directedPosition;

    /**
     * Constructor of a moving bomb.
     * 
     * @param bomb
     * @param directedPosition
     */
    public MovingBomb(Bomb bomb, Sq<DirectedPosition> directedPosition) {
        this.bomb = Objects.requireNonNull(bomb);
        this.directedPosition = Objects.requireNonNull(directedPosition);
    }

    /**
     * Getter of the ownerID.
     * 
     * @return the ownerID
     */
    public PlayerID ownerId() {
        return bomb.ownerId();
    }

    /**
     * Getter for the bomb inside the moving bomb.
     * 
     * @return the bomb
     */
    public Bomb bomb() {
        return bomb;
    }

    /**
     * Getter for directed positions of a moving bomb.
     * 
     * @return the moving bomb directed positions
     */
    public Sq<DirectedPosition> getDirectedPosition() {
        return directedPosition;
    }

    /**
     * Getter of the bomb position.
     * 
     * @return position of the bomb
     */
    public SubCell subCell() {
        return directedPosition.head().position();
    }

    /**
     * Getter of the bomb position.
     * 
     * @return position of the bomb
     */
    public Cell cell() {
        return subCell().containingCell();
    }

    /**
     * Getter of fuseLength sequence. (present and future fuseLengths)
     * 
     * @return a sequence representing the fuseLengths
     */
    public Sq<Integer> fuseLengths() {
        return bomb.fuseLengths();
    }

    /**
     * Returns the length of the current fuse.
     * 
     * @return current length of fuse
     */
    public int fuseLength() {
        return fuseLengths().head();
    }

    /**
     * Getter of the bomb range.
     * 
     * @return the bomb range
     */
    public int range() {
        return bomb.range();
    }

    /**
     * Returns the explosion corresponding to the bomb. The duration of the
     * explosion is given through Ticks.EXPLOSION_TICKS.
     * 
     * @return List<Sq<Sq<Cell>>> 4 arms that represent the explosion of the
     *         current bomb over time
     */
    public List<Sq<Sq<Cell>>> explosion() {
        List<Sq<Sq<Cell>>> explosion = new ArrayList<>();

        // for every direction add an explosion arm to the explosion
        for (Direction dir : Direction.values()) {
            explosion.add(explosionArmTowards(dir));
        }
        return Collections.unmodifiableList(explosion);
    }

    /**
     * Public method that evolves a moving bomb.
     * 
     * @return evolved instance of moving bomb
     */
    public MovingBomb next() {
        Bomb newBomb = new Bomb(ownerId(), cell(), bomb.fuseLengths().tail(),
                range());
        return new MovingBomb(newBomb, directedPosition.tail());
    }

    /**
     * Helper Method: creates a single arm of the explosion towards given
     * direction.
     * 
     * @param dir
     *            direction to which the explosion arm is heading
     * @return the sequence representing only one direction of the explosion
     *         over the time
     */
    private Sq<Sq<Cell>> explosionArmTowards(Direction dir) {

        Sq<Cell> arm = Sq.iterate(cell(), c -> c.neighbor(dir)).limit(range());

        return Sq.repeat(Ticks.EXPLOSION_TICKS, arm);
    }
}

package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.ArgumentChecker;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.SubCell;

/**
 * A bomb that explodes after given amount of Ticks.
 * 
 * @author Loïc Vandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 *
 */
public final class Bomb {
    
    // Attributes
    private final PlayerID ownerId;
    private final Sq<SubCell> positions;
    private final Sq<Integer> fuseLengths;
    private final int range;

    /**
     * First Constructor USED FOR MOVING BOMB. BONUS TODO
     * 
     * @param ownerId
     *            PayerID of the bomb owner
     * @param position
     *            Cell in which the bomb will be located
     * @param fuseLengths
     *            Sequence of Integers to represent the fuse length
     * @param range
     *            Integer to represent bomb detonation range
     * @throws NullPointerException
     *             if one of the first three parameters is null
     * @throws IllegalArgumentException
     *             if range is negative or fuseLength sequence empty
     */
    public Bomb(PlayerID ownerId, Sq<SubCell> positions, Sq<Integer> fuseLengths, int range) {
        this.ownerId = Objects.requireNonNull(ownerId);
        this.positions = Objects.requireNonNull(positions);
        this.range = ArgumentChecker.requireNonNegative(range);
        this.fuseLengths = Objects.requireNonNull(fuseLengths);
        if (fuseLengths.isEmpty()) {
            throw new IllegalArgumentException(
                    "fuseLentghs sequence cannot be empty.");
        }
    }
    
    /**
     * OLD
     * SECOND Constructor BONUS TODO
     * 
     * @param ownerId
     *            PayerID of the bomb owner
     * @param position
     *            Cell in which the bomb will be located
     * @param fuseLengths
     *            Sequence of Integers to represent the fuse length
     * @param range
     *            Integer to represent bomb detonation range
     * @throws NullPointerException
     *             if one of the first three parameters is null
     * @throws IllegalArgumentException
     *             if range is negative or fuseLength sequence empty
     */
    public Bomb(PlayerID ownerId, Cell position, Sq<Integer> fuseLengths, int range) {
        this(ownerId, 
                Sq.iterate(Objects.requireNonNull(SubCell.centralSubCellOf(position)), p -> p),
                fuseLengths,
                range);
    }

    /**
     * OLD
     * Third Constructor BONUS TODO
     * 
     * @param ownerId
     *            PayerID of the bomb owner
     * @param position
     *            Cell in which the bomb is located
     * @param fuseLengths
     *            Integer value of the fuse length
     * @param range
     *            Integer to represent bomb detonation range
     * @throws NullPointerException
     *             if one of the first two parameters are null
     * @throws IllegalArgumentException
     *             if range is negative or fuseLength non-positive
     */
    public Bomb(PlayerID ownerId, Cell position, int fuseLength, int range) {
        this(ownerId, 
             Sq.iterate(Objects.requireNonNull(SubCell.centralSubCellOf(position)), p -> p),
             Sq.iterate(ArgumentChecker.requireNonNegative(fuseLength),i -> i - 1).limit(fuseLength),
             range);
    }
    
    

    /**
     * Getter of the ownerID.
     * 
     * @return the ownerID
     */
    public PlayerID ownerId() {
        return ownerId;
    }

    /**
     * Getter of the exact bomb position.
     * 
     * ATTENTION: this was done like this in order to assure backwards compatibility!
     * 
     * @return position of the bomb
     */
    public SubCell positionExact() {
        return positions().head();
    }

    /**
     * Getter of the bomb position.
     * 
     * @return position of the bomb
     */
    public Cell position() {
        return positions().head().containingCell();
    }

    /**
     * Getter of position sequence. (present and future positions)
     * 
     * @return a sequence representing the positions
     */
    public Sq<SubCell> positions() {
        return positions;
    }
    
    /**
     * Getter of fuseLength sequence. (present and future fuseLengths)
     * 
     * @return a sequence representing the fuseLengths
     */
    public Sq<Integer> fuseLengths() {
        return fuseLengths;
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
        return range;
    }

    /**
     * BONUS Determines if the bomb is moving or not.
     * 
     * @return true if bomb is moving, false otherwise.
     */
    public boolean isMoving(){
        Bomb nextBomb = new Bomb(ownerId(), positions.tail() ,fuseLengths(), range());
        return positionExact().x() != nextBomb.positionExact().x() || 
                positionExact().y() != nextBomb.positionExact().y();
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
     * Helper Method: creates a single arm of the explosion towards given
     * direction.
     * 
     * @param dir
     *            direction to which the explosion arm is heading
     * @return the sequence representing only one direction of the explosion
     *         over the time
     */
    private Sq<Sq<Cell>> explosionArmTowards(Direction dir) {

        Sq<Cell> arm = Sq.iterate(position(), c -> c.neighbor(dir)).limit(range());

        return Sq.repeat(Ticks.EXPLOSION_TICKS, arm);
    }
}
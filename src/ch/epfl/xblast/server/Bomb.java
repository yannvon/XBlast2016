package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.ArgumentChecker;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerID;

/**
 * A bomb that can explode.
 * 
 * @author Loïc Vandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 *
 */
public final class Bomb {
    // Attributes
    private final PlayerID ownerId;
    private final Cell position;
    private final Sq<Integer> fuseLengths;
    private final int range;

    /**
     * First Constructor, taking a sequence of Integer as fuse length.
     * 
     * @param ownerId
     *            PayerID of the bomb owner
     * @param position
     *            Cell in which the bomb is located
     * @param fuseLengths
     *            Sequence of Integers to represent the fuse length
     * @param range
     *            Integer to represent bomb detonation range
     * @throws NullPointerException
     *             if range is negative or fuseLength sequence empty
     * @throws IllegalArgumentException
     *             if one of the first three parameters are null
     */
    public Bomb(PlayerID ownerId, Cell position, Sq<Integer> fuseLengths, int range) {
        this.ownerId = Objects.requireNonNull(ownerId);
        this.position = Objects.requireNonNull(position);
        this.range = ArgumentChecker.requireNonNegative(range);
        this.fuseLengths = Objects.requireNonNull(fuseLengths);
        if (fuseLengths.isEmpty()) { 
            throw new IllegalArgumentException("fuseLentghs sequence is empty");
        }
    }

    /**
     * Second Constructor, taking an integer to represent initial fuseLength.
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
     *             if range is negative or fuseLength non-positive
     * @throws IllegalArgumentException
     *             if one of the first two parameters are null
     */
    public Bomb(PlayerID ownerId, Cell position, int fuseLengths, int range) {
        this(ownerId, position,
                Sq.iterate(ArgumentChecker.requireNonNegative(fuseLengths), i -> i - 1).limit(fuseLengths), range); // FIXME
                                                                              
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
     * Getter of the bomb position.
     * 
     * @return position of the bomb
     */
    public Cell position() {
        return position;
    }

    /**
     * Getter of fuseLength sequence. (present and future fuseLengths)
     * 
     * @return a sequence representing the fuseLength
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
        return fuseLengths.head();
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
     * Returns the explosion corresponding to the bomb. The duraction of the
     * explosion is given through Ticks.EXPLOSIO_TICKS.
     * 
     * @return List<Sq<Sq<Cell>>> 4 arms that represent the explosion of the
     *         current bomb over time
     */
    public List<Sq<Sq<Cell>>> explosion() {
        List<Sq<Sq<Cell>>> explosion = new ArrayList<>();

        for (Direction dir : Direction.values()) {
            explosion.add(explosionArmTowards(dir));
        }
        return explosion;

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

        Sq<Cell> part = Sq.iterate(position, c -> c.neighbor(dir)).limit(range);

        return Sq.repeat(Ticks.EXPLOSION_TICKS, part);
    }
}
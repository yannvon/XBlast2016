package ch.epfl.xblast.server;

import java.util.Objects;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.ArgumentChecker;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;

/**
 * @author Loïc Vandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 *
 */
public class Bomb {
    // Attributes
    private final PlayerID ownerId;
    private final Cell position;
    private final Sq<Integer> fuseLengths;
    private final int range;

    /**
     * constructor with a sequence of fuseLengths
     * @param ownerId
     * @param position
     * @param fuseLengths
     * @param range
     * @throws NullPointerException
     * @throws IllegalArgumentException
     */
    public Bomb(PlayerID ownerId, Cell position, Sq<Integer> fuseLengths,
            int range) {
        this.ownerId = Objects.requireNonNull(ownerId);
        this.position = Objects.requireNonNull(position);
        this.range = ArgumentChecker.requireNonNegative(range);
        if (fuseLengths.isEmpty()) {
            throw new IllegalArgumentException("fuseLentghs sequence is empty");
        }
        this.fuseLengths = Objects.requireNonNull(fuseLengths);
    }
    
    /**
     * constructor with a int of fuseLength
     * @param ownerId
     * @param position
     * @param fuseLengths
     * @param range
     * @throws NullPointerException
     * @throws IllegalArgumentException
     */
    public Bomb(PlayerID ownerId, Cell position, int fuseLengths,
            int range) {
        this(ownerId, position, Sq.iterate(fuseLengths, i-> i-1).limit(fuseLengths), range);
    }

    
    public PlayerID ownerId() {
        return ownerId;
    }

    public Cell position() {
        return position;
    }

    public Sq<Integer> fuseLengths() {
        return fuseLengths;//FIXME return the reference of the object 
    }
    public int fuseLength(){
        return fuseLengths.head();
    }

    public int range() {
        return range;
    }

    
    
}

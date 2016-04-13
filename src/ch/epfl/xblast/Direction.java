package ch.epfl.xblast;

/**
 * Enumeration of Directions.
 * 
 * @author Loïc Vandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 */
public enum Direction {
    N, E, S, W;

    /**
     * Returns the opposite Direction.
     * 
     * @return the opposite Direction
     * @throws Error
     *             if the argument is not one of the 4 directions
     */
    public Direction opposite() {
        switch (this) {
        case N:
            return S;
        case S:
            return N;
        case E:
            return W;
        case W:
            return E;
        default:
            throw new Error(); // will never happen
        }
    }

    /**
     * Determines whether the Direction is horizontal or not.
     * 
     * @return true if horizontal, false otherwise
     */
    public boolean isHorizontal() {
        return(this == E || this == W);
    }

    /**
     * Determines whether two Directions are parallel to each other
     * 
     * @param that
     *            the other Direction
     * @return true if that is parallel to this Direction, false otherwise
     */
    public boolean isParallelTo(Direction that) {
        return this == that || this == that.opposite();
    }
}

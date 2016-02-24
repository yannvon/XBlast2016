package ch.epfl.xblast;

/**
 * Enum of Directions.
 * 
 * @author Loic Vandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 */
public enum Direction {
    N, E, S, W;

    /**
     * Returns the opposite Direction.
     * @return the opposite Direction
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
            throw new Error();  //FIXME
        }
    }

    /**
     * Determines whether the Direction is horizontal or not.
     * @return true if horizontal, false otherwise
     */
    public boolean isHorizontal() {
        switch (this) {
        case E:
        case W:
            return true;
        case S:
        case N:
            return false;
        default:
            throw new Error();
        }
    }

    /**
     * Determines whether two Directions are parallel to each other
     * 
     * @param that
     *            the other Direction
     * @return true if that is parallel to Direction, false otherwise
     */
    public boolean isParallelTo(Direction that) {
        return this == that || this == that.opposite();
    }
}

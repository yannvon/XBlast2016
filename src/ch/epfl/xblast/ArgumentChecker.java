package ch.epfl.xblast;

/**
 * Non-instantiable class that contains methods to check an argument
 * 
 * @author Loic Vandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 *
 */
public final class ArgumentChecker {

    /**
     * private constructor
     */
    private ArgumentChecker() {

    }

    public static int requireNonNegative(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("negative value");
        }
        return value;
    }

}

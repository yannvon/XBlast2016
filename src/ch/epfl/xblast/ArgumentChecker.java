package ch.epfl.xblast;

/**
 * Non-instantiable class that contains methods to check validity of an
 * argument.
 * 
 * @author Loïc Vandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 *
 */
public final class ArgumentChecker {

    /**
     * Constructor: private since class should not be instantiable.
     */
    private ArgumentChecker() {}

    /**
     * Determines if given integer is non-negative
     * 
     * @param value
     *            integer that has to be checked
     * @return same value, if argument was non-negative
     * @throws IllegalArgumentException
     *             if the argument was negative
     */
    public static int requireNonNegative(int value) {
        if (value < 0) {
            throw new IllegalArgumentException(
                    "Checked argument has negative value : " + value);
        }
        return value;
    }
}

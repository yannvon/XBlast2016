package ch.epfl.xblast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.UnaryOperator;

/**
 * Non instantiable class that represents a run length encoder and offers two
 * static methods for encoding and decoding.
 * 
 * @author Loïc Vandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 *
 */
public final class RunLengthEncoder {
   

    /*
     * Constants
     */
    private static final int LONGEST_RUN = 130;
    private static final int MAX_NON_ENCODED_RUN = 2;

    /**
     *  Private Constructor: non instantiable class.
     */
    private RunLengthEncoder() {}

    /**
     * Takes a list of Bytes as argument and returns an run length encoded list.
     * 
     * The technique used is a slightly improved version of the usual run length
     * encoding: It consists of keeping the bytes how they are if they don't
     * occur more than twice in a row. Otherwise a negative number is added in
     * front of the compressed byte. The absolute value of the indicator byte
     * plus two corresponds to the amount of occurrences of said byte.
     * 
     * This technique requires that the given list of byte does not contain any
     * negative bytes.
     * 
     * @param l
     *            list of bytes to be encoded
     * @return list of compressed bytes (see explanation above)
     * @throws IllegalArgumentException
     *             if the received list contains a negative byte
     */
    public static List<Byte> encode(List<Byte> l) {
        List<Byte> output = new ArrayList<>();
        byte lastByte = 0;
        int count = 0;

        for (Byte b : l) {
            if (b < 0)
                throw new IllegalArgumentException(
                        "Cannot encode a negative byte!");
            // since we can only use negative byte values as indicator, we can
            // only encode 130 consecutive occurrences at once.
            if (b == lastByte && count < LONGEST_RUN)
                count++;
            else {
                output.addAll(encodedBytes(count, lastByte));
                lastByte = b;
                count = 1;
            }
        }
        output.addAll(encodedBytes(count, lastByte));

        return Collections.unmodifiableList(output);
    }

    /**
     * Decodes a list of bytes that was encoded using the run length encoding
     * described at the {@link #encode(List)} method.
     * 
     * @param l
     *            list of run length encoded bytes
     * @return decoded version of the list of bytes
     * @throws IllegalArgumentException
     *             if the last element of given list is negative
     */
    public static List<Byte> decode(List<Byte> l) {
        
        /*
         * Throw illegalArgumentException if last element is negative.
         */
        ArgumentChecker.requireNonNegative(l.get(l.size() - 1));

        /*
         * By saving a negative number as the occurrence count we later re-add
         * the correct amount of the next positive element to an output list.
         */
        List<Byte> output = new ArrayList<>();
        int ocount = 1;
        for (Byte b : l) {
            if (b >= 0) {
                output.addAll(Collections.nCopies(ocount, b));
                ocount = 1;
            } else {
                ocount = MAX_NON_ENCODED_RUN - b;
            }
        }
        return Collections.unmodifiableList(output);
    }

    /**
     * Additional private method that returns the correct encoding for a given
     * pair. The pair consists of a (positive) byte value that has to be encoded
     * and an integer that represents the number of consecutive occurrences of
     * said byte.
     * 
     * The returned list consists of maximum two bytes, the exacted technique
     * used is described at {@link #encode(List)} the method.
     * 
     * Careful: the count argument is not allowed to succeed LONGEST_RUN!
     * 
     * @param count
     *            number of consecutive occurrences of following byte
     * @param b
     *            byte that is encoded
     * @return list consisting of maximum two bytes representing a number of
     *         occurrences of given byte
     */
    private static List<Byte> encodedBytes(int count, byte b) {
        List<Byte> encoded = new ArrayList<>();

        if (count <= MAX_NON_ENCODED_RUN)
            encoded.addAll(Collections.nCopies(count, b));
        else {
            encoded.add((byte) (MAX_NON_ENCODED_RUN - count));
            encoded.add(b);
        }
        return Collections.unmodifiableList(encoded);
    }
}

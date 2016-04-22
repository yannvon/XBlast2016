package ch.epfl.xblast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Non instanciable class that represents a run length encoder and offers two
 * static methods for encoding and decoding.
 * 
 * @author Loïc Vandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 *
 */
public final class RunLengthEncoder {

    private RunLengthEncoder() {}

    /**
     * TODO
     * @param l
     * @return
     */
    public static List<Byte> encode(List<Byte> l){
        List<Byte> output = new ArrayList<>();
        
        byte lastByte = 0;
        byte count = 0;
        
        for(Byte b : l){
            if(b < 0)
                throw new IllegalArgumentException("Cannot encode negative byte!");
            
            if(b == lastByte){
                count++;
            }
            else {
                if (count <= 2)
                    output.addAll(Collections.nCopies(count, lastByte));
                else {
                    output.add((byte) -(count - 2));
                    output.add(lastByte);
                }
                lastByte = b;
                count = 1;
            }
        }
        return output;
    }
    
    /**
     * TODO
     * @param l
     * @return
     */
    public static List<Byte> decode(List<Byte> l){
        List<Byte> output = new ArrayList<>();
        
        int n = 1;
        for(Byte b : l){
            if(b >= 0){
                output.addAll(Collections.nCopies(n, b));
                n = 1;
            }
            else{
                n = -b + 2;
            }
        }
        return output;
    }
}

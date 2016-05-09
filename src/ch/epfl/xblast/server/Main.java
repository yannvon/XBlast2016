package ch.epfl.xblast.server;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * Main class of the server. Manage a game and TODO with the client
 * @author Loïc Vandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 *
 */
public class Main {

    /*
     * Constants
     */
    private static final int DEFAULT_NUMBER_OF_CLIENT = 4;    
    private static final SocketAddress PORT_ADDRESS= new InetSocketAddress(2016);
    
    /**
     * Main method of the server
     * @param args
     *          Determine the number of client to connect before the game begin
     */
    public static void main(String[] args) {
        /*
         * Phase 1
         */
        int numberOfClient = (args.length!=0)?Integer.parseInt(args[0]):DEFAULT_NUMBER_OF_CLIENT ;
        

    }

}

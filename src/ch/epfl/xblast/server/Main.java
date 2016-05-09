package ch.epfl.xblast.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardProtocolFamily;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.HashMap;
import java.util.Map;

import ch.epfl.xblast.PlayerAction;
import ch.epfl.xblast.PlayerID;

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
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException { //FIXME exeption?
        /*
         * Phase 1
         */
        int numberOfClient = (args.length!=0)?Integer.parseInt(args[0]):DEFAULT_NUMBER_OF_CLIENT ;
        if(numberOfClient>4)
            numberOfClient=4; //FIXME
        
        DatagramChannel channel = DatagramChannel.open(StandardProtocolFamily.INET);
        channel.bind(PORT_ADDRESS);
        channel.configureBlocking(true);
        Map<PlayerID,SocketAddress> address = new HashMap<>();
        ByteBuffer buffer = ByteBuffer.allocate(1);
        
        while(address.size()!=numberOfClient){
            SocketAddress senderAddress = channel.receive(buffer);
            if(buffer.get(0)==PlayerAction.JOIN_GAME.ordinal()){
                address.put(PlayerID.values()[address.size()],senderAddress);//FIXME
                
            }
        }
        
        

    }

}

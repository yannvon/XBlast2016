package ch.epfl.xblast.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardProtocolFamily;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.sun.org.apache.bcel.internal.generic.NEWARRAY;

import ch.epfl.xblast.Direction;
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
    public static void main(String[] args) throws IOException { //FIXME exception?
        /*
         * Phase 1
         */
        int numberOfClient = (args.length!=0)?Integer.parseInt(args[0]):DEFAULT_NUMBER_OF_CLIENT ;
        if(numberOfClient > 4)
            numberOfClient=4; //FIXME constant
        
        DatagramChannel channel = DatagramChannel.open(StandardProtocolFamily.INET);
        channel.bind(PORT_ADDRESS);
        channel.configureBlocking(true);
        Map<SocketAddress,PlayerID> address = new HashMap<>();//TODO player->address or adress->player
        ByteBuffer buffer = ByteBuffer.allocate(1);
        
        /*
         * add the client who want to join the game
         */
        while(address.size()!=numberOfClient){
            SocketAddress senderAddress = channel.receive(buffer);
            if(buffer.get(0)==PlayerAction.JOIN_GAME.ordinal()){
                address.put(senderAddress,PlayerID.values()[address.size()]);
                buffer.reset();
            }
        }
        
        
        
        /*
         * Phase 2
         */
        GameState game = Level.DEFAULT_LEVEL.initialGameState();      
        channel.configureBlocking(false);
        
        while(!game.isGameOver()){
            Map<PlayerID,PlayerAction> actions= new HashMap<>();
            while(channel.receive(buffer)!=null){
                //TODO?????
            }
            
            Map<PlayerID,Optional<Direction>> mouvements= new HashMap<>();
            Set<PlayerID> bombDrpEvent = new HashSet<>();
            
            actions.forEach((p,a)->{
                if(a==PlayerAction.DROP_BOMB)
                    bombDrpEvent.add(p);
                
                switch(a){
                case MOVE_N:
                case MOVE_S:
                case MOVE_E:
                case MOVE_W:
                    mouvements.put(p,
                            Optional.of(Direction.values()[a.ordinal() - 1]));
                    // FIXME add attribute (or function) to PlayerAction
                break;
                case STOP:
                    mouvements.put(p,Optional.empty());
                }
            });//FIXME use stream?
            
            
            
            game.next(mouvements,bombDrpEvent);
            
            
        }
        Optional<PlayerID> winner = game.winner();
        System.out.println(winner.isPresent()? winner : "no winner");   //FIXME
    }

}

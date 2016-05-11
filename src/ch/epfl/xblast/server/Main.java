package ch.epfl.xblast.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardProtocolFamily;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

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
    private static final Level LEVEL = Level.DEFAULT_LEVEL;//FIXME or level?    
    private static final int DEFAULT_NUMBER_OF_CLIENT = 4;    
    private static final SocketAddress PORT_ADDRESS= new InetSocketAddress(2016);
    
    
    /**
     * Main method of the server
     * @param args
     *          Determine the number of client to connect before the game begins.
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException { //FIXME exception?
        
        /*
         * Phase 1
         */
        int numberOfClients = (args.length != 0) ? Integer.parseInt(args[0])
                : DEFAULT_NUMBER_OF_CLIENT;
        if (numberOfClients > 4)
            numberOfClients = 4; // FIXME constant OR throw error?
 
        DatagramChannel channel = DatagramChannel.open(StandardProtocolFamily.INET);
        channel.bind(PORT_ADDRESS);
        channel.configureBlocking(true);
        Map<SocketAddress, PlayerID> clientAdresses = new HashMap<>();
        ByteBuffer oneByteBuffer = ByteBuffer.allocate(1);

        /*
         * add the client who want to join the game
         */
        while (clientAdresses.size() != numberOfClients) {
            SocketAddress senderAddress = channel.receive(oneByteBuffer);
            if (oneByteBuffer.get(0) == PlayerAction.JOIN_GAME.ordinal()) {    //get()
                clientAdresses.put(senderAddress,
                        PlayerID.values()[clientAdresses.size()]);
            }
            oneByteBuffer.reset();
        }
        
        
        
        /*
         * Phase 2
         */
        GameState game = LEVEL.initialGameState();
        channel.configureBlocking(false);       //TODO comment
        
        while(!game.isGameOver()){
            // 1) send current GameState to clients
            // 1.1) serialize GameState
            List<Byte> serialized = GameStateSerializer.serialize(LEVEL.boardPainter(), game);
            ByteBuffer gameStateBuffer = ByteBuffer.allocate(serialized.size());
            gameStateBuffer.put((byte) 0);  //TODO explain 
            serialized.forEach(gameStateBuffer::put);

            for(Entry<SocketAddress, PlayerID> e : clientAdresses.entrySet()){
                gameStateBuffer.put(0, (byte) e.getValue().ordinal());  //FIXME move tampon?
                gameStateBuffer.flip(); //FIXME if possible put out of loop
                channel.send(gameStateBuffer, e.getKey());
            }

            
            Map<PlayerID,PlayerAction> actions= new HashMap<>();
            while(channel.receive(oneByteBuffer)!=null){
                //TODO?????
            }
            
            // 2) get client input
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
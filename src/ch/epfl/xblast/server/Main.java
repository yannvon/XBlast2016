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
import java.util.function.UnaryOperator;

import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerAction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.Time;

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
    private static final Level LEVEL = Level.DEFAULT_LEVEL;  
    private static final int DEFAULT_NUMBER_OF_CLIENT = 4;    
    private static final SocketAddress PORT_ADDRESS= new InetSocketAddress(2016);
    private static final UnaryOperator<Integer> MOVE_ACTION_TO_DIRECTION_ORDINAL = x-> x-1; 
    
    /**
     * Main method of the server
     * @param args
     *          Determine the number of client to connect before the game begins.
     * @throws IOException 
     * @throws InterruptedException 
     */
    public static void main(String[] args) throws IOException, InterruptedException { //FIXME exception?
        
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
            oneByteBuffer.clear();
        }
                
        /*
         * Phase 2
         */
        GameState game = LEVEL.initialGameState();
        channel.configureBlocking(false);       //TODO comment
        long startingTime = System.nanoTime();
        
        while(!game.isGameOver()){
            // 1) send current GameState to clients
            // 1.1) serialize GameState
            List<Byte> serialized = GameStateSerializer.serialize(LEVEL.boardPainter(), game);
            ByteBuffer gameStateBuffer = ByteBuffer.allocate(serialized.size() + 1);
            gameStateBuffer.put((byte) 0);  //TODO explain 
            serialized.forEach(gameStateBuffer::put);
            gameStateBuffer.flip();

            //1.2) send gameState to each client
            for(Entry<SocketAddress, PlayerID> e : clientAdresses.entrySet()){
                gameStateBuffer.put(0, (byte) e.getValue().ordinal());
                channel.send(gameStateBuffer, e.getKey());
            }

            
            //2)Wait a tick duration            
            long timeForNextTick = (long) startingTime
                    + ((long) game.ticks() + 1) * ((long) Ticks.TICK_NANOSECOND_DURATION);
            long waitingTime = timeForNextTick - System.nanoTime();
            if (waitingTime > 0)
                Thread.sleep(waitingTime/ Time.US_PER_S);            
            

            // 3) get client input
            Map<PlayerID, Optional<Direction>> speedChangeEvents = new HashMap<>();
            Set<PlayerID> bombDrpEvent = new HashSet<>();
            SocketAddress senderAddress;
            
            while((senderAddress = channel.receive(oneByteBuffer)) != null){
                oneByteBuffer.flip();
                PlayerID id = clientAdresses.get(senderAddress);
                PlayerAction action= PlayerAction.values()[oneByteBuffer.get()];
                if(id != null){
                    switch(action){
                    case DROP_BOMB:
                        bombDrpEvent.add(id);
                        break;
                    case MOVE_S:
                    case MOVE_N:
                    case MOVE_W:
                    case MOVE_E:
                        int dirOrdinal = MOVE_ACTION_TO_DIRECTION_ORDINAL
                                .apply(action.ordinal());
                        speedChangeEvents.put(id,
                                Optional.of(Direction.values()[dirOrdinal]));
                        break;
                    case STOP:
                        speedChangeEvents.put(id,Optional.empty());
                    case JOIN_GAME:
                        break;  //FIXME throw error?
                    }
                }
                oneByteBuffer.clear();
            }

            //4) evolve GameState
            game = game.next(speedChangeEvents,bombDrpEvent);       //lol!
        }
        
        Optional<PlayerID> winner = game.winner();
        System.out.println(winner.isPresent()? winner : "no winner");   //FIXME
        channel.close();
    }

}

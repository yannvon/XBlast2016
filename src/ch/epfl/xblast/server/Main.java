package ch.epfl.xblast.server;

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

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerAction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.Time;

/**
 * Main class of the server. Manages a game and communicates with the clients.
 * 
 * @author Loïc Vandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 *
 */
public class Main {

    /*
     * Constants
     */
    private static final Level LEVEL = Level.DEFAULT_LEVEL;
    private static final int NUMBER_OF_PLAYERS = PlayerID.values().length;
    private static final int DEFAULT_NUMBER_OF_CLIENTS = NUMBER_OF_PLAYERS;
    private static final int MAX_SENDING_BYTES = 2 * (Cell.COUNT + 1)
            + 4 * NUMBER_OF_PLAYERS + 1 + 1;
    private static final SocketAddress PORT_ADDRESS = new InetSocketAddress(2016);
    private static final UnaryOperator<Integer> MOUVEMENT_TO_DIR_ORDINAL = x -> x - 1;

    /**
     * Main method of the XBlast 2016 Server.
     * 
     * @param args
     *            Amount of clients that should connect before the game
     *            launches. If this argument is omitted the server waits for the
     *            default amount of players.
     * @throws Exception 
     */
    public static void main(String[] args) {

        /*
         * PHASE 1
         * 1.1) Determine how many player will be playing.
         */
        int numberOfClients = (args.length != 0) ? Integer.parseInt(args[0])
                : DEFAULT_NUMBER_OF_CLIENTS;
        if (numberOfClients > NUMBER_OF_PLAYERS)
            throw new IllegalArgumentException("There cannot be more than "
                    + NUMBER_OF_PLAYERS + " players.");

        /*
         * 1.2) Open channel in charge of communication with the clients.
         */
        try (DatagramChannel channel = DatagramChannel
                .open(StandardProtocolFamily.INET)) {
            
            //Bind the port to the used channel
            channel.bind(PORT_ADDRESS);
            
            //Enable blocking mode, since the server waits for the clients.
            channel.configureBlocking(true);

            /*
             * 1.3) Look for clients that want to join the game and save them in
             *      an "address book".
             */
            Map<SocketAddress, PlayerID> clientAdresses = new HashMap<>();
            ByteBuffer oneByteBuffer = ByteBuffer.allocate(1);

            while (clientAdresses.size() != numberOfClients) {
                SocketAddress senderAddress = channel.receive(oneByteBuffer);
                oneByteBuffer.flip();
                if (!clientAdresses.containsKey(senderAddress) && oneByteBuffer
                        .get() == PlayerAction.JOIN_GAME.ordinal()) {
                    clientAdresses.put(senderAddress,
                            PlayerID.values()[clientAdresses.size()]);
                }
                oneByteBuffer.clear();
            }

            /*
             * Phase 2:
             * 2.1) Start the game and save the starting time for later time management.
             */
            GameState gameState = LEVEL.initialGameState();
            long startingTime = System.nanoTime();

            // Disable blocking mode since in Phase 2 the clients are not
            // required to send anything.
            channel.configureBlocking(false);

            // Prepare Buffer in order to send the GameState. The maximal
            // transmission size equals the max GameState size + 1 byte for the playerID.
            ByteBuffer gameStateBuffer = ByteBuffer
                    .allocate(MAX_SENDING_BYTES);

            while (!gameState.isGameOver()) {
                
                /*
                 *  2.2) Serialize the current GameState and prepare buffer.
                 */
                List<Byte> serialized = GameStateSerializer
                        .serialize(LEVEL.boardPainter(), gameState);
                gameStateBuffer.put((byte) 0);    // Placeholder where PlayerID belongs
                serialized.forEach(gameStateBuffer::put);
                gameStateBuffer.flip();

                /*
                 * 2.2) Send the GameState to each client, the first byte
                 *      represents the playerID, for which the GameState is meant.
                 */
                for (Entry<SocketAddress, PlayerID> e : clientAdresses
                        .entrySet()) {
                    gameStateBuffer.put(0, (byte) e.getValue().ordinal());
                    channel.send(gameStateBuffer, e.getKey());
                    gameStateBuffer.rewind();
                }
                gameStateBuffer.clear();

                /*
                 * 2.3) Wait the correct amount of time, so that the tick duration is
                 *      correct.
                 */
                long timeForNextTick = startingTime
                        + ((long) gameState.ticks() + 1)
                                * ((long) Ticks.TICK_NANOSECOND_DURATION);
                long waitingTime = timeForNextTick - System.nanoTime();
                if (waitingTime > 0)
                    Thread.sleep(waitingTime / Time.US_PER_S);

                /*
                 * 2.4) Check if the clients sent an action they want to
                 *      execute. This is done by receiving the messages until
                 *      there are none left.
                 */
                Map<PlayerID, Optional<Direction>> speedChangeEvents = new HashMap<>();
                Set<PlayerID> bombDrpEvent = new HashSet<>();
                SocketAddress senderAddress;

                while ((senderAddress = channel
                        .receive(oneByteBuffer)) != null) {
                    oneByteBuffer.flip();
                    PlayerID id = clientAdresses.get(senderAddress);
                    PlayerAction action = PlayerAction.values()[oneByteBuffer
                            .get()];
                    if (id != null) {
                        switch (action) {
                        case DROP_BOMB:
                            bombDrpEvent.add(id);
                            break;
                        case MOVE_S:
                        case MOVE_N:
                        case MOVE_W:
                        case MOVE_E:
                            int dirOrdinal = MOUVEMENT_TO_DIR_ORDINAL
                                    .apply(action.ordinal());
                            speedChangeEvents.put(id, Optional
                                    .of(Direction.values()[dirOrdinal]));
                            break;
                        case STOP:
                            speedChangeEvents.put(id, Optional.empty());
                        default:
                            break;
                        }
                    }
                    oneByteBuffer.clear();
                }

                /*
                 * 2.4) Evolve GameState to the next Tick.
                 */
                gameState = gameState.next(speedChangeEvents, bombDrpEvent);
            }

            Optional<PlayerID> winner = gameState.winner();
            System.out.println(winner.isPresent() ? winner.get() : "There was no winner.");
            
        }catch(Exception e){
            System.err.println();
            e.printStackTrace();
        }
    }
}

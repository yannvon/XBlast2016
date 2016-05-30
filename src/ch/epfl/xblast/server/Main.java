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
     * 
     * The max amount of sending bytes consists of the worst case encoding plus
     * one first byte that represents the playerID of the recipient.
     */
    private static final Level LEVEL = Level.DEFAULT_LEVEL;
    private static final int NUMBER_OF_PLAYERS = PlayerID.values().length;
    private static final int DEFAULT_NUMBER_OF_CLIENTS = NUMBER_OF_PLAYERS;
    private static final int MAX_SENDING_BYTES = 2 * (Cell.COUNT + 1)
            + PlayerID.values().length * 3 * 9 + 4 * PlayerID.values().length
            + 2;
    private static final SocketAddress PORT_ADDRESS = new InetSocketAddress(2016);

    /**
     * Main method of the XBlast 2016 Server. Is separated in two phases. First
     * the server waits for the specified amount of players to join the game,
     * then the server is in charge of evolving the game and sending the
     * information to each client.
     * 
     * Note: There will always be 4 players on the map, but if a number smaller
     * than 4 was specified, some players won't move.
     * 
     * @param args
     *            optional parameter: amount of clients that should connect
     *            before the game launches. If this argument is omitted the
     *            server waits for the default amount of players.
     * @throws IOException
     *             if the channel cannot be opened
     * @throws InterruptedException
     *             if any thread has interrupted this thread
     * @throws IllegalArgumentException
     *             if the given amount of players doesn't lay in the correct
     *             range
     * @throws NumberFormatException
     *             if the given argument was not an integer 
     */
    public static void main(String[] args)
            throws IOException, InterruptedException {

        /*
         * PHASE 1 
         * 
         * 1.1) Determine how many players will be playing.
         * 
         * If they were multiple arguments we read the first one only.
         * If an incorrect argument is given, the client halts.
         */
        int numberOfClients = (args.length != 0) ? Integer.parseInt(args[0])
                : DEFAULT_NUMBER_OF_CLIENTS;
        if (numberOfClients < 0 || numberOfClients > NUMBER_OF_PLAYERS)
            throw new IllegalArgumentException("There cannot be more than "
                    + NUMBER_OF_PLAYERS + " players nor a negative amount of players");

        /*
         * 1.2) Open channel in charge of the communication with the clients.
         */
        try (DatagramChannel channel = DatagramChannel
                .open(StandardProtocolFamily.INET)) {

            // Bind the port to the used channel
            channel.bind(PORT_ADDRESS);

            // Blocking mode is already enabled by default
            // -> the server has to wait for the clients.

            /*
             * 1.3) Look for clients that want to join the game and save them in
             * an address book.
             */
            Map<SocketAddress, PlayerID> clientAdresses = new HashMap<>();
            ByteBuffer oneByteBuffer = ByteBuffer.allocate(1);

            while (clientAdresses.size() != numberOfClients) {
                SocketAddress senderAddress = channel.receive(oneByteBuffer);
                oneByteBuffer.flip();

                // To prevent an error due to incorrectly sent buffer, we first check
                // if the buffer has a remaining element.
                if (!clientAdresses.containsKey(senderAddress)
                        && oneByteBuffer.hasRemaining() 
                        && oneByteBuffer.get() == PlayerAction.JOIN_GAME.ordinal()) {
                    
                    clientAdresses.put(senderAddress,
                            PlayerID.values()[clientAdresses.size()]);
                }
                
                // Clear oneByteBuffer for next client
                oneByteBuffer.clear();
            }

            /*
             * Phase 2
             * 
             * 2.1) Start the game using the default initial GameState and save
             * the starting time for later time management.
             */
            GameState gameState = (args.length < 2)
                    ? LEVEL.initialGameState()
                    : Level.chargeGameState(args[1]);
            BoardPainter boardPainter = LEVEL.boardPainter();
            long startingTime = System.nanoTime();

            // Disable blocking mode since in Phase 2 the clients are not
            // required to send anything.
            channel.configureBlocking(false);

            // Declare buffer used to send the GameState to the clients.
            ByteBuffer gameStateBuffer = ByteBuffer.allocate(MAX_SENDING_BYTES);

            while (!gameState.isGameOver()) {

                /*
                 * 2.2) Serialize the current GameState and prepare buffer.
                 */
                List<Byte> serialized = GameStateSerializer
                        .serialize(boardPainter, gameState);
                // Put any byte as Placeholder where the PlayerID belongs
                gameStateBuffer.put((byte) 0);
                serialized.forEach(gameStateBuffer::put);
                gameStateBuffer.flip();

                /*
                 * 2.3) Send the GameState to each client, the first byte
                 * represents the playerID, for which the GameState is meant.
                 */
                for (Entry<SocketAddress, PlayerID> e : clientAdresses
                        .entrySet()) {
                    gameStateBuffer.put(0, (byte) e.getValue().ordinal());
                    channel.send(gameStateBuffer, e.getKey());
                    gameStateBuffer.rewind();
                }
                gameStateBuffer.clear();

                /*
                 * 2.4) Wait the correct amount of time, so that the actual tick
                 * duration stays the same. We add one to the amount of ticks
                 * already played, in order to have a break between the first
                 * and second GameState.
                 */
                long timeForNextTick = startingTime
                        + ((long) gameState.ticks() + 1)
                                * Ticks.TICK_NANOSECOND_DURATION;
                long waitingTime = timeForNextTick - System.nanoTime();
                if (waitingTime > 0)
                    Thread.sleep(waitingTime / Time.NS_PER_MS,
                            (int) waitingTime % Time.NS_PER_MS);

                /*
                 * 2.5) Check if the clients sent an action they want to
                 * execute. This is done by receiving the messages until there
                 * are none left.
                 */
                Map<PlayerID, Optional<Direction>> speedChangeEvents = new HashMap<>();
                Set<PlayerID> bombDrpEvents = new HashSet<>();
                SocketAddress senderAddress;

                while ((senderAddress = channel
                        .receive(oneByteBuffer)) != null) {
                    oneByteBuffer.flip();
                    PlayerID id = clientAdresses.get(senderAddress);

                    /*
                     *  Check that a participating player correctly sent a byte.
                     */
                    if (id != null && oneByteBuffer.hasRemaining()) {
                        byte receivedValue = oneByteBuffer.get();
                        PlayerAction action = null;
                        
                        /*
                         * Check that the sent value corresponds to an action.
                         */
                        if (0 <= receivedValue
                                && receivedValue < PlayerAction.values().length)
                            action = PlayerAction.values()[receivedValue];

                        switch (action) {
                        case DROP_BOMB:
                            bombDrpEvents.add(id);
                            break;
                        case MOVE_S:
                            speedChangeEvents.put(id, Optional.of(Direction.S));
                            break;
                        case MOVE_N:
                            speedChangeEvents.put(id, Optional.of(Direction.N));
                            break;
                        case MOVE_W:
                            speedChangeEvents.put(id, Optional.of(Direction.W));
                            break;
                        case MOVE_E:
                            speedChangeEvents.put(id, Optional.of(Direction.E));
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
                 * 2.6) Evolve GameState to the next Tick.
                 */
                gameState = gameState.next(speedChangeEvents, bombDrpEvents);
            }
            
            /*
             * 3) Send last GameState (this time it is mandatory due to winner
             * message displaying)
             */
            List<Byte> serialized = GameStateSerializer
                    .serialize(boardPainter, gameState);
            
            // Put any byte as Placeholder where the PlayerID belongs
            gameStateBuffer.put((byte) 0);
            serialized.forEach(gameStateBuffer::put);
            gameStateBuffer.flip();

            for (Entry<SocketAddress, PlayerID> e : clientAdresses
                    .entrySet()) {
                gameStateBuffer.put(0, (byte) e.getValue().ordinal());
                channel.send(gameStateBuffer, e.getKey());
                gameStateBuffer.rewind();
            }
            gameStateBuffer.clear();


            /*
             * 4) Print winner in console if there was one.
             */
            Optional<PlayerID> winner = gameState.winner();
            if(winner.isPresent()){
                System.out.println("The winner is: " + winner.get() + "!");
            } else {
                System.out.println("There is no winner.");
            }
        }
    }
}

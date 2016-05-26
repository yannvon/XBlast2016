package ch.epfl.xblast.client;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardProtocolFamily;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerAction;
import ch.epfl.xblast.PlayerID;

/**
 * Main class of the Client. In charge of communicating with the server and
 * displaying the current GameState.
 * 
 * @author Loïc Vandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 *
 */
public class Main {

    /*
     * Constants.
     * 
     * The max amount of receiving bytes consists of the worst case encoding
     * plus one first byte that represents the playerID of the recipient.
     */
    private static final int PORT = 2016;
    private static final int MAX_RECEIVING_BYTES = 2 * (Cell.COUNT + 1)
            + 4 * PlayerID.values().length + 2;
    private static final int JOIN_REQUEST_REPEATING_TIME = 1000;
    private static final String DEFAULT_HOST = "localhost";

    /*
     * Static attributes
     */
    private static XBlastComponent xbc;
    private static SocketAddress serverAddress;

    /**
     * Main method of the XBlast 2016 Client. In Phase 1 the client repeats the
     * sending of a game join request. As soon as the first GameState was
     * received Phase 2 starts: The client opens a parallel Swing thread in
     * charge of displaying the GameState and managing the keyboard inputs. The
     * main thread will keep receiving the GameStates.
     * 
     * @param args
     *            optional parameter: IP-address of the Server. If none is given
     *            localhost is used as default address.
     * @throws IOException
     *             if the channel fails to open
     * @throws InterruptedException
     *             if this thread gets interrupted
     * @throws InvocationTargetException
     *             if the invocation of the parallel thread fails   //FIXME all exceptions or not?
     */
    public static void main(String[] args) throws IOException,
            InterruptedException, InvocationTargetException {

        /*
         * PHASE 1
         * 
         * 1.1) retrieve IP-address and open channel. If there was more than one
         * argument we take the first, if there was none we assign a default
         * host.
         */
        String hostName = (args.length != 0) ? args[0] : DEFAULT_HOST;
        serverAddress = new InetSocketAddress(hostName, PORT);

        try (DatagramChannel channel = DatagramChannel
                .open(StandardProtocolFamily.INET)) {

            // Switch to non-blocking mode
            channel.configureBlocking(false);
            
            /*
             * 1.2) Send request to join game to the Server as long as the
             * server doesn't send the initial GameState.
             */
            ByteBuffer sendBuffer = ByteBuffer.allocate(1);
            ByteBuffer receiveBuffer = ByteBuffer
                    .allocate(MAX_RECEIVING_BYTES);
            
            sendBuffer.put((byte) PlayerAction.JOIN_GAME.ordinal());
            sendBuffer.flip();
            
            do {
                channel.send(sendBuffer, serverAddress);
                sendBuffer.rewind();
                Thread.sleep(JOIN_REQUEST_REPEATING_TIME);
            } while (channel.receive(receiveBuffer) == null);

            /*
             * PHASE 2 
             * 
             * 2.1) Start by invoking the parallel EDT Swing thread.
             */
            SwingUtilities.invokeAndWait(() -> createUI(channel));

            // From now one the client will wait until he gets the next
            // GameState from the server.
            channel.configureBlocking(true);

            /*
             * 2.2) As long as the program runs the client waits for a new
             * GameState and shares it with the parallel Swing thread.
             */
            while (true) {
                receiveBuffer.flip();   //FIXME hasRemaining?
                PlayerID id = PlayerID.values()[receiveBuffer.get()];
                List<Byte> serialized = new ArrayList<>();
                while (receiveBuffer.hasRemaining()) {
                    serialized.add(receiveBuffer.get());
                }
                GameState gameState = GameStateDeserializer
                        .deserializeGameState(serialized);
                SwingUtilities.invokeLater(() -> xbc.setGameState(gameState, id));
                receiveBuffer.clear();
                channel.receive(receiveBuffer);
            }
        }
    }

    /**
     * Method invoked by the main method in a other thread in charge of managing
     * the Swing graphical interface.
     */
    public static void createUI(DatagramChannel channel) {

        /*
         * Create new Window.
         */
        JFrame f = new JFrame("XBlast 2016");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        xbc = new XBlastComponent();
        f.getContentPane().add(xbc);
        
        f.setResizable(false);
        f.pack();
        f.setVisible(true);

        /*
         * Manage the Keyboard input by adding a keyListener to the
         * XBlastComponent that sends a message to the server when a relevant
         * key was pressed.
         */
        Consumer<PlayerAction> c = (playerAction) -> {
            ByteBuffer oneByteBuffer = ByteBuffer.allocate(1);
            oneByteBuffer.put((byte) playerAction.ordinal());
            oneByteBuffer.flip();
            try {
                channel.send(oneByteBuffer, serverAddress);
            } catch (IOException e) {
                /*
                 * Since we cannot treat the exception, nor add the throws tag,
                 * we throw an unchecked expression.
                 */
                throw new UncheckedIOException(e);
            }
        };
        xbc.addKeyListener(new KeyboardEventHandler(
                KeyboardEventHandler.DEFAULT_CONTROL_MAP, c));
        xbc.requestFocusInWindow();
    }
}

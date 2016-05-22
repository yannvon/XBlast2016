package ch.epfl.xblast.client;

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
     * The max amount of receiving bytes consists of the worst case encoding +
     * one first byte that represents the playerID of the recipient.
     */
    private static final int PORT = 2016;
    private static final int MAX_RECEIVING_BYTES = 2 * (Cell.COUNT + 1)
            + 4 * PlayerID.values().length + 2; // FIXME declare this in Deserializer? /Same on server side
    private static final int GAME_JOIN_REQUEST_REPEATING_TIME = 1000;
    private static final String DEFAULT_HOST = "localhost";

    /*
     * Static attributes
     */
    private static XBlastComponent xbc;
    private static SocketAddress serverAddress;

    /**
     * Main method of the XBlast 2016 Client.
     * 
     * @param args
     *            IP-address of the Server. If none localhost is used as default
     *            address.
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        /*
         * PHASE 1 
         * 1.1) retrieve IP-address and open channel
         */
        String hostName = (args.length == 0) ? DEFAULT_HOST : args[0]; // FIXME throw error?
        serverAddress = new InetSocketAddress(hostName, PORT);

        try (DatagramChannel channel = DatagramChannel
                .open(StandardProtocolFamily.INET)) {

            /*
             * 1.2) Send request to join game to Server as long as the server
             * doesn't send the initial GameState.
             */
            channel.configureBlocking(false);

            ByteBuffer sendByteBuffer = ByteBuffer.allocate(1);
            ByteBuffer receiveByteBuffer = ByteBuffer
                    .allocate(MAX_RECEIVING_BYTES);
            sendByteBuffer.put((byte) PlayerAction.JOIN_GAME.ordinal());
            sendByteBuffer.flip();
            do {
                channel.send(sendByteBuffer, serverAddress);
                sendByteBuffer.rewind();
                Thread.sleep(GAME_JOIN_REQUEST_REPEATING_TIME);
            } while ((channel.receive(receiveByteBuffer)) == null);

            /*
             * PHASE 2 
             * 2.1) Start by invoking the parallel EDT Swing thread.
             */
            SwingUtilities.invokeAndWait(() -> createUI(channel));

            // From now one the client will wait to get the next GameState from
            // the server.
            channel.configureBlocking(true);

            /*
             * 2.2) As long as the program runs the client waits for a new
             * GameState and shares it with the parallel Swing thread.
             */
            do {
                receiveByteBuffer.flip();
                PlayerID id = PlayerID.values()[receiveByteBuffer.get()]; // FIXME do this every time? attribute/throw exception?
                List<Byte> serialized = new ArrayList<>();
                while (receiveByteBuffer.hasRemaining()) {
                    serialized.add(receiveByteBuffer.get());
                }
                GameState gameState = GameStateDeserializer
                        .deserializeGameState(serialized);
                xbc.setGameState(gameState, id);
                receiveByteBuffer.clear();
                channel.receive(receiveByteBuffer);
            } while (true); //FIXME correct?
        }
    }

    /**
     * Method invoked by the main in a other thread in charge of managing the
     * Swing graphical interface.
     */
    public static void createUI(DatagramChannel channel) {

        /*
         * Open new Window.
         */
        JFrame f = new JFrame("XBlast 2016");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        xbc = new XBlastComponent();

        f.getContentPane().add(xbc);
        f.setResizable(false);
        f.pack();
        f.setVisible(true);
        xbc.requestFocusInWindow();

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
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        xbc.addKeyListener(new KeyboardEventHandler(
                KeyboardEventHandler.DEFAULT_CONTROL_MAP, c));
    }
}

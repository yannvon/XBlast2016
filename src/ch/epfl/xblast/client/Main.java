package ch.epfl.xblast.client;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardProtocolFamily;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;

/**
 * Main class in charge of communicating with the server and displaying the
 * current GameState.
 * 
 * @author Loïc Vandenberghe (257742)
 * @author Yann Vonlanthen(258857)
 *
 */
public class Main {
    
    /*
     * Constants
     */
    private static final int PORT = 2016;
    private static final int MAX_BYTES = 2*(Cell.COUNT + 1) + 16 + 1 + 1;   //FIXME
    private static final int GAME_JOIN_REQUEST_REPEATING_TIME = 1000;
    private static final String DEFAULT_HOST = "localhost";

    /*
     * Attributes
     */
    private static XBlastComponent xbc;
    private static GameState gameState;
    private static PlayerID id;   //FIXME should never change?

    public static void main(String[] args) throws IOException, InterruptedException, InvocationTargetException {//FIXME
        //1) send server the intention to join a game.
        //1.1) retrieve Ip-adress and open channel FIXME
        String hostName = (args.length == 0)? DEFAULT_HOST : args[0];   //FIXME throw error?
        SocketAddress address = new InetSocketAddress(hostName, PORT);
        
        DatagramChannel channel = DatagramChannel.open(StandardProtocolFamily.INET);
        channel.bind(address);
        channel.configureBlocking(false);
        
        //1.2)) send request to join game
        ByteBuffer sendByteBuffer = ByteBuffer.allocate(1);
        ByteBuffer receiveByteBuffer = ByteBuffer.allocate(MAX_BYTES);
        sendByteBuffer.put((byte) 0);   // place to later put PlayerID
        do{
            channel.send(sendByteBuffer, address);
            Thread.sleep(GAME_JOIN_REQUEST_REPEATING_TIME);
        }while((channel.receive(receiveByteBuffer)) != null);   //FIXME i don't save the senderAdress, should we check that
                                                                // the server is always the same?
        
        
        //2) after receiving the initial GameState the Clients and waits for the next one
        channel.configureBlocking(true);
        

        do{
            receiveByteBuffer.flip();
            id = PlayerID.values()[receiveByteBuffer.get()];  //FIXME do this everytime? /check/throw exception?
            List<Byte> serialized = new ArrayList<>();
            while(receiveByteBuffer.hasRemaining()){
                serialized.add(receiveByteBuffer.get());
            }
            receiveByteBuffer.clear();
            gameState = GameStateDeserializer.deserializeGameState(serialized);
            
            SwingUtilities.invokeAndWait(() -> createUI()); //FIXME why not invokeLater?

           //FIXME gameState as attribute?
            channel.receive(receiveByteBuffer);
       }while(true); //FIXME add game.isOver() condition!
           //FIXME dont forget close channel!

    }

    public static void createUI(){
        
        /*
         * Display given GameState
         */
        
        JFrame f = new JFrame("TEST");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        xbc = new XBlastComponent();    //FIXME create everytime??
        xbc.setGameState(gameState, id);  //FIXME

        f.getContentPane().add(xbc);
        f.setResizable(false);
        f.pack();
        f.setVisible(true);
        
        /*
         * Manage the Keyboard input
         */
    }
}

package ch.epfl.xblast.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardProtocolFamily;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

import javax.swing.SwingUtilities;

import ch.epfl.xblast.Cell;

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
    private static final int GAME_JOIN_REQUEST_TIME = 1000;


    public static void main(String[] args) throws IOException, InterruptedException {//FIXME
        //1) send server the intention to join a game.
        //1.1) retrieve Ip-adress and open channel FIXME
        InetSocketAddress i = new InetSocketAddress(args[0], PORT);
        
        DatagramChannel channel = DatagramChannel.open(StandardProtocolFamily.INET);
        channel.bind(i);
        channel.configureBlocking(false);
        
        //1.2)) send request to join game
        ByteBuffer oneByteBuffer = ByteBuffer.allocate(1);
        ByteBuffer b = ByteBuffer.allocate(MAX_BYTES);
        oneByteBuffer.put((byte)0);
        SocketAddress s;
        do{
            channel.send(oneByteBuffer, i);
            Thread.sleep(GAME_JOIN_REQUEST_TIME);
        }while((s = channel.receive(b)) != null);
        
        
        //2)
        
        
        
        SwingUtilities.invokeLater(() -> createUI());
    }

    public static void createUI(){
        
    }
}

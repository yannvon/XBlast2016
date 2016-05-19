package ch.epfl.xblast.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardProtocolFamily;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerAction;
import ch.epfl.xblast.PlayerID;

/**
 * Main class of the Client. In charge of communicating with the server and
 * displaying the current GameState.
 * 
 * @author Loïc Vandenberghe (257742)
 * @author Yann Vonlanthen(258857)
 *
 */
public class MainGUI {

    /*
     * Constants
     */
    private static final ImageCollection GUI_COLLECTION = new ImageCollection(
            "gui");
    
    private static final int PORT = 2016;
    private static final int MAX_RECEIVING_BYTES = 2 * (Cell.COUNT + 1)
            + 4 * PlayerID.values().length + 1 + 1; // FIXME
    private static final int GAME_JOIN_REQUEST_REPEATING_TIME = 1000;
    private static final String DEFAULT_HOST = "localhost";
    // FIXME KeyBoard Control map here?

    /*
     * Attributes
     */
    private static XBlastComponent xbc;
    private static DatagramChannel channel;
    private static SocketAddress serverAddress;
    
    private static String hostName = DEFAULT_HOST;
    private static boolean input = false;

    /**
     * Main method of the XBlast 2016 Client.
     * 
     * @param args
     *            IP-address of the Server. If none localhost is used as default
     *            address.
     * @throws Exception
     */
    public static void main(String[] args) throws Exception{    //FIXME error handling!!
        
        /*
         * PHASE 0
         * IP Address input
         */
        SwingUtilities.invokeAndWait(() -> createMenu());

        while(!input){
            Thread.sleep(50);
        }
        
        /*
         * PHASE 1
         * 1.1) retrieve IP-address and open channel
         */
        serverAddress = new InetSocketAddress(hostName, PORT);
        
        //FIXME try with resources. HOW?
        channel = DatagramChannel.open(StandardProtocolFamily.INET);
        
        //TODO comments
        channel.configureBlocking(false);

        /*
         * 1.2) Send request to join game to Server as long as the server
         * doesn't send the initial GameState.
         */
        ByteBuffer sendByteBuffer = ByteBuffer.allocate(1);
        ByteBuffer receiveByteBuffer = ByteBuffer.allocate(MAX_RECEIVING_BYTES);
        sendByteBuffer.put((byte) PlayerAction.JOIN_GAME.ordinal());
        sendByteBuffer.flip();
        do{
            channel.send(sendByteBuffer, serverAddress);
            sendByteBuffer.rewind();
            Thread.sleep(GAME_JOIN_REQUEST_REPEATING_TIME);
        }while((channel.receive(receiveByteBuffer)) == null);
        
        /*
         * PHASE 2
         * 2.1) Start by invoking the parallel EDT Swing thread.
         */
        SwingUtilities.invokeAndWait(() -> createUI());
        
        
        // From now one the client will wait to get the next GameState from the server.
        channel.configureBlocking(true);

        /*
         * 2.2) As long as the program runs the client waits for a new GameState
         * and shares it with the parallel Swing thread.
         */
        do{
            receiveByteBuffer.flip();
            PlayerID id = PlayerID.values()[receiveByteBuffer.get()];  //FIXME do this everytime? /check/throw exception?
            List<Byte> serialized = new ArrayList<>();
            while(receiveByteBuffer.hasRemaining()){
                serialized.add(receiveByteBuffer.get());
            }
            GameState gameState = GameStateDeserializer.deserializeGameState(serialized);
            xbc.setGameState(gameState, id);
            receiveByteBuffer.clear();
            channel.receive(receiveByteBuffer);
        } while (true);
        //FIXME close channel?
    }

    public static void createUI(){
        
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
                //Do nothing.
            }
        };
        xbc.addKeyListener(new KeyboardEventHandler(KeyboardEventHandler.DEFAULT_CONTROL_MAP, c));
    }
    
    public static void createMenu(){
        
        /*
         * Open new Window.
         */
        JFrame f = new JFrame("XBlast 2016");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        /*
         * Title
         */
        JPanel p = new JPanel(new BorderLayout());
        ImageIcon i = new ImageIcon(GUI_COLLECTION.image((byte) 000));
        JLabel title = new JLabel(i);
        title.setBorder(BorderFactory.createMatteBorder(20, 20, 20, 20, new Color(0, 200, 255)));
        p.add(title, BorderLayout.PAGE_START);

        /*
         * Input
         */
        //FIRST
        JLabel instruction1 = new JLabel("Enter server ip address");
        instruction1.setBorder(BorderFactory.createMatteBorder(10, 10, 10, 0, new Color(0, 170, 255)));
        p.add(instruction1, BorderLayout.LINE_START);
        
        JFormattedTextField textboxIP = new JFormattedTextField();
        //add text field
        textboxIP.setValue(DEFAULT_HOST);
        textboxIP.setColumns(10);
        textboxIP.setBorder(BorderFactory.createMatteBorder(10, 0, 10, 10, new Color(0, 170, 255)));
        p.add(textboxIP, BorderLayout.CENTER);
        
//        //SECOND
//        JLabel instruction2 = new JLabel("Enter server ip address");
//        p.add(instruction2, BorderLayout.LINE_START);
//        
//        JFormattedTextField textboxServer = new JFormattedTextField();
//        //add text field
//        textboxIP.setValue(DEFAULT_HOST);
//        textboxIP.setColumns(10);
//        p.add(textboxServer, BorderLayout.CENTER);
        
        /*
         * Buttons
         */
        JButton b1 = new JButton("Local Game");
        b1.addActionListener(e -> {
            hostName = DEFAULT_HOST;
            input = true;
            f.dispose();});
        
        JButton b2 = new JButton("Enter IP-address");
        b2.addActionListener(e -> {
            hostName = (String) textboxIP.getValue();
            input = true;
            f.dispose();});
        
        JButton b3 = new JButton("Start Server");
        b3.addActionListener(e -> {
            String[] args = { "1" };
            Runnable serverMain = new Runnable() {
                public void run() {
                    try {
                        ch.epfl.xblast.server.Main.main(args);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            };

            new Thread(serverMain).start();
        });

        JPanel buttons = new JPanel(new FlowLayout());
        buttons.add(b1);
        buttons.add(b2);
        buttons.add(b3);

        
        p.add(buttons, BorderLayout.PAGE_END);
        p.setBorder(BorderFactory.createMatteBorder(40, 40, 40, 40, new Color(0, 255, 255)));
        f.getContentPane().add(p);

        //f.setResizable(false);
        f.pack();
        f.setVisible(true);
    }
}
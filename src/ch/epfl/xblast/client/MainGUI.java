package ch.epfl.xblast.client;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Label;
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
    
    private static final String DEFAULT_HOST = "localhost";

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
         * add list for players
         */
        Label nombreText = new Label("number of player", Label.RIGHT);
        Choice nbPlayers = new Choice();
        for(int n=1; n<=4;n++)
            nbPlayers.add(new Integer(n).toString());
        nbPlayers.select(0);
        
        /*
         * Buttons
         */
        JButton b1 = new JButton("Local Game");
        b1.addActionListener(e -> {
            Runnable localMain = new Runnable() {
                public void run() {
                    try {
                        ch.epfl.xblast.LocalGame.main(new String[0]);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            new Thread(localMain).start();
            f.dispose();});
        
        JButton b2 = new JButton("Enter IP-address");
        b2.addActionListener(e -> {
            String[] address = { (String) textboxIP.getValue()};
            Runnable clientMain = new Runnable() {
                public void run() {
                    try {
                        ch.epfl.xblast.client.Main.main(address);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            new Thread(clientMain).start();
            f.dispose();});
        
        JButton b3 = new JButton("Start Server");
        b3.addActionListener(e -> {
            String[] argsMain = { nbPlayers.getSelectedItem() }; //TODO
            String[] argsClient = { DEFAULT_HOST };
            Runnable serverMain = new Runnable() {
                public void run() {
                    try {
                        ch.epfl.xblast.server.Main.main(argsMain);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            };
            Runnable clientMain = new Runnable() {
                public void run() {
                    try {
                        ch.epfl.xblast.client.Main.main(argsClient);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            new Thread(serverMain).start();
            new Thread(clientMain).start();
            f.dispose();
            
        });

        
        
        JPanel buttons = new JPanel(new FlowLayout());
        buttons.add(b1);
        buttons.add(b2);
        buttons.add(b3);
        buttons.add(nombreText);
        buttons.add(nbPlayers);

        
        p.add(buttons, BorderLayout.PAGE_END);
        p.setBorder(BorderFactory.createMatteBorder(40, 40, 40, 40, new Color(0, 255, 255)));
        f.getContentPane().add(p);

        //f.setResizable(false);
        f.pack();
        f.setVisible(true);
    }
}
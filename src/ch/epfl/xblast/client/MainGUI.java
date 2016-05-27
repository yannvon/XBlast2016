package ch.epfl.xblast.client;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Label;
import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import ch.epfl.xblast.server.Level;

/**
 * Bonus: Main class of the XBlast 2016 Game.
 * 
 * @author Loïc Vandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 *
 */
public class MainGUI {

    /*
     * Constants
     */
    private static final ImageCollection GUI_COLLECTION = new ImageCollection(
            "gui");
    private static final int TABBED_ICON_WIDTH = 50;
    private static final int TABBED_ICON_HEIGHT = 40;
    private static final Color TITLE_COLOR = new Color(0, 225, 255);
    private static final Color TAB_COLOR = new Color(0, 200, 255);
    private static final Color LEVEL_COLOR = new Color(0, 200, 255);
    private static final Color GENERAL_COLOR = new Color(0, 225, 255);
    
    private static final String DEFAULT_HOST = "localhost";
    private static final String NO_IP_MESSAGE = "No IP address found.";

    /**
     * Main method of the XBlast 2016.
     * 
     * @param args
     *            IP-address of the Server. If none localhost is used as default
     *            address.
     * @throws Exception
     */
    public static void main(String[] args) throws Exception{
        
        /*
         * PHASE 0
         * Open Menu
         */
        SwingUtilities.invokeAndWait(() -> createMenu());
    }


    
    public static void createMenu() {
        
        /*
         * Open new Window.
         */
        JFrame f = new JFrame("XBlast 2016");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        /*
         * Title
         */
        ImageIcon i = new ImageIcon(GUI_COLLECTION.image((byte) 000));
        JLabel title = new JLabel(i);
        title.setBorder(BorderFactory.createMatteBorder(20, 20, 20, 20, TITLE_COLOR));

        /*
         * Input Level
         */
        //1) add choice for level
        Label chooseLevel = new Label("Choose Level : ",Label.RIGHT);
        Choice levelChoice = new Choice();
        try{
            File file =  new File(Level.class
                    .getClassLoader()
                    .getResource("gameStates")
                    .toURI());
            for(String fileName: file.list())
                levelChoice.add(fileName);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        levelChoice.select("DEFAULT");
        
        JPanel levelPanel = new JPanel(new BorderLayout());
        
        levelPanel.add(chooseLevel,BorderLayout.WEST);
        levelPanel.add(levelChoice, BorderLayout.CENTER);
        levelPanel.setBorder(BorderFactory.createMatteBorder(10, 10, 10, 10, LEVEL_COLOR));
        
        
        
        /*
         * LOCAL GAME
         */
        //1)Input TODO different Levels or options
        
        //2)button
        JButton localButton = new JButton("Local Game");
        localButton.addActionListener(e -> {
            String[] arg= {levelChoice.getSelectedItem()};
            Runnable localMain = new Runnable() {
                public void run() {
                    try {
                        ch.epfl.xblast.LocalGame.main(arg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            new Thread(localMain).start();
            f.dispose();});
        
        //3)Panel
        JPanel localPanel = new JPanel(new BorderLayout());
        localPanel.add(localButton,BorderLayout.EAST);
        localPanel.setBorder(BorderFactory.createMatteBorder(10, 10, 10, 10, TAB_COLOR));
        
        /*
         * JOIN GAME
         */
        //1)Input
        JLabel instruction1 = new JLabel(" Enter server ip address  :  ",Label.LEFT);//FIXME What the fuck????
        
        JFormattedTextField textboxIP = new JFormattedTextField();
        textboxIP.setValue(DEFAULT_HOST);
        textboxIP.setColumns(10);

        //2)button
        JButton clientButton = new JButton("Join Game");
        clientButton.addActionListener(e -> {
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
        
        //3)Panel
        JPanel clientPanel = new JPanel(new BorderLayout());
        clientPanel.add(instruction1,BorderLayout.NORTH);
        clientPanel.add(textboxIP,BorderLayout.CENTER);
        clientPanel.add(clientButton,BorderLayout.EAST);
        clientPanel.setBorder(BorderFactory.createMatteBorder(10, 10, 10, 10, TAB_COLOR));
        
        
        /*
         * HOST GAME
         */
        //1)Input        
        JLabel numberText = new JLabel("Number of players");
        
        Choice nbPlayers = new Choice();
        for (int n = 1; n <= 4; n++)
            nbPlayers.add(new Integer(n).toString());
        nbPlayers.select(0);

        JCheckBox checkLaunchClient = new JCheckBox("Launch Server Only", false);
        
        //2)Button
        JButton serverButtton = new JButton("Host Game");
        serverButtton.addActionListener(e -> {
            String[] argsMain = { nbPlayers.getSelectedItem(),levelChoice.getSelectedItem() };
            String[] argsClient = { DEFAULT_HOST };
            Runnable serverMain = new Runnable() {
                public void run() {
                    try {
                        ch.epfl.xblast.server.Main.main(argsMain);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            if(!checkLaunchClient.isSelected()){
                Runnable clientMain = new Runnable() {
                    public void run() {
                        try {
                            ch.epfl.xblast.client.Main.main(argsClient);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                new Thread(clientMain).start();
            }
            new Thread(serverMain).start();
            
            f.dispose();
            
        });

        //3) Get own ip address
        String myip = NO_IP_MESSAGE;
        try {
            myip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        }
        
        JFormattedTextField textIP = new JFormattedTextField();
        textIP.setValue(myip);
        ImageIcon ipIcon = new ImageIcon(GUI_COLLECTION.image((byte) 004)
                .getScaledInstance(50, 50,
                        Image.SCALE_SMOOTH));
        JLabel ipLabel = new JLabel("Your IP-Address is: ", ipIcon, 0);
        
        //4) center panel
        JPanel centerPanel = new JPanel(new FlowLayout());
        centerPanel.add(checkLaunchClient);
        centerPanel.add(ipLabel);
        centerPanel.add(textIP);
        
        //5)final panel
        JPanel serverPanel = new JPanel(new BorderLayout());
        serverPanel.add(numberText,BorderLayout.NORTH);
        serverPanel.add(nbPlayers,BorderLayout.WEST);
        serverPanel.add(centerPanel,BorderLayout.CENTER);
        serverPanel.add(serverButtton,BorderLayout.EAST);
        
        serverPanel.setBorder(BorderFactory.createMatteBorder(10, 10, 10, 10, TAB_COLOR));
        
        
        /*
         * General Panel
         */
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setForeground(Color.darkGray);
        
        JPanel panel = new JPanel(new BorderLayout());
        ImageIcon join = new ImageIcon(GUI_COLLECTION.image((byte) 001)
                .getScaledInstance(TABBED_ICON_HEIGHT, TABBED_ICON_WIDTH,
                        Image.SCALE_SMOOTH));
        ImageIcon host = new ImageIcon(GUI_COLLECTION.image((byte) 002)
                .getScaledInstance(TABBED_ICON_HEIGHT, TABBED_ICON_WIDTH,
                        Image.SCALE_SMOOTH));
        ImageIcon local = new ImageIcon(GUI_COLLECTION.image((byte) 003)
                .getScaledInstance(TABBED_ICON_HEIGHT, TABBED_ICON_WIDTH,
                        Image.SCALE_SMOOTH));

        tabbedPane.addTab("JOIN GAME", join, clientPanel);
        tabbedPane.addTab("HOST GAME", host, serverPanel);
        tabbedPane.addTab("LOCAL GAME", local, localPanel);
        
        panel.add(title,BorderLayout.NORTH);
        panel.add(levelPanel,BorderLayout.CENTER);
        panel.add(tabbedPane,BorderLayout.SOUTH);
        panel.setBorder(BorderFactory.createMatteBorder(20, 20, 20, 20, GENERAL_COLOR));
        f.getContentPane().add(panel);

        f.setResizable(false);
        f.pack();
        f.setVisible(true);
    }
}
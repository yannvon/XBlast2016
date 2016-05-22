package ch.epfl.xblast.client;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Choice;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.plaf.TabbedPaneUI;

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
    
    private static final String DEFAULT_HOST = "localhost";

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


    
    public static void createMenu(){
        
//      //SECOND
//      JLabel instruction2 = new JLabel("Enter server ip address");
//      p.add(instruction2, BorderLayout.LINE_START);
//      
//      JFormattedTextField textboxServer = new JFormattedTextField();
//      //add text field
//      textboxIP.setValue(DEFAULT_HOST);
//      textboxIP.setColumns(10);
//      p.add(textboxServer, BorderLayout.CENTER);
        
        
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
        title.setBorder(BorderFactory.createMatteBorder(20, 20, 20, 20, new Color(0, 200, 255)));

        /*
         * LOCAL GAME
         */
        //1)Input TODO different Levels or options
        
        //2)button
        JButton localButton = new JButton("Local Game");
        localButton.addActionListener(e -> {
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
        
        //3)Panel
        JPanel localPanel = new JPanel(new BorderLayout());
        localPanel.add(localButton,BorderLayout.EAST);
        localPanel.setBorder(BorderFactory.createMatteBorder(10, 10, 10, 10, new Color(0, 0, 255)));
        
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
        clientPanel.setBorder(BorderFactory.createMatteBorder(10, 10, 10, 10, new Color(0, 125, 255)));
        
        
        /*
         * HOST GAME
         */
        
        //1)Input        
        JLabel numberText = new JLabel("Number of players");
        
        Choice nbPlayers = new Choice();
        for (int n = 1; n <= 4; n++)
            nbPlayers.add(new Integer(n).toString());
        nbPlayers.select(0);

        Checkbox checkLaunchClient = new Checkbox("Launch Server Only", false);
        
        //2)Button
        JButton serverButtton = new JButton("Host Game");
        serverButtton.addActionListener(e -> {
            String[] argsMain = { nbPlayers.getSelectedItem() };
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
            if(!checkLaunchClient.getState()){
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

        //3)Panel
        JPanel serverPanel = new JPanel(new BorderLayout());
        serverPanel.add(numberText,BorderLayout.NORTH);
        serverPanel.add(nbPlayers,BorderLayout.WEST);
        serverPanel.add(checkLaunchClient,BorderLayout.CENTER);
        serverPanel.add(serverButtton,BorderLayout.EAST);
        
        serverPanel.setBorder(BorderFactory.createMatteBorder(10, 10, 10, 10, new Color(0, 0, 255)));
        
        
        /*
         * General Panel
         */
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setForeground(Color.darkGray);
//        tabbedPane.setUI(new TabbedPaneUI);
        
        tabbedPane.addTab("JOIN GAME", clientPanel);
        tabbedPane.addTab("HOST GAME", serverPanel);
        tabbedPane.addTab("LOCAL GAME", localPanel);
        

        JPanel panel = new JPanel(new BorderLayout());
        
        panel.add(title,BorderLayout.NORTH);
        panel.add(tabbedPane,BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createMatteBorder(40, 40, 40, 40, new Color(0, 255, 255)));
        f.getContentPane().add(panel);

        //f.setResizable(false);
        f.pack();
        f.setVisible(true);
    }
}
package ch.epfl.xblast.client;


import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

import ch.epfl.xblast.PlayerID;


/**
 * @author Loïc Vandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 *
 */
public final class XBlastComponent extends JComponent {

    private static final int PREFERRED_HEIGHT = 688;
    private static final int PREFERRED_WIDTH = 960;
    
    private GameState gameState;
    
    /**
     * 
     */
    public XBlastComponent() {
        gameState=null;
    }
  
    /**
     * @param g
     * @param p
     */
    public void setGameState(GameState g,PlayerID p){
        //TODO
        gameState=g;
        repaint();
    }
    
    @Override
    public Dimension getPreferredSize(){
        return new Dimension(PREFERRED_WIDTH,PREFERRED_HEIGHT);
    }
    
    
    @Override
    protected void paintComponent(Graphics g0){
        Graphics2D g = (Graphics2D)g0;
    }
}

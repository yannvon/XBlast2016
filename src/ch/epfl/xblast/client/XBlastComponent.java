package ch.epfl.xblast.client;


import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Iterator;

import javax.swing.JComponent;

import ch.epfl.xblast.Cell;
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
    private PlayerID playerId;
    
    /**
     * 
     */
    public XBlastComponent() {
        gameState=null;
        playerId=null;
    }
  
    /**
     * @param g
     * @param p
     * @param playerId 
     */
    public void setGameState(GameState g,PlayerID p){
        playerId=p;
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
        
        Iterator<Image> bloc = gameState.board().iterator();
        Iterator<Image> explosion = gameState.explosions().iterator();
        int imageWidth = gameState.explosions().get(0).getWidth(null);
        int imageHeigth= gameState.explosions().get(0).getHeight(null);
        
        
        for(int y=0; y<Cell.ROWS*imageHeigth; y+=imageHeigth ){
            for(int x=0; x<Cell.COLUMNS*imageWidth;x+=imageWidth){
                Image b = bloc.next();
                g.drawImage(b,x,y,null);
                g.drawImage(explosion.next(), x, y, null);
            }
         }
        
        
        
    }
}

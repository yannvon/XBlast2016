package ch.epfl.xblast.client;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.swing.JComponent;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.client.GameState.Player;


/**
 * @author Loïc Vandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 *
 */
public final class XBlastComponent extends JComponent {

    private static final int PREFERRED_HEIGHT = 688;
    private static final int PREFERRED_WIDTH = 960;
    private static final int Y_SCORELINE = 659;
    private static final int X_PLAYER1_SCORE = 96;
    private static final int X_PLAYER2_SCORE = 240;
    private static final int X_PLAYER3_SCORE = 768;
    private static final int X_PLAYER4_SCORE = 912;
    
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
        /*
         * Draw Board
         */
        Iterator<Image> bloc = gameState.board().iterator();
        Iterator<Image> explosion = gameState.explosions().iterator();
        int imageWidth = gameState.board().get(0).getWidth(null); 
        int imageHeigth= gameState.board().get(0).getHeight(null); 
        int heigthBoard = Cell.ROWS * imageHeigth;
        
        for(int y=0; y<heigthBoard; y+=imageHeigth ){
            for(int x=0; x<Cell.COLUMNS*imageWidth;x+=imageWidth){
                g.drawImage(bloc.next(),x,y,null);
                g.drawImage(explosion.next(), x, y, null);
            }
         }
        /*
         * Draw ScoreLine and TimeLine
         */
        int x=0;
        
        for(Image i: gameState.scoreLine()){
            g.drawImage(i, x, heigthBoard, null);
            x+=i.getWidth(null);
        }
        x=0;
        for(Image i: gameState.timeLine()){
            g.drawImage(i, x, heigthBoard+48, null);//FIXME
            x+=i.getWidth(null);
        }
        
        /*
         * Draw Score
         */
        Font font = new Font("Arial", Font.BOLD, 25);
        g.setColor(Color.WHITE);
        g.setFont(font);
        
        g.drawString(Integer.toString(gameState.players().get(0).lives()),X_PLAYER1_SCORE,Y_SCORELINE );
        g.drawString(Integer.toString(gameState.players().get(1).lives()), X_PLAYER2_SCORE, Y_SCORELINE);
        g.drawString(Integer.toString(gameState.players().get(2).lives()), X_PLAYER3_SCORE, Y_SCORELINE);
        g.drawString(Integer.toString(gameState.players().get(3).lives()), X_PLAYER4_SCORE, Y_SCORELINE);
        
        /*
         * DrawPlayer
         */
        Comparator<Player> c1 = (p1,p2) ->Integer.compare(p1.position().y(), p2.position().y());
        int ordinal= playerId.ordinal();
        Comparator<Player> c2 = (p1, p2) -> Integer.compare(
                Math.floorMod(ordinal + p2.id().ordinal(),
                        PlayerID.values().length),
                Math.floorMod(ordinal + p1.id().ordinal(),
                        PlayerID.values().length));    
        
        Comparator<Player> comparator= c1.thenComparing(c2);
        
        List<Player> orderedPlayers=new ArrayList<>(gameState.players());

        Collections.sort(orderedPlayers,comparator);
        
        
    }
}

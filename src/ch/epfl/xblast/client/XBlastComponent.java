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
import java.util.function.UnaryOperator;

import javax.swing.JComponent;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.client.GameState.Player;

/**
 * XBlastComponent is a (non-immutable) Swing component displaying a GameState.
 * 
 * @author Loïc Vandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 *
 */
@SuppressWarnings("serial")
public final class XBlastComponent extends JComponent {

    /*
     * Constants
     */
    private static final int PREFERRED_HEIGHT = 688;
    private static final int PREFERRED_WIDTH = 960;
    
    // --- blocks
    private static final int BLOCK_HEIGHT = 48;
    private static final int BLOCK_WIDTH = 64;
    private static final int BOARD_HEIGHT = Cell.ROWS * BLOCK_HEIGHT;
    private static final int BOARD_WIDTH = Cell.COLUMNS * BLOCK_WIDTH;
    
    // --- players
    private final static int NUMBER_OF_PLAYERS = PlayerID.values().length;
    private static final UnaryOperator<Integer> X_FUNCTION = (x) -> 4 * x - 24;
    private static final UnaryOperator<Integer> Y_FUNCTION = (y) -> 3 * y - 52;
    
    // --- scorLine
    private static final int SCORELINE_HEIGHT = 48;
    private static final int SCORELINE_IMAGE_WIDTH = BOARD_WIDTH / GameStateDeserializer.SCORELINE_LENGTH;
    private static final Font SCORE_FONT = new Font("Arial", Font.BOLD, 25);
    private static final int SCORELINE_Y = 659;
    private static final int[] SCORELINE_X = {96, 240, 768, 912};
    
    // --- timeLine
    private static final int TIMELINE_IMAGE_WIDTH = BOARD_WIDTH / GameStateDeserializer.TIMELINE_LENGTH;
    
    
    /*
     * Attributes
     */

    private GameState gameState;
    private PlayerID playerId;

    /**
     * Constructor initialising all attributes to null.
     */
    public XBlastComponent() {
        gameState = null;
        playerId = null;
    }

    /**
     * Allows to display a new GameState, and refreshes the Display accordingly.
     * 
     * @param gameState
     *            new GameSate to display
     * @param playerId
     *            playerId of the player who's client it is
     */
    public void setGameState(GameState gameState, PlayerID playerId) {
        this.gameState = gameState;
        this.playerId = playerId;
        repaint();
    }

    
    @Override
    /**
     * Returns the Dimension of the preferred size of the Component.
     * 
     * @returns prefferedSize of the Component
     */
    public Dimension getPreferredSize() {
        return new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT);
    }

    @Override
    /**
     * Method called by Swing to redraw the content of the XBlastComponent.
     */
    protected void paintComponent(Graphics g0) {
        // don't do anything if the attributes are not yet initialised.
        if (gameState == null || playerId == null)
            return;
        
        Graphics2D g = (Graphics2D) g0;
        
        /*
         * Draw Board
         */
        Iterator<Image> bloc = gameState.board().iterator();
        Iterator<Image> explosion = gameState.explosions().iterator();
        
        for (int y = 0; y < BOARD_HEIGHT; y += BLOCK_HEIGHT) {
            for (int x = 0; x < BOARD_WIDTH; x += BLOCK_WIDTH) {
                g.drawImage(bloc.next(), x, y, null);
                g.drawImage(explosion.next(), x, y, null);
            }
        }
        
        /*
         * Draw ScoreLine
         */
        int xCoordinate = 0;
        for (Image i : gameState.scoreLine()) {
            g.drawImage(i, xCoordinate, BOARD_HEIGHT, null);
            xCoordinate += SCORELINE_IMAGE_WIDTH;
        }
        
        /*
         * Draw TimeLine
         */
        xCoordinate = 0;
        for (Image i : gameState.timeLine()) {
            g.drawImage(i, xCoordinate, BOARD_HEIGHT + SCORELINE_HEIGHT, null);
            xCoordinate += TIMELINE_IMAGE_WIDTH;
        }

        /*
         * Draw Score
         */
        g.setColor(Color.WHITE);
        g.setFont(SCORE_FONT);

        for(int i = 0; i < NUMBER_OF_PLAYERS; i++) {
            g.drawString(Integer.toString(gameState.players().get(i).lives()),
                    SCORELINE_X[i], SCORELINE_Y);
        }
        
        /*
         * Draw Players
         */
        Comparator<Player> c1 = (p1, p2) -> Integer.compare(p1.position().y(),
                p2.position().y());
                
        Comparator<Player> c2 = (p1, p2) -> Integer.compare(
                Math.floorMod(p1.id().ordinal() + (NUMBER_OF_PLAYERS - 1 - playerId.ordinal()),
                        NUMBER_OF_PLAYERS),
                Math.floorMod(p2.id().ordinal() + (NUMBER_OF_PLAYERS - 1 - playerId.ordinal()),
                        NUMBER_OF_PLAYERS));

        Comparator<Player> comparator = c1.thenComparing(c2);
        List<Player> orderedPlayers = new ArrayList<>(gameState.players());
        Collections.sort(orderedPlayers, comparator);

        for (Player p : orderedPlayers) {
            g.drawImage(p.image(), X_FUNCTION.apply((p.position().x())),
                    Y_FUNCTION.apply((p.position().y())), null);
        }
    }
}

package ch.epfl.xblast.personal.part2;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.client.GameState;
import ch.epfl.xblast.client.GameStateDeserializer;
import ch.epfl.xblast.client.XBlastComponent;
import ch.epfl.xblast.server.Block;
import ch.epfl.xblast.server.Board;
import ch.epfl.xblast.server.GameStateSerializer;
import ch.epfl.xblast.server.Level;
import ch.epfl.xblast.server.Player;
import ch.epfl.xblast.server.debug.RandomGame;

public class PlayerDisplayingVisualTest {

    /*
     * Constants
     */

    private static final PlayerID CLIENT_PLAYERID = PlayerID.PLAYER_1;
    
    private static final Block __ = Block.FREE;
    private static final Block XX = Block.INDESTRUCTIBLE_WALL;
    private static final Block xx = Block.DESTRUCTIBLE_WALL;
    private static final Board board = Board.ofQuadrantNWBlocksWalled(
            Arrays.asList(
                    Arrays.asList(__, __, __, __, __, xx, __),
                    Arrays.asList(__, XX, xx, XX, xx, XX, xx),
                    Arrays.asList(__, xx, __, __, __, xx, __),
                    Arrays.asList(xx, XX, __, XX, XX, XX, XX),
                    Arrays.asList(__, xx, __, xx, __, __, __),
                    Arrays.asList(xx, XX, xx, XX, xx, XX, __)));
    
    
    private static final List<Player> playersDefault= Arrays.asList(
            new Player(PlayerID.PLAYER_1,3,new Cell(1,1),2,3),
            new Player(PlayerID.PLAYER_2,3,new Cell(13,1),2,3),
            new Player(PlayerID.PLAYER_3,3,new Cell(13,11),2,3),
            new Player(PlayerID.PLAYER_4,3,new Cell(1,11),2,3)
            );
    private static final List<Player> playersSameSpot= Arrays.asList(
            new Player(PlayerID.PLAYER_1,3,new Cell(1,1),2,3),
            new Player(PlayerID.PLAYER_2,3,new Cell(1,1),2,3),
            new Player(PlayerID.PLAYER_3,3,new Cell(1,1),2,3),
            new Player(PlayerID.PLAYER_4,3,new Cell(1,1),2,3)
            );
    private static final List<Player> playersOnTop= Arrays.asList(
            new Player(PlayerID.PLAYER_1,3,new Cell(1,1),2,3),
            new Player(PlayerID.PLAYER_2,3,new Cell(1,2),2,3),
            new Player(PlayerID.PLAYER_3,3,new Cell(1,3),2,3),
            new Player(PlayerID.PLAYER_4,3,new Cell(1,4),2,3)
            );
    

    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createUIRandomGame());        
    }

    public static void createUIRandomGame() {
        JFrame f = new JFrame("Viusal Player Order Test");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        XBlastComponent xblastComponent = new XBlastComponent();

        xblastComponent
                .setGameState(
                        GameStateDeserializer.deserializeGameState(
                                GameStateSerializer.serialize(
                                        Level.DEFAULT_LEVEL.boardPainter(),
                                        new ch.epfl.xblast.server.GameState(board, playersOnTop))),
                CLIENT_PLAYERID);

        f.getContentPane().add(xblastComponent);
        f.setResizable(false);
        f.pack();
        f.setVisible(true);
    }

}

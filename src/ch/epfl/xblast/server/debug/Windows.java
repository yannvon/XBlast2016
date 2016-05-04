package ch.epfl.xblast.server.debug;

import java.awt.Component;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.client.GameState;
import ch.epfl.xblast.client.GameStateDeserializer;
import ch.epfl.xblast.client.XBlastComponent;
import ch.epfl.xblast.server.GameStateSerializer;
import ch.epfl.xblast.server.Level;

public class Windows {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> createUI());
        
        
    }

    public static void createUI(){
        JFrame f = new JFrame("TEST");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        XBlastComponent game = new XBlastComponent();
        Level lvl=Level.DEFAULT_LEVEL;
        List<Byte> ser= GameStateSerializer.serialize(lvl.boardPainter(),lvl.initialGameState());
        GameState gameState = GameStateDeserializer.deserializeGameState(ser);
        game.setGameState(gameState, PlayerID.PLAYER_1);
        
        f.setSize(game.preferredSize());
        f.add(game);
        f.setVisible(true);
    }

}

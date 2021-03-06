package ch.epfl.xblast.server.debug;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.Time;
import ch.epfl.xblast.client.GameState;
import ch.epfl.xblast.client.GameStateDeserializer;
import ch.epfl.xblast.client.XBlastComponent;
import ch.epfl.xblast.server.Ticks;

public class VisualRandomGame {
    
   
    private VisualRandomGame() {}

    public static void main(String[] args) throws InterruptedException, IOException {
        
        SwingUtilities.invokeLater(() -> createUIRandomGame());
    }
    
    public static void createUIRandomGame(){
        JFrame f = new JFrame("TEST");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        

        XBlastComponent game = new XBlastComponent();
        List<List<Byte>> games = RandomGame.randomGame();
        game.setGameState(GameStateDeserializer.deserializeGameState(games.get(0)), PlayerID.PLAYER_1);

        f.getContentPane().add(game);
        f.setResizable(false);
        f.pack();
        f.setVisible(true);
        
        Iterator<List<Byte>> it =games.iterator();

        Timer t = new Timer(50,
                (a) -> {

                    if (it.hasNext()) {
                        List<Byte> ser = it.next();
                        GameState gameState = GameStateDeserializer
                                .deserializeGameState(ser);
                        game.setGameState(gameState, PlayerID.PLAYER_1);
                    }
                });
        t.setRepeats(true);
        t.start();
        
    }
}

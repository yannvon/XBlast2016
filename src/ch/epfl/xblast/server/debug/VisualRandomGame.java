package ch.epfl.xblast.server.debug;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.client.GameState;
import ch.epfl.xblast.client.GameStateDeserializer;
import ch.epfl.xblast.client.XBlastComponent;

public class VisualRandomGame {
    
    
    private static final RandomEventGenerator RANDOM = new RandomEventGenerator(2016, 30, 100);

    private VisualRandomGame() {}

    public static void main(String[] args) throws InterruptedException, IOException {
        
        SwingUtilities.invokeLater(() -> createUI());
    }
    
    public static void createUI(){
        JFrame f = new JFrame("TEST");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        

        XBlastComponent game = new XBlastComponent();
        List<List<Byte>> games = RandomGame.randomGame();
        game.setGameState(GameStateDeserializer.deserializeGameState(games.get(0)), PlayerID.PLAYER_1);
        f.getContentPane().add(game);
        f.setVisible(true);
        Iterator<List<Byte>> it =games.iterator();
        
            Timer t= new Timer(200,(a)->{
                
                if(it.hasNext()) {
                    List<Byte> ser = it.next();
                GameState gameState = GameStateDeserializer.deserializeGameState(ser);
                game.setGameState(gameState, PlayerID.PLAYER_1);}
            });
            t.setRepeats(true);
            t.start();
          
        
        
        
        
    }
}

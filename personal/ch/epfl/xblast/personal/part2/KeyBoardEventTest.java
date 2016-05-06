package ch.epfl.xblast.personal.part2;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import ch.epfl.xblast.PlayerAction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.client.GameStateDeserializer;
import ch.epfl.xblast.client.KeyboardEventHandler;
import ch.epfl.xblast.client.XBlastComponent;
import ch.epfl.xblast.server.debug.RandomGame;

public class KeyBoardEventTest {


    private KeyBoardEventTest() {}

    public static void main(String[] args) throws InterruptedException, IOException {
        
        SwingUtilities.invokeLater(() -> createUIRandomGame());
    }
    
    public static void createUIRandomGame(){
        JFrame f = new JFrame("TEST");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setBounds(0, 0, XBlastComponent.PREFERRED_WIDTH+18, XBlastComponent.PREFERRED_HEIGHT+45);//FIXME adjust
        

        XBlastComponent game = new XBlastComponent();
        List<List<Byte>> games = RandomGame.randomGame();
        game.setGameState(GameStateDeserializer.deserializeGameState(games.get(0)), PlayerID.PLAYER_1);
        
        Map<Integer, PlayerAction> kb = new HashMap<>();
        kb.put(KeyEvent.VK_UP, PlayerAction.MOVE_N);
        kb.put(KeyEvent.VK_DOWN, PlayerAction.MOVE_S);
        kb.put(KeyEvent.VK_LEFT, PlayerAction.MOVE_W);
        kb.put(KeyEvent.VK_RIGHT, PlayerAction.MOVE_E);
        kb.put(KeyEvent.VK_SPACE, PlayerAction.DROP_BOMB);
        kb.put(KeyEvent.VK_SHIFT, PlayerAction.STOP);
        
        Consumer<PlayerAction> c = System.out::println;
        game.addKeyListener(new KeyboardEventHandler(kb, c));
        f.getContentPane().add(game);
        f.setVisible(true);
        game.requestFocusInWindow();
       

        
    }

}

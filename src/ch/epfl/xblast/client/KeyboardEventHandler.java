package ch.epfl.xblast.client;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyListener;
import java.util.Map;
import java.util.function.Consumer;

import ch.epfl.xblast.PlayerAction;

/**
 * This class represents a Listener of Keyboard events.
 * 
 * @author Loïc Vandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 *
 */
public final class KeyboardEventHandler extends KeyAdapter
        implements KeyListener {    
    
    public KeyboardEventHandler(Map<Integer, PlayerAction> controls,
            Consumer<PlayerAction> o) {
        
    }

}

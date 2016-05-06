package ch.epfl.xblast.client;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
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
    /*
     * Attributes
     */
    private final Map<Integer, PlayerAction> controls;
    private final Consumer<PlayerAction> consumer;
    
    /**
     * @param controls
     * @param consumer
     */
    public KeyboardEventHandler(Map<Integer, PlayerAction> controls,
            Consumer<PlayerAction> consumer) {
        this.controls = Collections.unmodifiableMap(
                new HashMap<>(Objects.requireNonNull(controls)));
        this.consumer=Objects.requireNonNull(consumer);
    }
    
    
    @Override
    public void keyPressed(KeyEvent e){
        int keyCode = e.getKeyCode();
        if(controls.containsKey(keyCode))
            consumer.accept(controls.get(keyCode));
    }

}

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
 * This class represents a Listener of Keyboard events and interpret them as PlayerAction
 * 
 * @author Loïc Vandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 *
 */
public final class KeyboardEventHandler extends KeyAdapter
        implements KeyListener { 
    /*
     * Constants
     */
    public static final Map<Integer, PlayerAction> DEFAULT_CONTROL_MAP = defaultControls(); //FIXME correct?
    
    /*
     * Attributes
     */
    private final Map<Integer, PlayerAction> controls;
    private final Consumer<PlayerAction> consumer;

    /**
     * ADDITIONAL method that constructs a map associating a key to a player
     * action in a default way.
     * 
     * @return a map associating keys to actions
     */
    private static Map<Integer, PlayerAction> defaultControls() {
        Map<Integer, PlayerAction> keyboardEvents = new HashMap<>();
        keyboardEvents.put(KeyEvent.VK_UP, PlayerAction.MOVE_N);
        keyboardEvents.put(KeyEvent.VK_DOWN, PlayerAction.MOVE_S);
        keyboardEvents.put(KeyEvent.VK_LEFT, PlayerAction.MOVE_W);
        keyboardEvents.put(KeyEvent.VK_RIGHT, PlayerAction.MOVE_E);
        keyboardEvents.put(KeyEvent.VK_SPACE, PlayerAction.DROP_BOMB);
        keyboardEvents.put(KeyEvent.VK_SHIFT, PlayerAction.STOP);
        return Collections.unmodifiableMap(keyboardEvents);
    }

    /**
     * Sole constructor taking the mapping between a key pressed and the action
     * to take, and a consumer as parameter.
     * 
     * @param controls
     *            map associating the player actions to the key's codes.
     * @param consumer
     *            a consumer of PlayerActions
     * @throws NullPointerException
     *             if one of the arguments was null.
     */
    public KeyboardEventHandler(Map<Integer, PlayerAction> controls,
            Consumer<PlayerAction> consumer) {
        this.controls = Collections.unmodifiableMap(
                new HashMap<>(Objects.requireNonNull(controls)));
        this.consumer=Objects.requireNonNull(consumer);
    }
    
    
    @Override
    /**
     * If a key that is present in the map is pressed, the consumer receives the
     * PlayerAction that was taken.
     */
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if(controls.containsKey(keyCode))
            consumer.accept(controls.get(keyCode));
    }
}

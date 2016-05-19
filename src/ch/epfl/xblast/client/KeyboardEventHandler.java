package ch.epfl.xblast.client;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
     * Constants
     */
    public static final Map<Integer, PlayerAction> DEFAULT_CONTROL_MAP = defaultControls(); //FIXME correct?
    public static final List<Map<Integer, PlayerAction>> CONTROL_MAP_MULTI = controlsMulti();
    /*
     * Attributes
     */
    private final Map<Integer, PlayerAction> controls;
    private final Consumer<PlayerAction> consumer;

    /**
     * TODO
     * @return
     */
    private static Map<Integer, PlayerAction> defaultControls(){
        Map<Integer, PlayerAction> keyboardEvents = new HashMap<>();
        keyboardEvents.put(KeyEvent.VK_UP, PlayerAction.MOVE_N);
        keyboardEvents.put(KeyEvent.VK_DOWN, PlayerAction.MOVE_S);
        keyboardEvents.put(KeyEvent.VK_LEFT, PlayerAction.MOVE_W);
        keyboardEvents.put(KeyEvent.VK_RIGHT, PlayerAction.MOVE_E);
        keyboardEvents.put(KeyEvent.VK_SPACE, PlayerAction.DROP_BOMB);
        keyboardEvents.put(KeyEvent.VK_SHIFT, PlayerAction.STOP);
        return Collections.unmodifiableMap(keyboardEvents);
    }
    
    private static List<Map<Integer, PlayerAction>> controlsMulti() {
        List<Map<Integer, PlayerAction>> controls= new ArrayList<>();
       
        //P1
        Map<Integer, PlayerAction> controlsP1 = new HashMap<>();
        controlsP1.put(KeyEvent.VK_UP, PlayerAction.MOVE_N);
        controlsP1.put(KeyEvent.VK_DOWN, PlayerAction.MOVE_S);
        controlsP1.put(KeyEvent.VK_LEFT, PlayerAction.MOVE_W);
        controlsP1.put(KeyEvent.VK_RIGHT, PlayerAction.MOVE_E);
        controlsP1.put(KeyEvent.VK_ENTER, PlayerAction.DROP_BOMB);
        controlsP1.put(KeyEvent.VK_SHIFT, PlayerAction.STOP);
        controls.add(controlsP1);
        
      //P2
        Map<Integer, PlayerAction> controlsP2 = new HashMap<>();
        controlsP2.put(KeyEvent.VK_W, PlayerAction.MOVE_N);
        controlsP2.put(KeyEvent.VK_S, PlayerAction.MOVE_S);
        controlsP2.put(KeyEvent.VK_A, PlayerAction.MOVE_W);
        controlsP2.put(KeyEvent.VK_D, PlayerAction.MOVE_E);
        controlsP2.put(KeyEvent.VK_E, PlayerAction.DROP_BOMB);
        controlsP2.put(KeyEvent.VK_Q, PlayerAction.STOP);
        controls.add(controlsP2);
       
        /*TODO
        //P3
        Map<Integer, PlayerAction> controlsP3 = new HashMap<>();
        controlsP3.put(KeyEvent.VK_UP, PlayerAction.MOVE_N);
        controlsP3.put(KeyEvent.VK_DOWN, PlayerAction.MOVE_S);
        controlsP3.put(KeyEvent.VK_LEFT, PlayerAction.MOVE_W);
        controlsP3.put(KeyEvent.VK_RIGHT, PlayerAction.MOVE_E);
        controlsP3.put(KeyEvent.VK_SPACE, PlayerAction.DROP_BOMB);
        controlsP3.put(KeyEvent.VK_SHIFT, PlayerAction.STOP);
        controls.add(controlsP3);
        
        // P4
        Map<Integer, PlayerAction> controlsP4 = new HashMap<>();
        controlsP4.put(KeyEvent.VK_UP, PlayerAction.MOVE_N);
        controlsP4.put(KeyEvent.VK_DOWN, PlayerAction.MOVE_S);
        controlsP4.put(KeyEvent.VK_LEFT, PlayerAction.MOVE_W);
        controlsP4.put(KeyEvent.VK_RIGHT, PlayerAction.MOVE_E);
        controlsP4.put(KeyEvent.VK_SPACE, PlayerAction.DROP_BOMB);
        controlsP4.put(KeyEvent.VK_SHIFT, PlayerAction.STOP);
        controls.add(controlsP4);
        */
        return Collections.unmodifiableList(controls);
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

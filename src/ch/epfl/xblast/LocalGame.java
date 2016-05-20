package ch.epfl.xblast;

import java.lang.reflect.InvocationTargetException;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import ch.epfl.xblast.client.GameStateDeserializer;
import ch.epfl.xblast.client.KeyboardEventHandler;
import ch.epfl.xblast.client.XBlastComponent;
import ch.epfl.xblast.server.GameState;
import ch.epfl.xblast.server.GameStateSerializer;
import ch.epfl.xblast.server.Level;
import ch.epfl.xblast.server.Ticks;

public class LocalGame {
    
    /**
     * Constants
     */

    private static final UnaryOperator<Integer> ACTION_TO_DIR_ORDINAL = x -> x - 1;
    
    /*
     * Controls 
     */
    
    
    
    /**
     * Attributes
     */
    private static XBlastComponent xbc;
    private static Map<PlayerID,PlayerAction> actions = new HashMap<>();
    
    
    
    

    public static void main(String[] args) throws InvocationTargetException, InterruptedException {
        /*
         * Phase 1:
         *      Select the Level TODO
         */

        Level lvl = Level.TWO_PLAYER_LEVEL;
        //TODO
        
        GameState gameState =  lvl.initialGameState();
        long startingTime = System.nanoTime();
        SwingUtilities.invokeAndWait(() -> createUI());
        
        /*
         * Phase 2: begin the game.
         */
        while (!gameState.isGameOver()) {

            /*
             *  2.2) Serialize the current GameState and prepare buffer.
             */
            List<Byte> serialized = GameStateSerializer
                    .serialize(Level.DEFAULT_LEVEL.boardPainter(), gameState);

            /*
             * 2.2) Set the GameState
             */
            xbc.setGameState(GameStateDeserializer.deserializeGameState(serialized), PlayerID.PLAYER_1);

            /*
             * 2.3) Wait the correct amount of time, so that the tick duration is
             *      correct.
             */
            long timeForNextTick = startingTime
                    + ((long) gameState.ticks() + 1)
                    * ((long) Ticks.TICK_NANOSECOND_DURATION);
            long waitingTime = timeForNextTick - System.nanoTime();
            if (waitingTime > 0)
                Thread.sleep(waitingTime / Time.US_PER_S);

            /*
             * 2.4) Check if the clients sent an action they want to
             *      execute. This is done by receiving the messages until
             *      there are none left.
             */
            Map<PlayerID, Optional<Direction>> speedChangeEvents = new HashMap<>();
            Set<PlayerID> bombDrpEvent = new HashSet<>();

            for (Map.Entry<PlayerID,PlayerAction> e: actions.entrySet()) {
                
                PlayerAction action = e.getValue();
                PlayerID id = e.getKey();
                switch (action) {
                case DROP_BOMB:
                    bombDrpEvent.add(id);
                    break;
                case MOVE_S:
                case MOVE_N:
                case MOVE_W:
                case MOVE_E:
                    int dirOrdinal = ACTION_TO_DIR_ORDINAL
                    .apply(action.ordinal());
                    speedChangeEvents.put(id, Optional
                            .of(Direction.values()[dirOrdinal]));
                    break;
                case STOP:
                    speedChangeEvents.put(id, Optional.empty());
                default:
                    break;

                }
                
            }
            actions.clear();

            /*
             * 2.4) Evolve GameState to the next Tick.
             */
            gameState = gameState.next(speedChangeEvents, bombDrpEvent);
        }

        Optional<PlayerID> winner = gameState.winner();
        System.out.println(winner.isPresent() ? winner.get() : "There was no winner.");

        
    }
    
    
    
        
    /**
     * 
     */
    public static void createUI(){
            
            /*
             * Open new Window.
             */
            JFrame f = new JFrame("XBlast 2016");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            xbc = new XBlastComponent();
            
            f.getContentPane().add(xbc);
            f.setResizable(false);
            f.pack();
            f.setVisible(true);
            xbc.requestFocusInWindow();
    
            /*
             * Consumers
             */
            Consumer<PlayerAction> CONSP1 = (playerAction) ->  actions.put(PlayerID.PLAYER_1,playerAction);
            Consumer<PlayerAction> CONSP2 = (playerAction) ->actions.put(PlayerID.PLAYER_2,playerAction); 
            Consumer<PlayerAction> CONSP3 = (playerAction) ->actions.put(PlayerID.PLAYER_3,playerAction); 
            Consumer<PlayerAction> CONSP4 = (playerAction) ->actions.put(PlayerID.PLAYER_4,playerAction); 
            
            /*
             * keyListener
             */
            xbc.addKeyListener(new KeyboardEventHandler(KeyboardEventHandler.CONTROL_MAP_MULTI.get(0), CONSP1));
            xbc.addKeyListener(new KeyboardEventHandler(KeyboardEventHandler.CONTROL_MAP_MULTI.get(1), CONSP2));

        }

}

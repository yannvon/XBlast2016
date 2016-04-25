package ch.epfl.xblast.client;

import java.awt.Image;
import java.util.List;

import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.client.GameState.Player;

/**
 * Non-instanciable class offering static method to deserialize the GameSate to the client.
 * @author Loïc Vandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 *
 */
public final class GameStateDeserializer {

    /*
     * Constants
     */
    private static final int PLAYER_BYTES_LENGTH = PlayerID.values().length * 4; //FIXME utile?
    
    private GameStateDeserializer(){}
    
    public static GameState deserializeGameState(List<Byte> serialized){
        /*
         * Get Sublists Indices
         */
        int lastIndex = serialized.size() - 1;
        int boardDelimiter = serialized.get(0) + 1;
        int explosionDelimiter = serialized.get(boardDelimiter) + boardDelimiter + 1;
        
        /*
         * Deserialize Board
         */
        List<Image> deBoard = deserializeBoard(serialized.subList(1, boardDelimiter));
        
        /*
         * Deserialize explosions and bombs
         */
        List<Image> deExplosions = deserializedExplosions(serialized.subList(boardDelimiter + 1, explosionDelimiter));
        
        /*
         * Deserialize Players
         */
        List<Player> dePlayers = deserializedPlayers(serialized.subList(explosionDelimiter + 1, lastIndex));
        
        /*
         * Deserialize Time
         */
        
        
        
        return new GameState(dePlayers, deBoard, deExplosions, scores, time);
    }

    private static List<Player> deserializedPlayers(List<Byte> subList) {
        // TODO Auto-generated method stub
        return null;
    }

    private static List<Image> deserializedExplosions(List<Byte> subList) {
        return null;
    }

    private static List<Image> deserializeBoard(List<Byte> subList) {
        return null;
    }
    
}

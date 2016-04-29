package ch.epfl.xblast.client;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.RunLengthEncoder;
import ch.epfl.xblast.SubCell;
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

    private static final ImageCollection BLOCK_COLLECTION= new ImageCollection("block");
    private static final ImageCollection PLAYER_COLLECTION= new ImageCollection("player");
    private static final ImageCollection EXPLOSION_COLLECTION= new ImageCollection("player");
    private static final ImageCollection SCORE_COLLECTION= new ImageCollection("score");

    
    
    
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
         * Construct Score line
         */
        List<Image> scoreLine= constructScoreLine(dePlayers);
        
        
        /*
         * Construct Time line
         */
        List<Image> timeLine= constructTimeLine(serialized.get(lastIndex));
        
        
        return new GameState(dePlayers, deBoard, deExplosions, scoreLine, timeLine);
    }

    /**
     * @param encodedBoard
     * @return
     */
    private static List<Image> deserializeBoard(List<Byte> encodedBoard) {
        List<Byte> decodedBoard = RunLengthEncoder.decode(encodedBoard);
        
        Iterator<Byte> boardIterator = decodedBoard.iterator();
        Iterator<Cell> spiralIterator = Cell.SPIRAL_ORDER.iterator();
        Image[] boardRepresentation = new Image[Cell.COUNT];
        while(boardIterator.hasNext()){
            boardRepresentation[spiralIterator.next().rowMajorIndex()]=
                    BLOCK_COLLECTION.image(boardIterator.next());
        }
        return Arrays.asList(boardRepresentation);
    }

    
    /**
     * @param encodedExplosions
     * @return
     */
    private static List<Image> deserializedExplosions(List<Byte> encodedExplosions) {
        List<Byte> decodedExplosions = RunLengthEncoder.decode(encodedExplosions);
        List<Image> explosionsRepresentation = new ArrayList<>();
        
        for(Byte b: decodedExplosions)
            explosionsRepresentation.add(EXPLOSION_COLLECTION.imageOrNull(b));
        
        return explosionsRepresentation;
    }

    /**
     * @param encodedPlayers
     * @return
     */
    private static List<Player> deserializedPlayers(List<Byte> encodedPlayers) {
        List<Player> players = new ArrayList<>();
        Iterator<Byte> encIt= encodedPlayers.iterator();
        for(int i=0; i<4;i++){
            int lives =Byte.toUnsignedInt(encIt.next());
            SubCell pos = new SubCell(Byte.toUnsignedInt(encIt.next()),
                    Byte.toUnsignedInt(encIt.next()));
            Image image = PLAYER_COLLECTION.imageOrNull(encIt.next());
            players.add(new Player(PlayerID.values()[i],lives,pos,image));
        }
        return players;
    }

    /**
     * @param dePlayers
     * @return
     */
    private static List<Image> constructScoreLine(List<Player> dePlayers) {
        List<Image> scoreLine =new ArrayList<>();
        for(Player p: dePlayers){
            int id;
        }
        return null;
    }

    /**
     * @param time
     * @return
     */
    private static List<Image> constructTimeLine(Byte time) {
        return null;
    }
    
}

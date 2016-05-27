package ch.epfl.xblast.client;

import java.awt.Image;
import java.util.Optional;

import ch.epfl.xblast.client.GameState.Player;;

/**
 * Class similar to a the other Painters but situated directly inside the
 * client, since no additional information of the server is needed.
 * 
 * @author Loïc Vandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 *
 */
public class GameEndPainter {
    
    /*
     * Constants
     */
    // --- images for winner printing
    private static final int BYTE_NO_WINNER = 000;
    private static final int[] BYTES_WINNER = {1, 2, 3, 4};

    // --- ImageCollection
    private static final ImageCollection SCORE_COLLECTION = new ImageCollection(
            "end");

    /**
     * Used to display the correct image of the winning Player.
     * 
     * @param p winner
     * @return byte to display winner
     */
    public static Image imageForWinner(Optional<Player> p){
        if(!p.isPresent()){
            System.out.println("i was called");
            return SCORE_COLLECTION.imageOrNull(BYTE_NO_WINNER);
        }
        else {
            return SCORE_COLLECTION.image(BYTES_WINNER[p.get().id().ordinal()]);
        }
    }
}

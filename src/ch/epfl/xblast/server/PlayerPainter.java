package ch.epfl.xblast.server;


/**
 * This non-instanciable class offers static methods to "paint" a player.
 * 
 * @author Loïc Vandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 */
public final class PlayerPainter {

    /*
     * Constants
     */
    private static final int WHITE_PLAYER_ORDINAL = 4;
    private static final int BYTE_FOR_LOSING_LIFE = 12;
    private static final int BYTE_FOR_DYING = 13;
    private static final int BYTE_FOR_DEAD = 15;
    private static final int NB_IMAGES_PER_PLAYER = 20;
    private static final int NB_IMAGES_PER_DIRECTION = 3;
    private static final int[] WALKING_CYCLE = {0,1,0,2};

    /**
     *  Private Constructor: non instantiable class.
     */
    private PlayerPainter(){}

    /**
     * Returns the byte corresponding to the image representation of the player
     * at the current tick.
     * 
     * @param tick
     *            number of current tick
     * @param player
     *            player that has to be represented
     * @return byte corresponding to the current representation of the player
     */
    public static byte byteForPlayer(int tick, Player player) {
        byte imageByte = 0;
        boolean white = false;

        switch (player.lifeState().state()) {
        case INVULNERABLE:
            white = tick % 2 == 1;
        case VULNERABLE:
            imageByte += player.direction().ordinal() * NB_IMAGES_PER_DIRECTION;
            int pos = player.direction().isHorizontal() ? player.position().x()
                    : player.position().y();
            imageByte += WALKING_CYCLE[pos % WALKING_CYCLE.length];
            break;
        case DYING:
            imageByte += player.lives() == 1 ? BYTE_FOR_DYING
                    : BYTE_FOR_LOSING_LIFE;
            break;
        case DEAD:
            imageByte += BYTE_FOR_DEAD;
            break;
        default:
            throw new Error("This state has not been defined in PlayerPainter!");
        }
        
        imageByte += (white ? WHITE_PLAYER_ORDINAL : player.id().ordinal()) //FIXME
                * NB_IMAGES_PER_PLAYER;
        
        return imageByte;
    }
}

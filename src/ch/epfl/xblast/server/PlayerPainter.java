package ch.epfl.xblast.server;

import ch.epfl.xblast.server.Player.LifeState.State;

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
    private static final int WALKING_CYCLE_SIZE = 4;    //FIXME SubCell.SUBDIVISION /4;

    /**
     * Returns the byte corresponding to the image representation of the player at the current tick.
     * 
     * @param tick
     *            number of current tick
     * @param player
     *            player that has to be represented
     * @return byte corresponding to the current representation of the player
     */
    public static byte byteForPlayer(int tick, Player player) {
        byte image = 0;
        State pState = player.lifeState().state();
        // 1) Player
        boolean white = pState == State.INVULNERABLE
                && tick % 2 == 1;
        image += (white ? WHITE_PLAYER_ORDINAL : player.id().ordinal())
                * NB_IMAGES_PER_PLAYER;

        if (player.lifeState().canMove()) {
            // 2) Player Direction
            image += player.direction().ordinal() * NB_IMAGES_PER_DIRECTION;
            // 3) Player Position (walking animation)
            int pos = player.direction().isHorizontal() ? player.position().x()
                    : player.position().y();
            switch (pos % WALKING_CYCLE_SIZE) {
            case 1:
                image += 1;
                break;
            case 3:
                image += 2;
            }
        }
        // 4) Player Dying/ Losing Life
        else if (pState == State.DYING) {
            image += player.lives() == 1 ? BYTE_FOR_DYING
                    : BYTE_FOR_LOSING_LIFE;
        }
        // 5) Player Dead
        else if (pState == State.DEAD) {
            image += BYTE_FOR_DEAD;
        }
        return image;
    }

    private PlayerPainter(){}
}

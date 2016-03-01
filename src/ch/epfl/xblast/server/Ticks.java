package ch.epfl.xblast.server;

/**
 * Interface serving the purpose of defining multiple aspects related to time
 * management inside the game.
 * 
 * @author Loic Vandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 */
public interface Ticks {

    // Constants
    public static final int PLAYER_DYING_TICKS = 8;
    public static final int PLAYER_INVULNERABLE_TICKS = 64;
    public static final int BOMB_FUSE_TICKS = 100;
    public static final int EXPLOSION_TICKS = 30;
    public static final int WALL_CRUMBLING_TICKS = 30;
    public static final int BONUS_DISAPPEARING_TICKS = 30;

}

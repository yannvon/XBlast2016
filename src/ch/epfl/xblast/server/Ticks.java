package ch.epfl.xblast.server;

import ch.epfl.xblast.Time;

/**
 * Interface serving the purpose of defining multiple aspects related to time
 * management inside the game.
 * 
 * @author Loic Vandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 */
public interface Ticks {
    // Constants$
    
    public static final int PLAYER_DYING_TICKS = 8;
    public static final int PLAYER_INVULNERABLE_TICKS = 64;
    public static final int BOMB_FUSE_TICKS = 100;
    public static final int EXPLOSION_TICKS = 30;
    public static final int WALL_CRUMBLING_TICKS = 30;
    public static final int BONUS_DISAPPEARING_TICKS = 30;
    public static final int TICKS_PER_SECOND = 20;
    public static final int TICK_NANOSECOND_DURATION = Time.NS_PER_S / TICKS_PER_SECOND;
    public static final int TOTAL_TICKS = TICKS_PER_SECOND * Time.S_PER_MIN * 2;
    
    //BONUS
    public static final int SNAIL_DURATION_TICKS = 70;
    public static final int ROLLER_DURATION_TICKS = 150;
}

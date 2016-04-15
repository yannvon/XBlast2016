package ch.epfl.xblast.server;

/**
 * Enumeration that defines every bonus and gives the possibility to apply the
 * bonus to a player.
 * 
 * @author Loïc Vandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 *
 */
public enum Bonus {
    INC_BOMB {
        @Override
        public Player applyTo(Player player) {
            return (player.maxBombs() < MAX_BOMBS)
                    ? player.withMaxBombs(player.maxBombs() + 1) : player;
        }
    },

    INC_RANGE {
        @Override
        public Player applyTo(Player player) {
            return (player.bombRange() < MAX_RANGE)
                    ? player.withBombRange(player.bombRange() + 1) : player;
        }
    };
    
    // Constants
    private static final int MAX_BOMBS = 9;
    private static final int MAX_RANGE = 9;
    

    /**
     * Applies the bonus to a player
     * 
     * @param player
     *            that picked up a bonus
     * @return an upgraded player
     */
    abstract public Player applyTo(Player player);
}
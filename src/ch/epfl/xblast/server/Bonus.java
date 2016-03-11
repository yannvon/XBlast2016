package ch.epfl.xblast.server;

/**
 * Enumeration that defines every bonus and gives the possibility to "give" the
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
            if (player.maxBombs() < 9) {
                return player.withMaxBombs(player.maxBombs() + 1);
            } else {
                return player;
            }
        }
    },

    INC_RANGE {
        @Override
        public Player applyTo(Player player) {
            if (player.bombRange() < 9) {
                return player.withBombRange(player.bombRange() + 1);
            } else {
                return player;
            }
        }
    };

    abstract public Player applyTo(Player player);
}
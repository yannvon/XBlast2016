package ch.epfl.xblast.server;

/**
 * Enum of Blocks.
 * 
 * @author Loic Vandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 */
public enum Block {

    FREE, INDESTRUCTIBLE_WALL, DESTRUCTIBLE_WALL, CRUMBLING_WALL;

    /**
     * Determines if the Block is free.
     * 
     * @return true is the block is free, false otherwise
     */
    public boolean isFree() {
        return this == FREE;
    }

    /**
     * Defines if the Block can host a player.
     * 
     * @return true if the block can host a player, false otherwise
     */
    public boolean canHostPlayer() {
        // TODO
        return this == FREE;
    }

    /**
     * Defines if the Blocks casts a shadow.
     * @return true if the Block casts a shadow, false otherwise
     */
    public boolean castsShadow() {
        switch (this) {
        case INDESTRUCTIBLE_WALL:
        case DESTRUCTIBLE_WALL:
        case CRUMBLING_WALL:
            return true;
        default:
            return false;
        }
    }

}

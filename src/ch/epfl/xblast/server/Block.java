package ch.epfl.xblast.server;

import java.util.NoSuchElementException;

/**
 * Enumeration of Blocks.
 * 
 * @author Loïc Vandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 */
public enum Block {

    FREE, INDESTRUCTIBLE_WALL, DESTRUCTIBLE_WALL, CRUMBLING_WALL, BONUS_BOMB(
            Bonus.INC_BOMB), BONUS_RANGE(Bonus.INC_RANGE), BONUS_ROLLER(
                    Bonus.ROLLER), BONUS_SNAIL(Bonus.SNAIL), BONUS_KICKBOMB(
                            Bonus.KICK_BOMB);

    // Attributes
    private Bonus maybeAssociatedBonus; // corresponding bonus or null

    /**
     * Primary constructor, used by the Bonus Blocks.
     * 
     * @param maybeAssociatedBonus
     *            Bonus associated to block
     */
    private Block(Bonus maybeAssociatedBonus) {
        // storing the associated bonus
        this.maybeAssociatedBonus = maybeAssociatedBonus;
    }

    /**
     * Default constructor, used by all other blocks.
     */
    private Block() {
        // this block has no associated bonus
        this.maybeAssociatedBonus = null;
    }

    /**
     * Determines if the Block is free.
     * 
     * @return true if the block is free, false otherwise
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
        return isFree() || isBonus();
    }

    /**
     * Defines if the Blocks casts a shadow.
     * 
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

    /**
     * Checks if Block is a bonus or not.
     * 
     * @return true if it is a bonus, false otherwise
     */
    public boolean isBonus() {
        switch (this) {
        case BONUS_BOMB:
        case BONUS_RANGE:
        case BONUS_SNAIL:
        case BONUS_ROLLER:
        case BONUS_KICKBOMB:
            return true;
        default:
            return false;
        }
    }

    /**
     * Returns the bonus associated with this Block or throws an exception if
     * there is no bonus associated.
     * 
     * @return the bonus associated to the block
     * @throws NoSuchElementException
     *             if the block has no associated bonus
     */
    public Bonus associatedBonus() {
        if (maybeAssociatedBonus == null) {
            throw new NoSuchElementException(
                    "This Block does not contain a bonus");
        }
        return maybeAssociatedBonus;
    }
}
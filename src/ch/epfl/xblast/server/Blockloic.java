package ch.epfl.xblast.server;

import java.util.NoSuchElementException;

/**
 * Enum of Blocks.
 * 
 * @author Loic Vandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 */
public enum Blockloic {

    FREE, INDESTRUCTIBLE_WALL, DESTRUCTIBLE_WALL, CRUMBLING_WALL, BONUS_BOMB(Bonus.INC_BOMB), BONUS_RANGE(Bonus.INC_RANGE);
 
    
    private Bonus associatedBonus;

    
    private Blockloic(Bonus associatedBonus) {
      this.associatedBonus=associatedBonus;
    }

    
    private Blockloic() {
      this.associatedBonus=null;
    }
    
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
        switch (this) {
        case FREE:
        case BONUS_RANGE:
        case BONUS_BOMB:
            return true;
        default:
            return false;
        }
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
    
    /**
     * Defines if the Blocks is a bonus
     * @return true if the block is a Bonus
     */
    public boolean isBonus(){
        switch (this) {
        case BONUS_RANGE:
        case BONUS_BOMB:
            return true;
        default:
            return false;
        }
    }
    
    
    /**
     * @return Bonus associate to the block
     */
    public Bonus associatedBonus(){
        if(associatedBonus==null){
            throw new NoSuchElementException("there is no Bonus associated to this block");
        }
        return associatedBonus;
    }
}

package ch.epfl.xblast.server;

public enum Block {
    
    FREE,
    INDESTRUCTIBLE_WALL,
    DESTRUCTIBLE_WALL,
    CRUMBLING_WALL;
    
    public boolean isFree(){
        return this==FREE;
    }
    
    public boolean canHostPlayer(){
        //TODO
        return this==FREE;
    }
    
    public boolean castsShadow(){
        switch(this){
        case INDESTRUCTIBLE_WALL:
        case DESTRUCTIBLE_WALL:
        case CRUMBLING_WALL:
            return true;
        default:
            return false;
        }
    }
    
}

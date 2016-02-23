//-------Project:XBlast
//-------Name:LoicVandenberghe

//------------------------------------
package ch.epfl.xblast;


public enum Direction {
    N,E,S,W;
    
    //retourne l'opposé
    public Direction opposite(){
        switch(this){
        case N:
            return S;
        case S:
            return N;
        case E:
            return W;
        case W:
            return E;
         default:
             throw new Error("quel est cette direction?");
        }    
    }
    
    //retourne true si la direction est horizontale
    public boolean isHorizontal(){
        switch(this){
        case E:
        case W:
            return true;
        case S:
        case N:
            return false;
        default:
           throw new Error("quel est cette direction?");
        }
    }
    
    //retourne true si la direction est parallèle à celle donné
    public boolean isParallelTo(Direction that){
        return this==that || this==that.opposite();
    }
}

package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.cs108.Sq;

/**
 * @author Loic Vandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 *
 */
public final class Board {

   private List<Sq<Block>> board;
       
    /**
     * constructor
     * @param blocks
     * @throws IllegalArgumentException if the size of the List is not correct
     */
    public Board(List<Sq<Block>> blocks){
        if(board.size() != 195){
            throw new IllegalArgumentException();
        }
        
        board=new ArrayList<>(blocks);
       
    }
    
    public static final Board ofRows(List<List<Block>> rows){
        //TODO
        for(int i=0; i<rows.size(); ){
            
        }
        
        return null;
    }
    
    public static final void checkBlockMatrix(List<List<Block>> matrix, 
            int rows, int columns) {
        //TODO
        
        
    }
}

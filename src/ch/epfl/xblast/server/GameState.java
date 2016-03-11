package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.ArgumentChecker;
import ch.epfl.xblast.Cell;

public final class GameState {

    //ATRIBUTES
    private int ticks;
    private Board board;
    private List<Player> players;
    private List<Bomb> bombs;
    private List<Sq<Sq<Cell>>> explosions;
    private List<Sq<Cell>> blasts;
    
    
    /**
     * Principal constructor
     * 
     * @param ticks
     * @param board
     * @param players
     * @param bombs
     * @param explosion
     * @param blasts
     */
    public GameState(int ticks, Board board, List<Player> players,
            List<Bomb> bombs, List<Sq<Sq<Cell>>> explosion,
            List<Sq<Cell>> blasts) {

        this.ticks=ArgumentChecker.requireNonNegative(ticks);
        int nbPlayers=players.size();
        if(nbPlayers!=4){
            throw new IllegalArgumentException("the Game requires 4 players instead of "+nbPlayers);
        }
        this.board= Objects.requireNonNull(board);
        this.players=Objects.requireNonNull(players);
        this.bombs=Objects.requireNonNull(bombs);
        this.blasts=Objects.requireNonNull(blasts);
    }
    
    public GameState(Board board, List<Player> players){
        this(0,
                board,
                players,
                new ArrayList<Bomb>(),        //
                new ArrayList<Sq<Sq<Cell>>>(),//FIXME  Array or Linked
                new ArrayList<Sq<Cell>>());   //
    }
}

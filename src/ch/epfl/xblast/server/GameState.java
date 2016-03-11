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
     * 
     * @throws IllegalArgumentException
     *             if the number of players is not 4 or if the ticks is negative.
     * @throws NullPointerException
     *              if one object is null.
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
    
    /**
     * Initial constructor:
     *      create a game with initials values:
     *          ticks:0
     *          empty List of Bomb, explosion and blast
     * 
     * @param board
     * @param players
     * 
     * @throws IllegalArgumentException
     *             if the number of players is not 4 or if the ticks is negative.
     */
    public GameState(Board board, List<Player> players){
        this(0,
                board,
                players,
                new ArrayList<Bomb>(),        //
                new ArrayList<Sq<Sq<Cell>>>(),//FIXME  Array or Linked
                new ArrayList<Sq<Cell>>());   //
    }
    
    
    /**
     * Getter of ticks
     * @return actual ticks
     */
    public int ticks() {
        return ticks;
    }
    
    /**
     * @return
     */
    public boolean isGameOver(){
        if(ticks>=Ticks.TOTAL_TICKS){
            return true;
        }
        boolean gameOver=true;
        players.forEach(player -> {
            gameOver &= !player.isAlive();
        });
        return gameOver;
    }
    
}

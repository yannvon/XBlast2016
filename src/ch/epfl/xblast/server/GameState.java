package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.ArgumentChecker;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;

public final class GameState {

    //ATRIBUTES
    private final int ticks;
    private final Board board;
    private final List<Player> players;
    private final List<Bomb> bombs;
    private final List<Sq<Sq<Cell>>> explosions;
    private final List<Sq<Cell>> blasts;
    
    
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
       
        this.players = Collections.unmodifiableList(
                new ArrayList<>(Objects.requireNonNull(players)));
        this.explosions = Collections.unmodifiableList(
                new ArrayList<>(Objects.requireNonNull(explosion)));
        this.bombs = Collections.unmodifiableList(
                new ArrayList<>(Objects.requireNonNull(bombs)));
        this.blasts = Collections.unmodifiableList(
                new ArrayList<>(Objects.requireNonNull(blasts)));
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
        return alivePlayers().size()==0;
        
    }
    
    /**
     * @return
     */
    public double remainingTime(){
        return ((double)Ticks.TOTAL_TICKS-ticks)/Ticks.TICKS_PER_SECOND;
    }
    
    /**
     * @return
     */
    public Optional<PlayerID> winner(){
        List<Player> alivePlayers=alivePlayers();
        if(alivePlayers.size()==1){
            return Optional.of(alivePlayers.get(0).id());
        }
        return Optional.empty();
    }
    
    
    
    /**
     * @return
     */
    public Board board() {
        return board;
    }

    
    /**
     * @return
     */
    public List<Player> getPlayers() {
        return players;
    }

    
    /**
     * @return
     */
    public List<Player> alivePlayers(){
        List<Player> alivePlayers=new ArrayList<>();
        for(Player p : players){
            if(p.isAlive()){
                alivePlayers.add(p);
            }
        }
        return alivePlayers;
    }
}

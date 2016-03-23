package ch.epfl.DONT_SUBMIT;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import org.junit.Test;

import ch.epfl.xblast.server.Block;
import ch.epfl.xblast.server.Board;
import ch.epfl.xblast.server.Bomb;
import ch.epfl.xblast.server.GameState;
import ch.epfl.xblast.server.Player;
import ch.epfl.xblast.server.debug.GameStatePrinter;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.Cell;
import ch.epfl.cs108.Sq;
import ch.epfl.xblast.PlayerID;

public class GamestateTest {

    @Test
    public void giveOnlyTheAlivePlayer() {
        List<Player> p = new ArrayList<>();

        p.add(new Player(PlayerID.PLAYER_1, 0, new Cell(1, 1), 1, 1));
        p.add(new Player(PlayerID.PLAYER_2, 0, new Cell(1, 1), 1, 1));
        p.add(new Player(PlayerID.PLAYER_3, 0, new Cell(1, 1), 1, 1));
        p.add(new Player(PlayerID.PLAYER_4, 1, new Cell(1, 1), 1, 1));
        List<Sq<Block>> b = new ArrayList<>();
        for (int i = 0; i < 195; ++i) {
            b.add(Sq.constant(Block.CRUMBLING_WALL));
        }
        GameState g = new GameState(new Board(b), p);
        assertEquals(PlayerID.PLAYER_4, g.alivePlayers().get(0).id());
        assertEquals(1, g.alivePlayers().size());
    }

    @Test
    public void giveTheWinnerIfThereIsOne() {
        List<Player> p = new ArrayList<>();

        p.add(new Player(PlayerID.PLAYER_1, 0, new Cell(1, 1), 1, 1));
        p.add(new Player(PlayerID.PLAYER_2, 0, new Cell(1, 1), 1, 1));
        p.add(new Player(PlayerID.PLAYER_3, 0, new Cell(1, 1), 1, 1));
        p.add(new Player(PlayerID.PLAYER_4, 1, new Cell(1, 1), 1, 1));
        List<Sq<Block>> b = new ArrayList<>();
        for (int i = 0; i < 195; ++i) {
            b.add(Sq.constant(Block.CRUMBLING_WALL));
        }
        GameState g = new GameState(new Board(b), p);
        assertEquals(Optional.of(PlayerID.PLAYER_4), g.winner());
    }

    @Test
    public void noWinnerIfThereShouldntBeOne() {
        List<Player> p = new ArrayList<>();

        p.add(new Player(PlayerID.PLAYER_1, 0, new Cell(1, 1), 1, 1));
        p.add(new Player(PlayerID.PLAYER_2, 0, new Cell(1, 1), 1, 1));
        p.add(new Player(PlayerID.PLAYER_3, 1, new Cell(1, 2), 1, 1));
        p.add(new Player(PlayerID.PLAYER_4, 1, new Cell(1, 1), 1, 1));
        List<Sq<Block>> b = new ArrayList<>();
        for (int i = 0; i < 195; ++i) {
            b.add(Sq.constant(Block.CRUMBLING_WALL));
        }
        GameState g = new GameState(new Board(b), p);
        assertEquals(Optional.empty(), g.winner());
        List<Player> t = new ArrayList<>();

        t.add(new Player(PlayerID.PLAYER_1, 0, new Cell(1, 1), 1, 1));
        t.add(new Player(PlayerID.PLAYER_2, 0, new Cell(1, 1), 1, 1));
        t.add(new Player(PlayerID.PLAYER_3, 0, new Cell(1, 2), 1, 1));
        t.add(new Player(PlayerID.PLAYER_4, 0, new Cell(1, 1), 1, 1));
        GameState u = new GameState(new Board(b), t);
        assertEquals(Optional.empty(), u.winner());
    }

    @Test
    public void isGameOverWorksIfNoOrOnePlayerLeft() {

        List<Player> t = new ArrayList<>();

        t.add(new Player(PlayerID.PLAYER_1, 0, new Cell(1, 1), 1, 1));
        t.add(new Player(PlayerID.PLAYER_2, 0, new Cell(1, 1), 1, 1));
        t.add(new Player(PlayerID.PLAYER_3, 0, new Cell(1, 2), 1, 1));
        t.add(new Player(PlayerID.PLAYER_4, 0, new Cell(1, 1), 1, 1));

        List<Sq<Block>> b = new ArrayList<>();
        for (int i = 0; i < 195; ++i) {
            b.add(Sq.constant(Block.CRUMBLING_WALL));
        }
        GameState u = new GameState(new Board(b), t);
        assertTrue(u.isGameOver());

        List<Player> p = new ArrayList<>();

        p.add(new Player(PlayerID.PLAYER_1, 0, new Cell(1, 1), 1, 1));
        p.add(new Player(PlayerID.PLAYER_2, 0, new Cell(1, 1), 1, 1));
        p.add(new Player(PlayerID.PLAYER_3, 0, new Cell(1, 2), 1, 1));
        p.add(new Player(PlayerID.PLAYER_4, 1, new Cell(1, 1), 1, 1));
        GameState g = new GameState(new Board(b), p);
        assertTrue(g.isGameOver());
    }

    @Test
    public void bonusDisappearCorrectly() {
        Block __ = Block.FREE;
        Block XX = Block.INDESTRUCTIBLE_WALL;
        Block xx = Block.DESTRUCTIBLE_WALL;
        Block b = Block.BONUS_BOMB;
        Block d = Block.BONUS_RANGE;
        Board board = Board.ofQuadrantNWBlocksWalled(
                Arrays.asList(Arrays.asList(__, __, __, __, __, xx, __),
                        Arrays.asList(__, XX, xx, XX, xx, XX, xx),
                        Arrays.asList(__, xx, b, __, __, xx, __),
                        Arrays.asList(xx, XX, d, XX, XX, XX, XX),
                        Arrays.asList(__, xx, __, xx, __, __, __),
                        Arrays.asList(xx, XX, xx, XX, xx, XX, __)));
        Cell cellForBomb = new Cell(3, 5);
        List<Bomb> bombList = new ArrayList<>();
        bombList.add(new Bomb(PlayerID.PLAYER_1, cellForBomb, 2, 3));
        List<Sq<Sq<Cell>>> explosions = new ArrayList<>();
        List<Sq<Cell>> blasts = new ArrayList<>();
        List<Player> p = new ArrayList<>();
        p.add(new Player(PlayerID.PLAYER_1, 9, new Cell(1, 4), 4, 4));
        p.add(new Player(PlayerID.PLAYER_2, 8, new Cell(13, 1), 4, 4));
        p.add(new Player(PlayerID.PLAYER_3, 7, new Cell(1, 11), 4, 4));
        p.add(new Player(PlayerID.PLAYER_4, 6, new Cell(13, 11), 4, 4));
        Map<PlayerID,Optional<Direction>> m = new HashMap<>();
        for(PlayerID pid: PlayerID.values()){
            m.put(pid, Optional.empty());
        }
        Set<PlayerID> bombDropEvents = new HashSet<>();
        
        GameState g = new GameState(0,board,p,bombList,explosions,blasts);
        for(int i = 0;i<34;++i){
            g = g.next(m, bombDropEvents);
            System.out.println(g.blastedCells());
        }
        
        assertTrue(g.board().blockAt(new Cell(3,4)).isFree());
        g = g.next(m, bombDropEvents);
        g = g.next(m, bombDropEvents);
        assertTrue(!(g.board().blockAt(new Cell(3,3)).isFree()));
        

    }

}

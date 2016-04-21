package ch.epfl.xblast.personal.part2;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.SubCell;
import ch.epfl.xblast.server.Player;
import ch.epfl.xblast.server.Player.DirectedPosition;
import ch.epfl.xblast.server.Player.LifeState;
import ch.epfl.xblast.server.Player.LifeState.State;
import ch.epfl.xblast.server.PlayerPainter;

public class PlayerPainterTest {
     
    private Sq<LifeState>  dying= Sq.constant(new LifeState(1, State.DYING));
    private Sq<LifeState>  losingLife= Sq.constant(new LifeState(2, State.DYING));
    private Sq<LifeState>  vulnerable= Sq.constant(new LifeState(1, State.VULNERABLE));
    private Sq<LifeState>  invulnerable= Sq.constant(new LifeState(1, State.INVULNERABLE));
    private Sq<LifeState>  dead= Sq.constant(new LifeState(1, State.DEAD));
    
    private SubCell central = SubCell.centralSubCellOf(new Cell(1,1));
    private SubCell x1y0 = new SubCell(1, 0);
    private SubCell x0y1 = new SubCell(1, 0);
    private Sq<DirectedPosition> centralNorth= DirectedPosition.stopped(new DirectedPosition(central, Direction.N));
    private Sq<DirectedPosition> centralEast= DirectedPosition.stopped(new DirectedPosition(central, Direction.E));
    private Sq<DirectedPosition> x1y0North= DirectedPosition.stopped(new DirectedPosition(x1y0, Direction.N));//impossible
    private Sq<DirectedPosition> x1y0East= DirectedPosition.stopped(new DirectedPosition(x1y0, Direction.E));
    private Sq<DirectedPosition> x0y1North= DirectedPosition.stopped(new DirectedPosition(x0y1, Direction.N));
    private Sq<DirectedPosition> x0y1East= DirectedPosition.stopped(new DirectedPosition(x0y1, Direction.E));//impossible
    
    @Test
    public void byteForPlayerTest(){
        List<Player> players =Arrays.asList(
                new Player(PlayerID.PLAYER_1, vulnerable, centralNorth, 0, 0), //0----
                new Player(PlayerID.PLAYER_1, vulnerable, centralEast, 0, 0),
                new Player(PlayerID.PLAYER_1, vulnerable,x1y0North , 0, 0),
                new Player(PlayerID.PLAYER_1, vulnerable,x1y0East , 0, 0),
                new Player(PlayerID.PLAYER_1, vulnerable, x0y1North, 0, 0),
                new Player(PlayerID.PLAYER_1, vulnerable, x0y1East, 0, 0),     //5---

                new Player(PlayerID.PLAYER_1, invulnerable, centralNorth, 0, 0),  //6---
                new Player(PlayerID.PLAYER_1, invulnerable, centralEast, 0, 0),
                new Player(PlayerID.PLAYER_1, invulnerable,x1y0North , 0, 0),
                new Player(PlayerID.PLAYER_1, invulnerable,x1y0East , 0, 0),
                new Player(PlayerID.PLAYER_1, invulnerable, x0y1North, 0, 0),
                new Player(PlayerID.PLAYER_1, invulnerable, x0y1East, 0, 0),  //11---

                new Player(PlayerID.PLAYER_1, losingLife, centralNorth, 0, 0), //12---
                new Player(PlayerID.PLAYER_1, losingLife, centralEast, 0, 0),
                new Player(PlayerID.PLAYER_1, losingLife,x1y0North , 0, 0),
                new Player(PlayerID.PLAYER_1, losingLife,x1y0East , 0, 0),
                new Player(PlayerID.PLAYER_1, losingLife, x0y1North, 0, 0),
                new Player(PlayerID.PLAYER_1, losingLife, x0y1East, 0, 0),  //17---

                new Player(PlayerID.PLAYER_1, dying, centralNorth, 0, 0),  //18---
                new Player(PlayerID.PLAYER_1, dying, centralEast, 0, 0),
                new Player(PlayerID.PLAYER_1, dying,x1y0North , 0, 0),
                new Player(PlayerID.PLAYER_1, dying,x1y0East , 0, 0),
                new Player(PlayerID.PLAYER_1, dying, x0y1North, 0, 0),
                new Player(PlayerID.PLAYER_1, dying, x0y1East, 0, 0),   //23---

                new Player(PlayerID.PLAYER_1, dead, centralNorth, 0, 0), //24---
                new Player(PlayerID.PLAYER_1, dead, centralEast, 0, 0),
                new Player(PlayerID.PLAYER_1, dead,x1y0North , 0, 0),
                new Player(PlayerID.PLAYER_1, dead,x1y0East , 0, 0),
                new Player(PlayerID.PLAYER_1, dead, x0y1North, 0, 0),
                new Player(PlayerID.PLAYER_1, dead, x0y1East, 0, 0));  //29---
                
        
        
        //DEAD Player
        
        for(int i=24; i<30;i++){
            assertTrue(PlayerPainter.byteForPlayer(i, players.get(i))%20 >13);
        }
        
        //normalPlayer
        
        assertEquals(000, PlayerPainter.byteForPlayer(23,players.get(0)));
        //TODO
                
    }

}

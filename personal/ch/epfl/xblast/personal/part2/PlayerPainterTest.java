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

/**
 * This non-instanciable class offers static methods to "paint" a player.
 * 
 * @author Loïc Vandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 */
public class PlayerPainterTest {
     
    private Sq<LifeState>  dying= Sq.constant(new LifeState(1, State.DYING));
    private Sq<LifeState>  losingLife= Sq.constant(new LifeState(2, State.DYING));
    private Sq<LifeState>  vulnerable= Sq.constant(new LifeState(1, State.VULNERABLE));
    private Sq<LifeState>  invulnerable= Sq.constant(new LifeState(1, State.INVULNERABLE));
    private Sq<LifeState>  dead= Sq.constant(new LifeState(1, State.DEAD));
    
    private SubCell central = SubCell.centralSubCellOf(new Cell(1,1));
    private SubCell x1y0 = new SubCell(1, 0);
    private SubCell x0y1 = new SubCell(0, 1);
    private Sq<DirectedPosition> centralNorth= DirectedPosition.stopped(new DirectedPosition(central, Direction.N));
    private Sq<DirectedPosition> centralEast= DirectedPosition.stopped(new DirectedPosition(central, Direction.E));
    private Sq<DirectedPosition> x1y0North= DirectedPosition.stopped(new DirectedPosition(x1y0, Direction.N));//impossible
    private Sq<DirectedPosition> x1y0East= DirectedPosition.stopped(new DirectedPosition(x1y0, Direction.E));
    private Sq<DirectedPosition> x0y1North= DirectedPosition.stopped(new DirectedPosition(x0y1, Direction.N));
    private Sq<DirectedPosition> x0y1East= DirectedPosition.stopped(new DirectedPosition(x0y1, Direction.E));//impossible
    
    @Test
    public void byteForPlayerTest(){
        List<Player> players =Arrays.asList(
                new Player(PlayerID.PLAYER_2, vulnerable, centralNorth, 0, 0), //0----
                new Player(PlayerID.PLAYER_2, vulnerable, centralEast, 0, 0),
                new Player(PlayerID.PLAYER_2, vulnerable,x1y0North , 0, 0),
                new Player(PlayerID.PLAYER_2, vulnerable,x1y0East , 0, 0),
                new Player(PlayerID.PLAYER_2, vulnerable, x0y1North, 0, 0),
                new Player(PlayerID.PLAYER_2, vulnerable, x0y1East, 0, 0),     //5---

                new Player(PlayerID.PLAYER_2, invulnerable, centralNorth, 0, 0),  //6---
                new Player(PlayerID.PLAYER_2, invulnerable, centralEast, 0, 0),
                new Player(PlayerID.PLAYER_2, invulnerable,x1y0East , 0, 0),
                new Player(PlayerID.PLAYER_2, invulnerable, x0y1North, 0, 0), //9---

                new Player(PlayerID.PLAYER_2, losingLife, centralNorth, 0, 0), //10---
                new Player(PlayerID.PLAYER_2, losingLife, centralEast, 0, 0),
                new Player(PlayerID.PLAYER_2, losingLife,x1y0North , 0, 0),
                new Player(PlayerID.PLAYER_2, losingLife,x1y0East , 0, 0),
                new Player(PlayerID.PLAYER_2, losingLife, x0y1North, 0, 0),
                new Player(PlayerID.PLAYER_2, losingLife, x0y1East, 0, 0),  //15---

                new Player(PlayerID.PLAYER_2, dying, centralNorth, 0, 0),  //16---
                new Player(PlayerID.PLAYER_2, dying, centralEast, 0, 0),
                new Player(PlayerID.PLAYER_2, dying,x1y0North , 0, 0),
                new Player(PlayerID.PLAYER_2, dying,x1y0East , 0, 0),
                new Player(PlayerID.PLAYER_2, dying, x0y1North, 0, 0),
                new Player(PlayerID.PLAYER_2, dying, x0y1East, 0, 0),   //21---

                new Player(PlayerID.PLAYER_2, dead, centralNorth, 0, 0), //22---
                new Player(PlayerID.PLAYER_2, dead, centralEast, 0, 0),
                new Player(PlayerID.PLAYER_2, dead,x1y0North , 0, 0),
                new Player(PlayerID.PLAYER_2, dead,x1y0East , 0, 0),
                new Player(PlayerID.PLAYER_2, dead, x0y1North, 0, 0),
                new Player(PlayerID.PLAYER_2, dead, x0y1East, 0, 0));  //27---
                
        
        
        //DEAD Player
        
        for(int i=22; i<players.size();i++){
            assertTrue(PlayerPainter.byteForPlayer(i, players.get(i))%20 >13);
        }
        
        //normalPlayer
        int p= 1 *20; //à changer lorsque l'on change de player
        
        //vulnerable
        assertEquals(000+p, PlayerPainter.byteForPlayer(23,players.get(0)));
        assertEquals(003+p, PlayerPainter.byteForPlayer(23,players.get(1)));
        assertEquals(000+p, PlayerPainter.byteForPlayer(23,players.get(2)));
        assertEquals(004+p, PlayerPainter.byteForPlayer(23,players.get(3)));
        assertEquals(001+p, PlayerPainter.byteForPlayer(23,players.get(4)));
        assertEquals(003+p, PlayerPainter.byteForPlayer(23,players.get(5)));
        
        //invulnerable
        assertEquals(80, PlayerPainter.byteForPlayer(23,players.get(6)));
        assertEquals(000+p, PlayerPainter.byteForPlayer(22,players.get(6)));
        assertEquals(83, PlayerPainter.byteForPlayer(23,players.get(7)));
        assertEquals(003+p, PlayerPainter.byteForPlayer(22,players.get(7)));
        assertEquals(84, PlayerPainter.byteForPlayer(23,players.get(8)));
        assertEquals(4+p, PlayerPainter.byteForPlayer(22,players.get(8)));
        assertEquals(81, PlayerPainter.byteForPlayer(23,players.get(9)));
        assertEquals(1+p, PlayerPainter.byteForPlayer(22,players.get(9)));
        
        
        //losinglife
        for(int i=10; i<16;i++){

            assertEquals(12+p, PlayerPainter.byteForPlayer(23,players.get(i)));
            
        }
        
        //dying
        for(int i=16; i<22;i++){

            assertEquals(13+p, PlayerPainter.byteForPlayer(23,players.get(i)));
            
        }
        
        
                
    }

}

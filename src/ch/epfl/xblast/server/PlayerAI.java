package ch.epfl.xblast.server;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerAction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.server.Player.DirectedPosition;

/**
 * Artificial Intelligence for XBlast 2016.
 * 
 * @author Loïc Vandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 *
 */
public class PlayerAI {
    private PlayerAI(){}
    
    public static Optional<PlayerAction> nextMove(GameState gameState, PlayerID id){
       Set<Cell> mostDangerousCell = gameState.blastedCells();
       
       Set<Cell> secondDangerousCell = new HashSet<>();
       for(Bomb b: gameState.bombedCells().values()){
           for(Sq<Sq<Cell>> ex : b.explosion()){
               Sq<Cell> arm =ex.head();
               while(!arm.isEmpty()){
                   secondDangerousCell.add(arm.head());
                   arm= arm.tail();
               }
           }
       }
       
       Player IaPlayer= null;
       for(Player p: gameState.players()){
           if(p.id()==id)
               IaPlayer = p;
       }
       
       Sq<DirectedPosition> futurPosition = IaPlayer.directedPositions();
       Cell nextCell = futurPosition.findFirst(p->p.position().isCentral()).position().containingCell();
       
       
       
        return null;
    }

}

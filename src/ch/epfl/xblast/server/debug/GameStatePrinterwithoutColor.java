package ch.epfl.xblast.server.debug;

import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.server.Block;
import ch.epfl.xblast.server.Board;
import ch.epfl.xblast.server.Bomb;
import ch.epfl.xblast.server.GameState;
import ch.epfl.xblast.server.Player;

public final class GameStatePrinterwithoutColor {
    // constants

    
    private GameStatePrinterwithoutColor() {}

    public static void printGameState(GameState s) {
        List<Player> ps = s.alivePlayers();
        Board board = s.board();
        Map<Cell,Bomb> bombs= s.bombedCells();
        Set<Cell> blasts= s.blastedCells();
        StringBuilder toPrint = new StringBuilder();
        toPrint.append("\n");

        for (int y = 0; y < Cell.ROWS; ++y) {
            // 1) print game board
            xLoop: for (int x = 0; x < Cell.COLUMNS; ++x) {
                Cell c = new Cell(x, y);
                
                // --- PRIORITY ORDER ---
                // players
                for (Player p: ps) {
                    if (p.position().containingCell().equals(c)) {
                        toPrint.append(stringForPlayer(p));
                        continue xLoop;
                    }
                }
                
                // bombs
                if (bombs.containsKey(c)){
                    toPrint.append("@@");
                    continue xLoop;
                }
                

                Block b = board.blockAt(c);
                
                // blasts
                if (blasts.contains(c) && b.isFree()){
                    toPrint.append("~~");
                    continue xLoop;
                }
                // blocks
                toPrint.append(stringForBlock(b));
                
            }
            toPrint.append("\n");

        }

        toPrint.append("\n\n");
        
        // 2) print additional player and game info
        for(Player p : ps){
            toPrint.append("P" + (p.id().ordinal()+1) + " : " + p.lives()  + " lives " + p.lifeState().state() );
            toPrint.append("    max bombs: " + p.maxBombs() + " range: "  + p.bombRange());
            toPrint.append("    position: " + p.position().containingCell() + stringForDistToCentral(p) );

            toPrint.append("\n");
        }
        if(s.isGameOver()){
            toPrint.append("GAME OVER : ");
            toPrint.append(" winner: "+s.winner());
        }
        
        toPrint.append("Remaining Time: " + String.format("%.1f", s.remainingTime()) );

        toPrint.append("\n");
        toPrint.append("TICK_NUMBER: " + s.ticks());
        toPrint.append("\n");

        System.out.println(toPrint.toString());
    }

    private static String stringForPlayer(Player p) {
        StringBuilder b = new StringBuilder();
        b.append(p.id().ordinal() + 1);
        switch (p.direction()) {
        case N: b.append('^'); break;
        case E: b.append('>'); break;
        case S: b.append('v'); break;
        case W: b.append('<'); break;
        }
        return b.toString();
    }

    private static String stringForBlock(Block b) {
        switch (b) {
        case FREE: return "  ";
        case INDESTRUCTIBLE_WALL: return "##" ;
        case DESTRUCTIBLE_WALL: return "??";
        case CRUMBLING_WALL: return "¿¿";
        case BONUS_BOMB: return"+b";
        case BONUS_RANGE: return "+r" ;
        default: throw new Error();
        }
    }
    
    private static String stringForDistToCentral(Player p){
        StringBuilder str = new StringBuilder();
        str.append("centralDist: ");
        switch (p.position().distanceToCentral()) {
        case 0:
            str.append(0);
            break;
        case 1:
            str.append(1);
            break;
        case 2:
            str.append(12);
            break;
        case 3:
            str.append(123);
            break;
        case 4:
            str.append(1234);
            break;
        case 5:
            str.append(12345);
            break;
        case 6:
            str.append(123456);
            break;
        case 7:
            str.append(1234567);
            break;
        case 8:
            str.append(12345678);
            break;
        }
        return str.toString();
    }
}

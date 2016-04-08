package ch.epfl.xblast.server.debug;

import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.server.Block;
import ch.epfl.xblast.server.Board;
import ch.epfl.xblast.server.Bomb;
import ch.epfl.xblast.server.GameState;
import ch.epfl.xblast.server.GameState;
import ch.epfl.xblast.server.Player;

public final class GameStatePrinter {
    // constants
    private static final String red = "\u001b[31m";
    private static final String yellow = "\u001b[33m";
    private static final String green = "\u001b[32m";
    private static final String blue = "\u001b[34m";
    private static final String black = "\u001b[30m";
    private static final String magenta = "\u001b[35m";
    private static final String cyan = "\u001b[36m";
    private static final String white = "\u001b[37m";
    private static final String framed = "\u001b[51m";
    private static final String std = "\u001b[0m";
    private static final String bBlack = "\u001b[40m";
    private static final String bGreen = "\u001b[42m";
    private static final String bCyan = "\u001b[46m";
    private static final String bWhite = "\u001b[47m";
    private static final String bBlue = "\u001b[44m";
    private static final String bRed = "\u001b[41m";
    private static final String nl = "\n";
    
    private GameStatePrinter() {}

    public static void printGameState(GameState s) {
        List<Player> ps = s.alivePlayers();
        Board board = s.board();
        Map<Cell,Bomb> bombs= s.bombedCells();
        Set<Cell> blasts= s.blastedCells();
        StringBuilder toPrint = new StringBuilder();

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
                    toPrint.append(red + "@@" + std);
                    continue xLoop;
                }
                

                Block b = board.blockAt(c);
                
                // blasts
                if (blasts.contains(c) && b.isFree()){
                    toPrint.append(bRed + "~~" + std);
                    continue xLoop;
                }
                // blocks
                toPrint.append(stringForBlock(b));
                
            }

            toPrint.append(nl);
        }
        // 2) print additional player and game info
        for(Player p : ps){
            toPrint.append("P" + p.id().ordinal() + " : " +red+ p.lives() + std + " lives " + p.lifeState().state() + nl);
            toPrint.append("    max bombs: " + green+ p.maxBombs()+std + " range: " +cyan + p.bombRange()+ std + nl);
            toPrint.append("    position: " + p.position().containingCell() + stringForDistToCentral(p) + nl);
            
        }
        if(s.isGameOver()){
            toPrint.append("GAME OVER : ");
            toPrint.append(" winner: "+s.winner());
        }
        toPrint.append(nl);
        toPrint.append("Remaining Time: " + String.format("%.1f", s.remainingTime()) + nl);
        toPrint.append("TICK_NUMBER: " + s.ticks() + nl);
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
        return bCyan + white + b.toString() + std;
    }

    private static String stringForBlock(Block b) {
        switch (b) {
        case FREE: return "  ";
        case INDESTRUCTIBLE_WALL: return bBlack + "##" + std;
        case DESTRUCTIBLE_WALL: return bBlack + "??" + std;
        case CRUMBLING_WALL: return bBlack + "¿¿" + std;
        case BONUS_BOMB: return bGreen + "+b" + std;
        case BONUS_RANGE: return bGreen + "+r" + std;
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

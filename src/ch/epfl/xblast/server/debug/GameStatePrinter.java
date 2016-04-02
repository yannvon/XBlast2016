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
    private static final String yellow = "\u001b[31m";
    private static final String green = "\u001b[32m";
    private static final String blue = "\u001b[34m";
    private static final String black = "\u001b[30m";
    private static final String magenta = "\u001b[35m";
    private static final String cyan = "\u001b[36m";
    private static final String white = "\u001b[37m";
    private static final String std = "\u001b[m";
    
    private GameStatePrinter() {}

    public static void printGameState(GameState s) {
        List<Player> ps = s.alivePlayers();
        Board board = s.board();
        Map<Cell,Bomb> bombs= s.bombedCells();
        Set<Cell> blasts= s.blastedCells();

        for (int y = 0; y < Cell.ROWS; ++y) {
            // 1) print game board
            xLoop: for (int x = 0; x < Cell.COLUMNS; ++x) {
                Cell c = new Cell(x, y);
                
                // --- PRIORITY ORDER ---
                // players
                for (Player p: ps) {
                    if (p.position().containingCell().equals(c)) {
                        System.out.print(stringForPlayer(p));
                        continue xLoop;
                    }
                }
                
                // bombs
                if (bombs.containsKey(c)){
                    System.out.print(red + "@@" + std);
                    continue xLoop;
                }
                

                Block b = board.blockAt(c);
                
                // blasts
                if (blasts.contains(c) && b.isFree()){
                    System.out.print(yellow + "~~" + std);
                    continue xLoop;
                }
                // blocks
                System.out.print(stringForBlock(b));
                
            }

            System.out.println();
        }
        // 2) print additional player and game info
        for(Player p : ps){
            System.out.println("P" + p.id().ordinal() + " : " +red+ p.lives() + std + " lives " + p.lifeState().state());
            System.out.println("    max bombs: " + green+ p.maxBombs()+std + " range: " +cyan + p.bombRange()+ std);
            System.out.println("    position: " + p.position().containingCell());
            
        }
        System.out.println();
        System.out.println("Remaining Time: " + Math.round(s.remainingTime()));
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
        return white + b.toString() + std;
    }

    private static String stringForBlock(Block b) {
        switch (b) {
        case FREE: return "  ";
        case INDESTRUCTIBLE_WALL: return black + "##" + std;
        case DESTRUCTIBLE_WALL: return black + "??" + std;
        case CRUMBLING_WALL: return white + "¿¿" + std;
        case BONUS_BOMB: return green + "+b" + std;
        case BONUS_RANGE: return cyan + "+r" + std;
        default: throw new Error();
        }
    }
}

package ch.epfl.DONT_SUBMIT;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.server.Block;
import ch.epfl.xblast.server.Board;
import ch.epfl.xblast.server.GameState;
import ch.epfl.xblast.server.Player;
import ch.epfl.xblast.server.debug.GameStatePrinter;
import ch.epfl.xblast.server.debug.GameStatePrinterwithoutColor;
import ch.epfl.xblast.server.debug.RandomEventGenerator;

/**
 * VisualTest that alows to move players.
 * 
 * @author Loïc Vandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 *
 */
public class VisualTest {

    // Constant used in Test
    private final static Block __ = Block.FREE;
    private final static Block XX = Block.INDESTRUCTIBLE_WALL;
    private final static Block xx = Block.DESTRUCTIBLE_WALL;
    private final static Block b = Block.BONUS_BOMB;
    private final static Block r = Block.BONUS_RANGE;


    private final static List<Board> boards = Arrays.asList(
            Board.ofQuadrantNWBlocksWalled(
            Arrays.asList(Arrays.asList(__, __, __, __, __, xx, __),
                    Arrays.asList(__, XX, xx, XX, xx, XX, xx),
                    Arrays.asList(__, xx, __, __, __, xx, __),
                    Arrays.asList(xx, XX, __, XX, XX, XX, XX),
                    Arrays.asList(__, xx, __, xx, __, __, __),
                    Arrays.asList(xx, XX, xx, XX, xx, XX, __)))
            ,
            Board.ofQuadrantNWBlocksWalled(
                    Arrays.asList(Arrays.asList(__, __, xx, xx, xx, xx, xx),
                            Arrays.asList(__, xx, xx, xx, xx, xx, xx),
                            Arrays.asList(__, xx, xx, xx, xx, xx, xx),
                            Arrays.asList(xx, xx, xx, xx, xx, xx, xx),
                            Arrays.asList(xx, xx, xx, xx, xx, xx, xx),
                            Arrays.asList(xx, xx, xx, xx, xx, xx, XX)))
            ,
            Board.ofQuadrantNWBlocksWalled(
                    Arrays.asList(Arrays.asList(__, __, r, b, b, b, b),
                            Arrays.asList(__, r, b, b, b, r, b),
                            Arrays.asList(__, b, b, r, b, b, b),
                            Arrays.asList(b, r, b, r, b, r, b),
                            Arrays.asList(b, r, b, r, b, r, b),
                            Arrays.asList(b, r, b, r, b, r, b)))
            
            
            
            );
    
    private VisualTest() {
       
    }

    /**
     * VISUAL TEST:
     * 
     * ---initialization---
     *  enter the coordinates of the starting Cell for each player
     *  enter the maximums bombs for the players
     *  enter the range of the players
     *  each players have only 1 life
     *   
     * ---Control---
     *   - "1","2","3","4" : select the player you control
     *   - "w","a","s","d" : change the direction of the controlled player
     *   - "x"             : stop the player at the next central SubCell 
     *   - "e"             : the controlled player drop a bomb
     *   - "all"           : all the players put a bomb
     * @throws IOException 
     * @throws InterruptedException 
     */
    public static void main(String[] args) throws InterruptedException, IOException {

        Scanner scan = new Scanner(System.in);
        List<Player> gPlayers = new ArrayList<>();
        System.out.println("maxBombs?");
        int maxBombs = scan.nextInt();
        System.out.println("range?");
        int range = scan.nextInt();
        System.out.println("life?");
        int life = scan.nextInt();
        for (int i = 0; i < 4; i++) {
            System.out.println("player " + (i + 1) + " position :");
            gPlayers.add(new Player(PlayerID.values()[i], life,
                    new Cell(scan.nextInt(), scan.nextInt()), maxBombs, range));
        }
        
        System.out.println("board?/n  0:normal/n  1:with lot of destructible wall/n  2:with lot of bonus");

        GameState game = new GameState(boards.get(scan.nextInt()%3), gPlayers);
        boolean inGame = true;
        PlayerID control = PlayerID.PLAYER_1;
        while (inGame) {
            String s = scan.nextLine();
            Set<PlayerID> bombdrp = new HashSet<>();
            Map<PlayerID, Optional<Direction>> speedChange = new HashMap<>();
            switch (s) {
            case "1":
                control = PlayerID.PLAYER_1;
                break;
            case "2":
                control = PlayerID.PLAYER_2;
                break;
            case "3":
                control = PlayerID.PLAYER_3;
                break;
            case "4":
                control = PlayerID.PLAYER_4;
                break;
            case "e":
                bombdrp.add(control);
                break;
            case "w":
                speedChange.put(control, Optional.of(Direction.N));
                break;
            case "a":
                speedChange.put(control, Optional.of(Direction.W));
                break;
            case "s":
                speedChange.put(control, Optional.of(Direction.S));
                break;
            case "d":
                speedChange.put(control, Optional.of(Direction.E));
                break;
            case "x":
                speedChange.put(control, Optional.empty());
                break;
            case "all":
                for(PlayerID id:PlayerID.values()){

                    bombdrp.add(id);
                }
                break;
            case "0":
                inGame = false;

            }
            game = game.next(speedChange, bombdrp);
            GameStatePrinterwithoutColor.printGameState(game);
        }
        scan.close();
    }

}

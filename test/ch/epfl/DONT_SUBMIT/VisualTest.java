package ch.epfl.DONT_SUBMIT;

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

    private static final RandomEventGenerator RANDOM = new RandomEventGenerator(
            2016, 30, 100);

    private final static Board board = Board.ofQuadrantNWBlocksWalled(
            Arrays.asList(Arrays.asList(__, __, __, __, __, xx, __),
                    Arrays.asList(__, XX, xx, XX, xx, XX, xx),
                    Arrays.asList(__, xx, __, __, __, xx, __),
                    Arrays.asList(xx, XX, __, XX, XX, XX, XX),
                    Arrays.asList(__, xx, __, xx, __, __, __),
                    Arrays.asList(xx, XX, xx, XX, xx, XX, __)));

    private final static List<Player> players = Arrays.asList(
            new Player(PlayerID.PLAYER_1, 3, new Cell(1, 1), 3, 3),
            new Player(PlayerID.PLAYER_2, 3, new Cell(7, 6), 3, 3),
            new Player(PlayerID.PLAYER_3, 3, new Cell(8, 9), 3, 3),
            new Player(PlayerID.PLAYER_4, 3, new Cell(3, 4), 3, 3));

    
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
     */
    public static void main(String[] args) {

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

        GameState game = new GameState(board, gPlayers);
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
            case "0":
                inGame = false;

            }
            game = game.next(speedChange, bombdrp);
            GameStatePrinter.printGameState(game);
            //GameStatePrinterwithoutColor.printGameState(game)s;
        }
        scan.close();
    }

}

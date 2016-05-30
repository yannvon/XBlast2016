package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;


import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;

/**
 * Class that represents a level by an initial GameState and a BoardPainter.
 * 
 * @author Loïc Vandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 *
 */
public final class Level {
    
    /*
     * Constants
     */
    private static final Block __ = Block.FREE;
    private static final Block XX = Block.INDESTRUCTIBLE_WALL;
    private static final Block xx = Block.DESTRUCTIBLE_WALL;
    
    /*
     * DEFAULT LEVEL
     */
    public static final Level DEFAULT_LEVEL = new Level(defaultBoardPainter(),
            chargeGameState("DEFAULT"));
    public static final Level TWO_PLAYER_LEVEL = new Level(defaultBoardPainter(),
            chargeGameState("DEFAULT_LOCAL"));

    /**
     * ADDITIONAL static method that constructs a classic BoardPainter that uses
     * the standard images.
     * 
     * @return a default BoardPainter
     */
    private static BoardPainter defaultBoardPainter(){
        
        Map<Block,BlockImage> palette = new HashMap<>();
        
        palette.put(Block.INDESTRUCTIBLE_WALL,BlockImage.DARK_BLOCK);
        palette.put(Block.FREE,BlockImage.IRON_FLOOR);
        palette.put(Block.DESTRUCTIBLE_WALL,BlockImage.EXTRA);
        palette.put(Block.CRUMBLING_WALL,BlockImage.EXTRA_O);
        palette.put(Block.BONUS_RANGE,BlockImage.BONUS_RANGE);
        palette.put(Block.BONUS_BOMB,BlockImage.BONUS_BOMB);
        palette.put(Block.BONUS_ROLLER,BlockImage.BONUS_ROLLER);
        palette.put(Block.BONUS_SNAIL, BlockImage.BONUS_SNAIL);
        palette.put(Block.BONUS_KICKBOMB, BlockImage.BONUS_KICKBOMB);

        return new BoardPainter(palette, BlockImage.IRON_FLOOR_S);
    }

    /**
     * ADDITIONAL static method that constructs the default initial GameState,
     * consisting of the default Board and the default player configuration.
     * 
     * The default player configuration places all players in the corner and the
     * players start with 3 lives, a bomb range of 3 and 2 maximal bombs.
     * 
     * @return a default BoardPainter
     */
    private static GameState defaultGameState() {
        int defaultLives = 3;
        int defaultBombs = 2;
        int defaultRange = 3;
        List<Cell> defaultStartingPos = Arrays.asList(new Cell(1, 1),
                new Cell(13, 1), new Cell(13, 11), new Cell(1, 11));

        Board board = Board.ofQuadrantNWBlocksWalled(
                Arrays.asList(
                        Arrays.asList(__, __, __, __, __, xx, __),
                        Arrays.asList(__, XX, xx, XX, xx, XX, xx),
                        Arrays.asList(__, xx, __, __, __, xx, __),
                        Arrays.asList(xx, XX, __, XX, XX, XX, XX),
                        Arrays.asList(__, xx, __, xx, __, __, __),
                        Arrays.asList(xx, XX, xx, XX, xx, XX, __)));

        List<Player> players = new ArrayList<>();
        for (PlayerID id : PlayerID.values()) {
            players.add(new Player(id, defaultLives,
                    defaultStartingPos.get(id.ordinal()), defaultBombs,
                    defaultRange));
        }

        return new GameState(board, players);
    }

    private static BoardPainter randomBoardPainter() {

        Map<Block, BlockImage> palette = new HashMap<>();

        palette.put(Block.INDESTRUCTIBLE_WALL, BlockImage.DARK_BLOCK);
        palette.put(Block.FREE, BlockImage.IRON_FLOOR);
        palette.put(Block.DESTRUCTIBLE_WALL, BlockImage.EXTRA);
        palette.put(Block.CRUMBLING_WALL, BlockImage.EXTRA_O);
        palette.put(Block.BONUS_RANGE, BlockImage.RANDOM_BONUS);
        palette.put(Block.BONUS_BOMB, BlockImage.RANDOM_BONUS);
        palette.put(Block.BONUS_SNAIL, BlockImage.RANDOM_BONUS);
        palette.put(Block.BONUS_ROLLER, BlockImage.RANDOM_BONUS);
        palette.put(Block.BONUS_KICKBOMB, BlockImage.RANDOM_BONUS);

        return new BoardPainter(palette, BlockImage.IRON_FLOOR_S);
    }

    /**
     * Bonus method that import a gameState from a file TODO method to export
     * gameState
     * 
     * @param name
     *            is the name of the file to import
     * @return the imported GameState
     */
    public static GameState chargeGameState(String name) {
        GameState gameState = null;
        File gameStateFile = null;
        
        /*
         * Charge the corresponding file
         */
        try {
            File file = new File(Level.class.getClassLoader()
                    .getResource("gameStates").toURI());

            gameStateFile = file.listFiles((f, n) -> name.equals(n))[0];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new Error("the file " + name + " does not exist");
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*
         * construct GameState
         */
        try (Scanner scanner = new Scanner(gameStateFile)) {
            int lives = scanner.nextInt();
            int maxBombs = scanner.nextInt();
            int range = scanner.nextInt();
            int alivesPlayers = scanner.nextInt();
            scanner.nextLine();

            List<List<Block>> boardList = new ArrayList<>();
            List<Player> players = new ArrayList<>();
            int startingX = 1, startingY = 1;
            int y = 0;
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                List<Block> row = new ArrayList<>();
                for (int x = 0; x < (Cell.COLUMNS - 1) / 2; x++) {
                    char c = line.charAt(x);
                    switch (c) {
                    case 'X':
                        row.add(Block.INDESTRUCTIBLE_WALL);
                        break;
                    case 'x':
                        row.add(Block.DESTRUCTIBLE_WALL);
                        break;
                    case 'p':
                        startingX = x + 1;
                        startingY = y + 1;
                    case '_':
                        row.add(Block.FREE);
                        break;
                    // TODO bonuses
                    }
                }
                boardList.add(row);
                y++;

            }
            /*
             * add alives players
             */
            Cell[] startingPos = { new Cell(startingX, startingY),
                    new Cell(-startingX - 1, -startingY - 1),
                    new Cell(-startingX - 1, startingY),
                    new Cell(startingX, -startingY - 1) };
            for (int i = 0; i < alivesPlayers; i++) {
                players.add(new Player(PlayerID.values()[players.size()], lives,
                        startingPos[i], maxBombs, range));
            }
            /*
             * add dead players
             */
            for (int i = players.size(); i < PlayerID.values().length; i++) {
                players.add(new Player(PlayerID.values()[i], 0, new Cell(0, 0),
                        maxBombs, range));
            }

            Board board = Board.ofQuadrantNWBlocksWalled(boardList);
            gameState = new GameState(board, players);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return gameState;
    }

    /*
     * Attributes
     */
    private final BoardPainter boardPainter;
    private final GameState initialGameState;

    /**
     * Constructor taking a BoardPainter and an initial GameState as arguments.
     * 
     * @param boardPainter
     *            the BoardPainter used for this level
     * @param initialGame
     *            the GameState at the start of the game
     * @throws NullPointerException
     *             if one of the arguments are null
     */
    public Level(BoardPainter boardPainter, GameState initialGame) {
        this.boardPainter = Objects.requireNonNull(boardPainter);
        this.initialGameState = Objects.requireNonNull(initialGame);
    }

    /**
     * Returns the BoardPainter of this Level
     * 
     * @return the boardPainter of the Level
     */
    public BoardPainter boardPainter() {
        return boardPainter;
    }

    /**
     * Returns the initial GameState of the Level
     * 
     * @return the initial GameState
     */
    public GameState initialGameState() {
        return initialGameState;
    }
}

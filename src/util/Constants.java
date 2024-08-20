package util;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The Constants class serves as a centralized repository for all constant values and configuration settings
 * used throughout the maze game. This class defines various parameters that control the game's behavior,
 * including maze dimensions, player attributes, game rules, and resource paths.
 * <p>
 * This utility class contains:
 * <p>
 * - Maze Configuration: Constants like `MAZE_SIZE`, `TILE_SIZE`, and `VIEW_RADIUS` that define the size
 * and structure of the maze.
 * <p>
 * - Player Defaults: Default values for player attributes such as `DEFAULT_STARTING_HP`,
 * `DEFAULT_STARTING_ABILITY_COUNT`, and `DEFAULT_STARTING_KEYS`.
 * <p>
 * - Gameplay Mechanics: Constants that influence gameplay, including `TRAP_SPAWN_RATIO` , 'TRAP_SPAWN_COUNT' ,
 * the `INPUTS_GUIDE` and `GAME_RULES` strings.
 * <p>
 * - Color Settings: Predefined colors for elements such as the player's path, walls, and the player
 * character (`TAKEN_PATH_COLOR`, `PLAYER_COLOR`, `PATH_COLOR`, `WALL_COLOR`).
 * <p>
 * - Resource Paths: File paths for various game resources like images for the background, treasure,
 * portal, keys, and traps.
 * <p>
 * - By using these constants, the game can be easily configured or adjusted without hardcoding values
 * throughout the codebase. This class is intended to be used statically and should not be instantiated.
 */


public class Constants {

    // Maze Configuration
    public static final int MAZE_SIZE = 49;
    public static final int TILE_SIZE = 15;
    public static final int VIEW_RADIUS = MAZE_SIZE / 10;
    // Player Defaults
    public static final int DEFAULT_STARTING_HP = 3;
    public static final int DEFAULT_STARTING_ABILITY_COUNT = 2;
    public static final List<String> DEFAULT_STARTING_KEYS = new ArrayList<>(0);
    // Gameplay Mechanics
    public static final double TRAP_SPAWN_RATIO = 0.33;
    public static final int TRAP_SPAWN_COUNT = (int) (MAZE_SIZE * TRAP_SPAWN_RATIO);
    public static final String INPUTS_GUIDE = "Inputs ↓\nUP: ARROW_UP ↑ , W Key\nDOWN: ARROW_DOWN ↓ , S Key\n" +
            "LEFT: ARROW_LEFT ← , A Key\nRIGHT: ARROW_RIGHT → , D Key\nJumpMode Toggle: J\nWallBreakerMode Toggle: G\nGive up: Esc";
    public static final String GAME_RULES = "Game Rules ↓:\nFind Treasure ASAP with least possible steps.\n" +
            "You lose ,if run out of HP or give up.\n" +
            "Portal can be used when you have it's key.\n" +
            "After moving on a Portal you are teleported around a random same colored Portal.\n" +
            "Your vision will be limited after first step but time still will be counted before first step.\n" +
            "In order to use jump and wallbreaker abilities you must turn their respective mode on and " +
            "choose a direction. (bear in mind though your abilities have limit uses.)";
    // Color Settings
    public static final Color TAKEN_PATH_COLOR = new Color(141, 57, 241);
    public static final Color PLAYER_COLOR = Color.GREEN;
    public static final Color PATH_COLOR = Color.WHITE;
    public static final Color WALL_COLOR = Color.BLACK;
    // Resource Paths
    public static final String LEADERBOARD_FILE_PATH = "resources/leaderboard.txt";
    public static final String BACKGROUND_RESOURCE_PATH = "resources/bg.png";
    public static final String TREASURE_RESOURCE_PATH = "resources/treasure.png";
    public static final String QUESTION_MARK_RESOURCE_PATH = "resources/question_mark.png";
    public static final String PORTAL_RESOURCE_PATH = "resources/portal.png";
    public static final String PORTAL_KEY_RESOURCE_PATH = "resources/key.png";
    public static final String TRAP_RESOURCE_PATH = "resources/bomb.png";
    // GitHub Repository
    public static final String GITHUB_REPOSITORY = "https://github.com/MohammadHosseinKv/Tomb-of-the-Mask";

}

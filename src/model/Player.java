package model;

import java.util.List;

import static util.Constants.*;

/**
 * The Player class represents the player character in the maze game. This class is implemented as a Singleton,
 * ensuring that only one instance of the player exists throughout the game. It extends the Entity class and
 * implements the EntityType interface, inheriting basic properties like coordinates while adding specific
 * player attributes such as username, steps taken, health points (HP), abilities, and collected keys.
 * <p>
 * The Player class provides methods to initialize the player instance, access the instance, and manipulate
 * the player's state, such as updating position, abilities, health, and keys collected.
 */


public class Player extends Entity { // this class is implemented base on singleton design pattern

    private static Player player = null;
    private final String username;
    private int steps;
    private int jumpAbilityCount;
    private int wallBreakerAbilityCount;
    private int HP;
    private List<String> keys;

    /**
     * Initializes the Player instance with the given username, type, and coordinates, setting default values
     * for steps, HP, abilities, and keys. If an instance already exists, it is replaced by the new one.
     *
     * @param type     the type of entity represented by the player.
     * @param username the player's username.
     * @param x        the initial x-coordinate of the player.
     * @param y        the initial y-coordinate of the player.
     * @return the initialized Player instance.
     */
    public static Player initInstance(String type, String username, int x, int y) {
        return (player = new Player(type, username, x, y, 0, DEFAULT_STARTING_HP, DEFAULT_STARTING_ABILITY_COUNT, DEFAULT_STARTING_ABILITY_COUNT, DEFAULT_STARTING_KEYS));
    }

    /**
     * Initializes the Player instance with the given username, type, coordinates, and steps taken, setting default
     * values for HP, abilities, and keys. If an instance already exists, it is replaced by the new one.
     *
     * @param type     the type of entity represented by the player.
     * @param username the player's username.
     * @param x        the initial x-coordinate of the player.
     * @param y        the initial y-coordinate of the player.
     * @param steps    the number of steps the player has taken.
     * @return the initialized Player instance.
     */
    public static Player initInstance(String type, String username, int x, int y, int steps) {
        return (player = new Player(type, username, x, y, steps, DEFAULT_STARTING_HP, DEFAULT_STARTING_ABILITY_COUNT, DEFAULT_STARTING_ABILITY_COUNT, DEFAULT_STARTING_KEYS));
    }

    /**
     * Initializes the Player instance with the given username, type, coordinates, steps taken, HP, abilities,
     * and keys. If an instance already exists, it is replaced by the new one.
     *
     * @param type                     the type of entity represented by the player.
     * @param username                 the player's username.
     * @param x                        the initial x-coordinate of the player.
     * @param y                        the initial y-coordinate of the player.
     * @param steps                    the number of steps the player has taken.
     * @param HP                       the player's health points.
     * @param jumpAbilityCount         the number of jump abilities the player has.
     * @param wallBreakerAbilityCount  the number of wall breaker abilities the player has.
     * @param keys                     the list of keys collected by the player.
     * @return the initialized Player instance.
     */
    public static Player initInstance(String type, String username, int x, int y, int steps, int HP, int jumpAbilityCount, int wallBreakerAbilityCount, List<String> keys) {
        return (player = new Player(type, username, x, y, steps, HP, jumpAbilityCount, wallBreakerAbilityCount, keys));
    }

    /**
     * Retrieves the current Player instance. If no instance exists, this method returns null.
     *
     * @return the current Player instance, or null if not initialized.
     */
    public static Player getInstance() {
        return player;
    }

    /**
     * Private constructor to create a Player instance with the specified attributes.
     *
     * @param type                     the type of entity represented by the player.
     * @param username                 the player's username.
     * @param x                        the initial x-coordinate of the player.
     * @param y                        the initial y-coordinate of the player.
     * @param steps                    the number of steps the player has taken.
     * @param HP                       the player's health points.
     * @param jumpAbilityCount         the number of jump abilities the player has.
     * @param wallBreakerAbilityCount  the number of wall breaker abilities the player has.
     * @param keys                     the list of keys collected by the player.
     */
    private Player(String type, String username, int x, int y, int steps, int HP, int jumpAbilityCount, int wallBreakerAbilityCount, List<String> keys) {
        super(x, y, type);
        this.username = username;
        this.steps = steps;
        this.HP = HP;
        this.jumpAbilityCount = jumpAbilityCount;
        this.wallBreakerAbilityCount = wallBreakerAbilityCount;
        this.keys = keys;
    }

    /**
     * Gets the player's username.
     *
     * @return the player's username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the player's coordinates on the game map.
     *
     * @param x the new x-coordinate.
     * @param y the new y-coordinate.
     */
    public void setPlayerCord(int x, int y) {
        setX(x);
        setY(y);
    }

    /**
     * Gets the number of steps the player has taken.
     *
     * @return the number of steps.
     */
    public int getSteps() {
        return steps;
    }

    /**
     * Sets the number of steps the player has taken.
     *
     * @param steps the new step count.
     */
    public void setSteps(int steps) {
        this.steps = steps;
    }

    /**
     * Gets the number of jump abilities the player has.
     *
     * @return the jump ability count.
     */
    public int getJumpAbilityCount() {
        return jumpAbilityCount;
    }

    /**
     * Sets the number of jump abilities the player has.
     *
     * @param jumpAbilityCount the new jump ability count.
     */
    public void setJumpAbilityCount(int jumpAbilityCount) {
        this.jumpAbilityCount = jumpAbilityCount;
    }

    /**
     * Gets the number of wall breaker abilities the player has.
     *
     * @return the wall breaker ability count.
     */
    public int getWallBreakerAbilityCount() {
        return wallBreakerAbilityCount;
    }

    /**
     * Sets the number of wall breaker abilities the player has.
     *
     * @param wallBreakerAbilityCount the new wall breaker ability count.
     */
    public void setWallBreakerAbilityCount(int wallBreakerAbilityCount) {
        this.wallBreakerAbilityCount = wallBreakerAbilityCount;
    }

    /**
     * Gets the player's health points (HP).
     *
     * @return the player's HP.
     */
    public int getHP() {
        return HP;
    }

    /**
     * Sets the player's health points (HP).
     *
     * @param HP the new HP value.
     */
    public void setHP(int HP) {
        this.HP = HP;
    }

    /**
     * Adds a key to the player's collection.
     *
     * @param key the key to be added.
     */
    public void addKey(String key) {
        keys.add(key);
    }

    /**
     * Gets the list of keys collected by the player.
     *
     * @return the list of keys.
     */
    public List<String> getKeys() {
        return keys;
    }
}

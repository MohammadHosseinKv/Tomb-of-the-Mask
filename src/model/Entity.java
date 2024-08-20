package model;

/**
 * The Entity class is an abstract base class that represents any object in the game with a specific position
 * on the 2D grid. It provides the foundational properties and methods for all game entities, including their
 * coordinates on the grid.
 * <p>
 * This class implements the EntityType interface, ensuring that all derived entities conform to a standard
 * set of behaviors and attributes defined by the interface.
 * <p>
 * Being an abstract class, `Entity` cannot be instantiated directly. Instead, it is meant to be subclassed
 * by other specific entity types within the game, such as the player, enemies, or any interactive objects.
 * <p>
 * Key features of this class include:
 * <p>
 * - **Position Management**: The class provides methods to get and set the `x` and `y` coordinates of the entity,
 * enabling precise control over its location within the game environment.
 * <p>
 * - **Abstract Design**: As an abstract class, `Entity` defines the core attributes shared by all entities but leaves
 * the specifics of those entities to be implemented by subclasses.
 */
public abstract class Entity implements EntityType{
    private final String type; // type of the entity on the grid
    private int x; // The x-coordinate of the entity on the grid
    private int y; // The y-coordinate of the entity on the grid

    /**
     * Constructs a new Entity at the specified coordinates.
     * <p>
     * This constructor initializes the entity's position on the grid. It is protected to ensure that only
     * subclasses can instantiate an Entity, maintaining the abstract nature of this class.
     *
     * @param x The initial x-coordinate of the entity on the grid.
     * @param y The initial y-coordinate of the entity on the grid.
     * @param type type of the entity on the grid.
     */
    Entity(int x, int y, String type) {
        this.type = type;
        this.x = x;
        this.y = y;
    }

    /**
     * Gets the x-coordinate of the entity on the grid.
     *
     * @return The current x-coordinate of the entity.
     */
    public int getX() {
        return x;
    }
    /**
     * Gets the y-coordinate of the entity on the grid.
     *
     * @return The current y-coordinate of the entity.
     */
    public int getY() {
        return y;
    }

    /**
     * Sets the x-coordinate of the entity on the grid.
     * <p>
     * This method allows for repositioning the entity horizontally within the game environment.
     *
     * @param x The new x-coordinate of the entity.
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Sets the y-coordinate of the entity on the grid.
     * <p>
     * This method allows for repositioning the entity vertically within the game environment.
     *
     * @param y The new y-coordinate of the entity.
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Returns the type of the entity.
     *
     * @return the type of {@link Entity}.
     */
    @Override
    public String getType() {
        return type;
    }
}

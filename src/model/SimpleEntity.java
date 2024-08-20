package model;

/**
 * The SimpleEntity enum represents various types of simple entities within the game,
 * each with a unique type identifier. These entities are fundamental building blocks
 * of the game environment, including elements like walls, paths, traps, and treasures.
 * <p>
 * Each enum constant's type field is automatically set to the name of the enum constant
 * itself.
 */
public enum SimpleEntity implements EntityType{

    WALL, // Represents an impassable wall
    PATH, // Represents a walkable path
    TAKEN_PATH, // Represents a path that has already been traversed
    TRAP, // Represents a trap entity
    TREASURE; // Represents a treasure entity


    private final String type;

    /**
     * Constructs a SimpleEntity with its type set to the name of the enum constant.
     */
    SimpleEntity(){
        this.type = this.name();
    }

    /**
     * Gets the type of the entity, which is the name of the enum constant.
     *
     * @return A string representing the type of the entity.
     */
    @Override
    public String getType() {
        return type;
    }
}

package model;

/**
 * The EntityType interface defines a contract for all entity types within the game.
 * Any class implementing this interface must provide a specific type designation
 * for the entity it represents. This is used to identify and differentiate between
 * various entities in the game.
 * <p>
 * The main purpose of this interface is to ensure that all game entities can be
 * categorized by a specific type, which can be useful for logic involving
 * entity interactions, rendering, and game mechanics.
 * <p>
 * Implementing this interface allows entities to provide their type in a
 * standardized manner, facilitating cleaner and more maintainable code.
 */
public interface EntityType {
    /**
     * Gets the type of the entity as a String.
     * <p>
     * This method should return a string that represents the specific type of
     * the entity, such as "Player", "Enemy", or "Obstacle". The returned value
     * can be used in various game logic to differentiate between different
     * entities and handle them accordingly.
     *
     * @return A string representing the type of the entity.
     */
    String getType();
}

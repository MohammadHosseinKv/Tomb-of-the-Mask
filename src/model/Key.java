package model;

/**
 * The {@code Key} class represents a key entity within the game. Each key has a unique identifier
 * that can be used to distinguish it from other keys. Keys are positioned on a 2D grid with
 * specified coordinates and a type, which is used for various game logic purposes.
 *
 * <p>This class extends the {@link Entity} class, inheriting properties such as position (x, y) and type.
 * The {@code Key} class allows for the identification and manipulation of keys within the game environment.
 * Keys are fundamental to gameplay mechanics, often used for unlocking areas or triggering events.</p>
 */
public class Key extends Entity {
    private final String keyIdentifier;

    /**
     * Constructs a new {@code Key} object with the specified type, coordinates, and unique identifier.
     *
     * @param type          The type of the entity, typically a string representing the entity type.
     * @param x             The x-coordinate of the key on the grid.
     * @param y             The y-coordinate of the key on the grid.
     * @param keyIdentifier A unique identifier for this key, used to distinguish it from other keys.
     */
    public Key(String type, int x, int y, String keyIdentifier) {
        super(x, y, type);
        this.keyIdentifier = keyIdentifier;
    }

    /**
     * Returns the unique identifier for this key.
     *
     * @return A {@code String} representing the unique identifier of this key.
     */
    public String getKeyIdentifier() {
        return keyIdentifier;
    }

}

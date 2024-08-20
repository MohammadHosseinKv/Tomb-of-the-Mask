package logic;

import model.Player;

/**
 * The {@code MazeObserver} interface defines a contract for objects that need to be notified
 * about changes occurring to a {@link Player} within a maze. Implementing classes should provide
 * specific behavior in response to these changes, such as updating the user interface, logging
 * events, or triggering game logic.
 *
 * <p>This interface follows the Observer design pattern, allowing observers to be updated whenever
 * the state of the player changes in the maze. Implementations of this interface are typically
 * used in conjunction with a {@link Maze} class or similar to observe player movements, status
 * changes, or other relevant events.</p>
 *
 * <p>Common use cases for this interface include:
 * <ul>
 *     <li>Tracking the player's position within the maze.</li>
 *     <li>Reacting to specific player actions, such as collecting items or encountering obstacles.</li>
 *     <li>Updating visual or auditory feedback based on the player's state.</li>
 * </ul>
 * </p>
 */
public interface MazeObserver {

    /**
     * This method is called to notify the observer of a change in the {@code Player}'s state within the maze.
     *
     * <p>Implementations of this method should define how the observer reacts to the player’s
     * state changes, which may include updating the game state, modifying the user interface,
     * or triggering other game events.</p>
     *
     * @param player The {@code Player} object whose state has changed. This includes information
     *               such as the player's current position, health, inventory, or other relevant attributes.
     */
    void update(Player player);

}

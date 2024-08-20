package logic;

/**
 * The {@code MazeListener} interface defines a contract for objects that need to be notified
 * of any changes in the state of the maze, particularly regarding special abilities or modes
 * such as jump mode and wall breaker mode. Implementing classes should provide specific behavior
 * in response to these changes, enabling dynamic interactions and updates within the game environment.
 *
 * <p>This interface is particularly useful in scenarios where certain gameplay mechanics, such
 * as the ability to jump over obstacles or break walls, can be toggled on or off. By implementing
 * this interface, objects can react accordingly to these changes, ensuring that the game state is
 * always in sync with the player's abilities.</p>
 *
 * <p>Common use cases for this interface include:
 * <ul>
 *     <li>Enabling or disabling certain user interface elements based on the current mode.</li>
 *     <li>Triggering visual or auditory feedback when a mode is toggled.</li>
 *     <li>Updating game logic to reflect the active mode, such as allowing or disallowing certain actions.</li>
 * </ul>
 * </p>
 */

public interface MazeListener {

    /**
     * Called when the jump mode is toggled in the maze. Implementations of this method should define
     * how the observer reacts when the jump mode is turned on or off.
     *
     * <p>This could involve enabling or disabling player actions that allow jumping over obstacles,
     * as well as updating the game state or user interface to reflect the current mode.</p>
     *
     * @param jumpMode {@code true} if jump mode is enabled, {@code false} if it is disabled.
     */
    void onJumpModeToggled(boolean jumpMode);

    /**
     * Called when the wall breaker mode is toggled in the maze. Implementations of this method should define
     * how the observer reacts when the wall breaker mode is turned on or off.
     *
     * <p>This could involve enabling or disabling the player's ability to break walls within the maze,
     * as well as updating the game state or user interface to reflect the current mode.</p>
     *
     * @param wallBreakerMode {@code true} if wall breaker mode is enabled, {@code false} if it is disabled.
     */
    void onWallBreakerModeToggled(boolean wallBreakerMode);

}

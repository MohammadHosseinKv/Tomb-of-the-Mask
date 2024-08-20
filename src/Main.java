import GUI.StartContentPane;
import javax.swing.*;
import java.awt.*;
/**
 * The Main class is the entry point for the Tomb of the Mask game application.
 * It is responsible for initializing the game environment, setting up the main user interface,
 * and launching the game loop. The class primarily manages the main window of the game and ensures
 * that the application is correctly displayed and ready for user interaction.
 * <p>
 * The Main class performs the following key functions:
 * <p>
 * - **Window Initialization**: Creates and configures the main JFrame, setting its title,
 * default close operation, and other display properties. It uses the system's default screen resolution
 * to adjust the display mode.
 * <p>
 * - **User Interface Setup**: Instantiates the StartContentPane, which is responsible for setting up
 * the initial game UI, including menus, buttons, and other interactive components. This class is central
 * to how the user first interacts with the game.
 * <p>
 * - **Frame Centralization**: Utilizes a utility method to center the game window on the screen,
 * ensuring a consistent and visually appealing user experience across different screen sizes and resolutions.
 * <p>
 * - **Rendering and Visibility**: After setting up the content pane and adjusting the window's properties,
 * the class finalizes the window setup by packing its components, revalidating and repainting the UI,
 * and finally making the window visible to the user. It also ensures that the window is not resizable
 * to maintain the integrity of the game layout.
 * <p>
 * This class is crucial as it orchestrates the startup process of the game, ensuring that all necessary
 * components are properly initialized and displayed, setting the stage for the player's interaction
 * with the game.
 */
public class Main {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Tomb of the Mask");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        DisplayMode screenRes = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
        // Initialize the game and start the UI
        new StartContentPane(screenRes,frame);

        util.Util.centralizeFrame(frame);
        frame.pack();
        frame.revalidate();
        frame.repaint();
        frame.setVisible(true);
        frame.setResizable(false);
    }
}
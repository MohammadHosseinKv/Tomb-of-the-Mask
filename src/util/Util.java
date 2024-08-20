package util;

import static util.Constants.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * The {@code Util} class provides a set of utility methods and constants
 * that are used throughout the maze game. This class includes methods for
 * handling user input, displaying messages, file operations, and game-related
 * constants. The {@code Util} class is intended to be used statically and
 * should not be instantiated.
 *
 * <p>Key features include:</p>
 * <ul>
 *   <li>Random number generation</li>
 *   <li>Direction vectors for movement</li>
 *   <li>Frame centralization</li>
 *   <li>User input and output handling</li>
 *   <li>File operations for leaderboard management</li>
 * </ul>
 *
 * <p>This class cannot be inherited and instantiated.</p>
 */
public final class Util {

    /**
     * A {@code Random} object initialized with the current time in milliseconds.
     * Used for generating random numbers in the game.
     */
    public static Random rand = new Random(new Date().getTime());
    /**
     * Direction vector representing movement to the left: [row change, column change].
     */
    public static int[] leftDirection = new int[]{0, -1};
    /**
     * Direction vector representing movement to the right: [row change, column change].
     */
    public static int[] rightDirection = new int[]{0, 1};
    /**
     * Direction vector representing upward movement: [row change, column change].
     */
    public static int[] upDirection = new int[]{-1, 0};
    /**
     * Direction vector representing downward movement: [row change, column change].
     */
    public static int[] downDirection = new int[]{1, 0};
    /**
     * A 2D array containing all direction vectors (left, right, up, down).
     * Can be used to iterate through all possible movement directions.
     */
    public static int[][] directions = new int[][]{leftDirection, rightDirection, upDirection, downDirection};

    /**
     * Centers the given {@code JFrame} on the screen.
     *
     * @param frame the {@code JFrame} to be centered.
     * @return {@code true} if the frame was successfully centered, {@code false} otherwise.
     */
    public static boolean centralizeFrame(JFrame frame) {
        try {
            Point CenterPoint = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
            frame.setLocation(new Point(CenterPoint.x - (frame.getPreferredSize().width / 2), CenterPoint.y - (frame.getPreferredSize().height / 2)));
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    /**
     * Displays a dialog box to the user to input a string.
     *
     * @param windowTitle the title of the input dialog window.
     * @param instruction the instruction to be displayed in the dialog.
     * @return the user's input as a {@code String}.
     */
    public static String getInput(String windowTitle, String instruction) {
        return JOptionPane.showInputDialog(null, instruction, windowTitle, JOptionPane.QUESTION_MESSAGE);
    }

    /**
     * Displays a message to the user in a dialog box.
     *
     * @param parentComponent the parent component of the dialog box, can be {@code null}.
     * @param outputMessage the message to be displayed.
     * @return {@code true} if the message was successfully displayed, {@code false} otherwise.
     */
    public static boolean showOutput(JComponent parentComponent, Object outputMessage) {
        try {
            JOptionPane.showMessageDialog(parentComponent, outputMessage);
            return true;
        } catch (HeadlessException e) {
            return false;
        }
    }

    /**
     * Reads the contents of the leaderboard file and returns it as a {@code StringBuilder}.
     *
     * @return a {@code StringBuilder} containing the contents of the leaderboard file.
     *         If the file does not exist, an empty {@code StringBuilder} is returned
     *         and a new file is created.
     */
    public static StringBuilder getLeaderBoardFileContents() {
        File leaderBoardFile = new File(LEADERBOARD_FILE_PATH);
        StringBuilder leaderboardFileContents = new StringBuilder();
        try (Scanner fileReader = new Scanner(leaderBoardFile)) {
            while (fileReader.hasNextLine()) {
                leaderboardFileContents.append(fileReader.nextLine()).append("\n");
            }
            return leaderboardFileContents;
        } catch (FileNotFoundException e) {
            try {
                leaderBoardFile.createNewFile();
                leaderBoardFile.setReadOnly();
            } catch (IOException ex) {
                showOutput(null, ex.getMessage());
            }
            return new StringBuilder();
        }
    }

    /**
     * Writes the provided list of lines to the leaderboard file, replacing any existing content.
     *
     * @param lines the list of strings to be written to the leaderboard file.
     * @return {@code true} if the file was successfully updated, {@code false} otherwise.
     */
    public static boolean updateLeaderboardFile(List<String> lines) {
        File leaderBoardFile = new File(LEADERBOARD_FILE_PATH);
        leaderBoardFile.setWritable(true);
        try (FileWriter fileWriter = new FileWriter(leaderBoardFile, false)) {
            for (String line : lines) {
                fileWriter.append(line).append("\n");
                fileWriter.flush();
            }
            return true;
        } catch (IOException e) {
            return false;
        }
        finally {
            leaderBoardFile.setReadOnly();
        }
    }

    /**
     * Private constructor to prevent instantiation of the {@code Util} class.
     */
    private Util() {}
}

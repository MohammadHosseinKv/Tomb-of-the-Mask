package GUI;

import static util.Constants.*;
import static util.Util.*;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

/**
 * The {@code StartContentPane} class is responsible for creating and managing the initial user interface
 * of the Tomb of the Mask game. This UI includes options to start a new game, view the leaderboard, and
 * access the source code repository. The class is designed to be visually appealing and user-friendly,
 * providing a smooth transition to other parts of the game.
 *
 * <p>The {@code StartContentPane} class extends {@code JLayeredPane} to allow effective layering of
 * components, such as the background image and interactive buttons, to enhance the overall user experience.
 *
 * <p>Main features of this class include:
 * <ul>
 *   <li>Setting a dynamic background image that scales according to screen resolution.</li>
 *   <li>Displaying a central panel with buttons for starting a new game, viewing the leaderboard, and
 *       accessing the source code repository.</li>
 *   <li>Handling user input for the username with validation to ensure it meets specific criteria.</li>
 *   <li>Implementing keyboard shortcuts for quick navigation.</li>
 * </ul>
 *
 * @see JLayeredPane
 */

public class StartContentPane extends JLayeredPane {

    /**
     * The {@code DisplayMode} object representing the screen resolution.
     * It is used to determine the scaling of UI components.
     */
    DisplayMode screenRes;

    /**
     * The main {@code JFrame} of the application.
     * This is the frame to which this content pane is attached.
     */
    JFrame frame;

    /**
     * Constructs a new {@code StartContentPane} with the specified screen resolution and JFrame.
     *
     * <p>This constructor initializes the layout, sets up the background image, and creates the center panel
     * containing the "New Game", "LeaderBoard", and "Source" buttons. It also manages input validation and
     * transitions to the next game pane or leaderboard.
     *
     * @param screenRes The {@code DisplayMode} of the screen, used to determine the resolution for scaling the UI components.
     * @param frame The main {@code JFrame} of the application, to which this content pane is attached.
     */
    public StartContentPane(DisplayMode screenRes, JFrame frame) {
        this.screenRes = screenRes;
        this.frame = frame;
        setPreferredSize(new Dimension(screenRes.getWidth() / 2, screenRes.getHeight() / 2));
        setLayout(null);

        ImageIcon background = new ImageIcon(BACKGROUND_RESOURCE_PATH);
        background.setImage(background.getImage().getScaledInstance(getPreferredSize().width, getPreferredSize().height, Image.SCALE_SMOOTH));
        JLabel backgroundLabel = new JLabel(background);
        backgroundLabel.setBounds(0, 0, getPreferredSize().width, getPreferredSize().height);
        add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);

        JPanel centerSection = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 50));
        centerSection.setBackground(new Color(0, 0, 0, 0));
        int width = getPreferredSize().width;
        int height = getPreferredSize().height;
        centerSection.setBounds((width / 2) - (width / 4), (height / 2) - (height / 4), width / 2, height / 2);
        add(centerSection, JLayeredPane.DRAG_LAYER);

        JButton NewGameButton = new JButton("New Game");
        NewGameButton.setFocusable(false);
        NewGameButton.setFont(new Font("Arial", Font.BOLD, 20));
        NewGameButton.addActionListener(this::handleNewGameAction);
        centerSection.add(NewGameButton, JLayeredPane.DRAG_LAYER);

        JButton LeaderBoardButton = new JButton("LeaderBoard");
        LeaderBoardButton.setFocusable(false);
        LeaderBoardButton.setFont(new Font("Arial", Font.BOLD, 20));
        LeaderBoardButton.addActionListener(this::handleLeaderBoardAction);
        centerSection.add(LeaderBoardButton, JLayeredPane.DRAG_LAYER);

        JButton SourceButton = new JButton("Source");
        SourceButton.setFocusable(false);
        SourceButton.setFont(new Font("Arial", Font.BOLD, 20));
        SourceButton.addActionListener(this::handleSourceButtonAction);
        centerSection.add(SourceButton, JLayeredPane.DRAG_LAYER);

        setupKeyboardShortcuts();
        frame.setContentPane(this);
        frame.pack();
        centralizeFrame(frame);
    }

    /**
     * Handles the action of the "New Game" button. Prompts the user to enter a valid username.
     *
     * <p>If the username is valid, it transitions to the {@code MazeContentPane}, passing the username as a parameter.
     * The username must be 2-13 characters long, start with an alphabetical character, and may contain numbers.
     *
     * @param e The {@code ActionEvent} triggered by clicking the "New Game" button.
     */
    private void handleNewGameAction(ActionEvent e) {
        String username;
        while (true) {
            username = getInput("New Game",
                    "Enter your Username " +
                            "(2 to 13 character , Only alphabetical characters and numbers are allowed " +
                            "and username must start with alphabet character): ");
            if (username == null) return;
            if (isValidUsername(username)) break;
            else showOutput(this, generateErrorMessage(username));
        }
        transitionToGame(username);
    }

    /**
     * Generates an appropriate error message based on the username validation failure.
     *
     * @param username The username that failed validation.
     * @return A string containing the error message.
     */
    private String generateErrorMessage(String username) {
        if (username.length() < 2 || username.length() > 13) {
            return "Username must be between 2 and 13 characters.";
        }
        return "Username must start with a letter and can contain only letters and numbers.";
    }

    /**
     * Validates the username according to the specified criteria:
     * <ul>
     *   <li>It must be between 2 and 13 characters long.</li>
     *   <li>It must start with an alphabetical character.</li>
     *   <li>It can only contain letters and numbers.</li>
     * </ul>
     *
     * @param username The username to validate.
     * @return {@code true} if the username is valid; {@code false} otherwise.
     */
    private boolean isValidUsername(String username) {
        return username.length() >= 2 && username.length() <= 13 &&
                Pattern.compile("^[a-zA-Z][a-zA-Z0-9]*$").matcher(username).matches();
    }

    /**
     * Transitions to the {@code MazeContentPane}, passing the validated username.
     *
     * @param username The validated username to pass to the next game pane.
     */
    private void transitionToGame(String username) {
        frame.setContentPane(new MazeContentPane(screenRes, frame, username));
        frame.pack();
        centralizeFrame(frame);
    }

    /**
     * Handles the action of the "LeaderBoard" button. Transitions to the {@code LeaderBoardContentPane}
     * where users can view the leaderboard.
     *
     * @param e The {@code ActionEvent} triggered by clicking the "LeaderBoard" button.
     */
    private void handleLeaderBoardAction(ActionEvent e) {
        new LeaderBoardContentPane(screenRes, frame);
    }

    /**
     * Handles the action of the "Source" button. Displays a dialog with a clickable hyperlink to the
     * GitHub repository containing the source code of the game.
     *
     * <p>When the "Source" button is clicked, a dialog box appears containing a hyperlink to the game's
     * GitHub repository. If the user clicks the link, the default web browser is opened to the specified
     * repository URL. If an error occurs while trying to open the browser, an error message is displayed
     * to the user.</p>
     *
     * @param e The {@code ActionEvent} triggered by clicking the "Source" button.
     */
    private void handleSourceButtonAction(ActionEvent e) {
        String htmlLink = "<html>" +
                "Click the link below to redirect to GitHub repository: <br>" +
                "<a href='" + GITHUB_REPOSITORY + "'>" + GITHUB_REPOSITORY + "</a>" +
                "</html>";
        JEditorPane editorPane = new JEditorPane("text/html", htmlLink);
        editorPane.setFont(new Font("Arial", Font.PLAIN, 25));
        editorPane.setEditable(false);
        editorPane.setOpaque(false);
        editorPane.addHyperlinkListener(new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (HyperlinkEvent.EventType.ACTIVATED.equals(e.getEventType())) {
                    try {
                        URI uri = new URI(GITHUB_REPOSITORY);
                        Desktop.getDesktop().browse(uri);
                    } catch (URISyntaxException | IOException ex) {
                        showOutput(StartContentPane.this, ex.getMessage());
                    }
                }
            }
        });
        showOutput(this, editorPane);
    }

    /**
     * Sets up keyboard shortcuts for the "New Game", "LeaderBoard", and "Source" buttons.
     *
     * <p>This method maps specific keyboard keys to actions that correspond to buttons on the
     * starting interface. Users can press "N" to start a new game, "L" to view the leaderboard,
     * or "S" to view the source code on the GitHub repository. This provides quick and efficient
     * navigation through the starting interface.</p>
     */
    private void setupKeyboardShortcuts() {
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();
        inputMap.put(KeyStroke.getKeyStroke("N"), "New Game");
        actionMap.put("New Game", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleNewGameAction(e);
            }
        });

        inputMap.put(KeyStroke.getKeyStroke("L"), "Leaderboard");
        actionMap.put("Leaderboard", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLeaderBoardAction(e);
            }
        });

        inputMap.put(KeyStroke.getKeyStroke("S"), "Source");
        actionMap.put("Source", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSourceButtonAction(e);
            }
        });
    }
}

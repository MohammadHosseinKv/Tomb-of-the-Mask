package GUI;

import logic.Maze;
import logic.MazeListener;
import logic.MazeObserver;
import model.Player;

import static util.Constants.*;
import static util.Util.*;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The MazeContentPane class is responsible for constructing and managing the main content pane of the game,
 * which includes both the maze and a sidebar that provides player information, game rules, and controls.
 *
 * <p>This class extends JPanel and implements both the MazeObserver and MazeListener interfaces, allowing it
 * to observe changes in the maze and respond to player actions. The class provides a comprehensive user
 * interface that integrates various components including the maze itself, player statistics, game instructions,
 * and action buttons.</p>
 *
 * <p>Key features of this class include:</p>
 * <ul>
 *   <li>Maze Integration: The class initializes the maze component, registers itself as an observer, and
 *   adds it to the content pane.</li>
 *   <li>Sidebar Components: The sidebar displays critical information such as the player's HP, steps taken,
 *   remaining abilities, and collected keys. It also includes buttons for user actions like giving up and
 *   toggling modes (Jump Mode, WallBreaker Mode).</li>
 *   <li>Game Timer: A timer is managed by this class to keep track of the elapsed time since the game started.</li>
 *   <li>Dynamic Updates: The class updates the sidebar's display based on the player's real-time stats and
 *   mode toggles, providing an interactive and responsive interface.</li>
 *   <li>Utility Methods: Several private methods are included to handle repetitive tasks like initializing
 *   scroll panes and inserting icons or strings into JTextPanes.</li>
 * </ul>
 *
 * <p>This class is crucial for the user experience, providing a well-organized and visually appealing interface
 * that displays important game information and facilitates player interaction.</p>
 */


public class MazeContentPane extends JPanel implements MazeObserver, MazeListener {

    /**
     * The screen resolution for the game.
     */
    DisplayMode screenRes;

    /**
     * The main application frame that holds the content pane.
     */
    JFrame frame;

    /**
     * Label for displaying the game timer.
     */
    private JLabel timerLabel;

    /**
     * Timer for tracking elapsed game time.
     */
    private Timer gameTimer;

    /**
     * Label for displaying the player's health points (HP).
     */
    private JLabel hpLabel;

    /**
     * Label for displaying the number of steps taken by the player.
     */
    private JLabel stepsLabel;

    /**
     * TextPane for displaying the keys collected by the player.
     */
    private JTextPane keysTextPane;

    /**
     * Label for displaying the remaining Jump abilities of the player.
     */
    private JLabel remainingJumpAbilityLabel;

    /**
     * Label for displaying the remaining WallBreaker abilities of the player.
     */
    private JLabel remainingWallBreakerAbilityLabel;

    /**
     * Label indicating whether Jump Mode is toggled ON or OFF.
     */
    private JLabel jumpModeTriggerLabel;

    /**
     * Label indicating whether WallBreaker Mode is toggled ON or OFF.
     */
    private JLabel wallBreakerModeTriggerLabel;

    /**
     * Tracks the total elapsed time in seconds.
     */
    private int elapsedSeconds;

    /**
     * Constructs the MazeContentPane with the specified screen resolution, frame, and username.
     *
     * @param screenRes the screen resolution for the game
     * @param frame     the main application frame
     * @param username  the username of the player
     */
    public MazeContentPane(DisplayMode screenRes, JFrame frame, String username) {
        this.screenRes = screenRes;
        this.frame = frame;
        Maze maze = new Maze(screenRes, frame, username);
        MazeRenderer mazeRenderer = new MazeRenderer(maze);
        Player player = Player.getInstance();
        maze.registerObserver(this);
        maze.setMazeListener(this);
        maze.setMazeContentPane(this);
        maze.setMazeRenderer(mazeRenderer);
        add(mazeRenderer);
        JPanel gameGuideLabel = new JPanel(null);
        int width = mazeRenderer.getPreferredSize().width;
        int height = mazeRenderer.getPreferredSize().height;
        gameGuideLabel.setPreferredSize(new Dimension(width, height));
        gameGuideLabel.setBackground(Color.WHITE);
        gameGuideLabel.setOpaque(true);
        add(gameGuideLabel);

        gameGuideLabel.add(setupUsernameLabel(username));

        gameGuideLabel.add(setupGiveUpButton());

        setupKeyBindings();

        JTextPane inputsTextPane = setupInputsTextPane();
        JScrollPane inputsScrollPane = initJScrollPane(inputsTextPane);
        inputsTextPane.setCaretPosition(0);
        gameGuideLabel.add(inputsScrollPane);

        JTextPane gameGuide = setupGameGuideTextPane();
        JScrollPane gameGuideScrollPane = initJScrollPane(gameGuide);
        gameGuide.setCaretPosition(0);
        gameGuideLabel.add(gameGuideScrollPane);

        JTextPane rulesTextPane = setupRulesTextPane();
        JScrollPane rulesScrollPane = initJScrollPane(rulesTextPane);
        rulesTextPane.setCaretPosition(0);
        gameGuideLabel.add(rulesScrollPane);

        JPanel gameAndPlayerInfo = setupGameAndPlayerInfo(player);
        gameGuideLabel.add(initJScrollPane(gameAndPlayerInfo));

        centralizeFrame(frame);
        startGameTimer();
    }

    /**
     * Initializes and returns a panel containing game and player information.
     *
     * @param player the current player instance
     * @return the initialized JPanel
     */
    private JPanel setupGameAndPlayerInfo(Player player) {
        JPanel gameAndPlayerInfo = new JPanel();
        gameAndPlayerInfo.setLayout(new BoxLayout(gameAndPlayerInfo, BoxLayout.Y_AXIS));
        gameAndPlayerInfo.setBackground(Color.WHITE);
        gameAndPlayerInfo.setOpaque(true);
        gameAndPlayerInfo.setBounds(380, 400, 300, 300);

        timerLabel = setupTimerLabel();
        gameAndPlayerInfo.add(timerLabel);
        gameAndPlayerInfo.add(Box.createVerticalStrut(10));

        gameTimer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                elapsedSeconds++;
                timerLabel.setText("Time: " + elapsedSeconds + " seconds");
            }
        });

        hpLabel = setupHPLabel(player);
        gameAndPlayerInfo.add(hpLabel);
        gameAndPlayerInfo.add(Box.createVerticalStrut(10));

        stepsLabel = setupStepsLabel(player);
        gameAndPlayerInfo.add(stepsLabel);
        gameAndPlayerInfo.add(Box.createVerticalStrut(10));

        remainingJumpAbilityLabel = setupRemainingJumpAbilityLabel(player);
        gameAndPlayerInfo.add(remainingJumpAbilityLabel);
        gameAndPlayerInfo.add(Box.createVerticalStrut(10));

        remainingWallBreakerAbilityLabel = setupRemainingWallBreakerAbilityLabel(player);
        gameAndPlayerInfo.add(remainingWallBreakerAbilityLabel);
        gameAndPlayerInfo.add(Box.createVerticalStrut(10));

        jumpModeTriggerLabel = setupJumpModeTriggerLabel();
        gameAndPlayerInfo.add(jumpModeTriggerLabel);
        gameAndPlayerInfo.add(Box.createVerticalStrut(10));

        wallBreakerModeTriggerLabel = setupWallBreakerModeTriggerLabel();
        gameAndPlayerInfo.add(wallBreakerModeTriggerLabel);
        gameAndPlayerInfo.add(Box.createVerticalStrut(10));

        keysTextPane = setupKeysTextPane();
        gameAndPlayerInfo.add(keysTextPane);
        gameAndPlayerInfo.add(Box.createVerticalStrut(10));

        return gameAndPlayerInfo;
    }

    /**
     * Initializes and returns a JTextPane for displaying collected keys.
     *
     * @return the initialized JTextPane
     */
    private JTextPane setupKeysTextPane() {
        JTextPane keysTextPane = new JTextPane();
        keysTextPane.setEditable(false);
        keysTextPane.setText("Keys: None");
        keysTextPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        keysTextPane.setFont(new Font("Arial", Font.BOLD, 20));
        return keysTextPane;
    }

    /**
     * Initializes and returns a JLabel indicating the status of WallBreaker Mode.
     *
     * @return the initialized JLabel
     */
    private JLabel setupWallBreakerModeTriggerLabel() {
        JLabel wallBreakerModeTriggerLabel = new JLabel("WallBreaker Mode: OFF");
        wallBreakerModeTriggerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        wallBreakerModeTriggerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return wallBreakerModeTriggerLabel;
    }

    /**
     * Initializes and returns a JLabel indicating the status of Jump Mode.
     *
     * @return the initialized JLabel
     */
    private JLabel setupJumpModeTriggerLabel() {
        JLabel jumpModeTriggerLabel = new JLabel("Jump Mode: OFF");
        jumpModeTriggerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        jumpModeTriggerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return jumpModeTriggerLabel;
    }

    /**
     * Initializes and returns a JLabel for displaying the remaining WallBreaker abilities.
     *
     * @param player the current player instance
     * @return the initialized JLabel
     */
    private JLabel setupRemainingWallBreakerAbilityLabel(Player player) {
        JLabel remainingWallBreakerAbilityLabel = new JLabel("WallBreaker Ability: " + player.getWallBreakerAbilityCount());
        remainingWallBreakerAbilityLabel.setFont(new Font("Arial", Font.BOLD, 20));
        remainingWallBreakerAbilityLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return remainingWallBreakerAbilityLabel;
    }

    /**
     * Initializes and returns a JLabel for displaying the remaining Jump abilities.
     *
     * @param player the current player instance
     * @return the initialized JLabel
     */
    private JLabel setupRemainingJumpAbilityLabel(Player player) {
        JLabel remainingJumpAbilityLabel = new JLabel("Jump Ability: " + player.getJumpAbilityCount());
        remainingJumpAbilityLabel.setFont(new Font("Arial", Font.BOLD, 20));
        remainingJumpAbilityLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return remainingJumpAbilityLabel;
    }

    /**
     * Initializes and returns a JLabel for displaying the number of steps taken by the player.
     *
     * @param player the current player instance
     * @return the initialized JLabel
     */
    private JLabel setupStepsLabel(Player player) {
        JLabel stepsLabel = new JLabel("Steps: " + player.getSteps());
        stepsLabel.setFont(new Font("Arial", Font.BOLD, 20));
        stepsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return stepsLabel;
    }

    /**
     * Initializes and returns a JLabel for displaying the player's health points (HP).
     *
     * @param player the current player instance
     * @return the initialized JLabel
     */
    private JLabel setupHPLabel(Player player) {
        JLabel hpLabel = new JLabel("HP: " + player.getHP());
        hpLabel.setFont(new Font("Arial", Font.BOLD, 20));
        hpLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return hpLabel;
    }

    /**
     * Initializes and returns a JLabel for the game timer.
     *
     * @return the initialized JLabel
     */
    private JLabel setupTimerLabel() {
        JLabel timerLabel = new JLabel("Time: 0 seconds");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        timerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return timerLabel;
    }

    /**
     * Initializes and returns a JTextPane for displaying game rules.
     *
     * @return the initialized JTextPane
     */
    private JTextPane setupRulesTextPane() {
        JTextPane rulesTextPane = new JTextPane();
        rulesTextPane.setEditable(false);
        rulesTextPane.setFont(new Font("Arial", Font.BOLD, 20));
        rulesTextPane.setText(GAME_RULES);
        rulesTextPane.setBounds(30, 400, 300, 300);
        StyledDocument rulesDocument = rulesTextPane.getStyledDocument();
        SimpleAttributeSet rulesSet = new SimpleAttributeSet();
        StyleConstants.setLineSpacing(rulesSet, 0.5f);
        rulesDocument.setParagraphAttributes(0, rulesDocument.getLength(), rulesSet, false);
        return rulesTextPane;
    }

    /**
     * Initializes and returns a JTextPane for displaying the game guide.
     *
     * @return the initialized JTextPane
     */
    private JTextPane setupGameGuideTextPane() {
        JTextPane gameGuide = new JTextPane();
        gameGuide.setEditable(false);
        gameGuide.setFont(new Font("Arial", Font.BOLD, 20));
        StyledDocument gameGuideDocument = gameGuide.getStyledDocument();
        SimpleAttributeSet gameGuideSet = new SimpleAttributeSet();

        insertStringAndPaintedRectangleIcon(gameGuide, gameGuideDocument, gameGuideSet, "Player: ", PLAYER_COLOR, TILE_SIZE, TILE_SIZE);
        insertStringAndPaintedRectangleIcon(gameGuide, gameGuideDocument, gameGuideSet, "\nTaken Path: ", TAKEN_PATH_COLOR, TILE_SIZE, TILE_SIZE);

        insertStringAndScaledIcon(gameGuide, gameGuideDocument, gameGuideSet, "\nTreasure: ", TREASURE_RESOURCE_PATH, TILE_SIZE, TILE_SIZE);
        insertStringAndScaledIcon(gameGuide, gameGuideDocument, gameGuideSet, "\nTrap: ", TRAP_RESOURCE_PATH, TILE_SIZE, TILE_SIZE);
        insertStringAndScaledIcon(gameGuide, gameGuideDocument, gameGuideSet, "\nPortal: ", PORTAL_RESOURCE_PATH, TILE_SIZE, TILE_SIZE);
        insertStringAndScaledIcon(gameGuide, gameGuideDocument, gameGuideSet, "\nPortal Key: ", PORTAL_KEY_RESOURCE_PATH, TILE_SIZE, TILE_SIZE);
        insertStringAndScaledIcon(gameGuide, gameGuideDocument, gameGuideSet, "\nLimited view: ", QUESTION_MARK_RESOURCE_PATH, TILE_SIZE, TILE_SIZE);

        StyleConstants.setLineSpacing(gameGuideSet, 0.4f);
        gameGuideDocument.setParagraphAttributes(0, gameGuideDocument.getLength(), gameGuideSet, false);
        gameGuide.setBounds(380, 130, 300, 250);
        return gameGuide;
    }

    /**
     * Initializes and returns a JTextPane for displaying user inputs.
     *
     * @return the initialized JTextPane
     */
    private JTextPane setupInputsTextPane() {
        JTextPane inputsTextPane = new JTextPane();
        inputsTextPane.setText(INPUTS_GUIDE);
        inputsTextPane.setFont(new Font("Arial", Font.BOLD, 20));
        StyledDocument inputsTextPaneStyledDocument = inputsTextPane.getStyledDocument();
        SimpleAttributeSet inputsSet = new SimpleAttributeSet();
        StyleConstants.setLineSpacing(inputsSet, 0.4f);
        inputsTextPaneStyledDocument.setParagraphAttributes(0, inputsTextPaneStyledDocument.getLength(), inputsSet, false);
        inputsTextPane.setOpaque(true);
        inputsTextPane.setEditable(false);
        inputsTextPane.setBackground(Color.WHITE);
        inputsTextPane.setBorder(null);
        inputsTextPane.setBounds(30, 130, 350, 250);
        return inputsTextPane;
    }

    /**
     * Sets up key bindings for the content pane.
     */
    private void setupKeyBindings() {
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();
        inputMap.put(KeyStroke.getKeyStroke("ESCAPE"), "Give up");
        actionMap.put("Give up", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleGiveUpAction(e);
            }
        });
    }

    /**
     * Initializes and returns a JButton for the "Give Up" action.
     *
     * @return the initialized JButton
     */
    private JButton setupGiveUpButton() {
        JButton giveUpButton = new JButton("Give Up");
        giveUpButton.setFocusPainted(false);
        giveUpButton.setFont(new Font("Arial", Font.BOLD, 20));
        giveUpButton.setBorder(BorderFactory.createLineBorder(Color.RED, 3, true));
        giveUpButton.setBackground(Color.BLACK);
        giveUpButton.setForeground(Color.WHITE);
        giveUpButton.setOpaque(true);
        giveUpButton.setToolTipText("Never Give up!");
        giveUpButton.addActionListener(this::handleGiveUpAction);
        giveUpButton.setBounds(380, 10, 300, 100);
        return giveUpButton;
    }

    /**
     * Initializes and returns a JScrollPane with the specified view component.
     *
     * @param viewPortView the component to be placed in the viewport
     * @return the initialized JScrollPane
     */
    private JScrollPane initJScrollPane(JComponent viewPortView) {
        JScrollPane ScrollPane = new JScrollPane(viewPortView);
        ScrollPane.setBounds(viewPortView.getBounds());
        ScrollPane.setBorder(null);
        ScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        ScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        ScrollPane.getHorizontalScrollBar().setUnitIncrement(16);
        ScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        return ScrollPane;
    }

    /**
     * Starts the game timer.
     */
    private void startGameTimer() {
        elapsedSeconds = 0;
        gameTimer.start();
    }

    /**
     * Stops the game timer.
     */
    public void stopGameTimer() {
        gameTimer.stop();
    }

    /**
     * Inserts text and a scaled icon into the specified JTextPane.
     *
     * @param textPane   the JTextPane to insert into
     * @param doc        the document associated with the JTextPane
     * @param set        the attribute set for the text
     * @param text       the text to insert
     * @param imagePath  the path to the image to be used as an icon
     * @param imageWidth the width to scale the icon to
     * @param imageHeight the height to scale the icon to
     * @return true if insertion was successful, false otherwise
     */
    private boolean insertStringAndScaledIcon(JTextPane textPane, StyledDocument doc, SimpleAttributeSet set, String text, String imagePath, int imageWidth, int imageHeight) {
        try {
            doc.insertString(doc.getLength(), text, set);
            textPane.setCaretPosition(doc.getLength());
            textPane.insertIcon(new ImageIcon(new ImageIcon(imagePath).getImage().getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH)));
            return true;
        } catch (BadLocationException e) {
            showOutput(this, e.getMessage());
            return false;
        }
    }

    /**
     * Inserts text and a painted rectangle icon into the specified JTextPane.
     *
     * @param textPane   the JTextPane to insert into
     * @param doc        the document associated with the JTextPane
     * @param set        the attribute set for the text
     * @param text       the text to insert
     * @param rectColor  the color of the rectangle
     * @param rectWidth  the width of the rectangle
     * @param rectHeight the height of the rectangle
     * @return true if insertion was successful, false otherwise
     */
    private boolean insertStringAndPaintedRectangleIcon(JTextPane textPane, StyledDocument doc, SimpleAttributeSet set, String text, Color rectColor, int rectWidth, int rectHeight) {
        try {
            doc.insertString(doc.getLength(), text, set);
            Icon rectangle = new Icon() {
                @Override
                public void paintIcon(Component c, Graphics g, int x, int y) {
                    g.setColor(rectColor);
                    g.fillRect(x, y, rectWidth, rectHeight);
                }

                @Override
                public int getIconWidth() {
                    return rectWidth;
                }

                @Override
                public int getIconHeight() {
                    return rectHeight;
                }
            };
            textPane.setCaretPosition(doc.getLength());
            textPane.insertIcon(rectangle);

            return true;
        } catch (BadLocationException e) {
            showOutput(this, e.getMessage());
            return false;
        }
    }

    /**
     * Initializes and returns a JLabel for displaying the player's username.
     *
     * @param username the player's username
     * @return the initialized JLabel
     */
    private JLabel setupUsernameLabel(String username) {
        JLabel userNameLabel = new JLabel("Username: " + username);
        userNameLabel.setFont(new Font("Arial", Font.BOLD, 20));
        userNameLabel.setBounds(30, 10, 300, 100);
        userNameLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        userNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        return userNameLabel;
    }

    /**
     * Handles the "Give Up" action, which resets the content pane to the start screen.
     *
     * @param e the ActionEvent triggered by the user
     */
    private void handleGiveUpAction(ActionEvent e) {
        new StartContentPane(screenRes, frame);
    }

    /**
     * Updates the content pane with the latest player information.
     *
     * @param player the current player instance
     */
    @Override
    public void update(Player player) {
        hpLabel.setText("HP: " + player.getHP());
        stepsLabel.setText("Steps: " + player.getSteps());
        remainingJumpAbilityLabel.setText("Jump Ability: " + player.getJumpAbilityCount());
        remainingWallBreakerAbilityLabel.setText("Wall Breaker Ability: " + player.getWallBreakerAbilityCount());
        keysTextPane.setText("Keys: " + (player.getKeys().isEmpty() ? "None" : ""));
        StyledDocument keysDocument = keysTextPane.getStyledDocument();
        SimpleAttributeSet keysSet = new SimpleAttributeSet();
        for (int i = 0; i < player.getKeys().size(); i++) {
            String key = player.getKeys().get(i);
            insertStringAndScaledIcon(keysTextPane, keysDocument, keysSet, "", key, TILE_SIZE, TILE_SIZE);
        }

        repaint();
    }

    @Override
    public void onJumpModeToggled(boolean jumpMode) {
        jumpModeTriggerLabel.setText("Jump Mode: " + (jumpMode ? "ON" : "OFF"));
    }

    @Override
    public void onWallBreakerModeToggled(boolean wallBreakerMode) {
        wallBreakerModeTriggerLabel.setText("WallBreaker Mode: " + (wallBreakerMode ? "ON" : "OFF"));
    }

}

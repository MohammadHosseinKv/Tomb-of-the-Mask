package GUI;

import static util.Util.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * The LeaderBoardContentPane class is responsible for displaying the leaderboard panel in the game's user interface.
 * It presents the top players' scores in a scrollable list, allowing users to view rankings and achievements.
 * The panel is designed to be visually engaging, with different styles applied to the top three players for emphasis.
 *
 * <p>This class extends JLayeredPane to support layered components, ensuring that the leaderboard and its controls
 * are presented in a well-organized and visually appealing manner.</p>
 *
 * <p>Key features of this class include:</p>
 * <ul>
 *   <li><b>Leaderboard Display:</b> The leaderboard is presented as a vertical list of player scores, with special formatting
 *   applied to the top three players. Each entry is shown with a rank and the player's score, allowing users to easily
 *   identify their position in the rankings.</li>
 *   <li><b>Scrollable Panel:</b> The leaderboard is contained within a JScrollPane, enabling users to scroll through the list
 *   of top players. This ensures that the UI remains clean and navigable, even when there are many players listed.</li>
 *   <li><b>Back Button:</b> A "Back" button is provided to allow users to return to the main menu. The button is styled
 *   distinctly and is positioned at the bottom of the screen for easy access.</li>
 *   <li><b>Keyboard Shortcuts:</b> The "ESCAPE" key is mapped to trigger the "Back" action, providing a convenient way for users
 *   to return to the main menu without using the mouse. Additional shortcuts are provided for scrolling through the leaderboard.</li>
 * </ul>
 *
 * <p>This class enhances the user's experience by providing a well-designed and functional leaderboard
 * that encourages competition and tracks player progress.</p>
 */

public class LeaderBoardContentPane extends JLayeredPane {
    DisplayMode screenRes;
    JFrame frame;

    /**
     * Constructs a new LeaderBoardContentPane with the specified screen resolution and JFrame.
     *
     * <p>This constructor sets up the leaderboard display, including the retrieval of player scores,
     * the creation of a scrollable leaderboard panel, and the addition of a back button.
     * It also manages input mappings for keyboard shortcuts.</p>
     *
     * @param screenRes The display mode of the screen, used to determine the resolution for scaling the UI components.
     * @param frame     The main JFrame of the application, to which this content pane is attached.
     */
    public LeaderBoardContentPane(DisplayMode screenRes, JFrame frame) {
        super();
        this.screenRes = screenRes;
        this.frame = frame;
        setPreferredSize(new Dimension(screenRes.getWidth() / 2, screenRes.getHeight() / 2));
        setLayout(null);
        StringBuilder leaderboardFileContents = getLeaderBoardFileContents();
        JScrollPane scrollPane = setupScrollPane(setupLeaderBoardPanel(leaderboardFileContents));
        add(scrollPane);

        add(createBackButton());

        setupKeyboardShortcuts(scrollPane);
        frame.setContentPane(this);
        frame.pack();
        centralizeFrame(frame);
    }

    /**
     * Configures the keyboard shortcuts for the leaderboard pane.
     *
     * <p>This method maps the "ESCAPE", "UP", and "DOWN" keys to their respective actions,
     * allowing the user to navigate the leaderboard and return to the main menu via the keyboard.</p>
     *
     * @param scrollPane The JScrollPane containing the leaderboard, to which the scrolling actions are applied.
     */
    private void setupKeyboardShortcuts(JScrollPane scrollPane) {
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();
        inputMap.put(KeyStroke.getKeyStroke("ESCAPE"), "Back");
        actionMap.put("Back", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleBackAction(e);
            }
        });

        inputMap.put(KeyStroke.getKeyStroke("UP"), "ScrollUP");
        actionMap.put("ScrollUP", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleScrollAction(e, scrollPane, KeyStroke.getKeyStroke("UP").getKeyCode());
            }
        });

        inputMap.put(KeyStroke.getKeyStroke("DOWN"), "ScrollDown");
        actionMap.put("ScrollDown", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleScrollAction(e, scrollPane, KeyStroke.getKeyStroke("DOWN").getKeyCode());
            }
        });
    }

    /**
     * Handles the scrolling action for the leaderboard based on user input.
     *
     * <p>This method adjusts the scrollbar position in the JScrollPane based on the key pressed (UP or DOWN).</p>
     *
     * @param e         The ActionEvent triggered by pressing the scroll keys.
     * @param scrollPane The JScrollPane containing the leaderboard, which will be scrolled.
     * @param keyCode   The keyCode of the key pressed, determining the scroll direction.
     */
    private void handleScrollAction(ActionEvent e, JScrollPane scrollPane, int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_UP:
                scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getValue() - 20);
                break;
            case KeyEvent.VK_DOWN:
                scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getValue() + 20);
                break;
        }
    }

    /**
     * Handles the action performed when the "Back" button is clicked or the "ESCAPE" key is pressed.
     * <p>
     * This method resets the content pane to the StartContentPane, effectively returning the user to
     * the main menu of the game. It ensures a smooth transition back to the start screen.</p>
     *
     * @param e The ActionEvent triggered by clicking the "Back" button or pressing the "ESCAPE" key.
     */
    private void handleBackAction(ActionEvent e) {
        new StartContentPane(screenRes, frame);
    }

    /**
     * Sets up the leaderboard panel, creating and formatting each entry based on the retrieved
     * leaderboard data. The top three entries are styled with distinct borders and background colors
     * to highlight their positions.
     *
     * <p>This method is responsible for dynamically generating the UI components that represent the leaderboard.
     * It ensures that each player's score is displayed prominently, with special emphasis on the top performers.</p>
     *
     * @param leaderboardFileContents The contents of the leaderboard file, used to populate the leaderboard panel.
     * @return The JPanel containing the formatted leaderboard entries.
     */
    private JPanel setupLeaderBoardPanel(StringBuilder leaderboardFileContents) {
        JPanel leaderBoardPanel = new JPanel();
        leaderBoardPanel.setLayout(new BoxLayout(leaderBoardPanel, BoxLayout.Y_AXIS));

        if (leaderboardFileContents.length() > 0) {
            String[] fileContentsSplitByLine = leaderboardFileContents.toString().split("\\n");
            for (int i = 0; i < fileContentsSplitByLine.length; i++) {
                JLabel lineLabel = createLeaderboardEntry(fileContentsSplitByLine[i], i);
                leaderBoardPanel.add(lineLabel);
                leaderBoardPanel.add(Box.createVerticalStrut(40));
            }
        }

        leaderBoardPanel.setBackground(new Color(0x2F80BA));
        return leaderBoardPanel;
    }

    /**
     * Creates and styles a JLabel for a leaderboard entry.
     *
     * <p>This method assigns a border and background color to the JLabel based on the rank of the player.
     * The top three ranks are highlighted with distinct colors, while others have a default style.</p>
     *
     * @param content The content of the leaderboard entry (e.g., player name and score).
     * @param rank    The rank of the player, used to determine the styling.
     * @return The styled JLabel representing a single leaderboard entry.
     */
    private JLabel createLeaderboardEntry(String content, int rank) {
        JLabel lineLabel = new JLabel((rank + 1) + "- " + content);
        lineLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        lineLabel.setAlignmentY(Component.CENTER_ALIGNMENT);

        switch (rank) {
            case 0 :
                applyStyle(lineLabel, new Color(0xDDD649), new Color(0xF0FF10));
                break;
            case 1 :
                applyStyle(lineLabel, Color.lightGray, Color.GRAY);
                break;
            case 2 :
                applyStyle(lineLabel, new Color(0xB68656), new Color(0x95672B));
                break;
            default :
                applyStyle(lineLabel, Color.BLUE, Color.WHITE);
                break;
        }

        lineLabel.setOpaque(true);
        lineLabel.setMaximumSize(new Dimension((int) (getPreferredSize().width / 1.2), 200));
        lineLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lineLabel.setVerticalAlignment(SwingConstants.CENTER);
        lineLabel.setFont(new Font("Arial", Font.BOLD, 20));
        return lineLabel;
    }

    /**
     * Applies a specific style to a JLabel, including border and background color.
     *
     * <p>This method is used by {@link #createLeaderboardEntry(String, int)} to style the leaderboard entries based on rank.</p>
     *
     * @param label         The JLabel to which the style will be applied.
     * @param borderColor   The color of the border around the JLabel.
     * @param backgroundColor The background color of the JLabel.
     */
    private void applyStyle(JLabel label, Color borderColor, Color backgroundColor) {
        label.setBorder(BorderFactory.createLineBorder(borderColor, 5, true));
        label.setBackground(backgroundColor);
    }

    /**
     * Configures the JScrollPane that contains the leaderboard panel.
     * <p>
     * This method sets the scroll behavior for both horizontal and vertical scrollbars, ensuring that the leaderboard is easily
     * navigable regardless of the number of entries.</p>
     *
     * @param leaderBoardPanel The JPanel containing the leaderboard entries.
     * @return The configured JScrollPane that wraps the leaderboard panel.
     */
    private JScrollPane setupScrollPane(JPanel leaderBoardPanel) {
        JScrollPane leaderBoardScrollPane = new JScrollPane(leaderBoardPanel);
        setOpaque(true);
        leaderBoardScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        leaderBoardScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        leaderBoardScrollPane.getHorizontalScrollBar().setUnitIncrement(16);
        leaderBoardScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        leaderBoardScrollPane.setBounds(0, 0, getPreferredSize().width, getPreferredSize().height - 50);
        return leaderBoardScrollPane;
    }

    /**
     * Creates and configures the "Back" button, which returns the user to the main menu.
     * <p>
     * This method sets the button's text, colors, font, and action listener.
     *
     * @return The configured JButton for the "Back" action.
     */
    private JButton createBackButton(){
        JButton backButton = new JButton("Back");
        backButton.setOpaque(true);
        backButton.setBackground(Color.BLACK);
        backButton.setForeground(Color.WHITE);
        backButton.setBorder(BorderFactory.createLineBorder(Color.RED, 3, true));
        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.setFocusable(false);
        backButton.setBounds(0, getPreferredSize().height - 50, getPreferredSize().width, 50);
        backButton.addActionListener(this::handleBackAction);
        return backButton;
    }


}

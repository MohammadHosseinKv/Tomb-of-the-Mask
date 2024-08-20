package GUI;

import logic.Maze;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import static util.Constants.*;
import static util.Util.*;
import static model.SimpleEntity.*;

/**
 * The {@code MazeRenderer} class is a custom Swing {@code JPanel} responsible for rendering
 * the maze and handling user inputs in the maze game. It provides functionality for painting
 * the maze, the player, and other entities on the screen. The class also handles key bindings
 * for player movement and toggling game modes like jump mode and wall breaker mode.
 *
 * <p>Key features include:</p>
 * <ul>
 *   <li>Rendering the maze and entities (walls, paths, traps, portals, etc.)</li>
 *   <li>Handling player movement and actions through key bindings</li>
 *   <li>Limiting the player's vision based on the game rules</li>
 * </ul>
 *
 * <p>This class is intended to be used as a part of the game's GUI and should be added to a {@code JFrame} or another container.</p>
 *
 * @see JPanel
 * @see Maze
 * @see Player
 * @see EntityType
 */
public class MazeRenderer extends JPanel {

    /**
     * The {@code Maze} object representing the current state of the maze.
     */
    private Maze maze;

    /**
     * Constructs a {@code MazeRenderer} object with the specified {@code Maze}.
     *
     * @param maze the {@code Maze} object representing the maze to be rendered.
     */
    public MazeRenderer(Maze maze) {
        this.maze = maze;
        setPreferredSize(new Dimension(TILE_SIZE * MAZE_SIZE, TILE_SIZE * MAZE_SIZE));
        setFocusable(true);
        setOpaque(true);
        setupKeyBindings();
    }

    /**
     * Sets up key bindings for player actions. The following keys are mapped:
     * <ul>
     *   <li>W or UP arrow: Move upward</li>
     *   <li>S or DOWN arrow: Move downward</li>
     *   <li>A or LEFT arrow: Move left</li>
     *   <li>D or RIGHT arrow: Move right</li>
     *   <li>J: Toggle jump mode</li>
     *   <li>G: Toggle wall breaker mode</li>
     * </ul>
     */
    private void setupKeyBindings() {
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();

        inputMap.put(KeyStroke.getKeyStroke("W"), "MoveUpward");
        inputMap.put(KeyStroke.getKeyStroke("UP"), "MoveUpward");
        actionMap.put("MoveUpward", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                maze.doAction(upDirection, 1, 2);
            }
        });

        inputMap.put(KeyStroke.getKeyStroke("S"), "MoveDownward");
        inputMap.put(KeyStroke.getKeyStroke("DOWN"), "MoveDownward");
        actionMap.put("MoveDownward", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                maze.doAction(downDirection, 1, 2);
            }
        });

        inputMap.put(KeyStroke.getKeyStroke("A"), "MoveLeft");
        inputMap.put(KeyStroke.getKeyStroke("LEFT"), "MoveLeft");
        actionMap.put("MoveLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                maze.doAction(leftDirection, 1, 2);
            }
        });

        inputMap.put(KeyStroke.getKeyStroke("D"), "MoveRight");
        inputMap.put(KeyStroke.getKeyStroke("RIGHT"), "MoveRight");
        actionMap.put("MoveRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                maze.doAction(rightDirection, 1, 2);
            }
        });

        inputMap.put(KeyStroke.getKeyStroke("J"), "toggleJumpMode");
        actionMap.put("toggleJumpMode", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                maze.toggleJumpMode();
            }
        });

        inputMap.put(KeyStroke.getKeyStroke("G"), "toggleWallBreakerMode");
        actionMap.put("toggleWallBreakerMode", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                maze.toggleWallBreakerMode();
            }
        });
    }

    /**
     * Overrides the {@code paintComponent} method to render the maze and the player on the panel.
     * This method is called whenever the panel needs to be repainted.
     *
     * @param g the {@code Graphics} object used to draw the maze and the player.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintMaze(g, Player.getInstance().getSteps() > 0);
//        paintMaze(g,false);
    }

    /**
     * Renders the maze and entities within it. This method paints each tile of the maze
     * based on the current state, and also limits the player's vision if necessary.
     *
     * @param g the {@code Graphics} object used to draw the maze and entities.
     * @param LimitedVision a boolean flag indicating whether the player's vision is limited.
     */
    private void paintMaze(Graphics g, boolean LimitedVision) {
        EntityType[][] Maze = maze.getMaze();
        Player player = Player.getInstance();
        int playerX = player.getX();
        int playerY = player.getY();
        if(maze.isGameOver()) LimitedVision = false;
        for (int i = 0; i < MAZE_SIZE; i++) {
            for (int j = 0; j < MAZE_SIZE; j++) {
                if (LimitedVision) {
                    if (playerX + VIEW_RADIUS >= i && playerX - VIEW_RADIUS <= i && playerY + VIEW_RADIUS >= j && playerY - VIEW_RADIUS <= j) {
                        paintEntities(g, Maze, i, j);
                    } else {
                        ImageIcon imageIcon = new ImageIcon(QUESTION_MARK_RESOURCE_PATH);
                        Image image = imageIcon.getImage();
                        g.drawImage(image, j * TILE_SIZE, i * TILE_SIZE, TILE_SIZE, TILE_SIZE, null, null);
                    }
                } else {
                    paintEntities(g, Maze, i, j);
                }

            }
        }

        // Draw player at last
        g.setColor(PLAYER_COLOR);
        g.fillRect(playerY * TILE_SIZE, playerX * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }

    /**
     * Renders individual entities in the maze. Depending on the type of entity (wall, path, treasure, etc.),
     * the appropriate color or image is drawn on the tile.
     *
     * @param g the {@code Graphics} object used to draw the entities.
     * @param maze a 2D array representing the maze's grid and its entities.
     * @param i the row index of the current tile.
     * @param j the column index of the current tile.
     */
    private void paintEntities(Graphics g, EntityType[][] maze, int i, int j) {
        String type = maze[i][j].getType();
        if (type.equals(WALL.getType())) {
            g.setColor(WALL_COLOR);
        } else if (type.equals(TREASURE.getType())) {
            g.drawImage(new ImageIcon(TREASURE_RESOURCE_PATH).getImage(), j * TILE_SIZE, i * TILE_SIZE, TILE_SIZE, TILE_SIZE, null, null);
            return;
        } else if (type.equals(TAKEN_PATH.getType())) {
            g.setColor(TAKEN_PATH_COLOR);
        } else if (type.equals(PATH.getType())) {
            g.setColor(PATH_COLOR);
        } else if (type.equals(TRAP.getType())) {
            g.drawImage(new ImageIcon(TRAP_RESOURCE_PATH).getImage(), j * TILE_SIZE, i * TILE_SIZE, TILE_SIZE, TILE_SIZE, null, null);
            return;
        } else if (maze[i][j] instanceof Portal) {
            g.drawImage(new ImageIcon(((Portal) maze[i][j]).getPortalIdentifier()).getImage(), j * TILE_SIZE, i * TILE_SIZE, TILE_SIZE, TILE_SIZE, null, null);
            return;
        } else if (maze[i][j] instanceof Key) {
            g.drawImage(new ImageIcon(((Key) maze[i][j]).getKeyIdentifier()).getImage(), j * TILE_SIZE, i * TILE_SIZE, TILE_SIZE, TILE_SIZE, null, null);
            return;
        }
        g.fillRect(j * TILE_SIZE, i * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }
}

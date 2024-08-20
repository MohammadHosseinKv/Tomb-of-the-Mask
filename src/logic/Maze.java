package logic;

import GUI.*;
import model.*;

import static model.SimpleEntity.*;
import static util.Constants.*;
import static util.Util.*;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * The {@code Maze} class is responsible for constructing the maze and handling the player gameplay logic.
 * It manages the player's movements, interactions with maze elements like traps, portals, and treasures,
 * and communicates with observers about changes in the maze state.
 */

public class Maze {

    /**
     * List of observers monitoring changes in the maze state.
     */
    private List<MazeObserver> observers = new ArrayList<>();

    /**
     * Listener for maze-related events.
     */
    MazeListener mazeListener;

    /**
     * The content pane for displaying the maze in the UI.
     */
    MazeContentPane mazeContentPane;

    /**
     * The maze renderer that renders the maze entities.
     */
    MazeRenderer mazeRenderer;

    /**
     * The player navigating the maze.
     */
    Player player;

    /**
     * The display mode for the screen resolution.
     */
    DisplayMode screenRes;

    /**
     * The JFrame that contains the maze UI.
     */
    JFrame frame;

    /**
     * The start time of the game in milliseconds.
     */
    long startTime;

    /**
     * The 2D array representing the maze grid with various entities.
     */
    private EntityType[][] maze = new EntityType[MAZE_SIZE][MAZE_SIZE];

    /**
     * The 2D array to track visited cells during maze generation.
     */
    private boolean[][] visited = new boolean[MAZE_SIZE][MAZE_SIZE];

    /**
     * Indicates whether the player is in jump mode.
     */
    private boolean jumpMode;

    /**
     * Indicates whether the player is in wall-breaker mode.
     */
    private boolean wallBreakerMode;

    /**
     * Indicates whether the game is over or not.
     */
    private boolean gameOver;

    /**
     * Constructs a new {@code Maze} object, initializes the maze, places traps, portals, and treasure,
     * and sets the start time.
     *
     * @param screenRes The display mode of the screen resolution.
     * @param frame     The JFrame that contains the maze UI.
     * @param username  The username of the player.
     */
    public Maze(DisplayMode screenRes, JFrame frame, String username) {
        this.screenRes = screenRes;
        this.frame = frame;
        player = Player.initInstance("Player", username, MAZE_SIZE - 2, 1);
        initializeMaze();
        generateMaze(MAZE_SIZE - 2, 1);
        placeTraps(TRAP_SPAWN_COUNT);
        placePortals(PORTAL_RESOURCE_PATH, PORTAL_KEY_RESOURCE_PATH, 4);
        placeTreasure();
        this.startTime = System.currentTimeMillis();
    }

    /**
     * Toggles the player's jump mode. If the player has jump abilities available,
     * it activates or deactivates the jump mode. If wall-breaker mode is active,
     * it will deactivate it before toggling jump mode.
     */
    public void toggleJumpMode() {
        if (player.getJumpAbilityCount() > 0) {
            if (wallBreakerMode) {
                wallBreakerMode = false;
                if (mazeListener != null) mazeListener.onWallBreakerModeToggled(wallBreakerMode);
            }
            jumpMode = !jumpMode;
            if (mazeListener != null) mazeListener.onJumpModeToggled(jumpMode);
        }
    }

    /**
     * Toggles the player's wall-breaker mode. If the player has wall-breaker abilities available,
     * it activates or deactivates the wall-breaker mode. If jump mode is active,
     * it will deactivate it before toggling wall-breaker mode.
     */
    public void toggleWallBreakerMode() {
        if (player.getWallBreakerAbilityCount() > 0) {
            if (jumpMode) {
                jumpMode = false;
                if (mazeListener != null) mazeListener.onJumpModeToggled(jumpMode);
            }
            wallBreakerMode = !wallBreakerMode;
            if (mazeListener != null) mazeListener.onWallBreakerModeToggled(wallBreakerMode);
        }
    }

    /**
     * Executes the player's action based on the current mode (normal, jump, or wall-breaker) and the specified direction.
     * Updates the player's position and health, destroys walls, or moves through portals as necessary.
     *
     * @param direction The direction in which the player wishes to move, represented as an array [x, y].
     * @param moveCount The number of tiles the player wishes to move.
     * @param jumpTile  The number of tiles the player wishes to jump, if in jump mode.
     */
    public void doAction(int[] direction, int moveCount, int jumpTile) {
        int playerX = player.getX();
        int playerY = player.getY();
        int directionX = direction[0];
        int directionY = direction[1];
        int moveX = playerX + directionX * moveCount;
        int moveY = playerY + directionY * moveCount;
        int jumpX = playerX + directionX * jumpTile;
        int jumpY = playerY + directionY * jumpTile;
        if (jumpMode) {
            if (player.getJumpAbilityCount() > 0)
                player.setJumpAbilityCount(player.getJumpAbilityCount() - 1);
            else {
                jumpMode = false;
                return;
            }
            move(playerX, playerY, jumpX, jumpY);
            jumpMode = false;
            if (mazeListener != null) mazeListener.onJumpModeToggled(jumpMode);
        } else if (wallBreakerMode) {
            if (player.getWallBreakerAbilityCount() > 0)
                player.setWallBreakerAbilityCount(player.getWallBreakerAbilityCount() - 1);
            else {
                wallBreakerMode = false;
                return;
            }
            destroyWall(moveX, moveY);
            wallBreakerMode = false;
            if (mazeListener != null) mazeListener.onWallBreakerModeToggled(wallBreakerMode);
        } else move(playerX, playerY, moveX, moveY);
        notifyObservers();
        checkGameCondition();
    }

    /**
     * Moves the player from the current position to the destination position.
     * Handles interactions with different maze elements such as walls, traps, portals, and keys.
     *
     * @param curX  The current X-coordinate of the player.
     * @param curY  The current Y-coordinate of the player.
     * @param destX The destination X-coordinate.
     * @param destY The destination Y-coordinate.
     * @return {@code true} if the move was successful, {@code false} otherwise.
     */
    private boolean move(int curX, int curY, int destX, int destY) {
        if (destX >= 0 && destY >= 0 && destX < MAZE_SIZE - 1 && destY < MAZE_SIZE - 1) {
            String type = maze[destX][destY].getType();
            if (type.equals(WALL.getType())) {
                player.setHP(player.getHP() - 1);
                return false;
            } else if (type.equals(TRAP.getType())) {
                player.setHP(player.getHP() - 1);
                maze[destX][destY] = PATH;
                return move(curX, curY, destX, destY);
            } else if (type.equals(PATH.getType()) || type.equals(TAKEN_PATH.getType()) || type.equals(TREASURE.getType())) {
                player.setPlayerCord(destX, destY);
                player.setSteps(player.getSteps() + 1);
                if (maze[curX][curY] == PATH) maze[curX][curY] = TAKEN_PATH;
                return true;
            } else if (maze[destX][destY] instanceof Portal) {
                if (player.getKeys().contains(((Portal) maze[destX][destY]).getPortalKeyIdentifier())) {
                    Portal portalToTeleport = ((Portal) maze[destX][destY]).getRandomLinkedPortal();
                    if (portalToTeleport != null) {
                        Collections.shuffle(Arrays.asList(directions));
                        for (int[] direction : directions) {
                            int directionX = direction[0];
                            int directionY = direction[1];
                            int newX = portalToTeleport.getX() + directionX;
                            int newY = portalToTeleport.getY() + directionY;
                            if (!maze[newX][newY].getType().equals(WALL.getType()))
                                if (move(curX, curY, newX, newY)) return true;
                        }
                    }
                }
            } else if (maze[destX][destY] instanceof Key) {
                player.addKey(((Key) maze[destX][destY]).getKeyIdentifier());
                maze[destX][destY] = PATH;
                return move(curX, curY, destX, destY);
            }

        }
        return false;
    }

    /**
     * Destroys the wall at the specified destination coordinates, turning it into a path.
     *
     * @param destX The X-coordinate of the wall to destroy.
     * @param destY The Y-coordinate of the wall to destroy.
     * @return {@code true} if the wall was successfully destroyed, {@code false} otherwise.
     */
    private boolean destroyWall(int destX, int destY) {
        if (destX >= 0 && destY >= 0 && destX < MAZE_SIZE - 1 && destY < MAZE_SIZE - 1) {
            if (maze[destX][destY] == WALL)
                maze[destX][destY] = PATH;
            return true;
        }
        return false;
    }

    /**
     * Initializes the maze with walls and sets the starting point.
     * The player start position is set to the second last row and second column.
     */
    private void initializeMaze() {
        for (EntityType[] mazeRow : maze) {
            Arrays.fill(mazeRow, WALL);
        }
        maze[MAZE_SIZE - 2][1] = PATH;
    }

    /**
     * Generates the maze using depth-first search starting from the given coordinates.
     * This creates a random path through the maze with some dead-ends.
     *
     * @param x The starting X-coordinate.
     * @param y The starting Y-coordinate.
     */
    private void generateMaze(int x, int y) {
        visited[x][y] = true;
        maze[x][y] = PATH;
        Collections.shuffle(Arrays.asList(directions));

        for (int[] direction : directions) {
            int directionX = direction[0];
            int directionY = direction[1];
            int newX = x + (directionX * 2);
            int newY = y + (directionY * 2);
            if (isValidMove(newX, newY)) {
                maze[x + directionX][y + directionY] = PATH; // Carve the path between
                generateMaze(newX, newY); // recursively generate maze until hit where move is not valid
            }
        }
    }

    private boolean isValidMove(int x, int y) {
        return x > 0 && x < MAZE_SIZE - 1 && y > 0 && y < MAZE_SIZE - 1 && !visited[x][y];
    }

    /**
     * Places a certain number of traps in the maze, based on the specified spawn ratio in {@link util.Constants} class.
     *
     * @param count The number of traps to place in the maze.
     */
    private void placeTraps(double count) {
        if (count <= 0) return; // recursive method base case
        int trapX, trapY;
        do {
            trapX = rand.nextInt(MAZE_SIZE - 2) + 1;
            trapY = rand.nextInt(MAZE_SIZE - 2) + 1;
        } while (maze[trapX][trapY] != PATH || (trapX == player.getX() && trapY == player.getY()));
        maze[trapX][trapY] = TRAP;
        placeTraps(--count); // recusively place traps until count is equal or lower than zero
    }

    /**
     * Places portals in the maze, linking each portal to other random portals.
     * Also places key that are required to use the portals.
     *
     * @param portalIdentifier    The resource path to load the portal images is used as portal identifier here.
     * @param portalKeyIdentifier The resource path to load the portal key images is used as key identifier here.
     * @param portalCount         The number of portals with same identifier to place in the maze.
     */
    private void placePortals(String portalIdentifier, String portalKeyIdentifier, int portalCount) {
        if (portalCount <= 0) { // recursive method base case
            placeKey(portalKeyIdentifier);
            return;
        }
        int portalX, portalY;
        do {
            portalX = rand.nextInt(MAZE_SIZE - 2) + 1;
            portalY = rand.nextInt(MAZE_SIZE - 2) + 1;
        } while (maze[portalX][portalY] != PATH || portalX == player.getX() || portalY == player.getY());
        maze[portalX][portalY] = new Portal("Portal", portalX, portalY, portalIdentifier, portalKeyIdentifier);
        placePortals(portalIdentifier, portalKeyIdentifier, --portalCount);
    }


    /**
     * Places portal key in the maze, linking each key with an identifier to portals with same identifier.
     *
     * @param keyIdentifier The resource path to load the portal key images is used as key identifier here.
     */
    private void placeKey(String keyIdentifier) {
        int keyX, keyY;
        do {
            keyX = rand.nextInt(MAZE_SIZE - 2) + 1;
            keyY = rand.nextInt(MAZE_SIZE - 2) + 1;
        } while (maze[keyX][keyY] != PATH || keyX == player.getX() || keyY == player.getY());
        maze[keyX][keyY] = new Key("Key", keyX, keyY, keyIdentifier);
    }

    /**
     * Places the treasure at a random position in the maze.
     */
    private void placeTreasure() {
        int treasureX, treasureY;
        do {
            treasureX = rand.nextInt(MAZE_SIZE - 2) + 1;
            treasureY = rand.nextInt(MAZE_SIZE - 2) + 1;
        } while (maze[treasureX][treasureY] != PATH || (treasureX == player.getX() && treasureY == player.getY()));
        maze[treasureX][treasureY] = TREASURE;
    }

    /**
     * Checks the condition of the game to determine if the player has won or lost.
     * If the player's health drops to zero, the game is lost. If the player reaches the treasure, the game is won.
     * Notifies the observers of the game result.
     */
    private void checkGameCondition() {

        if ((player.getHP() <= 0) || maze[player.getX()][player.getY()] == TREASURE) {
            gameOver = true;
            long endTime = System.currentTimeMillis();
            mazeContentPane.stopGameTimer();
            if (mazeRenderer != null) mazeRenderer.repaint();
            long time = (endTime - startTime) / 1000;
            if (player.getHP() <= 0) {
                showOutput(null, "Game over, you ran out of HP.\n" + player.getSteps() + " steps in " + time + " s");
                new StartContentPane(screenRes, frame);
            } else if (maze[player.getX()][player.getY()] == TREASURE) {
                showOutput(null, "You found the treasure! You win!\n" + player.getSteps() + " steps in " + time + " s");
                updateLeaderboard(player.getUsername(), player.getSteps(), time);
                new LeaderBoardContentPane(screenRes, frame);
            }
        }
    }

    /**
     * Updates the leaderboard with the player's performance after the game ends.
     * <p>
     * The method checks if the player has an existing record and updates it if the new
     * performance (time or steps) is better. If the player doesn't have a record, it
     * adds a new one. The records are sorted by time and, in case of a tie, by steps.
     *
     * @param username The player's username.
     * @param steps    The number of steps the player took to complete the maze.
     * @param time     The time taken by the player to complete the maze, in seconds.
     */
    private void updateLeaderboard(String username, int steps, long time) {
        try {
            StringBuilder leaderBoardFileContents = getLeaderBoardFileContents();
            List<String> playerRecords = leaderBoardFileContents.length() == 0 ? new ArrayList<>()
                    : new ArrayList<>(Arrays.asList(leaderBoardFileContents.toString().split("\\n")));
            for (int i = 0; i < playerRecords.size(); i++) {
                String playerRecord = playerRecords.get(i);
                if (playerRecord.toLowerCase().startsWith(username.toLowerCase())) {
                    long playerExistingRecordTime = Long.parseLong(playerRecord.split("\\|")[2].trim().split(" ")[0]);
                    if (time > playerExistingRecordTime) return;
                    else if (time == playerExistingRecordTime) {
                        int playerExistingRecordSteps = Integer.parseInt(playerRecord.split("\\|")[1].trim().split(" ")[0]);
                        if (steps >= playerExistingRecordSteps) return;
                        else playerRecords.remove(playerRecord);
                    } else playerRecords.remove(playerRecord);
                }
            }
            String playerRecord = username + " | " + steps + " steps | " + time + " s";
            playerRecords.add(playerRecord);
            playerRecords.sort(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    long o1Time = Long.parseLong(o1.split("\\|")[2].trim().split(" ")[0]);
                    long o2Time = Long.parseLong(o2.split("\\|")[2].trim().split(" ")[0]);
                    int o1Steps = Integer.parseInt(o1.split("\\|")[1].trim().split(" ")[0]);
                    int o2Steps = Integer.parseInt(o2.split("\\|")[1].trim().split(" ")[0]);
                    return o1Time == o2Time ? Integer.compare(o1Steps, o2Steps) : Long.compare(o1Time, o2Time);
                }
            });

            updateLeaderboardFile(playerRecords);
        } catch (Throwable e) {
            showOutput(null, e.getMessage());
        }
    }

    /**
     * @return returns the 2D array representing maze.
     */
    public EntityType[][] getMaze() {
        return maze;
    }

    /**
     * Adds an observer to the maze, allowing it to be notified of maze changes.
     *
     * @param observer The {@code MazeObserver} to be added.
     */
    public void registerObserver(MazeObserver observer) {
        observers.add(observer);
    }

    /**
     * Removes an observer from the maze.
     *
     * @param observer The {@code MazeObserver} to be removed.
     */
    public void removeObserver(MazeObserver observer) {
        observers.remove(observer);
    }

    /**
     * Notifies all registered observers of the current maze state.
     */
    private void notifyObservers() {
        for (MazeObserver o : observers) {
            o.update(player);
        }
    }

    /**
     * Sets a maze listener for maze, allowing it to be notified of maze changes.
     *
     * @param mazeListener The {@code MazeListener} to be set.
     */
    public void setMazeListener(MazeListener mazeListener) {
        this.mazeListener = mazeListener;
    }

    /**
     * Sets the content pane for displaying the maze in the UI.
     */
    public void setMazeContentPane(MazeContentPane mazeContentPane) {
        this.mazeContentPane = mazeContentPane;
    }

    /**
     * Sets the maze renderer to connect maze logic with maze UI.
     */
    public void setMazeRenderer(MazeRenderer mazeRenderer) {
        this.mazeRenderer = mazeRenderer;
    }

    /**
     * @return {@code true} if the game is over, {@code false} otherwise.
     */
    public boolean isGameOver() {
        return gameOver;
    }
}


# Tomb of the Mask

Tomb of the Mask is a maze-based adventure game that challenges players to navigate through randomly generated perfect maze while avoiding obstacles and collecting treasure. This project is a personal exploration into game development, focusing on implementing game mechanics, user interface design, and algorithmic problem-solving in Java.

## Features

- **Dynamic Maze Generation**: Each time user starts new game , a new maze will be generated using custom algorithms, ensuring a unique experience every time.
- **User-Friendly Interface**: Intuitive controls and a sleek, minimalist design provide a smooth gameplay experience.
- **Leaderboard Integration**: Tracks and displays the top scores, allowing players to compete for the highest rank.
- **Source Code Access**: The full source code with Java doc is available for exploration on GitHub.

## How It Works

### Maze Generation
The maze generation algorithm is at the core of the gameplay experience. It uses a depth-first search (DFS) algorithm combined with randomized decisions to ensure that the maze is both solvable and challenging. Here's a brief overview:

1. **Initialization**: The maze starts as a grid of walls.
2. **Randomized DFS**: The algorithm starts from player starting point, carving out a random path and marking visited cells.
3. **Backtracking**: When the algorithm hits a dead-end (either all adjacent cells are visited and or adjacent cells are outside maze bounds), it backtracks to the last cell with unvisited neighbors and continues the process until all maze cells are visited.
4. **Final Touches**: Treasure and additional obstacles are placed randomly in the maze to enhance difficulty and gameplay variety.

### Game Mechanics
- **Player Movement**: The player can move in four directions (up, down, left, right) using keyboard controls. The objective is to pick up the treasure while avoiding hitting traps and walls.
- **Obstacles**: Hitting obstacles like bombs and walls will decrease player's HP to increase the challenge.
- **Portal System**: Maze contain portals that teleport the player to different sections of the maze, adding a layer of complexity to navigation.

## Getting Started

### Prerequisites
- **Java Development Kit (JDK)** version 8 or higher.
- **IntelliJ IDEA** or any other Java IDE for easier navigation and development.

### Installation
1. Clone the repository:
    ```bash
    git clone https://github.com/MohammadHosseinKv/tomb-of-the-mask
    ```
2. Open the project in your preferred IDE.
3. Run the `Main.java` file to start the game.

### Running the Game
After launching, you'll be presented with options to start a new game, view the leaderboard, or access the source code on GitHub. Navigate using your keyboard or mouse, enter a username, and start playing!

## Project Structure

- **src/**: Contains the Java source files.
  - **GUI/**: Manages the user interface components.
  - **logic/**: Contains the maze generation and game logic.
  - **model/**: Defines the entities used in the game (e.g., Player, Key, Portal).
  - **util/**: Utility classes for common functions and constants.
- **resources/**: Static assets like images used in the game.

## Contributing
This project is open to contributions. Feel free to fork the repository, make changes, and submit a pull request. Whether you're fixing bugs, improving the UI, or adding new features, your contributions are welcome.

## License
This project is licensed under the MIT License. See the `LICENSE` file for more details.

## Contact
For any questions or feedback, you can reach me at [MohammadHossein.Kv@gmail.com] or through my GitHub profile [@MohammadHosseinKv](https://github.com/MohammadHosseinKv).

## Acknowledgements
- The game concept and name is inspired by Tomb of the Mask arcade game and Guilan University CE Advanced Programming project (PDF document of project is uploaded in repository).

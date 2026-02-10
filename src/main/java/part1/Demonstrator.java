package part1;
import java.util.ArrayList;
import java.util.List;


/**
 * Demonstrator
 * Runs a full game simulation.
 * Creates 4 computer players, builds the board, and starts the game.
 *
 * @author Team 28
 */

public class Demonstrator {

    /**
     * Program entry point.
     * Builds the main objects needed for the simulation and starts the game.
     *
     * @param args unused
     */

    public static void main(String[] args) {

        // max 8192 rounds (32768 turns)
        // 4 agents
        // the configuration is defined in a config file
        // The simulator shall print the actions taken by the agents on the console in the specified encoding
        // The simulator shall print the current victory points at the end of each round
        // Agents with more than 7 cards in their hand must try to spend those cards by building something.
        // implement a simple linear check of all the actions that can be executed, and pick one randomly.

        List<Player> players = new ArrayList<>();
        
    // Add 4 computer players (ids 0 to 3).
        players.add(new ComputerPlayer(0));
        players.add(new ComputerPlayer(1));
        players.add(new ComputerPlayer(2));
        players.add(new ComputerPlayer(3));

        Board board = new Board();
        Game game = new Game(board, players);

        game.startGame();

    }
}

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

        int turns = ConfigLoader.loadTurns("config.txt");

        Board board = new Board();

        List<Player> players = new ArrayList<>();

        
    // Add 4 computer players (ids 0 to 3).
        players.add(new ComputerPlayer(0));

        players.add(new ComputerPlayer(1));
        players.add(new ComputerPlayer(2));
        players.add(new ComputerPlayer(3));
        players.add(new ComputerPlayer(4));

        Game game = new Game(board, players, turns);
        game.startGame();
    }
}

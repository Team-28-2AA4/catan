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
        java.util.Scanner loopScanner = new java.util.Scanner(System.in);

        while (true)
        {
            Board board = new Board();

            List<Player> players = new ArrayList<>();

            // Add 1 human player and 3 computer players
            players.add(new HumanPlayer(0));
            players.add(new ComputerPlayer(1));
            players.add(new ComputerPlayer(2));
            players.add(new ComputerPlayer(3));

            Game game = new Game(board, players, turns);
            game.startGame();

            System.out.print("\nPlay again? (yes / no): ");
            String answer = loopScanner.nextLine().trim();
            if (!answer.equalsIgnoreCase("yes"))
            {
                System.out.println("Thanks for playing!");
                break;
            }
        }
    }
}

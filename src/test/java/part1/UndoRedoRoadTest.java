package part1;

import java.util.ArrayList;
import java.util.List;

/**
 * UndoRedoRoadTest
 * Simple test driver for road build undo/redo functionality.
 *
 * This test:
 * 1. creates a board and game
 * 2. gives a player enough resources to build one road
 * 3. executes a BuildRoadCommand
 * 4. undoes it
 * 5. redoes it
 *
 * Expected behavior:
 * - after execute: road exists, resources decrease
 * - after undo: road is removed, resources restored
 * - after redo: road exists again, resources decrease again
 */
public class UndoRedoRoadTest {

    public static void main(String[] args) {
        Board board = new Board();

        List<Player> players = new ArrayList<>();
        Player player1 = new HumanPlayer(1);
        players.add(player1);

        Game game = new Game(board, players);

        // Give player enough resources to build one road
        player1.addResource(ResourceType.LUMBER, 1);
        player1.addResource(ResourceType.BRICK, 1);

        int edgeIndex = 0;

        System.out.println("=== INITIAL STATE ===");
        printRoadState(board, edgeIndex);
        printPlayerRoadResources(player1);

        // Execute road build command
        GameCommand buildRoad = new BuildRoadCommand(game, board, player1, edgeIndex);
        game.getCommandManager().executeCommand(buildRoad);

        System.out.println("\n=== AFTER BUILD ROAD ===");
        printRoadState(board, edgeIndex);
        printPlayerRoadResources(player1);

        // Undo
        boolean undone = game.undo();
        System.out.println("\nUndo called: " + undone);

        System.out.println("\n=== AFTER UNDO ===");
        printRoadState(board, edgeIndex);
        printPlayerRoadResources(player1);

        // Redo
        boolean redone = game.redo();
        System.out.println("\nRedo called: " + redone);

        System.out.println("\n=== AFTER REDO ===");
        printRoadState(board, edgeIndex);
        printPlayerRoadResources(player1);
    }

    /**
     * Prints whether a road exists on the given edge.
     *
     * @param board game board
     * @param edgeIndex edge index being checked
     */
    private static void printRoadState(Board board, int edgeIndex) {
        Board.Road road = board.getRoad(edgeIndex);

        if (road == null) {
            System.out.println("Edge " + edgeIndex + ": no road");
        } else {
            System.out.println("Edge " + edgeIndex + ": road owned by player " + road.ownerPlayerId);
        }
    }

    /**
     * Prints the player's road-related resources and remaining road pieces.
     *
     * @param player player being checked
     */
    private static void printPlayerRoadResources(Player player) {
        System.out.println("Player " + player.getPlayerId()
                + " | LUMBER=" + player.getResourceCount(ResourceType.LUMBER)
                + " | BRICK=" + player.getResourceCount(ResourceType.BRICK)
                + " | roadsLeft=" + player.getRoadsCount());
    }
}
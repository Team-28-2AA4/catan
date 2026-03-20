package part1;

import java.util.ArrayList;
import java.util.List;

/**
 * UndoRedoCityTest
 * Simple test driver for city build undo/redo functionality.
 */
public class UndoRedoCityTest {

    public static void main(String[] args) {
        Board board = new Board();

        List<Player> players = new ArrayList<>();
        Player player1 = new HumanPlayer(1);
        players.add(player1);

        Game game = new Game(board, players);

        // First place a settlement directly so it can be upgraded
        board.placeSettlement(0, player1);

        // Give player enough resources to build one city
        player1.addResource(ResourceType.GRAIN, 2);
        player1.addResource(ResourceType.ORE, 3);

        int nodeId = 0;

        System.out.println("=== INITIAL STATE ===");
        printCityState(board, nodeId);
        printPlayerCityResources(player1);

        GameCommand buildCity = new BuildCityCommand(game, board, player1, nodeId);
        game.getCommandManager().executeCommand(buildCity);

        System.out.println("\n=== AFTER BUILD CITY ===");
        printCityState(board, nodeId);
        printPlayerCityResources(player1);

        boolean undone = game.undo();
        System.out.println("\nUndo called: " + undone);

        System.out.println("\n=== AFTER UNDO ===");
        printCityState(board, nodeId);
        printPlayerCityResources(player1);

        boolean redone = game.redo();
        System.out.println("\nRedo called: " + redone);

        System.out.println("\n=== AFTER REDO ===");
        printCityState(board, nodeId);
        printPlayerCityResources(player1);
    }

    private static void printCityState(Board board, int nodeId) {
        Board.Building building = board.getBuilding(nodeId);

        if (building == null) {
            System.out.println("Node " + nodeId + ": no building");
        } else {
            System.out.println("Node " + nodeId + ": " + building.kind + " owned by player " + building.ownerPlayerId);
        }
    }

    private static void printPlayerCityResources(Player player) {
        System.out.println("Player " + player.getPlayerId()
                + " | GRAIN=" + player.getResourceCount(ResourceType.GRAIN)
                + " | ORE=" + player.getResourceCount(ResourceType.ORE)
                + " | citiesLeft=" + player.getBuildingCount(BuildingKind.CITY)
                + " | victoryPoints=" + player.getVictoryPoints());
    }
}
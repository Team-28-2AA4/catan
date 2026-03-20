package part1;

import java.util.ArrayList;
import java.util.List;

/**
 * UndoRedoSettlementTest
 * Simple test driver for settlement build undo/redo functionality.
 */
public class UndoRedoSettlementTest {

    public static void main(String[] args) {
        Board board = new Board();

        List<Player> players = new ArrayList<>();
        Player player1 = new HumanPlayer(1);
        players.add(player1);

        Game game = new Game(board, players);

        player1.addResource(ResourceType.LUMBER, 1);
        player1.addResource(ResourceType.BRICK, 1);
        player1.addResource(ResourceType.WOOL, 1);
        player1.addResource(ResourceType.GRAIN, 1);

        int nodeId = 0;

        System.out.println("=== INITIAL STATE ===");
        printSettlementState(board, nodeId);
        printPlayerSettlementResources(player1);

        GameCommand buildSettlement = new BuildSettlementCommand(game, board, player1, nodeId);
        game.getCommandManager().executeCommand(buildSettlement);

        System.out.println("\n=== AFTER BUILD SETTLEMENT ===");
        printSettlementState(board, nodeId);
        printPlayerSettlementResources(player1);

        boolean undone = game.undo();
        System.out.println("\nUndo called: " + undone);

        System.out.println("\n=== AFTER UNDO ===");
        printSettlementState(board, nodeId);
        printPlayerSettlementResources(player1);

        boolean redone = game.redo();
        System.out.println("\nRedo called: " + redone);

        System.out.println("\n=== AFTER REDO ===");
        printSettlementState(board, nodeId);
        printPlayerSettlementResources(player1);
    }

    private static void printSettlementState(Board board, int nodeId) {
        Board.Building building = board.getBuilding(nodeId);

        if (building == null) {
            System.out.println("Node " + nodeId + ": no building");
        } else {
            System.out.println("Node " + nodeId + ": " + building.kind + " owned by player " + building.ownerPlayerId);
        }
    }

    private static void printPlayerSettlementResources(Player player) {
        System.out.println("Player " + player.getPlayerId()
                + " | LUMBER=" + player.getResourceCount(ResourceType.LUMBER)
                + " | BRICK=" + player.getResourceCount(ResourceType.BRICK)
                + " | WOOL=" + player.getResourceCount(ResourceType.WOOL)
                + " | GRAIN=" + player.getResourceCount(ResourceType.GRAIN)
                + " | settlementsLeft=" + player.getBuildingCount(BuildingKind.SETTLEMENT)
                + " | victoryPoints=" + player.getVictoryPoints());
    }
}
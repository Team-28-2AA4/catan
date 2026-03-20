package part1;

import java.util.ArrayList;
import java.util.List;

/**
 * UndoRedoMaritimeTradeTest
 * Simple test driver for maritime trade undo/redo functionality.
 */
public class UndoRedoMaritimeTradeTest {

    public static void main(String[] args) {
        Board board = new Board();

        List<Player> players = new ArrayList<>();
        Player player1 = new HumanPlayer(1);
        players.add(player1);

        Game game = new Game(board, players);

        player1.addResource(ResourceType.LUMBER, 4);
        game.returnToBank(ResourceType.BRICK, 19);

        System.out.println("=== INITIAL STATE ===");
        printPlayerTradeResources(player1, game);

        GameCommand tradeCommand = new MaritimeTradeCommand(game, player1, ResourceType.LUMBER, ResourceType.BRICK);
        game.getCommandManager().executeCommand(tradeCommand);

        System.out.println("\n=== AFTER MARITIME TRADE ===");
        printPlayerTradeResources(player1, game);

        boolean undone = game.undo();
        System.out.println("\nUndo called: " + undone);

        System.out.println("\n=== AFTER UNDO ===");
        printPlayerTradeResources(player1, game);

        boolean redone = game.redo();
        System.out.println("\nRedo called: " + redone);

        System.out.println("\n=== AFTER REDO ===");
        printPlayerTradeResources(player1, game);
    }

    private static void printPlayerTradeResources(Player player, Game game) {
        System.out.println("Player " + player.getPlayerId()
                + " | LUMBER=" + player.getResourceCount(ResourceType.LUMBER)
                + " | BRICK=" + player.getResourceCount(ResourceType.BRICK)
                + " | bankLUMBER=" + game.getBankCount(ResourceType.LUMBER)
                + " | bankBRICK=" + game.getBankCount(ResourceType.BRICK));
    }
}
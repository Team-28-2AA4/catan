package part1;

import java.util.List;

/**
 * ConsoleGameObserver
 * Prints game events to the console.
 * This includes:
 * - start of game messages
 * - turn summaries
 * - round summaries
 * - robber / discard / steal messages
 * - end of game messages
 *
 * @author Team 28
 */
public class ConsoleGameObserver implements GameObserver {

    @Override
    public void onGameStarted(Board board) {
        System.out.println("\nGame has started!\n");
    }

    @Override
    public void onTurnSummary(int roundNumber, int playerId, String action) {
        System.out.println("[" + roundNumber + "] / [" + playerId + "]: " + action);
    }

    @Override
    public void onRoundSummary(int roundNumber, List<Player> players) {
        StringBuilder summary = new StringBuilder();
        summary.append("Round ").append(roundNumber).append(" Summary: \n\n");

        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);
            summary.append("Player ").append(p.getPlayerId()).append(": ");

            for (int r = 0; r < ResourceType.values().length; r++) {
                ResourceType type = ResourceType.values()[r];
                summary.append(type.name()).append("=").append(p.getResourceCount(type));

                if (r < ResourceType.values().length - 1) {
                    summary.append(", ");
                }
            }

            summary.append(" | longestRoadStreak=").append(p.getLongestRoadStreak());
            summary.append(" | victoryPoints=").append(p.getVictoryPoints());
            summary.append("\n\n");
        }

        summary.append("---------------------------------------------------------------------------------\n\n");
        System.out.print(summary.toString());
    }

    @Override
    public void onInfoMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void onGameEnded(Player winner, int roundNumber, boolean maxRoundReached) {
        if (maxRoundReached) {
            System.out.println("\n\nGame Over! Max round reached.\n");
            return;
        }

        System.out.println("\n\nGame Over! Player " + winner.getPlayerId()
                + " reached " + winner.getVictoryPoints() + " victory points.\n");
    }
}

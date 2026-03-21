package part1;

import java.util.List;

/**
 * GameObserver
 * Observer interface for major game events such as:
 * - game start
 * - state changes
 * - turn summaries
 * - round summaries
 * - game end
 *
 * Concrete observers can react to these events in different ways,
 * for example by printing to the console or updating the visualizer.
 *
 * @author Team 28
 */
public interface GameObserver {

    default void onGameStarted(Board board) {
    }

    default void onStateChanged(Board board) {
    }

    default void onTurnSummary(int roundNumber, int playerId, String action) {
    }

    default void onRoundSummary(int roundNumber, List<Player> players) {
    }

    default void onInfoMessage(String message) {
    }

    default void onGameEnded(Player winner, int roundNumber, boolean maxRoundReached) {
    }
}

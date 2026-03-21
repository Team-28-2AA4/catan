package part1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Observer pattern integration in Game.
 * Verifies that Game notifies observers during startup and setup.
 */
public class GameObserverTest {

    @Test
    void startGameNotifiesObserversDuringSetup() {
        Board board = new Board();
        List<Player> players = new ArrayList<>();
        players.add(new ComputerPlayer(0));
        players.add(new ComputerPlayer(1));
        players.add(new ComputerPlayer(2));
        players.add(new ComputerPlayer(3));

        Game game = new Game(board, players, 2);
        RecordingObserver observer = new RecordingObserver();

        game.clearObservers();
        game.addObserver(observer);
        game.startGame();

        assertEquals(1, observer.gameStartedCount);
        assertEquals(8, observer.stateChangedCount);
        assertEquals(8, observer.turnSummaryCount);
        assertEquals(2, observer.roundSummaryCount);
        assertEquals(1, observer.gameEndedCount);
        assertTrue(observer.maxRoundReached);
    }

    private static final class RecordingObserver implements GameObserver {
        private int gameStartedCount;
        private int stateChangedCount;
        private int turnSummaryCount;
        private int roundSummaryCount;
        private int gameEndedCount;
        private boolean maxRoundReached;

        @Override
        public void onGameStarted(Board board) {
            gameStartedCount++;
        }

        @Override
        public void onStateChanged(Board board) {
            stateChangedCount++;
        }

        @Override
        public void onTurnSummary(int roundNumber, int playerId, String action) {
            turnSummaryCount++;
        }

        @Override
        public void onRoundSummary(int roundNumber, List<Player> players) {
            roundSummaryCount++;
        }

        @Override
        public void onGameEnded(Player winner, int roundNumber, boolean maxRoundReached) {
            gameEndedCount++;
            this.maxRoundReached = maxRoundReached;
        }
    }
}

package ca.mcmaster.catan;

import java.util.List;

public class Game {
    private static final int TARGET_VICTORY_POINTS = 0; // TODO: set value
    private static final int MAX_ROUNDS = 0;            // TODO: set value

    private final Board board;
    private final List<Player> players;
    private int startingIndex;
    private int currentPlayerIndex;
    private int rounds;
    private boolean isWinner;
    private Player winner;
    private Player longestRoadHolder;

    public Game(Board board, List<Player> players) {
        this.board = board;
        this.players = players;
        // TODO: initialize remaining fields
    }

    public void startGame() {
        // TODO: implement
    }

    public void initBankCards() {
        // TODO: implement
    }

    public void determineStartingPlayer() {
        // TODO: implement
    }

    public void playFirstTwoRoundsSetup() {
        // TODO: implement
    }

    public void playMainGame() {
        // TODO: implement
    }

    public void checkWinner() {
        // TODO: implement
    }

    public String awardResourcesForAllPlayers(
        int roll,
        int currentPlayerId
    ) {
        // TODO: implement
        return null;
    }

    private Player getPlayerId(int playerId) {
        // TODO: implement
        return null;
    }

    private int getPlayerIndexClockwise(
        int startIndex,
        int stepsForward
    ) {
        // TODO: implement
        return 0;
    }

    private int getPlayerIndexCounterClockwise(
        int startIndex,
        int stepsBackward
    ) {
        // TODO: implement
        return 0;
    }

    private int placeInitialSettlementAndRoad(
        Player p,
        int roundNumber
    ) {
        // TODO: implement
        return -1;
    }

    private void addStartingResourcesFromSecondSettlement(
        Player p,
        int settlementNodeId
    ) {
        // TODO: implement
    }

    private void displayTurnSummary(
        int roundNumber,
        int playerId,
        String action
    ) {
        // TODO: implement
    }

    private void udpateLongestRoadHolder() {
        // TODO: implement (note: typo "udpate" preserved from UML)
    }
}

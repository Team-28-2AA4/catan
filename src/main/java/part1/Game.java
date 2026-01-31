package part1;
import java.util.ArrayList;
import java.util.List;

public class Game {

    private static final int TARGET_VICTORY_POINTS = 10;
    private static final int MAX_ROUNDS = 8192;

    private final Board board;
    private final List<Player> players;

    private int startingPlayerIndex;
    private int currentPlayerIndex;
    private int rounds;

    private boolean isWinner;
    private Player winner;


    public Game(Board board, List<Player> players) {

        this.board = board;
        this.players = new ArrayList<>(players);

        this.startingPlayerIndex = 0;
        this.currentPlayerIndex = 0;
        this.rounds = 0;

        this.isWinner = false;
        this.winner = null;

    }






    public void startGame() {

        determineStartingPlayer();

        playFirstTwoRoundsSetup();
        // playMainGame();

    }

    private void determineStartingPlayer() {

        int bestRoll = -1;
        int bestIndex = 0;

        while (true) {

            bestRoll = -1;
            bestIndex = 0;
            boolean tie = false;

            for (int i = 0; i < players.size(); i++) {

                int roll = players.get(i).diceRoll();
                if (roll > bestRoll) {

                    bestRoll = roll;
                    bestIndex = i;
                    tie = false;
                }
                else if (roll == bestRoll) {

                    tie = true;
                }
            }

            if (!tie) {
                break;
            }

        }
        startingPlayerIndex = bestIndex;
        currentPlayerIndex = startingPlayerIndex;

    }

    private void playFirstTwoRoundsSetup() {

        // Round 1
        for (int step = 0; step < players.size(); step++){
            int index = getPlayerIndexClockwise(startingPlayerIndex, step);
            Player p = players.get(index);

            placeInitialSettlementAndRoad(p, 1);
            currentPlayerIndex = index;
        }


        // Round 2
        for (int step = 0; step < players.size(); step++){
            int index = getPlayerIndexCounterClockwise(startingPlayerIndex, step);
            Player p = players.get(index);

            int secondSettlementNodeId = placeInitialSettlementAndRoad(p, 2);
            addStartingResourcesFromSecondSettlement(p, secondSettlementNodeId);
            
            currentPlayerIndex = index;
        }

        currentPlayerIndex = startingPlayerIndex;
        rounds = 0;

    }

    private void playMainGame() {

        while (!isWinner && rounds < MAX_ROUNDS) {
            for (int step = 0; step < players.size(); step++){
                currentPlayerIndex = getPlayerIndexClockwise(startingPlayerIndex, step);
                Player p = players.get(currentPlayerIndex);

                p.turn(board);
                checkWinner();

                if (isWinner) {
                    break;
                }

                rounds++;
            }
        }
    }











    private void checkWinner() {


    }

    private int getPlayerIndexClockwise(int startIndex, int stepsForward) {

        int playerCount = players.size();
        int indexAfterMovingForward = startIndex + stepsForward;
        int indexAfterRotating = indexAfterMovingForward % playerCount;
        return indexAfterRotating;

    }

    private int getPlayerIndexCounterClockwise(int startIndex, int stepsBackward) {

        int playerCount = players.size();
        int indexAfterMovingBackwards = startIndex - stepsBackward;

        while (indexAfterMovingBackwards < 0) {
            indexAfterMovingBackwards = indexAfterMovingBackwards + playerCount;
        }

        int indexAfterRotating = indexAfterMovingBackwards % playerCount;
        return indexAfterRotating;

    }

    private int placeInitialSettlementAndRoad(Player p, int roundNumber) {

        if (p instanceof ComputerPlayer) {

            ComputerPlayer cp = (ComputerPlayer) p;

            int settlementNodeId = cp.placeRandomValidHouse(board, (roundNumber == 2));
            displayTurnSummary(roundNumber, p.getPlayerId(), "Placed settlement at node " + settlementNodeId);

            int edgeIndex = cp.placeRandomValidRoad(board, settlementNodeId);
            displayTurnSummary(roundNumber, p.getPlayerId(), "Placed settlement at node " + settlementNodeId);

            return settlementNodeId;

        }

        return -1;

    }

    private void addStartingResourcesFromSecondSettlement() {

        Board.Node node = board.getNode(settlementNodeId);

        Integer [] adjacentTileIds = node.adjacentTileIds.toArray(new Integer[0]);

        for (int i = 0; i < adjacentTileIds.length; i++) {

            int tileId = adjacentTileIds[i];

            Board.TerrainTile tile = board.getTile(tileId);
            if (tile == null) {
                continue;
            }

            p.addResource(tile.resourceType, 1);
        }

    }







    
}
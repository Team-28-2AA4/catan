package part1;
import java.util.ArrayList;
import java.util.List;

public class Game {

    private static final int TARGET_VICTORY_POINTS = 10;
    private static final int MAX_ROUNDS = 8192;
    private static final int MAX_LUMBER_CARDS = 19;
    private static final int MAX_BRICK_CARDS = 19;
    private static final int MAX_WOOL_CARDS = 19;
    private static final int MAX_GRAIN_CARDS = 19;
    private static final int MAX_ORE_CARDS = 19;

    private final Board board;
    private final List<Player> players;
    private final int[] resourceCardsInTheBank = new int[ResourceType.values().length];

    private int startingPlayerIndex;
    private int currentPlayerIndex;
    private int rounds;

    private boolean isWinner;
    private Player winner;

    private Player longestRoadHolder;


    public Game(Board board, List<Player> players) {

        this.board = board;
        this.players = new ArrayList<>(players);

        initBankCards();
        for (int i = 0; i < this.players.size(); i++) {
            this.players.get(i).setGame(this);
        }

        this.startingPlayerIndex = 0;
        this.currentPlayerIndex = 0;
        this.rounds = 0;

        this.isWinner = false;
        this.winner = null;

        this.longestRoadHolder = null;

    }






    public void startGame() {

        determineStartingPlayer();

        playFirstTwoRoundsSetup();
        playMainGame();

    }

    private void initBankCards() {
        resourceCardsInTheBank[ResourceType.LUMBER.ordinal()] = MAX_LUMBER_CARDS;
        resourceCardsInTheBank[ResourceType.BRICK.ordinal()] = MAX_BRICK_CARDS;
        resourceCardsInTheBank[ResourceType.WOOL.ordinal()] = MAX_WOOL_CARDS;
        resourceCardsInTheBank[ResourceType.GRAIN.ordinal()] = MAX_GRAIN_CARDS;
        resourceCardsInTheBank[ResourceType.ORE.ordinal()] = MAX_ORE_CARDS;

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
        System.out.println("\nGame has started!\n");

        for (int step = 0; step < players.size(); step++){
            int index = getPlayerIndexClockwise(startingPlayerIndex, step);
            Player p = players.get(index);

            placeInitialSettlementAndRoad(p, 1);
            currentPlayerIndex = index;
        }
        rounds = 1;
        displayRoundSummary();

        // Round 2
        for (int step = 0; step < players.size(); step++){
            int index = getPlayerIndexCounterClockwise(startingPlayerIndex, step);
            Player p = players.get(index);

            int secondSettlementNodeId = placeInitialSettlementAndRoad(p, 2);
            addStartingResourcesFromSecondSettlement(p, secondSettlementNodeId);
            
            currentPlayerIndex = index;
        }
        
        rounds = 2;
        displayRoundSummary();
        currentPlayerIndex = startingPlayerIndex;
        

    }

    private void playMainGame() {

        while (!isWinner && rounds < MAX_ROUNDS) {

            for (int step = 0; step < players.size(); step++){
                currentPlayerIndex = getPlayerIndexClockwise(startingPlayerIndex, step);
                Player p = players.get(currentPlayerIndex);


                int roll = p.diceRoll();
                String resourceCollectionSummary = awardResourcesForAllPlayers(roll, p.getPlayerId());

                String action = p.turn(board);
                String finalTurnSummary = resourceCollectionSummary + action;
                displayTurnSummary(rounds, p.getPlayerId(), finalTurnSummary);
                checkWinner();

                if (isWinner) {
                    break;
                }

            }

            rounds++;
            displayRoundSummary();

        }

        if (!isWinner) {
            System.out.println("\n\nGame Over! Max round reached.\n");
        }
    }

    private void checkWinner() {

        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);

            if (p.getVictoryPoints() >= TARGET_VICTORY_POINTS) {
                isWinner = true;
                winner = p;

                System.out.println("\n\nGame Over! Player " + p.getPlayerId() + " reached " + p.getVictoryPoints() + " victory points.\n");

                return;
            }
        }

    }











    private int takeFromBank(Player player, ResourceType type, int requestedAmount) {
        if (type == null) {
            return 0;
        }

        int index = type.ordinal();
        int available = resourceCardsInTheBank[index];

        int actualAmount;
        if (available <= 0) {
            actualAmount = 0;
        } else if (requestedAmount <= available) {
            actualAmount = requestedAmount;
        } else {
            actualAmount = available;
        }

        resourceCardsInTheBank[index] -= actualAmount;
        player.addResource(type, actualAmount);

        return actualAmount;
    }

    public void returnToBank(ResourceType type, int amount) {

        if (type == null) {
            return;
        }
        if (amount <= 0) {
            return;
        }
    }

    public String awardResourcesForAllPlayers(int roll, int currentPlayerId) {

        int lumberGained = 0;
        int brickGained = 0;
        int woolGained = 0;
        int grainGained = 0;
        int oreGained = 0;

        for (int tileId = 0; tileId < Board.TILE_COUNT; tileId++) {

            Board.TerrainTile tile = board.getTile(tileId);

            if (tile == null){
                continue;
            }
            if (tile.diceToken != roll) {
                continue;
            }
            if (tile.resourceType == null) { // desert
                continue;
            }
            
            for (int i = 0; i < tile.cornerNodeIdsClockwise.length; i++) {

                int nodeId = tile.cornerNodeIdsClockwise[i];

                Board.Building building = board.getBuilding(nodeId);
                if (building == null){
                    continue;
                }

                int amount;
                if (building.kind == BuildingKind.SETTLEMENT) {
                    amount = 1; 
                } else {
                    amount = 2;
                }

                Player owner = getPlayerById(building.ownerPlayerId);
                if (owner == null) {
                    continue;
                }

                int actual = takeFromBank(owner, tile.resourceType, amount);
                if (actual == 0) {
                    continue;
                }

                if (building.ownerPlayerId == currentPlayerId) {
                    if (tile.resourceType == ResourceType.LUMBER) {
                        lumberGained += actual;
                    }
                    if (tile.resourceType == ResourceType.BRICK) {
                        brickGained += actual;
                    }
                    if (tile.resourceType == ResourceType.WOOL) {
                        woolGained += actual;
                    }
                    if (tile.resourceType == ResourceType.GRAIN) {
                        grainGained += actual;
                    }
                    if (tile.resourceType == ResourceType.ORE) {
                        oreGained += actual;
                    }
                }
                
            }
        }

        String gained = "";
        if (lumberGained > 0) {
            gained += "+ " + lumberGained + " LUMBER ";
        }
        if (brickGained > 0) {
            gained += "+ " + brickGained + " BRICK ";
        }
        if (woolGained > 0) {
            gained += "+ " + woolGained + " WOOL ";
        }
        if (grainGained > 0) {
            gained += "+ " + grainGained + " GRAIN ";
        }
        if (oreGained > 0) {
            gained += "+ " + oreGained + " ORE ";
        }

        if (gained.length() == 0) {
            gained = "gained nothing";
        } else {
            gained = "gained " + gained.trim();
        }
        
        return "Rolled " + roll + ", " + gained + ". ";

    }

    private Player getPlayerById(int playerId) {
        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);
            if (p.getPlayerId() == playerId) {
                return p;
            }
        }
        return null;
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

    /**
     * Instead of using instanceof,
     * Use abstract methods in player class to handle human and computer
     */
    private int placeInitialSettlementAndRoad(Player p, int roundNumber) {

        if (p instanceof ComputerPlayer) {

            ComputerPlayer cp = (ComputerPlayer) p;

            int settlementNodeId = cp.placeRandomValidSettlement(board, (roundNumber == 2));
            displayTurnSummary(roundNumber, p.getPlayerId(), "Placed settlement at node " + settlementNodeId);

            int edgeIndex = cp.placeRandomValidRoad(board, settlementNodeId);
            displayTurnSummary(roundNumber, p.getPlayerId(), "Placed road at edge " + edgeIndex + " (adjacent to node " + settlementNodeId + ")");

            return settlementNodeId;

        }

        return -1;

    }

    private void addStartingResourcesFromSecondSettlement(Player p, int settlementNodeId) {

        Board.Node node = board.getNode(settlementNodeId);

        Integer[] adjacentTileIds = node.adjacentTileIds.toArray(new Integer[0]);

        for (int i = 0; i < adjacentTileIds.length; i++) {

            int tileId = adjacentTileIds[i].intValue();

            Board.TerrainTile tile = board.getTile(tileId);
            if (tile == null) {
                continue;
            }
            if (tile.resourceType == null) { // desert
                continue;
            }

            takeFromBank(tile.resourceType, 1);
        }

        displayTurnSummary(2, p.getPlayerId(), "Gained starting resources from 2nd settlement at node " + settlementNodeId);

    }

    private void displayTurnSummary(int roundNumber, int playerId, String action) {

        System.out.println("[" + roundNumber + "] / [" + playerId + "]: " + action);

        
        try { 
            Thread.sleep(500);
        } 

        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

    }

    private void displayRoundSummary() {

        StringBuilder summary = new StringBuilder();
        summary.append("Round " + rounds + " Summary: \n\n");

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

            summary.append(" | longestRoadStreak=").append(p.getLongestRoadStreak()).append(" | victoryPoints=").append(p.getVictoryPoints());
            summary.append("\n\n");
                    
        }

        summary.append("---------------------------------------------------------------------------------\n\n");
        System.out.print(summary.toString());

/*
        try { 
            Thread.sleep(500);
        } 

        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
*/
    }


    
}
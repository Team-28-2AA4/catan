package part1;

import java.util.Random;
import java.util.ArrayList;
import java.util.List;

/**
 * Player
 * Base class for any player (computer or human).
 * Stores resources, pieces left to build, and basic helper methods.
 *
 * Subclasses implement turn() to choose what to do.
 *
 * @author Team 28
 */
public abstract class Player {

    private final int playerId;

    /** Amount of resources you have in your hand. */
    private final int[] resourceCounts = new int[ResourceType.values().length];

    /** Amount of buildings you have off the board. */
    private int[] buildingCounts = new int[BuildingKind.values().length];
    /** Amount of roads you have off the board. */
    private int roadsCount;

    private int victoryPoints;
    private int longestRoadStreak;
    private int turns;

    private final Random r;

    public Player(int playerId) {

        this.roadsCount = 15;
        addBuilding(BuildingKind.SETTLEMENT, 5);
        addBuilding(BuildingKind.CITY, 4);

        this.playerId = playerId;
        this.victoryPoints = 0;
        this.longestRoadStreak = 0;
        this.turns = 0;
        this.r = new Random();

    }
    /**
     * Runs one turn decision for this player.
     * Each subclass decides what to do and returns a TurnResult.
     *
     * @param board game board
     * @return the chosen action for this turn
     */
    public abstract TurnResult turn(Board board);









    // helpers


    /**
     * Rolls 2 six-sided dice and returns the sum (2..12).
     *
     * @return dice sum
     */

    public int diceRoll() {
        int die1 = r.nextInt(6) + 1;
        int die2 = r.nextInt(6) + 1;
        return die1 + die2;
    }

    /**
     * Builds a road by spending resources and a road piece, then placing it on the board.
     *
     * @param board game board
     * @param edgeIndex edge index where the road goes
     */
    public void buildRoad(Board board, int edgeIndex) {
        spendResource(ResourceType.LUMBER, 1);
        spendResource(ResourceType.BRICK, 1);

        spendRoads(1);

        board.placeRoad(edgeIndex, getPlayerId());
    }
    /**
     * Builds a settlement by spending resources and a settlement piece, then placing it on the board.
     *
     * @param board game board
     * @param nodeId node id where the settlement goes
     */

    public void buildSettlement(Board board, int nodeId) {
        spendResource(ResourceType.LUMBER, 1);
        spendResource(ResourceType.BRICK, 1);
        spendResource(ResourceType.WOOL, 1);
        spendResource(ResourceType.GRAIN, 1);

        spendBuilding(BuildingKind.SETTLEMENT, 1);

        board.placeSettlement(nodeId, this);
    }
    /**
     * Builds a city by spending resources and a city piece, then upgrading on the board.
     *
     * @param board game board
     * @param nodeId node id where the city upgrade happens
     */
    public void buildCity(Board board, int nodeId) {
        spendResource(ResourceType.GRAIN, 2);
        spendResource(ResourceType.ORE, 3);

        spendBuilding(BuildingKind.CITY, 1);

        board.placeCity(nodeId, this);
    }

    /**
     * Checks if the player has enough resources + road pieces to build a road.
     *
     * @return true if affordable
     */
    public boolean canAffordRoad() {
        boolean enoughResources = hasAtLeastXResources(ResourceType.LUMBER, 1)
                && hasAtLeastXResources(ResourceType.BRICK, 1);
        boolean enoughRoads = hasAtLeastXRoads(1);
        boolean affordable = enoughResources && enoughRoads;
        return affordable;
    }

    /**
     * Checks if the player has enough resources + settlement pieces to build a settlement.
     *
     * @return true if affordable
     */
    public boolean canAffordSettlement() {
        boolean enoughResources = hasAtLeastXResources(ResourceType.LUMBER, 1)
                && hasAtLeastXResources(ResourceType.BRICK, 1) && hasAtLeastXResources(ResourceType.WOOL, 1)
                && hasAtLeastXResources(ResourceType.GRAIN, 1);
        boolean enoughSettlements = hasAtLeastXBuildings(BuildingKind.SETTLEMENT, 1);
        boolean affordable = enoughResources && enoughSettlements;
        return affordable;
    }

    public boolean canAffordCity() {
        boolean enoughResources = hasAtLeastXResources(ResourceType.GRAIN, 2)
                && hasAtLeastXResources(ResourceType.ORE, 3);
        boolean enoughCities = hasAtLeastXBuildings(BuildingKind.CITY, 1);
        boolean affordable = enoughResources && enoughCities;
        return affordable;
    }

    
    /**
     * Finds all edges where this player can place a road.
     * Edge must be empty and connected to this player's network.
     *
     * @param board game board
     * @return list of edge indices
     */
    public List<Integer> findValidRoadPlacements(Board board) {
        List<Integer> result = new ArrayList<Integer>();

        for (int edgeIndex = 0; edgeIndex < Board.EDGE_COUNT; edgeIndex++) {
            if (!board.isRoadEmpty(edgeIndex)) {
                continue;
            }

            if (isEdgeConnectedToMyRoad(board, edgeIndex)) {
                result.add(Integer.valueOf(edgeIndex));
            }
        }

        return result;
    }
    /**
     * Finds all nodes where this player can place a settlement.
     * Node must be empty, pass distance rule, and touch this player's road.
     *
     * @param board game board
     * @return list of node ids
     */
    public List<Integer> findValidSettlementPlacements(Board board) {
        List<Integer> result = new ArrayList<Integer>();

        for (int nodeId = 0; nodeId < Board.NODE_COUNT; nodeId++) {
            if (!board.isNodeEmpty(nodeId)) {
                continue;
            }
            if (board.violatesDistanceRule(nodeId)) {
                continue;
            }
            if (!doesNodeTouchMyRoad(board, nodeId)) {
                continue;
            }

            result.add(Integer.valueOf(nodeId));

        }

        return result;
    }

    public List<Integer> findValidCityPlacements(Board board) {
        List<Integer> result = new ArrayList<Integer>();

        for (int nodeId = 0; nodeId < Board.NODE_COUNT; nodeId++) {
            Board.Building b = board.getBuilding(nodeId);
            if (b == null) {
                continue;
            }
            if (b.ownerPlayerId != getPlayerId()) {
                continue;
            }
            if (b.kind != BuildingKind.SETTLEMENT) {
                continue;
            }

            result.add(Integer.valueOf(nodeId));

        }

        return result;
    }

    public boolean isEdgeConnectedToMyRoad(Board board, int edgeIndex) {
        Board.Edge e = board.getEdge(edgeIndex);

        if (doesNodeHaveMyBuilding(board, e.node1)) {
            return true;
        }
        if (doesNodeHaveMyBuilding(board, e.node2)) {
            return true;
        }

        if (doesNodeTouchMyRoad(board, e.node1)) {
            return true;
        }
        if (doesNodeTouchMyRoad(board, e.node2)) {
            return true;
        }

        return false;
    }

    public boolean doesNodeHaveMyBuilding(Board board, int nodeId) {
        Board.Building b = board.getBuilding(nodeId);
        if (b == null) {
            return false;
        }
        return b.ownerPlayerId == getPlayerId();
    }

    public boolean doesNodeTouchMyRoad(Board board, int nodeId) {
        List<Integer> adjacentEdges = board.getAdjacentEdgeIndicesForNode(nodeId);

        for (int i = 0; i < adjacentEdges.size(); i++) {
            int edgeIndex = adjacentEdges.get(i).intValue();

            Board.Road road = board.getRoad(edgeIndex);
            if (road == null) {
                continue;
            }

            int ownerId = road.ownerPlayerId;
            if (ownerId == getPlayerId()) {
                return true;
            }
        }

        return false;
    }

    // Getters && Setters

    public int getPlayerId() {
        return playerId;
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }

    public int getLongestRoadStreak() {
        return longestRoadStreak;
    }

    public void addVictoryPoints(int amount) {
        victoryPoints += amount;
    }

    // RESOURCES

    public int getResourceCount(ResourceType type) {
        return resourceCounts[type.ordinal()];
    }

    public boolean hasAtLeastXResources(ResourceType type, int amount) {
        return resourceCounts[type.ordinal()] >= amount;
    }

    public void addResource(ResourceType type, int amount) {
        resourceCounts[type.ordinal()] += amount;
    }

    public void spendResource(ResourceType type, int amount) {
        int index = type.ordinal();
        if (resourceCounts[index] < amount) {
            throw new IllegalStateException("Not enough" + type);
        }
        resourceCounts[index] -= amount;
    }

    // BUILDINGS

    public int getBuildingCount(BuildingKind type) {
        return buildingCounts[type.ordinal()];
    }

    public boolean hasAtLeastXBuildings(BuildingKind type, int amount) {
        return buildingCounts[type.ordinal()] >= amount;
    }

    public void addBuilding(BuildingKind type, int amount) {
        buildingCounts[type.ordinal()] += amount;
    }

    public void spendBuilding(BuildingKind type, int amount) {
        int index = type.ordinal();
        if (buildingCounts[index] < amount) {
            throw new IllegalStateException("Not enough" + type);
        }
        buildingCounts[index] -= amount;
    }

    // ROADS

    public int getRoadsCount() {
        return roadsCount;
    }

    public boolean hasAtLeastXRoads(int amount) {
        return roadsCount >= amount;
    }

    public void addRoads(int amount) {
        roadsCount += amount;
    }

    public void spendRoads(int amount) {
        if (roadsCount < amount) {
            throw new IllegalStateException("Not enough roads");
        }
        roadsCount -= amount;
    }





    /** Nested Classes */
    public static final class TurnResult {

        public final ActionType actionType;
        public final int edgeIndex;
        public final int nodeId;
        public final String decisionSummary;

        private TurnResult(ActionType actionType, int edgeIndex, int nodeId, String decisionSummary) {
            this.actionType = actionType;
            this.edgeIndex = edgeIndex;
            this.nodeId = nodeId;
            this.decisionSummary = decisionSummary;
        }

        public static TurnResult pass(String summary) {
            return new TurnResult(ActionType.PASS, -1, -1, summary);
        }

        public static TurnResult buildRoad(int edgeIndex, String summary) {
            return new TurnResult(ActionType.BUILD_ROAD, edgeIndex, -1, summary);
        }

        public static TurnResult buildSettlement(int nodeId, String summary) {
            return new TurnResult(ActionType.BUILD_SETTLEMENT, -1, nodeId, summary);
        }

        public static TurnResult buildCity(int nodeId, String summary) {
            return new TurnResult(ActionType.BUILD_CITY, -1, nodeId, summary);
        }        

    }
}
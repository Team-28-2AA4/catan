package ca.mcmaster.catan;

import java.util.List;
import java.util.Random;

public abstract class Player {
    private final int playerId;
    private final int[] resourceCounts;   // indexed by ResourceType.ordinal()
    private int[] buildingCounts;         // indexed by BuildingKind.ordinal()
    private int roadsCount;
    private int victoryPoints;
    private int longestRoadStreak;
    private int turns;
    private final Random r;

    public Player(int playerId) {
        this.playerId = playerId;
        this.resourceCounts =
            new int[ResourceType.values().length];
        this.buildingCounts =
            new int[BuildingKind.values().length];
        this.r = new Random();
        // TODO: initialize remaining fields
    }

    public abstract TurnResult turn(Board board);

    public int diceRoll() {
        // TODO: implement
        return 0;
    }

    public void buildRoad(Board board, int edgeIndex) {
        // TODO: implement
    }

    public void buildSettlement(Board board, int nodeId) {
        // TODO: implement
    }

    public void buildCity(Board board, int nodeId) {
        // TODO: implement
    }

    public boolean canAffordRoad() {
        // TODO: implement
        return false;
    }

    public boolean canAffordCity() {
        // TODO: implement
        return false;
    }

    public boolean canAffordSettlement() {
        // TODO: implement
        return false;
    }

    public List<Integer> findValidRoadPlacements(Board board) {
        // TODO: implement
        return null;
    }

    public List<Integer> findValidCityPlacements(Board board) {
        // TODO: UML param was typed as Integer - assumed Board
        return null;
    }

    public boolean isEdgeConnectedToMyRoad(Board board, int edgeIndex) {
        // TODO: implement
        return false;
    }

    public boolean doesNodeHaveMyBuilding(Board board, int nodeId) {
        // TODO: implement
        return false;
    }

    public boolean doesNodeTouchMyRoad(Board board, int nodeId) {
        // TODO: implement
        return false;
    }

    public int getPlayerId() {
        return this.playerId;
    }

    public int getVictoryPoints() {
        return this.victoryPoints;
    }

    public int getLongestRoadStreak() {
        return this.longestRoadStreak;
    }

    public void addVictoryPoints(int amount) {
        // TODO: implement
    }

    public int getResourceCount(ResourceType type) {
        // TODO: implement
        return 0;
    }

    public boolean hasAtLeastXResources(
        ResourceType type,
        int amount
    ) {
        // TODO: implement
        return false;
    }

    public void addResource(ResourceType type, int amount) {
        // TODO: implement
    }

    public void spendResource(ResourceType type, int amount) {
        // TODO: implement
    }

    public int getBuildingCount(BuildingKind type) {
        // TODO: implement
        return 0;
    }

    public boolean hasAtLeastXBuildings(
        BuildingKind type,
        int amount
    ) {
        // TODO: implement
        return false;
    }

    public void addBuilding(BuildingKind type, int amount) {
        // TODO: implement
    }

    public void spendBuilding(BuildingKind type, int amount) {
        // TODO: implement
    }

    public int getRoadsCount() {
        return this.roadsCount;
    }

    public boolean hasAtLeastXRoads(int amount) {
        // TODO: implement
        return false;
    }

    public void addRoads(int amount) {
        // TODO: implement
    }

    public void spendRoads(int amount) {
        // TODO: implement
    }
}

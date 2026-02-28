package ca.mcmaster.catan;

import java.util.List;

public class Board {
    public static final int TILE_COUNT = 0;  // TODO: set actual value
    public static final int NODE_COUNT = 0;  // TODO: set actual value
    public static final int EDGE_COUNT = 0;  // TODO: set actual value

    // Compositions (Board owns these collections)
    // TODO: declare internal storage for tiles, nodes, edges, buildings, roads

    public Board() {
        // TODO: implement
    }

    private void initEmptyState() {
        // TODO: implement
    }

    private void initNode0t053() {
        // TODO: implement
    }

    private void initTilesFromSetupArray() {
        // TODO: implement
    }

    private void populateNodeAdjacentTileIdsFromTiles() {
        // TODO: implement
    }

    private void buildingEdgesFromTilePerimetersAndAssignIds() {
        // TODO: implement
    }

    private void populateNodeNeighbourNodeIdsFromEdges() {
        // TODO: implement
    }

    public void placeSettlement(int nodeId, Player owner) {
        // TODO: implement
    }

    public void placeCity(int nodeId, Player owner) {
        // TODO: implement
    }

    public void placeRoad(int edgeIndex, Player owner) {
        // TODO: implement
    }

    public boolean violatesDistanceRule(int nodeId) {
        // TODO: implement
        return false;
    }

    public int computeLongestRoadForPlayer(int playerId) {
        // TODO: implement
        return 0;
    }

    public int dfsExtendFromNode(
        int playerId,
        int nodeId,
        boolean[] usedEdges
    ) {
        // TODO: implement
        return 0;
    }

    public List<Integer> getAdjecentEdgeIndicesForNode(int nodeId) {
        // TODO: implement
        return null;
    }

    public List<ResourceType> getResourceTypesForDiceToken(int diceToken) {
        // TODO: implement
        return null;
    }

    public TerrainTile getTile(int tileId) {
        // TODO: implement
        return null;
    }

    public Node getNode(int nodeId) {
        // TODO: implement
        return null;
    }

    public Building getBuilding(int nodeId) {
        // TODO: implement - UML shows [*] return, may return null if empty
        return null;
    }

    public Road getRoad(int edgeIndex) {
        // TODO: implement - UML shows [*] return, may return null if empty
        return null;
    }

    public Edge getEdge(int edgeIndex) {
        // TODO: implement
        return null;
    }

    public boolean isRoadEmpty(int edgeIndex) {
        // TODO: implement
        return false;
    }

    public boolean isNodeEmpty(int nodeId) {
        // TODO: implement
        return false;
    }
}

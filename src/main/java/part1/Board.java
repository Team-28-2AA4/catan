package part1;
import java.util.*;

/**
 * Board
 * Manages the initialization of the game Board and the state of the placed pieces 
 * 
 * Nested Classes:
 *      1) TerrainTile
 *      2) Node
 *      3) Edge
 *      4) Building
 *      5) Road
 * 
 * 
 * Denzel was also here
 */

/**
 * 
 * 
 * Fatihun committed
 * 
 * Ify is here
 */

public class Board {

    // finals
    public static final int TILE_COUNT = 19;
    public static final int NODE_COUNT = 54;
    public static final int EDGE_COUNT = 72;

    // initial board state
    private final TerrainTile[] tiles = new TerrainTile[TILE_COUNT];
    private final Node[] nodes = new Node[NODE_COUNT];
    private final List<Edge> edges = new ArrayList<>();

    // dynamic infrastructure state
    private Building[] buildings = new Building[NODE_COUNT];
    private Road[] roads = new Road[EDGE_COUNT];

    private final List<Integer>[] nodeToEdgeIndices = (List<Integer>[]) new List[NODE_COUNT];
    private static final TerrainTile[] terrainTilesSetup = new TerrainTile[] {

        new TerrainTile(0, ResourceType.LUMBER, 10, new int[] {0, 1, 2, 3, 4, 5}),
        new TerrainTile(1, ResourceType.GRAIN, 11, new int[] {6, 7, 8, 9, 2, 1}),
        new TerrainTile(2, ResourceType.BRICK, 8, new int[] {2, 9, 10, 11, 12, 3}),
        new TerrainTile(3, ResourceType.ORE, 3, new int[] {4, 3, 12, 13, 14, 15}),
        new TerrainTile(4, ResourceType.WOOL, 11, new int[] {16, 5, 4, 15, 17, 18}),
        new TerrainTile(5, ResourceType.WOOL, 5, new int[] {19, 20, 0, 5, 16, 21}),
        new TerrainTile(6, ResourceType.WOOL, 12, new int[] {22, 23, 6, 1, 0, 20}),        
        new TerrainTile(7, ResourceType.GRAIN, 3, new int[] {24, 25, 26, 27 ,8, 7}),
        new TerrainTile(8, ResourceType.ORE, 6, new int[] {8, 27, 28, 29, 10, 9}),
        new TerrainTile(9, ResourceType.LUMBER, 4, new int[] {10, 29, 30, 31, 32, 11}),
        new TerrainTile(10, ResourceType.ORE, 6, new int[] {12, 11, 32, 33, 34, 13}),
        new TerrainTile(11, ResourceType.GRAIN, 9, new int[] {14, 13, 34, 35, 36, 37}),
        new TerrainTile(12, ResourceType.LUMBER, 5, new int[] {17, 15, 14, 37, 38, 39}),
        new TerrainTile(13, ResourceType.BRICK, 9, new int[] {40, 18, 17, 39, 41, 42}),
        new TerrainTile(14, ResourceType.BRICK, 8, new int[] {43, 21, 16, 18, 40, 44}),
        new TerrainTile(15, ResourceType.GRAIN, 4, new int[] {47, 46, 19, 21, 43, 45}),
        new TerrainTile(16, null,               0, new int[] {48, 49, 22, 20, 19, 46}),
        new TerrainTile(17, ResourceType.LUMBER, 2, new int[] {50, 51, 52, 23, 22, 49}),
        new TerrainTile(18, ResourceType.WOOL, 10, new int[] {52, 53, 24, 7, 6, 23})

    };


    public Board() {

        initEmptyState();
        initNodes0to53();
        initTilesFromSetupArray();
        populateNodeAdjacentTileIdsFromTiles();
        buildEdgesFromTilePerimetersAndAssignIds();
        populateNodeNeighbourNodeIdsFromEdges();

    }

    // Initialize Board helpers
    private void initEmptyState() {
        for (int i = 0; i < NODE_COUNT; i++) {
            buildings[i] = null;
        }
        for (int i = 0; i < EDGE_COUNT; i++) {
            roads[i] = null;
        }
        edges.clear();
    }

    private void initNodes0to53() {
        for (int nodeId = 0; nodeId < NODE_COUNT; nodeId++) {
            nodes[nodeId] = new Node(nodeId);
        }
    }

    private void initTilesFromSetupArray() {
        for (int i = 0; i < terrainTilesSetup.length; i++) {
            TerrainTile t = terrainTilesSetup[i];
            int tileId = t.tileId;
            tiles[tileId] = t;
        }
    }

    private void populateNodeAdjacentTileIdsFromTiles() {
        for (int tileId = 0; tileId < TILE_COUNT; tileId++) {
            TerrainTile t = tiles[tileId];

            for (int i = 0; i < t.cornerNodeIdsClockwise.length; i++) {
                int nodeId = t.cornerNodeIdsClockwise[i];

                if (nodeId < 0 || nodeId >= NODE_COUNT) {
                    throw new IllegalStateException("Tile " + tileId + " has invalid nodeId " + nodeId);
                }

                nodes[nodeId].adjacentTileIds.add(Integer.valueOf(tileId));
            }
        }
    }

    private void buildEdgesFromTilePerimetersAndAssignIds() {

        boolean[][] hasEdge = new boolean[NODE_COUNT][NODE_COUNT];

        int[][] edgePairs = new int[EDGE_COUNT][2];
        int edgeCount = 0;

        for (int tileId = 0; tileId < TILE_COUNT; tileId++) {
            int[] c = tiles[tileId].cornerNodeIdsClockwise;

            for (int i = 0; i < 6; i++) {
                int a = c[i];
                int b = c[(i+1) % 6];

                int n1 = Math.min(a,b);
                int n2 = Math.max(a,b);

                if (!hasEdge[n1][n2]) {
                    hasEdge[n1][n2] = true;

                    if (edgeCount >= EDGE_COUNT) {
                        throw new IllegalStateException("Generated more than " + EDGE_COUNT + " unique edges.");
                    }

                    edgePairs[edgeCount][0] = n1;
                    edgePairs[edgeCount][1] = n2;
                    edgeCount++;
                }
            }
        }

        for (int i = 0; i < EDGE_COUNT - 1; i++) {
            int best = i;

            for (int j = i + 1; j < EDGE_COUNT; j++) {
                int a1 = edgePairs[j][0];
                int a2 = edgePairs[j][1];
                int b1 = edgePairs[best][0];
                int b2 = edgePairs[best][1];

                boolean isSmaller = (a1 < b1) || (a1 == b1 && a2 < b2);
                if (isSmaller) {
                    best = j;
                }
            }

            if (best != i) {
                int t1 = edgePairs[i][0];
                int t2 = edgePairs[i][1];

                edgePairs[i][0] = edgePairs[best][0];
                edgePairs[i][1] = edgePairs[best][1];

                edgePairs[best][0] = t1;
                edgePairs[best][1] = t2;

            }
        }

        edges.clear();

        for (int edgeIndex = 0; edgeIndex < EDGE_COUNT; edgeIndex++) {
            int node1 = edgePairs[edgeIndex][0];
            int node2 = edgePairs[edgeIndex][1];

            edges.add(new Edge (node1, node2, edgeIndex));

        }

    }

    private void populateNodeNeighbourNodeIdsFromEdges() {

        for (int nodeId = 0; nodeId < NODE_COUNT; nodeId++) {
            nodes[nodeId].neighbourNodeIds.clear();
        }

        for (int i = 0; i < edges.size(); i++) {
            Edge e = edges.get(i);

            int a = e.node1;
            int b = e.node2;

            nodes[a].neighbourNodeIds.add(Integer.valueOf(b));
            nodes[b].neighbourNodeIds.add(Integer.valueOf(a));
        }
    }
































    // Methods

    public void placeSettlement(int nodeId, Player owner) {

        int ownerPlayerId = owner.getPlayerId();

        if (!isNodeEmpty(nodeId)){
            throw new IllegalStateException("Node is alreayd occupied: " + nodeId);
        }
        if (violatesDistanceRule(nodeId)) {
            throw new IllegalStateException("Distance rule violated at node: " + nodeId);
        }

        buildings[nodeId] = new Building(ownerPlayerId, nodeId, BuildingKind.SETTLEMENT);
        owner.addVictoryPoints(1);
    
    }

    public void placeCity(int nodeId, Player owner) {

        int ownerPlayerId = owner.getPlayerId();
        Building existing = buildings[nodeId];

        if (existing == null) {
            throw new IllegalStateException("No settlement to upgrade at node " + nodeId);
        }
        if (existing.ownerPlayerId != ownerPlayerId) {
            throw new IllegalStateException("Cannot upgrade! Building not owned by player " + ownerPlayerId);
        }
        if (existing.kind != BuildingKind.SETTLEMENT) {
            throw new IllegalStateException("Only settlements can be upgraded to cities at node " + nodeId);
        }

        buildings[nodeId] = new Building(ownerPlayerId, nodeId, BuildingKind.CITY);
        owner.addVictoryPoints(1);
    }

    public void placeRoad(int edgeIndex, int ownerPlayerId) {

        if (edgeIndex < 0 || edgeIndex >= EDGE_COUNT){
            throw new IllegalStateException("Invalid edgeIndex: " + edgeIndex);
        }
        if (roads[edgeIndex] != null) {
            throw new IllegalStateException("There is already a road at edgeIndex: " + edgeIndex);
        }

        roads[edgeIndex] = new Road(ownerPlayerId, edgeIndex);
    
    }

    public boolean violatesDistanceRule(int nodeId) {
        
        Node n = nodes[nodeId];
        if (n == null) {
            return false;
        }

        Integer[] neighbourIds = n.neighbourNodeIds.toArray(new Integer[0]);

        for (int i = 0; i < neighbourIds.length; i++) {

            int neighbourId = neighbourIds[i].intValue();

            boolean neighbourIdIsValid = (neighbourId >= 0) && (neighbourId <   NODE_COUNT);
            if (!neighbourIdIsValid) {
                continue;
            }

            boolean neighbourHasBuilding = (buildings[neighbourId] != null);
            if (neighbourHasBuilding) {
                return true;
            }
        }

        return false;
    }

    public List<Integer> getAdjacentEdgeIndicesForNode(int nodeId){

        List<Integer> result = new ArrayList<Integer>();
        
        for (int i = 0; i < edges.size(); i++) {
            
            Edge e = edges.get(i);

            if (e == null) {
                continue;
            }

            boolean touchesNode = (e.node1 == nodeId) || (e.node2 == nodeId);
            if (touchesNode) {
                result.add(Integer.valueOf(e.edgeIndex));
            }
        }

        return result;
    }


    public List<ResourceType> getResourceTypesForDiceToken(int diceToken) {

        List<ResourceType> resourceTypes = new ArrayList<>();

        for (int i = 0; i < tiles.length; i++) {

            TerrainTile tile = tiles[i];
            if (tile != null && tile.diceToken == diceToken) {

                resourceTypes.add(tile.resourceType);
            }
        }

        return resourceTypes;
    }










    // Getters
    public TerrainTile getTile(int tileId) {
        return tiles[tileId];
    }
    public Node getNode(int nodeId) {
        return nodes[nodeId];
    }
    public Building getBuilding(int nodeId){
        return buildings[nodeId];
    }
    public Road getRoad(int edgeIndex) {
        return roads[edgeIndex];
    }
    public Edge getEdge(int edgeIndex) {
        return edges.get(edgeIndex);
    }
    public boolean isRoadEmpty(int edgeIndex) {
        return roads[edgeIndex] == null;
    }
    public boolean isNodeEmpty(int nodeId) {
        return buildings[nodeId] == null;
    }





















    // Nested Types

    public static final class TerrainTile {

        public final int tileId;
        public final ResourceType resourceType;
        public final int diceToken;
        public final int[] cornerNodeIdsClockwise;

        public TerrainTile(int tileId, ResourceType resourceType, int diceToken, int[] cornerNodeIdsClockwise){
            this.tileId = tileId;
            this.resourceType = resourceType;
            this.diceToken = diceToken;
            this.cornerNodeIdsClockwise = cornerNodeIdsClockwise;
            if (cornerNodeIdsClockwise.length != 6) {
                throw new IllegalArgumentException("cornerNodeIdsClockwise must always have a length of 6.");
            }
        }
    }


    public static final class Node {

        public final int nodeId;
        public final Set<Integer> neighbourNodeIds = new HashSet<>();
        public final Set<Integer> adjacentTileIds = new HashSet<>();

        public Node(int nodeId) {
            this.nodeId = nodeId;
        }
    }


    public static final class Edge {
        
        public final int node1;
        public final int node2;
        public final int edgeIndex;

        public Edge(int nodeA, int nodeB, int edgeIndex) {
            this.node1 = Math.min(nodeA, nodeB);
            this.node2 = Math.max(nodeA, nodeB);
            this.edgeIndex = edgeIndex;
        }
    }


    public static final class Building {
    
        public final int ownerPlayerId;
        public final int nodeId;
        public final BuildingKind kind;

        public Building(int ownerPlayerId, int nodeId, BuildingKind kind){
            this.ownerPlayerId = ownerPlayerId;
            this.nodeId = nodeId;
            this.kind = kind;
        }

    }


    public static final class Road {

        public final int ownerPlayerId;
        public final int edgeIndex;

        public Road(int ownerPlayerId, int edgeIndex) {
            this.ownerPlayerId = ownerPlayerId;
            this.edgeIndex = edgeIndex;
        }

    }

}
package part1;
import java.util.*;

/**
 * Board
 * Sets up the Catan board layout (tiles, nodes, edges) and keeps track of what
 * buildings and roads have been placed during the game.
 *
 * This class also defines a few small nested types used by the board:
 * TerrainTile, Node, Edge, Building, Road.
 *
 * @author Team 28
 */


public class Board {

    /** The number of tiles on a standard Catan board. */
    public static final int TILE_COUNT = 19;

    /** number of intersection points (corners) on the board. */
    public static final int NODE_COUNT = 54;

    /** The number of unique edges. */
    public static final int EDGE_COUNT = 72;

    // initial board state
    private final TerrainTile[] tiles = new TerrainTile[TILE_COUNT];
    private final Node[] nodes = new Node[NODE_COUNT];
    private final List<Edge> edges = new ArrayList<>();

    // dynamic infrastructure state
    private Building[] buildings = new Building[NODE_COUNT];

     /** roads are currently placed on edges. */
    private Road[] roads = new Road[EDGE_COUNT];


    /** For each node, stores the edge indices that touch it  */
    private final List<Integer>[] nodeToEdgeIndices = (List<Integer>[]) new List[NODE_COUNT];

    /**
     * Hard-coded board setup.
     * Each TerrainTile stores: tile id, resource type, dice token, and the 6 corner nodes.
     * The corner nodes are in clockwise order so we can build edges cleanly.
     */

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

    /**
     * Builds a fresh board.
     * Creates empty state, creates nodes, loads tiles, then builds edges and
     * neighbor relationships.
     */
    public Board() {

        initEmptyState();
        initNodes0to53();
        initTilesFromSetupArray();
        populateNodeAdjacentTileIdsFromTiles();
        buildEdgesFromTilePerimetersAndAssignIds();
        populateNodeNeighbourNodeIdsFromEdges();

    }

    /**
     * Clears placed buildings/roads and resets the edges list.
     * Used when we are creating a new board state.
     */
    private void initEmptyState() {
        for (int i = 0; i < NODE_COUNT; i++) {
            buildings[i] = null;
        }
        for (int i = 0; i < EDGE_COUNT; i++) {
            roads[i] = null;
        }
        edges.clear();
    }

    /**
     * Creates Node objects for ids 0 to 53.
     */
    private void initNodes0to53() {
        for (int nodeId = 0; nodeId < NODE_COUNT; nodeId++) {
            nodes[nodeId] = new Node(nodeId);
        }
    }
    /**
     * Loads the TerrainTile objects from the static setup array into the tiles array.
     */
    private void initTilesFromSetupArray() {
        for (int i = 0; i < terrainTilesSetup.length; i++) {
            TerrainTile t = terrainTilesSetup[i];
            int tileId = t.tileId;
            tiles[tileId] = t;
        }
    }
    /**
     * For each tile, adds that tile id into the adjacentTileIds set of each corner node.
     * This lets a node know which tiles touch it.
     */
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
    /**
     * Builds all unique edges by walking around each tile perimeter.
     * Uses a boolean matrix to avoid duplicate edges.
     *
     * After collecting all unique node pairs, the pairs are sorted so edge ids
     * are consistent across runs.
     */
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
        // Selection sort edge pairs so thats the edgeIndex assignment is predictable
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

        // Create Edge objects and assign edgeIndex based on sorted order.

        for (int edgeIndex = 0; edgeIndex < EDGE_COUNT; edgeIndex++) {
            int node1 = edgePairs[edgeIndex][0];
            int node2 = edgePairs[edgeIndex][1];

            edges.add(new Edge (node1, node2, edgeIndex));

        }

    }
    /**
     * Builds the neighbor list for each node based on the edge list.
     * If two nodes share an edge, they are neighbors.
     */
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

    /**
     * Places a settlement on a node for a player.
     * Checks the node is empty and the distance rule is not broken.
     *
     * @param nodeId node to place the settlement on
     * @param owner player who owns the settlement
     */

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
     /**
     * Upgrades a settlement to a city at the given node.
     * The node must already have the owner's settlement.
     *
     * @param nodeId node to upgrade
     * @param owner player who is upgrading
     */
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
    /**
     * Places a road on an edge index for a player.
     *
     * @param edgeIndex which edge to place on
     * @param ownerPlayerId id of the owner player
     */
    public void placeRoad(int edgeIndex, int ownerPlayerId) {

        if (edgeIndex < 0 || edgeIndex >= EDGE_COUNT){
            throw new IllegalStateException("Invalid edgeIndex: " + edgeIndex);
        }
        if (roads[edgeIndex] != null) {
            throw new IllegalStateException("There is already a road at edgeIndex: " + edgeIndex);
        }

        roads[edgeIndex] = new Road(ownerPlayerId, edgeIndex);
    
    }
    /**
     * Checks the distance rule for settlements/cities.
     * Returns true if any neighbor node already has a building.
     *
     * @param nodeId node being checked
     * @return true if the rule is violated, false otherwise
     */
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
    /**
     * Returns the edge indices that touch a given node.
     *
     * @param nodeId node to check
     * @return list of edge indices connected to that node
     */
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

    /**
     * Finds all resource types that match a dice token.
     * An example could be that a token 8 might return multiple tiles, so multiple types.)
     *
     * @param diceToken dice number rolled
     * @return list of resource types for tiles with that token
     */
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

    /**
     * Gets the tile by tile id.
     *
     * @param tileId tile id
     * @return tile object
     */
    public TerrainTile getTile(int tileId) {
        return tiles[tileId];
    }
    /**
     * Gets the node by node id.
     *
     * @param nodeId node id
     * @return node object
     */
    public Node getNode(int nodeId) {
        return nodes[nodeId];
    }
    /**
     * Gets the building at a node (null if empty).
     *
     * @param nodeId node id
     * @return building or null
     */
    public Building getBuilding(int nodeId){
        return buildings[nodeId];
    }
    /**
     * Gets the road at an edge (null if empty).
     *
     * @param edgeIndex edge index
     * @return road or null
     */
    public Road getRoad(int edgeIndex) {
        return roads[edgeIndex];
    }
    /**
     * Gets the edge object at an index.
     *
     * @param edgeIndex edge index
     * @return edge object
     */
    public Edge getEdge(int edgeIndex) {
        return edges.get(edgeIndex);
    }
    /**
     * Checks if an edge has no road.
     *
     * @param edgeIndex edge index
     * @return true if empty, false otherwise
     */
    public boolean isRoadEmpty(int edgeIndex) {
        return roads[edgeIndex] == null;
    }
    /**
     * Checks if a node has no building.
     *
     * @param nodeId node id
     * @return true if empty, false otherwise
     */
    public boolean isNodeEmpty(int nodeId) {
        return buildings[nodeId] == null;
    }





















    // Nested Types
    /**
     * TerrainTile
     * Stores one hex tile's info: id, resource type, dice token, and its 6 corner nodes.
     *
     * @author Team 28
     */

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

/**
     * Node
     * Represents a board corner where a settlement/city can be placed.
     *
     */
    public static final class Node {

        public final int nodeId;
        public final Set<Integer> neighbourNodeIds = new HashSet<>();
        public final Set<Integer> adjacentTileIds = new HashSet<>();

        public Node(int nodeId) {
            this.nodeId = nodeId;
        }
    }

/**
     * Edge
     * Represents one road location between two nodes.
     *
     */
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

/**
     * Building
     * This just store the info for a settlement or city placed on a node.
     *
     */
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

/**
     * Road
     * This stores the info for a road placed on an edge.
     */
    public static final class Road {

        public final int ownerPlayerId;
        public final int edgeIndex;

        public Road(int ownerPlayerId, int edgeIndex) {
            this.ownerPlayerId = ownerPlayerId;
            this.edgeIndex = edgeIndex;
        }

    }

}
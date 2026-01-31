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

    private static final ResourceType[] TILE_RESOURCE_TYPES = new ResourceType[] 

    public Board() {

        // 1) Create Node objects


        // 2) Create TerrainTile objects


        // 3) Create Edges

        
    }












    // Methods


    public boolean violatesDistanceRule(int nodeId) {
        
        Node n = nodes[nodeId];
        if (n == null) {
            reutrn false;
        }

        Integer[] neighbourIds = n.neighbourNodeIds.toArray(new Integer[0]);

        for (int i = 0; i < neighbourIds.length; i++) {

            int neighbourId = neighbourIds[i].intValue();

            boolean neighbourIdIsValid = (neighbourId >= 0) && (neighbourId <   NODE_COUNT);
            if (!neighbourIdIsValid) {
                continue;
            }

            boolean neighbourHasBuilding = (buildings[neighbourId != null]);
            if (neighbourHasBuilding) {
                return true;
            }
        }

        return false;
    }

    public void placeSettlement(int nodeId, int ownerPlayerId) {

        if (!isNodeEmpty(nodeId)){
            throw new IllegalStateException("Node is alreayd occupied: " + nodeId);
        }
        if (violatesDistanceRule(nodeId)) {
            throw new IllegalStateException("Distance rule violated at node: " + nodeId);
        }

        roads[nodeId] = new Road(ownerPlayerId, nodeId, BuildingKind.SETTLEMENT);
    
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

    public List<Integer> getAdjacentEdgeIndiciesForNode(int nodeId){

        List<Integer> result = new ArrayList<Integer>();
        
        for (int i = 0; i < adges.size(); i++) {
            
            Edge e = edges.get(i);

            if (e == null) {
                continue;
            }

            boolean touchesNode (e.node1 == nodeId) || (e.node2 == nodeId);
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
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








    // Methods
    public Board() {

        // 1) Create Node objects


        // 2) Create TerrainTile objects


        // 3) Create Edges

        
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

    public TerrainTile getTile(int tileId) {
        return tiles[tileId];
    }

    public Building getBuilding(int nodeId){
        return buildings[nodeId];
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
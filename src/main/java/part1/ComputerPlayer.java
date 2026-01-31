package part1;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ComputerPlayer extends Player {

    private final Random r = new Random();

    public ComputerPlayer(int playerId){

        super(playerId);

    } 
    




    // most important method
    @Override
    public void turn(Board board){

        // 1) start turn with dice roll
        int roll = diceRoll();

        // 2) check to add resources based on roll
        for (int i = 0; tileId < Board.TILE_COUNT; tileId++) {

            Board.TerrainTile tile = board.getTile(tileId);

            if (tile == null){
                continue
            }
            if (tile.diceToken != roll) {
                continue;
            }
            
            for (int i = 0; i < tile.cornerNodeIdsClockwise.length; i++) {

                int nodeId = tile.cornerNodeIdsClockwise[i];

                Board.Building building = board.getBuilding(nodeId);
                if (building == null){
                    continue;
                }

                if (building.ownerPlayerId != getPlayerId()){
                    continue;
                }

                int amount = (building.kind == BuildingKind.SETTLEMENT) ? 1 : 2;
                addResource(title.resourceType, amount);
            }

        }

        // 3) regular turn options
        
    }









    // helpers
    public int placeRandomValidHouse(Board board, boolean isSecondSettlement) {

        int maxAttempts = 10000;

        for (int attempt = 0; attempt < maxAttempts; attempt++) {

            int nodeId = r.nextInt(Board.NODE_COUNT);

            if (!board.isNodeEmpty(nodeId)){
                continue;
            }
            if (board.violatesDistanceRule(nodeId)) {
                continue
            }

            board.placeSettlement(nodeId, getPlayerId());
            return nodeId;

        }

        return -1;

    }

    public int placeRandomValidRoad(Board board, boolean isSecondSettlement) {

        List<Integer> adjacentEdgeIndicies = board.getAdjacentEdgeIndiciesForNode(settlementNodeId);

        if (adjacentEdgeIndicies.isEmpty()) {
            throw new IllegalStateException("No edges adjacent to node " + settlementNodeId + ". Board edges arent initialized yet.");
        }

        List<Integer> freeAdjacentEdges = new ArrayList<>();
        for (int i = 0; i < adjacentEdgeIndicies.size(); i++){

            int edgeIndex = adjacentEdgeIndicies.get(i);
            if (board.isRoadEmpty(edgeIndex)){
                freeAdjacentEdges.add(edgeIndex);
            }

        }

        if (freeAdjacentEdges.isEmpty()) {
            throw new IllegalStateException("No free adjacent edges for the road the node: " + settlementNodeId);
        }

        int chosenEdgeIndex = freeAdjacentEdges.get(r.nextInt(freeAdjacentEdges.size()));
        board.placeRoad(chosenEdgeIndex, getPlayerId());
        return chosenEdgeIndex;

    }
}
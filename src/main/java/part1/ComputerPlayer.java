package part1;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * ComputerPlayer
 * A Player that makes random choices.
 * It rolls the dicethen collects resources, then randomly builds a road/settlement/city
 * if it can and if there are valid spots.
 *
 * @author Team 28
 */

public class ComputerPlayer extends Player {

    private final Random r = new Random();

    public ComputerPlayer(int playerId){

        super(playerId);

    }
    @Override
    protected Player.TurnResult choosePriorityTurnAction(Board board, Game game, TurnOptions turnOptions){

        // 0) Maritime trade: if holding >= 4 of any resource, trade with the bank before making a move
        for (int resourceIndex = 0; resourceIndex < ResourceType.values().length; resourceIndex++)
        {
            ResourceType giveResource = ResourceType.values()[resourceIndex];
            if (!hasAtLeastXResources(giveResource, 4))
            {
                continue;
            }

            // Shuffle candidate receive resources to avoid always picking the same one
            ResourceType[] allResourceTypes = ResourceType.values();
            int[] shuffledIndices = buildShuffledIndicesExcluding(allResourceTypes.length, resourceIndex);

            for (int candidateIndex = 0; candidateIndex < shuffledIndices.length; candidateIndex++)
            {
                ResourceType getResource = allResourceTypes[shuffledIndices[candidateIndex]];
                if (game.canMaritiмeTrade(this, giveResource, getResource))
                {
                    return Player.TurnResult.maritimeTrade(
                            giveResource,
                            getResource,
                            "Computer trades 4 " + giveResource + " for 1 " + getResource
                    );
                }
            }
            // No valid trade found for this resource — continue checking others
        }

        if (turnOptions.canBuildRoad) {
            int gapEdge = findGapBridgingEdge(board);
            if (gapEdge != -1) {
                return Player.TurnResult.buildRoad(gapEdge, "Chose to build ROAD to connect road segments");
            }

            int defenceEdge = findLongestRoadExtensionEdge(board, game);
            if (defenceEdge != -1) {
                return Player.TurnResult.buildRoad(defenceEdge, "Chose to build ROAD to protect longest road");
            }
        }

        return null;
    }

    @Override
    protected boolean shouldAutoPassTurn(TurnOptions turnOptions) {
        if (turnOptions.totalCards <= 7) {
            return true;
        }

        if (!turnOptions.canAffordAnyBuild()) {
            return true;
        }

        return !turnOptions.hasAnyValidMove();
    }

    @Override
    protected Player.TurnResult createAutoPassResult(TurnOptions turnOptions) {
        if (turnOptions.totalCards <= 7) {
            return Player.TurnResult.pass("Under 8 cards, so no move.");
        }

        if (!turnOptions.canAffordAnyBuild()) {
            return Player.TurnResult.pass("Cannot afford any roads or buildings.");
        }

        return Player.TurnResult.pass("No valid moves to be made.");
    }

    @Override
    protected Player.TurnResult chooseTurnAction(Board board, Game game, TurnOptions turnOptions) {
        int totalOptions = turnOptions.validRoadEdges.size()
                + turnOptions.validSettlementNodes.size()
                + turnOptions.validCityNodes.size();
        int choice = r.nextInt(totalOptions);

        // buildRoad
        if (choice < turnOptions.validRoadEdges.size()) {
            int edgeIndex = turnOptions.validRoadEdges.get(choice).intValue();
            return Player.TurnResult.buildRoad(edgeIndex, "Chose to build ROAD at edge " + edgeIndex);
        }

        // buildSettlement
        choice -= turnOptions.validRoadEdges.size();
        if (choice < turnOptions.validSettlementNodes.size()) {
            int nodeId = turnOptions.validSettlementNodes.get(choice).intValue();
            return Player.TurnResult.buildSettlement(nodeId, "Chose to build SETTLEMENT at node " + nodeId);
        }

        // buildCity
        choice -= turnOptions.validSettlementNodes.size();
        int nodeId = turnOptions.validCityNodes.get(choice).intValue();
        return Player.TurnResult.buildCity(nodeId, "Chose to build CITY at node " + nodeId);
    }









    // Helpers
    /**
     * Builds a shuffled array of indices from 0..length-1, excluding one specific index.
     * Used to randomly try different receive resources for maritime trading.
     *
     * @param length total number of resource types
     * @param excludedIndex index to skip (the give resource)
     * @return shuffled array of candidate receive resource indices
     */
    private int[] buildShuffledIndicesExcluding(int length, int excludedIndex)
    {
        int candidateCount = length - 1;
        int[] indices = new int[candidateCount];
        int insertPosition = 0;

        for (int i = 0; i < length; i++)
        {
            if (i != excludedIndex)
            {
                indices[insertPosition] = i;
                insertPosition++;
            }
        }

        for (int swapTarget = candidateCount - 1; swapTarget > 0; swapTarget--)
        {
            int swapSource = r.nextInt(swapTarget + 1);
            int temp = indices[swapTarget];
            indices[swapTarget] = indices[swapSource];
            indices[swapSource] = temp;
        }

        return indices;
    }

    private int findGapBridgingEdge(Board board)
    {
        List<Integer> validRoadEdges = findValidRoadPlacements(board);
        if (validRoadEdges.isEmpty())
        {
            return -1;
        }

        int[] componentByNode = buildRoadComponentLookup(board);

        for (int i = 0; i < validRoadEdges.size(); i++)
        {
            int edgeIndex = validRoadEdges.get(i).intValue();
            Board.Edge edge = board.getEdge(edgeIndex);
            int component1 = componentByNode[edge.node1];
            int component2 = componentByNode[edge.node2];

            if (component1 != -1 && component2 != -1 && component1 != component2)
            {
                return edgeIndex;
            }
        }

        for (int i = 0; i < validRoadEdges.size(); i++)
        {
            int edgeIndex = validRoadEdges.get(i).intValue();
            Board.Edge edge = board.getEdge(edgeIndex);
            int component1 = componentByNode[edge.node1];
            int component2 = componentByNode[edge.node2];

            if (component1 != -1 && component2 == -1)
            {
                if (reachesDifferentComponentInOneMoreRoad(board, edgeIndex, edge.node2, component1, componentByNode))
                {
                    return edgeIndex;
                }
            }

            if (component2 != -1 && component1 == -1)
            {
                if (reachesDifferentComponentInOneMoreRoad(board, edgeIndex, edge.node1, component2, componentByNode))
                {
                    return edgeIndex;
                }
            }
        }

        return -1;
    }

    private int findLongestRoadExtensionEdge(Board board, Game game)
    {
        int myLongestRoad = board.computeLongestRoadForPlayer(getPlayerId());
        if (!isOpponentWithinOneRoad(board, myLongestRoad))
        {
            return -1;
        }

        List<Integer> validRoadEdges = findValidRoadPlacements(board);
        if (validRoadEdges.isEmpty())
        {
            return -1;
        }

        for (int i = 0; i < validRoadEdges.size(); i++)
        {
            int edgeIndex = validRoadEdges.get(i).intValue();
            if (extendsRoadEndpoint(board, edgeIndex))
            {
                return edgeIndex;
            }
        }

        return validRoadEdges.get(0).intValue();
    }

    private int[] buildRoadComponentLookup(Board board)
    {
        int[] componentByNode = new int[Board.NODE_COUNT];
        for (int i = 0; i < componentByNode.length; i++)
        {
            componentByNode[i] = -1;
        }

        int nextComponentId = 0;
        for (int edgeIndex = 0; edgeIndex < Board.EDGE_COUNT; edgeIndex++)
        {
            Board.Road road = board.getRoad(edgeIndex);
            if (road == null || road.ownerPlayerId != getPlayerId())
            {
                continue;
            }

            Board.Edge edge = board.getEdge(edgeIndex);
            if (componentByNode[edge.node1] == -1)
            {
                markRoadComponent(board, edge.node1, nextComponentId, componentByNode);
                nextComponentId++;
            }
        }

        return componentByNode;
    }

    private void markRoadComponent(Board board, int startNodeId, int componentId, int[] componentByNode)
    {
        List<Integer> queue = new ArrayList<Integer>();
        queue.add(Integer.valueOf(startNodeId));
        componentByNode[startNodeId] = componentId;

        for (int cursor = 0; cursor < queue.size(); cursor++)
        {
            int nodeId = queue.get(cursor).intValue();
            List<Integer> adjacentEdges = board.getAdjacentEdgeIndicesForNode(nodeId);

            for (int i = 0; i < adjacentEdges.size(); i++)
            {
                int edgeIndex = adjacentEdges.get(i).intValue();
                Board.Road road = board.getRoad(edgeIndex);
                if (road == null || road.ownerPlayerId != getPlayerId())
                {
                    continue;
                }

                Board.Edge edge = board.getEdge(edgeIndex);
                int nextNodeId = getOtherNodeId(edge, nodeId);
                if (componentByNode[nextNodeId] != -1)
                {
                    continue;
                }

                componentByNode[nextNodeId] = componentId;
                queue.add(Integer.valueOf(nextNodeId));
            }
        }
    }

    private boolean reachesDifferentComponentInOneMoreRoad(Board board, int firstEdgeIndex, int frontierNodeId, int sourceComponentId, int[] componentByNode)
    {
        List<Integer> adjacentEdges = board.getAdjacentEdgeIndicesForNode(frontierNodeId);
        for (int i = 0; i < adjacentEdges.size(); i++)
        {
            int edgeIndex = adjacentEdges.get(i).intValue();
            if (edgeIndex == firstEdgeIndex || !board.isRoadEmpty(edgeIndex))
            {
                continue;
            }

            Board.Edge edge = board.getEdge(edgeIndex);
            int otherNodeId = getOtherNodeId(edge, frontierNodeId);
            int otherComponentId = componentByNode[otherNodeId];
            if (otherComponentId != -1 && otherComponentId != sourceComponentId)
            {
                return true;
            }
        }

        return false;
    }

    private boolean isOpponentWithinOneRoad(Board board, int myLongestRoad)
    {
        List<Integer> opponentIds = collectOpponentPlayerIds(board);
        for (int i = 0; i < opponentIds.size(); i++)
        {
            int opponentId = opponentIds.get(i).intValue();
            int opponentLongestRoad = board.computeLongestRoadForPlayer(opponentId);
            if (opponentLongestRoad >= myLongestRoad - 1)
            {
                return true;
            }
        }

        return false;
    }

    private List<Integer> collectOpponentPlayerIds(Board board)
    {
        List<Integer> opponentIds = new ArrayList<Integer>();

        for (int edgeIndex = 0; edgeIndex < Board.EDGE_COUNT; edgeIndex++)
        {
            Board.Road road = board.getRoad(edgeIndex);
            if (road == null || road.ownerPlayerId == getPlayerId())
            {
                continue;
            }
            addIdIfMissing(opponentIds, road.ownerPlayerId);
        }

        for (int nodeId = 0; nodeId < Board.NODE_COUNT; nodeId++)
        {
            Board.Building building = board.getBuilding(nodeId);
            if (building == null || building.ownerPlayerId == getPlayerId())
            {
                continue;
            }
            addIdIfMissing(opponentIds, building.ownerPlayerId);
        }

        return opponentIds;
    }

    private void addIdIfMissing(List<Integer> ids, int playerId)
    {
        Integer boxedPlayerId = Integer.valueOf(playerId);
        if (!ids.contains(boxedPlayerId))
        {
            ids.add(boxedPlayerId);
        }
    }

    private boolean extendsRoadEndpoint(Board board, int edgeIndex)
    {
        Board.Edge edge = board.getEdge(edgeIndex);
        return isEndpointExtension(board, edge.node1, edge.node2) || isEndpointExtension(board, edge.node2, edge.node1);
    }

    private boolean isEndpointExtension(Board board, int connectedNodeId, int newNodeId)
    {
        if (getMyRoadDegree(board, connectedNodeId) != 1)
        {
            return false;
        }

        return !doesNodeTouchMyRoad(board, newNodeId);
    }

    private int getMyRoadDegree(Board board, int nodeId)
    {
        int roadCount = 0;
        List<Integer> adjacentEdges = board.getAdjacentEdgeIndicesForNode(nodeId);
        for (int i = 0; i < adjacentEdges.size(); i++)
        {
            int edgeIndex = adjacentEdges.get(i).intValue();
            Board.Road road = board.getRoad(edgeIndex);
            if (road != null && road.ownerPlayerId == getPlayerId())
            {
                roadCount++;
            }
        }

        return roadCount;
    }

    private int getOtherNodeId(Board.Edge edge, int nodeId)
    {
        if (edge.node1 == nodeId)
        {
            return edge.node2;
        }
        return edge.node1;
    }

    /**
     * Places the initial settlement and road during setup.
     * Picks a random valid node for the settlement, then a random free adjacent edge for the road.
     *
     * @param board game board
     * @param roundNumber 1 or 2 (setup round number)
     * @return the node id where the settlement was placed
     */
    @Override
    public int placeInitialSettlementAndRoad(Board board, int roundNumber)
    {
        boolean isSecondSettlement = (roundNumber == 2);
        int settlementNodeId = placeRandomValidSettlement(board, isSecondSettlement);
        placeRandomValidRoad(board, settlementNodeId);
        return settlementNodeId;
    }

    /**
     * Places a settlement at a random valid node.
     * It tries many random nodes until it finds one that works.
     *
     * @param board game board
     * @param isSecondSettlement true if this is the second starting settlement (not used here)
     * @return the node id where it placed the settlement
     */
    public int placeRandomValidSettlement(Board board, boolean isSecondSettlement) {

        int maxAttempts = 10000;

        for (int attempt = 0; attempt < maxAttempts; attempt++) {

            int nodeId = r.nextInt(Board.NODE_COUNT);

            if (!board.isNodeEmpty(nodeId)){
                continue;
            }
            if (board.violatesDistanceRule(nodeId)) {
                continue;
            }

            board.placeSettlement(nodeId, this);
            return nodeId;

        }

        throw new IllegalStateException("Nowhere to place a valid settlement");

    }
    /**
     * Places a road next to a given settlement node.
     * Picks a random adjacent edge that is empty.
     *
     * @param board game board
     * @param settlementNodeId node id of the settlement
     * @return the edge index where it placed the road
     */
    public int placeRandomValidRoad(Board board, int settlementNodeId) {

        List<Integer> adjacentEdgeIndices = board.getAdjacentEdgeIndicesForNode(settlementNodeId);

        if (adjacentEdgeIndices.isEmpty()) {
            throw new IllegalStateException("No edges adjacent to node " + settlementNodeId + ". Board edges arent initialized yet.");
        }

        List<Integer> freeAdjacentEdges = new ArrayList<>();
        for (int i = 0; i < adjacentEdgeIndices.size(); i++){

            int edgeIndex = adjacentEdgeIndices.get(i).intValue();
            if (board.isRoadEmpty(edgeIndex)){
                freeAdjacentEdges.add(Integer.valueOf(edgeIndex));
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
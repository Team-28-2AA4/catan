package part1;

import java.util.Scanner;

/**
 * HumanPlayer
 * A Player controlled by a human through console input.
 *
 * @author Team 28
 */
public class HumanPlayer extends Player
{
    private static final Scanner inputScanner = new Scanner(System.in);

    public HumanPlayer(int playerId)
    {
        super(playerId);
    }

    @Override
    protected TurnResult chooseTurnAction(Board board, Game game, TurnOptions turnOptions)
    {
        System.out.println("--- Player " + getPlayerId() + "'s turn ---");
        System.out.println("[Roll] [Go] [List] [Build [settlement [nodeId] | city [nodeId] | road [fromNodeId, toNodeId]]] [Trade [give] [get]]");
        while (true)
        {
            System.out.print("Player " + getPlayerId() + "> ");
            String input = inputScanner.nextLine();

            Player.TurnResult result = CommandParser.parse(input, board);

            if (result == null)
            {
                System.out.println("Unknown command. Try: Roll, Go, List, Build settlement [nodeId], Build city [nodeId], Build road [node1, node2], Trade [give] [get]");
                continue;
            }

            // Handle List command - print resources and continue loop
            if (result.decisionSummary != null && result.decisionSummary.equals("List"))
            {
                printResourceCounts();
                continue;
            }

            // Handle Roll command - just end turn (dice already rolled by Game)
            if (result.decisionSummary != null && result.decisionSummary.equals("Roll"))
            {
                return Player.TurnResult.pass("Rolled dice.");
            }

            // Handle Go command or any build command - end turn
            return result;
        }
    }

    @Override
    public int placeInitialSettlementAndRoad(Board board, int roundNumber)
    {
        System.out.println("Setup Round " + roundNumber + " - Player " + getPlayerId());

        int settlementNodeId = promptForValidSettlementNode(board);
        board.placeSettlement(settlementNodeId, this);

        int roadEdgeIndex = promptForValidRoadEdge(board, settlementNodeId);
        board.placeRoad(roadEdgeIndex, getPlayerId());

        return settlementNodeId;
    }

    /**
     * Prompts the human to enter a valid node ID for a settlement.
     * Loops until a valid, empty, distance-rule-passing node is entered.
     *
     * @param board game board
     * @return valid node ID chosen by the human
     */
    private int promptForValidSettlementNode(Board board)
    {
        int settlementNodeId = -1;
        while (settlementNodeId == -1)
        {
            System.out.print("Enter node ID for settlement: ");
            String input = inputScanner.nextLine().trim();

            try
            {
                settlementNodeId = Integer.parseInt(input);
                settlementNodeId = validateSettlementNode(board, settlementNodeId);
            }
            catch (NumberFormatException e)
            {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
        return settlementNodeId;
    }

    /**
     * Validates a settlement node ID against board rules.
     * Returns the node ID if valid, or -1 if invalid (with an error message printed).
     *
     * @param board game board
     * @param nodeId node ID to validate
     * @return nodeId if valid, -1 otherwise
     */
    private int validateSettlementNode(Board board, int nodeId)
    {
        if (nodeId < 0 || nodeId >= Board.NODE_COUNT)
        {
            System.out.println("Invalid node ID. Must be between 0 and " + (Board.NODE_COUNT - 1));
            return -1;
        }
        if (!board.isNodeEmpty(nodeId))
        {
            System.out.println("Node " + nodeId + " is already occupied.");
            return -1;
        }
        if (board.violatesDistanceRule(nodeId))
        {
            System.out.println("Node " + nodeId + " violates distance rule.");
            return -1;
        }
        return nodeId;
    }

    /**
     * Prompts the human to enter a valid edge index for a road adjacent to a settlement.
     * Loops until a valid, empty, adjacent edge is entered.
     *
     * @param board game board
     * @param settlementNodeId the node the road must be adjacent to
     * @return valid edge index chosen by the human
     */
    private int promptForValidRoadEdge(Board board, int settlementNodeId)
    {
        java.util.List<Integer> adjacentEdges = board.getAdjacentEdgeIndicesForNode(settlementNodeId);
        System.out.println("Valid edges adjacent to node " + settlementNodeId + ": " + adjacentEdges);

        int roadEdgeIndex = -1;
        while (roadEdgeIndex == -1)
        {
            System.out.print("Enter edge index for road: ");
            String input = inputScanner.nextLine().trim();

            try
            {
                roadEdgeIndex = Integer.parseInt(input);
                roadEdgeIndex = validateRoadEdge(board, roadEdgeIndex, adjacentEdges, settlementNodeId);
            }
            catch (NumberFormatException e)
            {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
        return roadEdgeIndex;
    }

    /**
     * Validates a road edge index against board rules.
     * Returns the edge index if valid, or -1 if invalid (with an error message printed).
     *
     * @param board game board
     * @param edgeIndex edge index to validate
     * @param adjacentEdges list of edges adjacent to the settlement node
     * @param settlementNodeId the settlement node for error messaging
     * @return edgeIndex if valid, -1 otherwise
     */
    private int validateRoadEdge(Board board, int edgeIndex, java.util.List<Integer> adjacentEdges, int settlementNodeId)
    {
        if (edgeIndex < 0 || edgeIndex >= Board.EDGE_COUNT)
        {
            System.out.println("Invalid edge index. Must be between 0 and " + (Board.EDGE_COUNT - 1));
            return -1;
        }
        if (!board.isRoadEmpty(edgeIndex))
        {
            System.out.println("Edge " + edgeIndex + " already has a road.");
            return -1;
        }
        if (!adjacentEdges.contains(Integer.valueOf(edgeIndex)))
        {
            System.out.println("Edge " + edgeIndex + " is not adjacent to node " + settlementNodeId);
            return -1;
        }
        return edgeIndex;
    }

    /**
     * Prints the current resource counts for this player.
     */
    private void printResourceCounts()
    {
        System.out.println("Player " + getPlayerId() + " resources:");
        System.out.println("  LUMBER: " + getResourceCount(ResourceType.LUMBER));
        System.out.println("  BRICK: " + getResourceCount(ResourceType.BRICK));
        System.out.println("  WOOL: " + getResourceCount(ResourceType.WOOL));
        System.out.println("  GRAIN: " + getResourceCount(ResourceType.GRAIN));
        System.out.println("  ORE: " + getResourceCount(ResourceType.ORE));
    }
}

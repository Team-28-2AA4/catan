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
    public TurnResult turn(Board board)
    {
        while (true)
        {
            System.out.print("Player " + getPlayerId() + "> ");
            String input = inputScanner.nextLine();

            Player.TurnResult result = CommandParser.parse(input, board);

            if (result == null)
            {
                System.out.println("Unknown command. Try: Roll, Go, List, Build settlement [nodeId], Build city [nodeId], Build road [node1, node2]");
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

        // Prompt for settlement node
        int settlementNodeId = -1;
        while (settlementNodeId == -1)
        {
            System.out.print("Enter node ID for settlement: ");
            String input = inputScanner.nextLine().trim();

            try
            {
                settlementNodeId = Integer.parseInt(input);
                if (settlementNodeId < 0 || settlementNodeId >= Board.NODE_COUNT)
                {
                    System.out.println("Invalid node ID. Must be between 0 and " + (Board.NODE_COUNT - 1));
                    settlementNodeId = -1;
                    continue;
                }
                if (!board.isNodeEmpty(settlementNodeId))
                {
                    System.out.println("Node " + settlementNodeId + " is already occupied.");
                    settlementNodeId = -1;
                    continue;
                }
                if (board.violatesDistanceRule(settlementNodeId))
                {
                    System.out.println("Node " + settlementNodeId + " violates distance rule.");
                    settlementNodeId = -1;
                    continue;
                }
            }
            catch (NumberFormatException e)
            {
                System.out.println("Invalid input. Please enter a number.");
            }
        }

        board.placeSettlement(settlementNodeId, this);

        // Prompt for road edge
        int roadEdgeIndex = -1;
        while (roadEdgeIndex == -1)
        {
            System.out.print("Enter edge index for road (adjacent to node " + settlementNodeId + "): ");
            String input = inputScanner.nextLine().trim();

            try
            {
                roadEdgeIndex = Integer.parseInt(input);
                if (roadEdgeIndex < 0 || roadEdgeIndex >= Board.EDGE_COUNT)
                {
                    System.out.println("Invalid edge index. Must be between 0 and " + (Board.EDGE_COUNT - 1));
                    roadEdgeIndex = -1;
                    continue;
                }
                if (!board.isRoadEmpty(roadEdgeIndex))
                {
                    System.out.println("Edge " + roadEdgeIndex + " already has a road.");
                    roadEdgeIndex = -1;
                    continue;
                }
                // Check if edge is adjacent to the settlement node
                java.util.List<Integer> adjacentEdges = board.getAdjacentEdgeIndicesForNode(settlementNodeId);
                if (!adjacentEdges.contains(Integer.valueOf(roadEdgeIndex)))
                {
                    System.out.println("Edge " + roadEdgeIndex + " is not adjacent to node " + settlementNodeId);
                    roadEdgeIndex = -1;
                    continue;
                }
            }
            catch (NumberFormatException e)
            {
                System.out.println("Invalid input. Please enter a number.");
            }
        }

        board.placeRoad(roadEdgeIndex, getPlayerId());

        return settlementNodeId;
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

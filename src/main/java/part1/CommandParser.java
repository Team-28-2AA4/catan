package part1;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * CommandParser
 * Parses human input commands using regular expressions.
 * Converts string commands into TurnResult objects.
 *
 * @author Team 28
 */
public class CommandParser
{
    // Case-insensitive patterns for each command
    private static final Pattern ROLL_PATTERN = Pattern.compile("^\\s*roll\\s*$", Pattern.CASE_INSENSITIVE);
    private static final Pattern GO_PATTERN = Pattern.compile("^\\s*go\\s*$", Pattern.CASE_INSENSITIVE);
    private static final Pattern LIST_PATTERN = Pattern.compile("^\\s*list\\s*$", Pattern.CASE_INSENSITIVE);
    private static final Pattern BUILD_SETTLEMENT_PATTERN = Pattern.compile("^\\s*build\\s+settlement\\s+(\\d+)\\s*$", Pattern.CASE_INSENSITIVE);
    private static final Pattern BUILD_CITY_PATTERN = Pattern.compile("^\\s*build\\s+city\\s+(\\d+)\\s*$", Pattern.CASE_INSENSITIVE);
    private static final Pattern BUILD_ROAD_PATTERN = Pattern.compile("^\\s*build\\s+road\\s+(\\d+)\\s*,\\s*(\\d+)\\s*$", Pattern.CASE_INSENSITIVE);

    /**
     * Parses a human input command string and returns the corresponding TurnResult.
     * Uses regular expressions to match commands case-insensitively.
     *
     * @param input the raw command string from the user
     * @param board the game board (needed to convert node IDs to edge index for roads)
     * @return TurnResult if command is valid, null if command is unrecognized
     */
    public static Player.TurnResult parse(String input, Board board)
    {
        if (input == null)
        {
            return null;
        }

        String trimmedInput = input.trim();

        // Check Roll command
        Matcher rollMatcher = ROLL_PATTERN.matcher(trimmedInput);
        if (rollMatcher.matches())
        {
            return Player.TurnResult.pass("Roll");
        }

        // Check Go command
        Matcher goMatcher = GO_PATTERN.matcher(trimmedInput);
        if (goMatcher.matches())
        {
            return Player.TurnResult.pass("Go");
        }

        // Check List command
        Matcher listMatcher = LIST_PATTERN.matcher(trimmedInput);
        if (listMatcher.matches())
        {
            return Player.TurnResult.pass("List");
        }

        // Check Build Settlement command
        Matcher settlementMatcher = BUILD_SETTLEMENT_PATTERN.matcher(trimmedInput);
        if (settlementMatcher.matches())
        {
            int nodeId = Integer.parseInt(settlementMatcher.group(1));
            return Player.TurnResult.buildSettlement(nodeId, "Build settlement at node " + nodeId);
        }

        // Check Build City command
        Matcher cityMatcher = BUILD_CITY_PATTERN.matcher(trimmedInput);
        if (cityMatcher.matches())
        {
            int nodeId = Integer.parseInt(cityMatcher.group(1));
            return Player.TurnResult.buildCity(nodeId, "Build city at node " + nodeId);
        }

        // Check Build Road command
        Matcher roadMatcher = BUILD_ROAD_PATTERN.matcher(trimmedInput);
        if (roadMatcher.matches())
        {
            int node1 = Integer.parseInt(roadMatcher.group(1));
            int node2 = Integer.parseInt(roadMatcher.group(2));

            // Find the edge index that connects these two nodes
            int edgeIndex = findEdgeIndexFromNodeIds(board, node1, node2);
            if (edgeIndex == -1)
            {
                // Invalid node pair - no edge exists between them
                return null;
            }

            return Player.TurnResult.buildRoad(edgeIndex, "Build road between nodes " + node1 + " and " + node2);
        }

        // No match found
        return null;
    }

    /**
     * Finds the edge index that connects two given node IDs.
     * Returns -1 if no such edge exists.
     *
     * @param board the game board
     * @param node1 first node ID
     * @param node2 second node ID
     * @return edge index, or -1 if not found
     */
    private static int findEdgeIndexFromNodeIds(Board board, int node1, int node2)
    {
        // Normalize node order (edges store node1 < node2)
        int minNode = Math.min(node1, node2);
        int maxNode = Math.max(node1, node2);

        // Search through all edges to find one matching both nodes
        for (int edgeIndex = 0; edgeIndex < Board.EDGE_COUNT; edgeIndex++)
        {
            Board.Edge edge = board.getEdge(edgeIndex);
            if (edge.node1 == minNode && edge.node2 == maxNode)
            {
                return edgeIndex;
            }
        }

        return -1;
    }
}

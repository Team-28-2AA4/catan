package part1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CommandParser.
 * Verifies that regular expressions correctly parse all valid human commands
 * and reject invalid inputs.
 */
public class CommandParserTest
{
    private Board boardForParsingTests;

    @BeforeEach
    void setUp()
    {
        boardForParsingTests = new Board();
    }

    // Test 1 --> Roll command parses correctly
    @Test
    void parseRollCommandReturnsPassWithRollSummary()
    {
        Player.TurnResult result = CommandParser.parse("Roll", boardForParsingTests);
        assertNotNull(result);
        assertEquals(ActionType.PASS, result.actionType);
        assertEquals("Roll", result.decisionSummary);
    }

    // Test 2 --> Go command parses correctly
    @Test
    void parseGoCommandReturnsPassWithGoSummary()
    {
        Player.TurnResult result = CommandParser.parse("Go", boardForParsingTests);
        assertNotNull(result);
        assertEquals(ActionType.PASS, result.actionType);
        assertEquals("Go", result.decisionSummary);
    }

    // Test 3 --> List command parses correctly
    @Test
    void parseListCommandReturnsPassWithListSummary()
    {
        Player.TurnResult result = CommandParser.parse("List", boardForParsingTests);
        assertNotNull(result);
        assertEquals(ActionType.PASS, result.actionType);
        assertEquals("List", result.decisionSummary);
    }

    // Test 4 --> Build settlement command captures node ID correctly
    @Test
    void parseBuildSettlementCommandCapturesNodeId()
    {
        Player.TurnResult result = CommandParser.parse("Build settlement 5", boardForParsingTests);
        assertNotNull(result);
        assertEquals(ActionType.BUILD_SETTLEMENT, result.actionType);
        assertEquals(5, result.nodeId);
    }

    // Test 5 --> Build city command captures node ID correctly
    @Test
    void parseBuildCityCommandCapturesNodeId()
    {
        Player.TurnResult result = CommandParser.parse("Build city 12", boardForParsingTests);
        assertNotNull(result);
        assertEquals(ActionType.BUILD_CITY, result.actionType);
        assertEquals(12, result.nodeId);
    }

    // Test 6 --> Build road command captures both node IDs and finds edge index
    @Test
    void parseBuildRoadCommandCapturesNodeIdsAndFindsEdgeIndex()
    {
        // Node 0 and node 1 are connected by an edge (both on tile 0)
        Player.TurnResult result = CommandParser.parse("Build road 0, 1", boardForParsingTests);
        assertNotNull(result);
        assertEquals(ActionType.BUILD_ROAD, result.actionType);
        assertTrue(result.edgeIndex >= 0);
        assertTrue(result.edgeIndex < Board.EDGE_COUNT);
    }

    // Test 7 --> Invalid garbage input returns null
    @Test
    void parseInvalidGarbageInputReturnsNull()
    {
        Player.TurnResult result = CommandParser.parse("hello world", boardForParsingTests);
        assertNull(result);
    }

    // Test 8 --> Mixed case commands are handled correctly (case-insensitive)
    @Test
    void parseMixedCaseCommandsAreHandledCaseInsensitively()
    {
        Player.TurnResult rollResult = CommandParser.parse("rOlL", boardForParsingTests);
        assertNotNull(rollResult);
        assertEquals("Roll", rollResult.decisionSummary);

        Player.TurnResult settlementResult = CommandParser.parse("BUILD SETTLEMENT 7", boardForParsingTests);
        assertNotNull(settlementResult);
        assertEquals(ActionType.BUILD_SETTLEMENT, settlementResult.actionType);
        assertEquals(7, settlementResult.nodeId);
    }

    // Test 9 --> Commands with extra whitespace are handled correctly
    @Test
    void parseCommandsWithExtraWhitespaceAreHandledCorrectly()
    {
        Player.TurnResult result = CommandParser.parse("  Build   settlement   15  ", boardForParsingTests);
        assertNotNull(result);
        assertEquals(ActionType.BUILD_SETTLEMENT, result.actionType);
        assertEquals(15, result.nodeId);
    }

    // Test 10 --> Build road with invalid node pair returns null
    @Test
    void parseBuildRoadWithInvalidNodePairReturnsNull()
    {
        // Node 0 and node 50 are not connected by an edge
        Player.TurnResult result = CommandParser.parse("Build road 0, 50", boardForParsingTests);
        assertNull(result);
    }

    // Test 11 --> Trade command with valid resource names parses to MARITIME_TRADE with correct give and get
    @Test
    void parseTradeCommandWithValidResourceNamesParsesToMaritimeTradeActionType()
    {
        Player.TurnResult maritimeTradeResult = CommandParser.parse("Trade LUMBER BRICK", boardForParsingTests);
        assertNotNull(maritimeTradeResult);
        assertEquals(ActionType.MARITIME_TRADE, maritimeTradeResult.actionType);
        assertEquals(ResourceType.LUMBER, maritimeTradeResult.resourceToGive);
        assertEquals(ResourceType.BRICK, maritimeTradeResult.resourceToGet);
    }

    // Test 12 --> Trade command with an invalid resource name returns null
    @Test
    void parseTradeCommandWithInvalidResourceNameReturnsNull()
    {
        Player.TurnResult invalidTradeResult = CommandParser.parse("Trade LUMBER INVALID", boardForParsingTests);
        assertNull(invalidTradeResult);
    }
}

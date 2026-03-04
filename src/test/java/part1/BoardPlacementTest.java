package part1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Board placement mechanics.
 * Covers road/settlement placement state changes and the distance rule.
 */
public class BoardPlacementTest
{
    private Board freshBoardForPlacementTesting;
    private Player playerUsedForPlacementTesting;

    @BeforeEach
    void setUp()
    {
        freshBoardForPlacementTesting = new Board();
        playerUsedForPlacementTesting = new ComputerPlayer(1);
    }

    // Test 1 --> Placing a road on an empty edge makes that edge no longer empty
    @Test
    void placingRoadOnEmptyEdgeMakesEdgeNoLongerEmpty()
    {
        freshBoardForPlacementTesting.placeRoad(0, playerUsedForPlacementTesting.getPlayerId());
        assertFalse(freshBoardForPlacementTesting.isRoadEmpty(0));
    }

    // Test 2 --> Placing a settlement on an empty node makes that node no longer empty
    @Test
    void placingSettlementOnEmptyNodeMakesNodeNoLongerEmpty()
    {
        freshBoardForPlacementTesting.placeSettlement(0, playerUsedForPlacementTesting);
        assertFalse(freshBoardForPlacementTesting.isNodeEmpty(0));
    }

    // Test 3 --> Partition A: distance rule returns false when the board has no buildings at all
    @Test
    void violatesDistanceRule_returnsFalseOnCompletelyEmptyBoard()
    {
        assertFalse(freshBoardForPlacementTesting.violatesDistanceRule(0));
    }

    // Test 4 --> Partition B: distance rule returns true when a neighbouring node already has a settlement
    @Test
    void violatesDistanceRule_returnsTrueWhenDirectNeighbourNodeHasSettlement()
    {
        // Node 0 and node 1 are neighbours (both on the corner of tile 0).
        // Placing at node 0 should cause the distance rule to trigger for node 1.
        freshBoardForPlacementTesting.placeSettlement(0, playerUsedForPlacementTesting);
        assertTrue(freshBoardForPlacementTesting.violatesDistanceRule(1));
    }
}

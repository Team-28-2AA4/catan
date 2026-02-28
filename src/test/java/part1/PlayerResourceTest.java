package part1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerResourceTest {

    private Player player;

    @BeforeEach
    void setUp() {
        // ComputerPlayer is the concrete subclass of the abstract Player
        player = new ComputerPlayer(1);
    }

    // Test 1 --> Adding a resource correctly increases the player's count
    @Test
    void addingResourceIncreasesCount() {
        player.addResource(ResourceType.LUMBER, 3);
        assertEquals(3, player.getResourceCount(ResourceType.LUMBER));
    }

    // Test 2 --> Spending a resource correctly decreases the player's count
    @Test
    void spendingResourceDecreasesCount() {
        player.addResource(ResourceType.BRICK, 3);
        player.spendResource(ResourceType.BRICK, 2);
        assertEquals(1, player.getResourceCount(ResourceType.BRICK));
    }

    // Test 3 --> Spending more than available throws an exception
    @Test
    void spendingMoreThanAvailableThrowsException() {
        assertThrows(IllegalStateException.class, () -> player.spendResource(ResourceType.ORE, 1));
    }

    // Test 4 --> canAffordRoad returns true when the player has sufficient resources
    // Partition A: player has exactly the resources needed for a road (1 lumber and 1 brick)
    @Test
    void canAffordRoadWithEnoughResources() {
        player.addResource(ResourceType.LUMBER, 1);
        player.addResource(ResourceType.BRICK, 1);
        assertTrue(player.canAffordRoad());
    }

    // Test 5 --> canAffordRoad returns false when the player is missing a resource
    // Partition B: player has lumber but no brick
    @Test
    void canAffordRoadWithNotEnoughResources() {
        player.addResource(ResourceType.LUMBER, 1);
        assertFalse(player.canAffordRoad());
    }
}

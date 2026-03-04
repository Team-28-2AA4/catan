package part1;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Game bank resource management.
 * Covers the returnToBank method across normal and edge-case inputs.
 */
public class GameBankTest
{
    private Game gameInstanceForBankTesting;

    @BeforeEach
    void setUp()
    {
        Board boardForBankTesting = new Board();
        List<Player> playersForBankTesting = new ArrayList<>();
        playersForBankTesting.add(new ComputerPlayer(1));
        gameInstanceForBankTesting = new Game(boardForBankTesting, playersForBankTesting);
    }

    // Test 1 --> Returning a valid amount of resources to the bank does not throw
    @Test
    void returningValidAmountOfLumberToBankDoesNotThrow()
    {
        assertDoesNotThrow(() -> gameInstanceForBankTesting.returnToBank(ResourceType.LUMBER, 10));
    }

    // Test 2 --> Returning more resources than the bank maximum throws an exception
    @Test
    void returningAmountThatExceedsBankMaximumThrowsIllegalStateException()
    {
        assertThrows(IllegalStateException.class, () -> gameInstanceForBankTesting.returnToBank(ResourceType.LUMBER, 20));
    }

    // Test 3 --> Returning a null resource type is safely ignored without throwing
    @Test
    void returningNullResourceTypeToBankIsIgnoredSafely()
    {
        assertDoesNotThrow(() -> gameInstanceForBankTesting.returnToBank(null, 5));
    }

    // Test 4 --> Returning zero of a resource is safely ignored without throwing
    @Test
    void returningZeroAmountToBankIsIgnoredSafely()
    {
        assertDoesNotThrow(() -> gameInstanceForBankTesting.returnToBank(ResourceType.BRICK, 0));
    }
}

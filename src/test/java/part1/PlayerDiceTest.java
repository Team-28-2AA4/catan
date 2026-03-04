package part1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Player dice rolling behaviour.
 * Uses boundary testing to verify the result always falls within the valid range of 2 to 12.
 */
public class PlayerDiceTest
{
    private static final int NUMBER_OF_ROLLS_FOR_BOUNDARY_VERIFICATION = 1000;

    private Player playerUsedForDiceRolling;

    @BeforeEach
    void setUp()
    {
        playerUsedForDiceRolling = new ComputerPlayer(1);
    }

    // Test 1 --> Boundary: dice roll result is never below the minimum possible value of 2
    @Test
    void diceRollResultIsNeverBelowMinimumValueOfTwo()
    {
        for (int rollAttempt = 0; rollAttempt < NUMBER_OF_ROLLS_FOR_BOUNDARY_VERIFICATION; rollAttempt++)
        {
            int result = playerUsedForDiceRolling.diceRoll();
            assertTrue(result >= 2, "Dice roll was below minimum: " + result);
        }
    }

    // Test 2 --> Boundary: dice roll result is never above the maximum possible value of 12
    @Test
    void diceRollResultIsNeverAboveMaximumValueOfTwelve()
    {
        for (int rollAttempt = 0; rollAttempt < NUMBER_OF_ROLLS_FOR_BOUNDARY_VERIFICATION; rollAttempt++)
        {
            int result = playerUsedForDiceRolling.diceRoll();
            assertTrue(result <= 12, "Dice roll exceeded maximum: " + result);
        }
    }
}

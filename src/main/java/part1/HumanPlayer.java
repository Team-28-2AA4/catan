package part1;

/**
 * HumanPlayer
 * A Player controlled by a human through console input.
 *
 * @author Team 28
 */
public class HumanPlayer extends Player
{
    public HumanPlayer(int playerId)
    {
        super(playerId);
    }

    @Override
    public TurnResult turn(Board board)
    {
        // TODO: read human input from the command line
        return TurnResult.pass("Human turn not yet implemented.");
    }

    @Override
    public int placeInitialSettlementAndRoad(Board board, int roundNumber)
    {
        // TODO: prompt human to choose a node and edge during setup
        return -1;
    }
}

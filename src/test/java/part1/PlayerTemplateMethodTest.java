package part1;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlayerTemplateMethodTest
{
    @Test
    void computerPlayerTurnReturnsPassFromTemplateWhenHoldingSevenOrFewerCards()
    {
        Board board = new Board();
        Player player = new ComputerPlayer(0);
        Game game = buildGame(board, player);

        Player.TurnResult result = player.turn(board, game);

        assertEquals(ActionType.PASS, result.actionType);
        assertEquals("Under 8 cards, so no move.", result.decisionSummary);
    }

    @Test
    void templateMethodBuildsTurnOptionsBeforeSubclassChoosesAction()
    {
        Board board = new Board();
        ProbePlayer player = new ProbePlayer(0);
        Game game = buildGame(board, player);

        board.placeSettlement(0, player);
        board.placeRoad(0, player.getPlayerId());
        player.addResource(ResourceType.LUMBER, 1);
        player.addResource(ResourceType.BRICK, 1);

        Player.TurnResult result = player.turn(board, game);

        assertEquals(ActionType.BUILD_ROAD, result.actionType);
        assertNotNull(player.lastTurnOptions);
        assertTrue(player.lastTurnOptions.canBuildRoad);
        assertFalse(player.lastTurnOptions.validRoadEdges.isEmpty());
    }

    private Game buildGame(Board board, Player player)
    {
        List<Player> players = new ArrayList<Player>();
        players.add(player);
        return new Game(board, players);
    }

    private static final class ProbePlayer extends Player
    {
        private TurnOptions lastTurnOptions;

        private ProbePlayer(int playerId)
        {
            super(playerId);
        }

        @Override
        protected TurnResult chooseTurnAction(Board board, Game game, TurnOptions turnOptions)
        {
            lastTurnOptions = turnOptions;
            int edgeIndex = turnOptions.validRoadEdges.get(0).intValue();
            return TurnResult.buildRoad(edgeIndex, "Probe road choice");
        }

        @Override
        public int placeInitialSettlementAndRoad(Board board, int roundNumber)
        {
            return 0;
        }
    }
}

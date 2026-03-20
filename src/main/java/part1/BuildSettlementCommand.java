package part1;

/**
 * BuildSettlementCommand
 * Represents the action of building one settlement and the logic required
 * to undo that settlement placement.
 *
 * @author Team 28
 */
public class BuildSettlementCommand implements GameCommand {

    private final Game game;
    private final Board board;
    private final Player player;
    private final int nodeId;

    public BuildSettlementCommand(Game game, Board board, Player player, int nodeId) {
        this.game = game;
        this.board = board;
        this.player = player;
        this.nodeId = nodeId;
    }

    @Override
    public void execute() {
        player.spendResource(ResourceType.LUMBER, 1);
        player.spendResource(ResourceType.BRICK, 1);
        player.spendResource(ResourceType.WOOL, 1);
        player.spendResource(ResourceType.GRAIN, 1);

        player.spendBuilding(BuildingKind.SETTLEMENT, 1);

        game.returnToBank(ResourceType.LUMBER, 1);
        game.returnToBank(ResourceType.BRICK, 1);
        game.returnToBank(ResourceType.WOOL, 1);
        game.returnToBank(ResourceType.GRAIN, 1);

        board.placeSettlement(nodeId, player);
    }

    @Override
    public void undo() {
        board.removeBuilding(nodeId);

        player.addResource(ResourceType.LUMBER, 1);
        player.addResource(ResourceType.BRICK, 1);
        player.addResource(ResourceType.WOOL, 1);
        player.addResource(ResourceType.GRAIN, 1);

        player.addBuilding(BuildingKind.SETTLEMENT, 1);
        player.addVictoryPoints(-1);
    }
}
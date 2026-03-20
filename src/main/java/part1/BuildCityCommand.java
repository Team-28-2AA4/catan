package part1;

/**
 * BuildCityCommand
 * Represents the action of upgrading one settlement into a city and the logic
 * required to undo that upgrade.
 *
 * @author Team 28
 */
public class BuildCityCommand implements GameCommand {

    private final Game game;
    private final Board board;
    private final Player player;
    private final int nodeId;

    public BuildCityCommand(Game game, Board board, Player player, int nodeId) {
        this.game = game;
        this.board = board;
        this.player = player;
        this.nodeId = nodeId;
    }

    @Override
    public void execute() {
        player.spendResource(ResourceType.GRAIN, 2);
        player.spendResource(ResourceType.ORE, 3);

        player.spendBuilding(BuildingKind.CITY, 1);

        game.returnToBank(ResourceType.GRAIN, 2);
        game.returnToBank(ResourceType.ORE, 3);

        board.placeCity(nodeId, player);
    }

    @Override
    public void undo() {
        board.downgradeCityToSettlement(nodeId, player);

        player.addResource(ResourceType.GRAIN, 2);
        player.addResource(ResourceType.ORE, 3);

        player.addBuilding(BuildingKind.CITY, 1);
        player.addVictoryPoints(-1);
    }
}
package part1;

/**
 * BuildRoadCommand
 * Represents the action of building one road and the logic required
 * to undo that road placement.
 *
 * On execute:
 *  the player spends the required resources
 *  the player spends one road piece
 *  the resources are returned to the bank
 *  the road is placed on the board
 *
 * On undo:
 *  the road is removed from the board
 *  the player gets the spent resources back
 *  the player gets the spent road piece back
 *
 * This class is a concrete command in the Command pattern.
 *
 * @author Team 28
 */
public class BuildRoadCommand implements GameCommand {

    /** Reference to the game, used for bank updates. */
    private final Game game;

    /** Reference to the board, used for road placement/removal. */
    private final Board board;

    /** The player performing the action. */
    private final Player player;

    /** The edge index where the road is built. */
    private final int edgeIndex;

    /**
     * Creates a command for building a road on a given edge.
     *
     * @param game current game
     * @param board current board
     * @param player player building the road
     * @param edgeIndex edge where the road should be placed
     */
    public BuildRoadCommand(Game game, Board board, Player player, int edgeIndex) {
        this.game = game;
        this.board = board;
        this.player = player;
        this.edgeIndex = edgeIndex;
    }

    /**
     * Executes the road-building action.
     */
    @Override
    public void execute() {
        player.spendResource(ResourceType.LUMBER, 1);
        player.spendResource(ResourceType.BRICK, 1);
        player.spendRoads(1);

        game.returnToBank(ResourceType.LUMBER, 1);
        game.returnToBank(ResourceType.BRICK, 1);

        board.placeRoad(edgeIndex, player.getPlayerId());
    }

    /**
     * Undoes the road-building action.
     */
    @Override
    public void undo() {
        board.removeRoad(edgeIndex);

        player.addResource(ResourceType.LUMBER, 1);
        player.addResource(ResourceType.BRICK, 1);
        player.addRoads(1);
    }
}
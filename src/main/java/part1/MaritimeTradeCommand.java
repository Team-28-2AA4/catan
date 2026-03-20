package part1;

/**
 * MaritimeTradeCommand
 * Represents the action of performing one maritime trade and the logic required
 * to undo that trade.
 *
 * On execute:
 *  the player spends 4 of one resource
 *  those resources are returned to the bank
 *  the player receives 1 of another resource from the bank
 *
 * On undo:
 *  the traded resource is returned to the bank
 *  the player gets back the 4 spent resources
 *
 * This class is a concrete command in the Command pattern.
 *
 * @author Team 28
 */
public class MaritimeTradeCommand implements GameCommand {

    /** Reference to the game, used for bank updates. */
    private final Game game;

    /** The player performing the trade. */
    private final Player player;

    /** The resource type the player gives to the bank. */
    private final ResourceType resourceToGive;

    /** The resource type the player gets from the bank. */
    private final ResourceType resourceToGet;

    /**
     * Creates a command for one maritime trade.
     *
     * @param game current game
     * @param player player performing the trade
     * @param resourceToGive resource given to the bank
     * @param resourceToGet resource received from the bank
     */
    public MaritimeTradeCommand(Game game, Player player, ResourceType resourceToGive, ResourceType resourceToGet) {
        this.game = game;
        this.player = player;
        this.resourceToGive = resourceToGive;
        this.resourceToGet = resourceToGet;
    }

    /**
     * Executes the maritime trade.
     */
    @Override
    public void execute() {
        player.spendResource(resourceToGive, 4);
        game.returnToBank(resourceToGive, 4);

        game.takeExactlyFromBank(player, resourceToGet, 1);
    }

    /**
     * Undoes the maritime trade.
     */
    @Override
    public void undo() {
        player.spendResource(resourceToGet, 1);
        game.returnToBank(resourceToGet, 1);

        game.takeExactlyFromBank(player, resourceToGive, 4);
    }
}
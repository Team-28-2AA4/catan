package ca.mcmaster.catan;

import java.util.Random;

public class ComputerPlayer extends Player {
    // TODO: UML shows a separate 'r' field here, but Player already has one.
    //       Keeping it per UML; consider removing duplication.
    public final Random r;

    public ComputerPlayer(int playerId) {
        super(playerId);
        this.r = new Random();
    }

    @Override
    public TurnResult turn(Board board) {
        // TODO: implement
        return null;
    }

    public int placeRandomValidSettlement(
        Board board,
        boolean isSecondSettlement
    ) {
        // TODO: implement
        return -1;
    }

    public int placeRandomValidRoad(Board board, int settlementNodeId) {
        // TODO: implement
        return -1;
    }
}

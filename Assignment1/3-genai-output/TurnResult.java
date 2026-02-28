package ca.mcmaster.catan;

public class TurnResult {
    public final ActionType actionType;
    public final int edgeIndex;
    public final int nodeId;
    public final String decisionSummary;

    private TurnResult(
        ActionType actionType,
        int edgeIndex,
        int nodeId,
        String decisionSummary
    ) {
        this.actionType = actionType;
        this.edgeIndex = edgeIndex;
        this.nodeId = nodeId;
        this.decisionSummary = decisionSummary;
    }

    public static TurnResult pass(String summary) {
        // TODO: implement
        return null;
    }

    public static TurnResult buildRoad(int edgeIndex, String summary) {
        // TODO: implement
        return null;
    }

    public static TurnResult buildSettlement(int nodeId, String summary) {
        // TODO: implement
        return null;
    }

    public static TurnResult buildCity(int nodeId, String summary) {
        // TODO: implement
        return null;
    }
}

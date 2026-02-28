package ca.mcmaster.catan;

public class Building {
    public final int ownerPlayerId;
    public final int nodeId;
    public final BuildingKind kind;

    public Building(int ownerPlayerId, int nodeId, BuildingKind kind) {
        this.ownerPlayerId = ownerPlayerId;
        this.nodeId = nodeId;
        this.kind = kind;
    }
}

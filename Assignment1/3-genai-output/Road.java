package ca.mcmaster.catan;

public class Road {
    public final int ownerId;
    public final int edgeIndex;

    public Road(int ownerPlayerId, int edgeIndex) {
        this.ownerId = ownerPlayerId;
        this.edgeIndex = edgeIndex;
    }
}

package ca.mcmaster.catan;

import java.util.List;

public class TerrainTile {
    public final int tileId;
    public final ResourceType resourceType;
    public final int diceToken;
    public final List<Integer> cornerNodeIdsClockwise;

    public TerrainTile(
        int tileId,
        ResourceType resourceType,
        int diceToken,
        List<Integer> cornerNodeIdsClockwise
    ) {
        this.tileId = tileId;
        this.resourceType = resourceType;
        this.diceToken = diceToken;
        this.cornerNodeIdsClockwise = cornerNodeIdsClockwise;
    }
}

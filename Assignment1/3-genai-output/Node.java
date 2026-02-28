package ca.mcmaster.catan;

import java.util.List;

public class Node {
    public final int nodeId;
    public final List<Integer> neighbourNodeIds;
    public final List<Integer> adjacentTileIds;

    public Node(int nodeId) {
        this.nodeId = nodeId;
        // TODO: initialize neighbourNodeIds and adjacentTileIds
        //       (populated later by Board init methods)
        this.neighbourNodeIds = new java.util.ArrayList<>();
        this.adjacentTileIds = new java.util.ArrayList<>();
    }
}

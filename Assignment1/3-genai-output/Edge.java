package ca.mcmaster.catan;

public class Edge {
    public final int node1;
    public final int node2;
    public final int edgeIndex;

    public Edge(int nodeA, int nodeB, int edgeIndex) {
        this.node1 = nodeA;
        this.node2 = nodeB;
        this.edgeIndex = edgeIndex;
    }
}

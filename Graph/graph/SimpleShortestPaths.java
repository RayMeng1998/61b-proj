package graph;

/* See restrictions in Graph.java. */

/** A partial implementation of ShortestPaths that contains the weights of
 *  the vertices and the predecessor edges.   The client needs to
 *  supply only the two-argument getWeight method.
 *  @author Ziyue Meng
 */
public abstract class SimpleShortestPaths extends ShortestPaths {

    /** The shortest paths in G from SOURCE. */
    public SimpleShortestPaths(Graph G, int source) {
        this(G, source, 0);
    }

    /** A shortest path in G from SOURCE to DEST. */
    public SimpleShortestPaths(Graph G, int source, int dest) {
        super(G, source, dest);
    }

    /** Returns the current weight of edge (U, V) in the graph.  If (U, V) is
     *  not in the graph, returns positive infinity. */
    @Override
    protected abstract double getWeight(int u, int v);

    @Override
    public double getWeight(int v) {
        double[] req = super.find(_form, v);
        return req[1];
    }

    @Override
    protected void setWeight(int v, double w) {
        double[] req = super.find(_form, v);
        req[1] = w;
    }

    @Override
    public int getPredecessor(int v) {
        double[] req = super.find(_form, v);
        return (int) req[2];
    }

    @Override
    protected void setPredecessor(int v, int u) {
        double[] req = super.find(_form, v);
        req[2] = u;
    }

}

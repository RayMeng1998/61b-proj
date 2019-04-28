package graph;

/* See restrictions in Graph.java. */

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.List;
import java.util.Collections;

/** The shortest paths through an edge-weighted graph.
 *  By overrriding methods getWeight, setWeight, getPredecessor, and
 *  setPredecessor, the client can determine how to represent the weighting
 *  and the search results.  By overriding estimatedDistance, clients
 *  can search for paths to specific destinations using A* search.
 *  @author Ziyue Meng
 */
public abstract class ShortestPaths {

    /** The shortest paths in G from SOURCE. */
    public ShortestPaths(Graph G, int source) {
        this(G, source, 0);
    }

    /** A shortest path in G from SOURCE to DEST. */
    public ShortestPaths(Graph G, int source, int dest) {
        _G = G;
        _source = source;
        _dest = dest;
        _comparator = new PQ();
        double inf = Double.POSITIVE_INFINITY;
        _form = new ArrayList<>();
        for (int i : _G.vertices()) {
            if (i == _source) {
                _form.add(new double[] {_source, 0, 0});
            } else {
                double[] v = {i, inf, 0};
                _form.add(v);
            }
        }
    }

    /** Initialize the shortest paths.  Must be called before using
     *  getWeight, getPredecessor, and pathTo. */
    public void setPaths() {
        PriorityQueue<Integer> fringe =
                new PriorityQueue<Integer>(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        double u = getWeight(o1) + estimatedDistance(o1);
                        double v = getWeight(o2) + estimatedDistance(o2);
                        if (u > v) {
                            return 1;
                        } else {
                            return -1;
                        }
                    }
                });
        ArrayList<Integer> marked = new ArrayList<Integer>();
        int current;
        for (int i : _G.vertices()) {
            fringe.add(i);
        }
        current = fringe.poll();
        while (current != _dest && !fringe.isEmpty()) {
            marked.add(current);
            for (int i : _G.successors(current)) {
                double weight = getWeight(current, i) + getWeight(current);
                if (weight < getWeight(i) && !marked.contains(i)) {
                    setWeight(i, weight);
                    setPredecessor(i, current);
                    if (fringe.contains(i)) {
                        fringe.remove(i);
                    }
                    fringe.add(i);
                }
            }
            current = fringe.poll();
        }
    }


    /** Returns the starting vertex. */
    public int getSource() {
        return _source;
    }

    /** Returns the target vertex, or 0 if there is none. */
    public int getDest() {
        return _dest;
    }

    /** Returns the current weight of vertex V in the graph.  If V is
     *  not in the graph, returns positive infinity. */
    public abstract double getWeight(int v);

    /** Set getWeight(V) to W. Assumes V is in the graph. */
    protected abstract void setWeight(int v, double w);

    /** Returns the current predecessor vertex of vertex V in the graph, or 0 if
     *  V is not in the graph or has no predecessor. */
    public abstract int getPredecessor(int v);

    /** Set getPredecessor(V) to U. */
    protected abstract void setPredecessor(int v, int u);

    /** Returns an estimated heuristic weight of the shortest path from vertex
     *  V to the destination vertex (if any).  This is assumed to be less
     *  than the actual weight, and is 0 by default. */
    protected double estimatedDistance(int v) {
        return 0.0;
    }

    /** Returns the current weight of edge (U, V) in the graph.  If (U, V) is
     *  not in the graph, returns positive infinity. */
    protected abstract double getWeight(int u, int v);

    /** Returns a list of vertices starting at _source and ending
     *  at V that represents a shortest path to V.  Invalid if there is a
     *  destination vertex other than V. */
    public List<Integer> pathTo(int v) {
        ArrayList<Integer> path = new ArrayList<>();
        int tmp = v;
        path.add((tmp));
        while (tmp != _source) {
            tmp = getPredecessor(tmp);
            path.add(tmp);
        }
        Collections.reverse(path);
        return path;
    }

    /** Returns a list of vertices starting at the source and ending at the
     *  destination vertex. Invalid if the destination is not specified. */
    public List<Integer> pathTo() {
        return pathTo(getDest());
    }
    /** find something in my _form.
     * @param from my form.
     * @param val my val.
     * @return a double[]. */
    protected double[] find(ArrayList<double[]> from, int val) {
        for (double[] i : from) {
            if ((int) i[0] == val) {
                return i;
            }
        }
        return null;
    }
    /** Comparator. */
    public class PQ implements Comparator<Integer> {

        @Override
        public int compare(Integer o1, Integer o2) {
            double u = getWeight(o1) + estimatedDistance(o1);
            double v = getWeight(o2) + estimatedDistance(o2);
            if (u > v) {
                return 1;
            } else {
                return -1;
            }
        }
    }
    /** The graph being searched. */
    protected final Graph _G;
    /** The starting vertex. */
    private final int _source;
    /** The target vertex. */
    private final int _dest;
    /** The form. */
    protected ArrayList<double[]> _form;
    /** The Comparator. */
    protected Comparator _comparator;

}

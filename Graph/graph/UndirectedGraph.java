package graph;

/* See restrictions in Graph.java. */

import java.util.ArrayList;

/** Represents an undirected graph.  Out edges and in edges are not
 *  distinguished.  Likewise for successors and predecessors.
 *
 *  @author Ziyue Meng
 */
public class UndirectedGraph extends GraphObj {

    @Override
    public boolean isDirected() {
        return false;
    }

    @Override
    public int inDegree(int v) {
        int count = 0;
        for (int[] i : getedges()) {
            if (i[0] == v || i[1] == v) {
                count++;
            }
        }
        return count;
    }

    @Override
    public Iteration<Integer> predecessors(int v) {
        ArrayList<Integer> things = new ArrayList<Integer>();
        for (int[] i : getedges()) {
            if (i[0] == v) {
                things.add(i[1]);
            } else if (i[1] == v) {
                things.add(i[0]);
            }

        }
        Iteration<Integer> pred = new Iteration<Integer>() {
            @Override
            public boolean hasNext() {
                return things.size() > 0;
            }

            @Override
            public Integer next() {
                return things.remove(0);
            }
        };
        return pred;
    }
    @Override
    public void remove(int u, int v) {
        for (int i = 0; i < getedges().size(); i++)  {
            if ((getedges().get(i)[0] == u && getedges().get(i)[1] == v)
                    || (getedges().get(i)[0] == v
                    && getedges().get(i)[1] == u)) {
                getedges().remove(getedges().get(i));
                i--;
            }
        }
    }
}

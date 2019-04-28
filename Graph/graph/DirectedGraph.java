package graph;

/* See restrictions in Graph.java. */

import java.util.ArrayList;

/** Represents a general unlabeled directed graph whose vertices are denoted by
 *  positive integers. Graphs may have self edges.
 *
 *  @author Ziyue Meng
 */
public class DirectedGraph extends GraphObj {

    @Override
    public boolean isDirected() {
        return true;
    }

    @Override
    public int inDegree(int v) {
        int count = 0;
        for (int[] i : getedges()) {
            if (i[1] == v) {
                count++;
            }
        }
        return count;
    }

    @Override
    public Iteration<Integer> predecessors(int v) {
        ArrayList<int[]> things = new ArrayList<int[]>();
        for (int[] i : getedges()) {
            if (i[1] == v) {
                things.add(i);
            }

        }
        Iteration<Integer> pred = new Iteration<Integer>() {
            @Override
            public boolean hasNext() {
                return things.size() > 0;
            }

            @Override
            public Integer next() {
                return things.remove(0)[0];
            }
        };
        return pred;
    }

}

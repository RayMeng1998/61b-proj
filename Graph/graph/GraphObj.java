package graph;

/* See restrictions in Graph.java. */

import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;

/** A partial implementation of Graph containing elements common to
 *  directed and undirected graphs.
 *
 *  @author Ziyue Meng
 */
abstract class GraphObj extends Graph {

    /** A new, empty Graph. */
    GraphObj() {
        _vertices = new ArrayList<>();
        _edges = new ArrayList<>();
        _removed = new PriorityQueue<>();

    }

    @Override
    public int vertexSize() {
        return _vertices.size();
    }

    @Override
    public int maxVertex() {
        if (_vertices.size() == 0) {
            return 0;
        }
        return _vertices.get(_vertices.size() - 1);
    }

    @Override
    public int edgeSize() {
        return _edges.size();
    }

    @Override
    public abstract boolean isDirected();

    @Override
    public int outDegree(int v) {
        int count = 0;
        for (int[] i : _edges) {
            if (isDirected()) {
                if (i[0] == v) {
                    count++;
                }
            } else {
                if (i[0] == v || i[1] == v) {
                    count++;
                }
            }
        }
        return count;
    }

    @Override
    public abstract int inDegree(int v);

    @Override
    public boolean contains(int u) {
        return _vertices.contains(u);
    }

    @Override
    public boolean contains(int u, int v) {
        boolean edgethere = false;
        for (int[] i : _edges) {
            if (isDirected()) {
                if (i[0] == u && i[1] == v) {
                    edgethere = true;
                }
            } else {
                if ((i[0] == u && i[1] == v) || (i[0] == v && i[1] == u)) {
                    edgethere = true;
                }
            }
        }
        return contains(u) && contains(v) && edgethere;
    }

    @Override
    public int add() {
        if (_vertices.isEmpty()) {
            _vertices.add(1);
            return 1;
        } else {
            int adder;
            if (_removed.size() == 0) {
                adder = _vertices.size() + 1;
            } else {
                adder = _removed.poll();
            }
            _vertices.add(adder);
            Collections.sort(_vertices);
            return adder;
        }
    }

    @Override
    public int add(int u, int v) {
        if (contains(u) && contains(v)) {
            for (int[] i : _edges) {
                if (!isDirected()) {
                    if ((i[0] == u && i[1] == v) || (i[0] == v && i[1] == u)) {
                        return _edges.indexOf(i) + 1;
                    }
                } else {
                    if ((i[0] == u && i[1] == v)) {
                        return _edges.indexOf(i) + 1;
                    }
                }
            }
            if (_edges.size() == 0) {
                _edges.add(new int[]{u, v});
                return 1;
            }
            _edges.add(new int[]{u, v});
            int adder = _edges.size();
            return adder;
        }
        return 0;
    }
    @Override
    public void remove(int v) {
        int index = _vertices.indexOf(v);
        if (index != -1) {
            _removed.add(_vertices.remove(index));
            for (int i = 0; i < _edges.size(); i++) {
                if (_edges.get(i)[0] == v || _edges.get(i)[1] == v) {
                    _edges.remove(_edges.get(i));
                    i--;
                }
            }
        }
    }

    @Override
    public void remove(int u, int v) {
        for (int i = 0; i < _edges.size(); i++)  {
            if ((_edges.get(i)[0] == u && _edges.get(i)[1] == v)
                    || (_edges.get(i)[0] == v && _edges.get(i)[1] == u)) {
                _edges.remove(_edges.get(i));
                i--;
            }
        }
    }

    @Override
    public Iteration<Integer> vertices() {
        ArrayList<Integer> things = new ArrayList<Integer>(_vertices);
        Iteration<Integer> verti = new Iteration<Integer>() {
            @Override
            public boolean hasNext() {
                return things.size() > 0;
            }

            @Override
            public Integer next() {
                return things.remove(0);
            }
        };
        return verti;
    }

    @Override
    public Iteration<Integer> successors(int v) {
        ArrayList<int[]> things = new ArrayList<int[]>();
        for (int[] i : _edges) {
            if (isDirected()) {
                if (i[0] == v) {
                    things.add(i);
                }
            } else {
                if (i[0] == v || i[1] == v) {
                    things.add(i);
                }
            }
        }
        Iteration<Integer> suc = new Iteration<Integer>() {
            @Override
            public boolean hasNext() {
                return things.size() > 0;
            }

            @Override
            public Integer next() {
                int[] tmp = things.remove(0);
                if (tmp[0] == v) {
                    return tmp[1];
                } else {
                    return tmp[0];
                }
            }
        };
        return suc;
    }

    @Override
    public abstract Iteration<Integer> predecessors(int v);

    @Override
    public Iteration<int[]> edges() {
        ArrayList<int[]> things = new ArrayList<int[]>(_edges);
        Iteration<int[]> edg = new Iteration<int[]>() {
            @Override
            public boolean hasNext() {
                return things.size() > 0;
            }

            @Override
            public int[] next() {
                return things.remove(0);
            }
        };
        return edg;
    }

    @Override
    protected void checkMyVertex(int v) {
        if (!contains(v)) {
            throw new IllegalArgumentException("vertex not from Graph");
        }
    }

    @Override
    protected int edgeId(int u, int v) {
        int id = 0;
        for (int[] i : _edges) {
            if (isDirected()) {
                if ((i[0] == u && i[1] == v)) {
                    id = _edges.indexOf(i) + 1;
                }
            } else {
                if ((i[0] == u && i[1] == v) || ((i[0] == v && i[1] == u))) {
                    id = _edges.indexOf(i) + 1;
                }
            }
        }
        return id;
    }
    /** vertices. */
    private ArrayList<Integer> _vertices;
    /** get vertices.
     * @return vertices. */
    public ArrayList<Integer> getvertices() {
        return _vertices;
    }
    /** edges. */
    private ArrayList<int[]> _edges;
    /** get edges.
     * @return edges. */
    public ArrayList<int[]> getedges() {
        return _edges;
    }
    /** Removed. */
    private PriorityQueue<Integer> _removed = new PriorityQueue<>();
}

package graph;

import org.junit.Test;


import static org.junit.Assert.*;

/** Unit tests for the Graph class.
 *  @author Ziyue Meng
 */
public class GraphTest {

    @Test
    public void emptyGraph() {
        DirectedGraph g = new DirectedGraph();
        assertEquals("Initial graph has vertices", 0, g.vertexSize());
        assertEquals("Initial graph has edges", 0, g.edgeSize());
    }
    @Test
    public void d1test() {
        DirectedGraph d1 = new DirectedGraph();
        for (int i = 0; i < 6; i++) {
            d1.add();
        }
        assertEquals(1,  d1.add(1, 1));
        assertEquals(2,  d1.add(1, 2));
        assertEquals(3,  d1.add(1, 3));
        assertEquals(4,  d1.add(1, 4));
        assertEquals(5,  d1.add(2, 1));
        assertEquals(6,  d1.add(2, 6));
        assertEquals(7,  d1.add(4, 6));
        assertEquals(8,  d1.add(5, 3));
        assertEquals("Initial graph has vertices", 6, d1.vertexSize());
        assertEquals("Initial graph has edges", 8, d1.edgeSize());
        assertEquals(2,  d1.add(1, 2));
        assertEquals(0,  d1.add(7, 5));
        Iteration v1 = d1.vertices();
        for (int i = 0; i < 6; i++) {
            v1.next();
        }
        assertFalse(v1.hasNext());
        Iteration edg1 = d1.edges();
        for (int i = 0; i < 8; i++) {
            edg1.next();
        }
        assertFalse(edg1.hasNext());
        Iteration suc11 = d1.successors(1);
        for (int i = 0; i < 4; i++) {
            suc11.next();
        }
        assertFalse(suc11.hasNext());
        Iteration pred11 = d1.predecessors(1);
        for (int i = 0; i < 2; i++) {
            pred11.next();
        }
        assertFalse(pred11.hasNext());
        assertEquals(6, d1.edgeId(2, 6));
        assertEquals(0, d1.edgeId(1, 5));
        assertEquals(0, d1.edgeId(1, 7));
        assertEquals(4, d1.outDegree(1));
        assertEquals(2, d1.inDegree(1));
        assertEquals(1, d1.outDegree(5));
        assertEquals(0, d1.inDegree(5));
        d1.remove(1);
        d1.remove(1);
        assertEquals("Initial graph has vertices", 5, d1.vertexSize());
        assertEquals("Initial graph has edges", 3, d1.edgeSize());
        d1.remove(3);
        assertEquals(1, d1.add());
        assertEquals(3, d1.add());
        assertEquals(6, d1.maxVertex());
        d1.remove(6);
        assertEquals(5, d1.maxVertex());
    }

    @Test
    public void u1test() {
        UndirectedGraph u1 = new UndirectedGraph();
        for (int i = 0; i < 4; i++) {
            u1.add();
        }
        assertEquals(1,  u1.add(1, 1));
        assertEquals(2,  u1.add(1, 2));
        assertEquals(3,  u1.add(1, 3));
        assertEquals(4,  u1.add(1, 4));
        assertEquals(2,  u1.add(2, 1));
        assertEquals("Initial graph has vertices", 4, u1.vertexSize());
        assertEquals("Initial graph has edges", 4, u1.edgeSize());
        assertEquals(2,  u1.add(1, 2));
        assertEquals(0,  u1.add(1, 5));
        Iteration v1 = u1.vertices();
        for (int i = 0; i < 4; i++) {
            v1.next();
        }
        assertFalse(v1.hasNext());
        Iteration edg1 = u1.edges();
        for (int i = 0; i < 4; i++) {
            edg1.next();
        }
        assertFalse(edg1.hasNext());
        Iteration suc11 = u1.successors(1);
        for (int i = 0; i < 4; i++) {
            suc11.next();
        }
        assertFalse(suc11.hasNext());
        Iteration pred11 = u1.predecessors(1);
        for (int i = 0; i < 4; i++) {
            pred11.next();
        }
        assertFalse(pred11.hasNext());
        assertEquals(0, u1.edgeId(1, 5));
        assertEquals(0, u1.edgeId(1, 7));
        assertEquals(4, u1.outDegree(1));
        assertEquals(4, u1.inDegree(1));
        assertEquals(4, u1.maxVertex());
        u1.remove(4);
        assertEquals(3, u1.maxVertex());
    }
}

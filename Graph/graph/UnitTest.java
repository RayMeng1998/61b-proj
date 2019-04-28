package graph;

import org.junit.Test;
import ucb.junit.textui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/* You MAY add public @Test methods to this class.  You may also add
 * additional public classes containing "Testing" in their name. These
 * may not be part of your graph package per se (that is, it must be
 * possible to remove them and still have your package work). */

/** Unit tests for the graph package.  This class serves to dispatch
 *  other test classes, which are listed in the argument to runClasses.
 *  @author Ziyue Meng
 */
public class UnitTest {

    /** Run all JUnit tests in the graph package. */
    public static void main(String[] ignored) {
        System.exit(textui.runClasses(graph.GraphTest.class));
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
        assertEquals(4,  d1.add(2, 4));
        assertEquals(5,  d1.add(3, 5));
        assertEquals(6,  d1.add(4, 6));
        DepthFirstTraversal dd1 = new DepthFirstTraversal(d1);
        BreadthFirstTraversal b1 = new BreadthFirstTraversal(d1);
        dd1.traverse(1);
        b1.traverse(1);
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

    @Test
    public void testBFT() {
        DirectedGraph g = new DirectedGraph();
        for (int i = 0; i < 7; i++) {
            g.add();
        }
        g.add(1, 2);
        g.add(2, 4);
        g.add(4, 6);
        g.add(1, 3);
        g.add(3, 5);
        g.add(5, 7);
        BreadthFirstTraversal bft = new BreadthFirstTraversal(g);
        bft.traverse(2);
        assertTrue(bft.marked(4));
        assertFalse(bft.marked(7));
        assertTrue(bft.marked(6));
    }

    @Test
    public void testBFT2() {
        DirectedGraph g = new DirectedGraph();
        for (int i = 0; i < 7; i++) {
            g.add();
        }
        g.add(1, 3);
        g.add(1, 2);
        g.add(4, 6);
        g.add(3, 5);
        g.add(2, 4);
        g.add(5, 7);
        BreadthFirstTraversal bft = new BreadthFirstTraversal(g);
        bft.traverse(1);
        assertTrue(bft.marked(1));
        assertTrue(bft.marked(2));
        assertTrue(bft.marked(3));
        assertTrue(bft.marked(4));
        assertTrue(bft.marked(5));
        assertTrue(bft.marked(6));
        assertTrue(bft.marked(7));
    }


    @Test
    public void testDFT() {
        DirectedGraph g = new DirectedGraph();
        for (int i = 0; i < 7; i++) {
            g.add();
        }
        g.add(1, 3);
        g.add(1, 2);
        g.add(4, 6);
        g.add(3, 5);
        g.add(2, 4);
        g.add(5, 7);
        DepthFirstTraversal dft = new DepthFirstTraversal(g);
        dft.traverse(2);
        assertFalse(dft.marked(1));
        assertTrue(dft.marked(2));
        assertFalse(dft.marked(3));
        assertTrue(dft.marked(4));
        assertFalse(dft.marked(5));
        assertTrue(dft.marked(6));
        assertFalse(dft.marked(7));
    }

    @Test
    public void testDFT2() {
        System.out.println("test");
        UndirectedGraph g = new UndirectedGraph();
        for (int i = 0; i < 5; i++) {
            g.add();
        }
        g.add(1, 2);
        g.add(1, 3);
        g.add(2, 4);
        g.add(2, 5);
        g.add(4, 5);
        g.add(3, 5);
        g.add(2, 3);
        DepthFirstTraversal dft = new DepthFirstTraversal(g);
        dft.traverse(1);
        assertTrue(dft.marked(1));
        assertTrue(dft.marked(2));
        assertTrue(dft.marked(3));
        assertTrue(dft.marked(4));
        assertTrue(dft.marked(5));
        System.out.println("end");
    }

    @Test
    public void testBFT3() {
        UndirectedGraph g = new UndirectedGraph();
        for (int i = 0; i < 5; i++) {
            g.add();
        }
        g.add(1, 2);
        g.add(1, 3);
        g.add(2, 4);
        g.add(2, 5);
        g.add(4, 5);
        g.add(3, 5);
        g.add(2, 3);
        BreadthFirstTraversal bft = new BreadthFirstTraversal(g);
        bft.traverse(1);
        assertTrue(bft.marked(1));
        assertTrue(bft.marked(2));
        assertTrue(bft.marked(3));
        assertTrue(bft.marked(4));
        assertTrue(bft.marked(5));
    }
}

package ex1;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WGraph_AlgoTest {

    private weighted_graph g;
    private weighted_graph_algorithms ga;

    @BeforeAll
    public static void BeforeAll() {
        System.out.println("start");
    }

    //reset graph to default
    @BeforeEach
    public void BeforeEach() {
        g = graph_creator();
        ga = new WGraph_Algo();
        ga.init(g);

    }

    @Test
    void initBase() {
//        weighted_graph_algorithms ga2 = new WGraph_Algo();
//        weighted_graph_algorithms ga3 = new WGraph_Algo();
        ga.init(g);
//        ga3.init(g);
//        assertTrue(ga2.equals(ga3),"init doesn't make a deep copy");


    }

    @Test
    void getGraphBase() {
        weighted_graph g1 = ga.getGraph();
        assertTrue(g.equals(g1), "getGraph doesn't return the same graph");
    }

    @Test
    void copyBase() {
        weighted_graph g1 = ga.copy();
        assertEquals(g.edgeSize(), g1.edgeSize(), "copy doesn't make a deep copy at edgeSize");
        assertEquals(g.nodeSize(), g1.nodeSize(), "copy doesn't make a deep copy at nodeSize");
        assertEquals(g, g1, "copy doesn't make a deep copy");
    }

    @Test
    void copyCheckMC() {
        g.addNode(17);
        g.removeNode(17);
        weighted_graph g1 = ga.copy();
        assertEquals(18, g1.getMC(), "copy doesn't reset the MC");
    }

    @Test
    void copyCheckAfterRemove() {
        weighted_graph g1 = ga.copy();
        assertEquals(g, g1, "copy doesn't make a deep copy");
        g1.addNode(5);
        assertFalse(g.equals(g1), "the copied graph stay the same after add node");
        g1.removeNode(5);
        assertTrue(g.equals(g1), "the copied graph stay the same after remove node");
    }

    @Test
    void copyOneNode() {
        weighted_graph g2 = new WGraph_DS();
        g2.addNode(2);
        ga.init(g2);
        weighted_graph g1 = ga.copy();
        assertEquals(g2, g1, "copy doesn't make a deep copy");
    }

    @Test
    void copyNull() {
        ga.init(null);
        weighted_graph g1 = ga.copy();
        assertTrue(g1.nodeSize() == 0 & g1.nodeSize() == 0 & g1.getMC() == 0, "copy doesn't reset the parameters for null graph");
        assertNull(g1.getV(), "copy make copy for null graph");
    }

    @Test
    void isConnectedBase() {
        assertTrue(ga.isConnected(), "isConnected return false when connected");
    }

    @Test
    void isConnectedNull() {
        ga.init(null);
        assertTrue(ga.isConnected(), "isConnected return false to null graph");
    }

    @Test
    void isConnectedLonelyNode() {
        g.removeNode(1996);
        weighted_graph g1 = new WGraph_DS();
        g1.addNode(1);
        ga.init(g1);
        assertTrue(ga.isConnected(), "isConnected return false to graph with one node");
    }

    @Test
    void isConnectedNotConnected() {
        g.addNode(1);
        assertFalse(ga.isConnected(), "isConnected return true when unconnected");
    }

    @Test
    void shortestPathDistBase() {
        g.connect(150799, 207080243, 5);
        ga.init(g);
        assertEquals(14, ga.shortestPathDist(555, 522670067), "shortestPathDist return uncorrected dist");
        assertEquals(10.1, ga.shortestPathDist(-1, 1996), "shortestPathDist return uncorrected dist");
        assertEquals(9.6, ga.shortestPathDist(150799, 0), "shortestPathDist return uncorrected dist");
    }

    @Test
    void shortestPathDistNodeToHimself() {
        assertEquals(0, ga.shortestPathDist(0, 0), "shortestPathDist return uncorrected dist from node to himself");
    }

    @Test
    void shortestPathDistNull() {
        ga.init(null);
        assertEquals(-1, ga.shortestPathDist(0, -1), "shortestPathDist return uncorrected dist at null graph");
    }

    @Test
    void shortestPathDistNodeNotExist() {
        assertEquals(-1, ga.shortestPathDist(0, -11), "shortestPathDist return uncorrected dist at one node not exist");
        assertEquals(-1, ga.shortestPathDist(50, -11), "shortestPathDist return uncorrected dist at two node not exist");
    }

    @Test
    void shortestPathDistNoDist() {
        g.addNode(16);
        assertEquals(-1, ga.shortestPathDist(0, 16), "shortestPathDist return uncorrected dist");
    }

    @Test
    void shortestPathBase() {
        g.connect(150799, 207080243, 5);
        ga.init(g);
        LinkedList<node_info> finalList = new LinkedList<>();
        finalList.addFirst(g.getNode(522670067));
        finalList.addFirst(g.getNode(207080243));
        finalList.addFirst(g.getNode(150799));
        finalList.addFirst(g.getNode(555));
        assertEquals(finalList, ga.shortestPath(555, 522670067), "shortestPath return uncorrected list");
        finalList = new LinkedList<>();
        finalList.addFirst(g.getNode(-1));
        finalList.addFirst(g.getNode(-101));
        finalList.addFirst(g.getNode(555));
        finalList.addFirst(g.getNode(150799));
        finalList.addFirst(g.getNode(1996));
        assertEquals(finalList, ga.shortestPath(1996, -1), "shortestPath return uncorrected list");
        finalList = new LinkedList<>();
        finalList.addFirst(g.getNode(0));
        finalList.addFirst(g.getNode(1996));
        finalList.addFirst(g.getNode(150799));
        assertEquals(finalList, ga.shortestPath(150799, 0), "shortestPath return uncorrected list");

    }

    @Test
    void shortestPathNodeToHimself() {
        LinkedList<node_info> finalList = new LinkedList<>();
        finalList.addFirst(g.getNode(0));
        assertEquals(finalList, ga.shortestPath(0, 0), "shortestPath return uncorrected list at node to himself");
    }

    @Test
    void shortestPathNullGraph() {
        ga.init(null);
        LinkedList<node_info> finalList = new LinkedList<>();
        assertNull(ga.shortestPath(0, 4), "shortestPath return uncorrected list at null graph");
    }

    @Test
    void saveBase() {
        try{
            ga.save("file");
            ga.save("file");
            assertTrue(ga.save("file"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void loadBase() {
        ga.save("file");
        weighted_graph_algorithms ga2 = new WGraph_Algo();
        ga2.load("file");
        weighted_graph g1 = ga2.getGraph();
        assertEquals(g,g1,"load return different graph");
        }

    @Test
    void loadAfterChange() {
        ga.save("file");
        weighted_graph_algorithms t1 = new WGraph_Algo();
        t1.load("file");
        weighted_graph g9 = t1.getGraph();
        g9.removeNode(-1);
        assertNotEquals(g,g9,"load return different graph");
    }


    @AfterAll
    public static void AfterAll() {
        System.out.println("finish succeed");
    }

    public weighted_graph graph_creator() {
        weighted_graph g = new WGraph_DS();

        int a, b, c, d, e, f, h, k, l;
        a = -101;
        b = -1;
        c = 0;
        d = 3;
        e = 522670067;
        f = 207080243;
        h = 150799;
        k = 1996;
        l = 555;


        g.addNode(a);
        g.addNode(b);
        g.addNode(c);
        g.addNode(d);
        g.addNode(e);
        g.addNode(f);
        g.addNode(h);
        g.addNode(k);
        g.addNode(l);

        g.connect(a, b, 6.0);
        g.connect(d, c, 3);
        g.connect(e, f, 8.9);
        g.connect(h, k, 2);
        g.connect(l, a, 2);
        g.connect(l, f, 11.1);
        g.connect(k, c, 7.6);
        g.connect(e, b, 12);
        g.connect(h, l, 0.1);


        return g;
    }
}
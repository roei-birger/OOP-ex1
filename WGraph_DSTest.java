package ex1;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

class WGraph_DSTest {
    private weighted_graph g;
    static long start = new Date().getTime();

    @BeforeAll
    public static void BeforeAll() {
        System.out.println("Start");
    }

    //reset graph to default
    @BeforeEach
    public void BeforeEach() {
        g = graph_creator();

    }

    @Test
    void copyConstructorBase() {
        weighted_graph g1 = new WGraph_DS(g);
        assertEquals(g, g1, "copyConstructor didn't make a deep copy");
    }

    @Test
    void copyConstructorError() {
        weighted_graph g1 = new WGraph_DS(g);
        g1.removeNode(3);
        assertNotEquals(g, g1, "copyConstructor didn't make a deep copy");
    }

    @Test
    void getNodeBase() {
        assertEquals(0, g.getNode(0).getKey(), "getNode didn't return node from the default constructor");
    }

    @Test
    void getNodeNegativeKey() {
        assertEquals(-1, g.getNode(-1).getKey(), "getNode didn't return node from the default constructor");
    }


    @Test
    void getNodeNull() {
        assertNull(g.getNode(9), "getNode didn't return null if the node didn't in the graph");
    }

    @Test
    void hasEdgeBasicTrue() {
        assertTrue(g.hasEdge(207080243, 555), "hasEdge didn't true about connect nodes");
    }

    @Test
    void hasEdgeBasicFalse() {
        assertFalse(g.hasEdge(3, 555), "hasEdge didn't false about unconnected nodes");
    }

    @Test
    void hasEdgeOneNoteExist() {
        assertFalse(g.hasEdge(9, 555), "hasEdge didn't false about one not exist node");
    }

    @Test
    void hasEdgeTwoNoteExist() {
        assertFalse((g.hasEdge(9, -200)), "hasEdge didn't false about two not exist nodes");
    }

    @Test
    void hasEdgeSameNode() {
        assertFalse(g.hasEdge(3, 3), "hasEdge didn't true about node with himself");
    }

    @Test
    void getEdgeBase() {
        assertEquals(3, g.getEdge(0, 3), "getEdge didn't return the correct weight");
    }

    @Test
    void getEdgeNotHave() {
        assertEquals(-1, g.getEdge(0, -101), "getEdge didn't return -1 if didn't have edge");
    }

    @Test
    void getEdgeOneNoteExist() {
        assertEquals(-1, g.getEdge(0, 9), "getEdge didn't return -1 about one not exist node");
    }

    @Test
    void getEdgeTwoNoteExist() {
        assertEquals(-1, g.getEdge(5, 9), "getEdge didn't return -1 about two not exist node");
    }

    @Test
    void addNodePos() {
        int temp1 = g.nodeSize();
        g.addNode(60);
        int temp2 = g.nodeSize();
        assertEquals(60, g.getNode(60).getKey(), "addNode didn't added a positive node");
        assertEquals(temp1 + 1, temp2, "addNode didn't added a positive node");
    }

    @Test
    void addNodeNeg() {
        int temp1 = g.nodeSize();
        g.addNode(-60);
        int temp2 = g.nodeSize();
        assertEquals(-60, g.getNode(-60).getKey(), "addNode didn't added a negative node");
        assertEquals(temp1 + 1, temp2, "addNode didn't update nodeSize");
    }


    @Test
    void connectBase() {
        g.connect(-1, 0, 0.7);
        assertTrue(g.hasEdge(-1, 0), "connect didn't insert edge between the nodes");
    }

    @Test
    void connectNegativeWeight() {
        g.connect(-1, 0, -0.7);
        assertFalse(g.hasEdge(-1, 0), "connect insert edge with negative weight");
    }

    @Test
    void connectUpdateNegativeWeight() {
        g.connect(3, 0, -0.7);
        assertEquals(3, g.getEdge(3, 0), "connect update a negative weight between exist nodes");
    }

    @Test
    void connectRightNum() {
        g.connect(-1, 0, 0.7);
        assertTrue(g.hasEdge(-1, 0), "connect didn't insert edge between the nodes");
        assertEquals(0.7, g.getEdge(-1, 0), "connect didn't insert the right edge weight between the nodes");
    }

    @Test
    void connectNotExistNode() {
        g.connect(3, 4, 0.5);
        assertFalse(g.hasEdge(3, 4), "connect insert edge between not exist node to exist node");
    }

    @Test
    void connectNotExistNodes() {
        g.connect(6, 4, 0.7);
        assertFalse(g.hasEdge(6, 4), "connect insert edge between not exist nodes");
    }

    @Test
    void connectAddEdgeSize() {
        int temp1 = g.edgeSize();
        g.connect(-1, 0, 0.7);
        int temp2 = g.edgeSize();
        assertEquals(temp1 + 1, temp2, "connect  didn't update edgeSize");
    }

    @Test
    void connectNotAddEdgeSize() {
        int temp1 = g.edgeSize();
        g.connect(9, 6, 0.7);
        g.connect(3, 0, 0.7);
        g.connect(3, 0, -0.7);
        int temp2 = g.edgeSize();
        assertEquals(temp1, temp2, "connect update edgeSize when he shouldn't need");
    }

    @Test
    void getVBase() {
        Collection<node_info> temp = new ArrayList<>();
        temp.add(g.getNode(-1));
        temp.add(g.getNode(0));
        temp.add(g.getNode(3));
        temp.add(g.getNode(-101));
        temp.add(g.getNode(522670067));
        temp.add(g.getNode(207080243));
        temp.add(g.getNode(555));
        temp.add(g.getNode(1996));
        temp.add(g.getNode(150799));

        assertEquals(temp.toString(), g.getV().toString(), "getV didn't return correct collection");
    }

    @Test
    void getVSize() {
        Collection<node_info> temp = new ArrayList<>();
        temp.add(g.getNode(-1));
        temp.add(g.getNode(0));
        temp.add(g.getNode(3));
        temp.add(g.getNode(-101));
        temp.add(g.getNode(522670067));
        temp.add(g.getNode(207080243));
        temp.add(g.getNode(555));
        temp.add(g.getNode(1996));
        temp.add(g.getNode(150799));
        assertEquals(temp.size(), g.getV().size(), "getV didn't return correct size of collection");
    }

    @Test
    void testGetV2() {
        Collection<node_info> temp = new ArrayList<>();
        temp.add(g.getNode(3));
        temp.add(g.getNode(1996));
        assertEquals(temp.toString(), g.getV(0).toString(), "getV didn't return correct collection");
    }

    @Test
    void removeNodeBase() {
        assertEquals(555, g.removeNode(555).getKey(), "removeNode didn't return correct node");
    }

    @Test
    void removeNodeChekSizes() {
        int nodeSize = g.nodeSize();
        int edgeSize = g.edgeSize();
        assertEquals(555, g.removeNode(555).getKey(), "removeNode didn't return correct node");
        assertEquals(nodeSize - 1, g.nodeSize(), "removeNode didn't update correct nodeSize");
        assertEquals(edgeSize - 3, g.edgeSize(), "removeNode didn't update correct edgeSize");
    }

    @Test
    void removeNodeNotExist() {
        assertNull(g.removeNode(9), "removeNode didn't return null when remove not exist node");
    }

    @Test
    void removeNodeNotExistChekSizes() {
        int nodeSize = g.nodeSize();
        int edgeSize = g.edgeSize();
        assertNull(g.removeNode(9), "removeNode didn't return null when remove not exist node");
        assertEquals(nodeSize, g.nodeSize(), "removeNode didn't update correct nodeSize");
        assertEquals(edgeSize, g.edgeSize(), "removeNode didn't update correct edgeSize");
    }

    @Test
    void removeNodeChekNeighbors() {
        assertEquals(555, g.removeNode(555).getKey(), "removeNode didn't return correct node");
        assertFalse(g.getV(g.getNode(-101).getKey()).contains(g.getNode(555)), "removeNode didn't remove the node from the nei's nei lise");
        assertFalse(g.getV(g.getNode(150799).getKey()).contains(g.getNode(555)), "removeNode didn't remove the node from the nei's nei lise");
        assertFalse(g.getV(g.getNode(207080243).getKey()).contains(g.getNode(555)), "removeNode didn't remove the node from the nei's nei lise");
    }

    @Test
    void removeEdgeBase() {
        int edgeSize = g.edgeSize();
        g.removeEdge(-1, -101);
        assertFalse(g.hasEdge(-1, -101), "removeEdge didn't remove the edge");
        assertEquals(edgeSize - 1, g.edgeSize(), "removeNode didn't update edgeSize");
    }

    @Test
    void removeEdgeNotExistEdge() {
        int edgeSize = g.edgeSize();
        g.removeEdge(-1, 0);
        assertEquals(edgeSize, g.edgeSize(), "removeEdge update edgeSize when remove not exist edge");
    }

    @Test
    void removeEdgeNotExistNode() {
        int edgeSize = g.edgeSize();
        g.removeEdge(6, 4);
        assertEquals(edgeSize, g.edgeSize(), "removeEdge update edgeSize when remove not exist nodes");
    }

    @Test
    void removeEdgeWithHimself() {
        int edgeSize = g.edgeSize();
        g.removeEdge(3, 3);
        assertEquals(edgeSize, g.edgeSize(), "removeEdge update edgeSize when remove edge between node to himself");
    }

    @Test
    void nodeSizeBase() {
        assertEquals(9, g.nodeSize(), "nodeSize didn't return correct size");
    }

    @Test
    void nodeSizeAfterRemoveNode() {
        g.removeNode(0);
        g.removeNode(555);
        assertEquals(7, g.nodeSize(), "nodeSize didn't update nodeSize after remove");
    }

    @Test
    void nodeSizeAfterAddNode() {
        g.addNode(10);
        g.addNode(16);
        assertEquals(11, g.nodeSize(), "nodeSize didn't update nodeSize after add");
    }

    @Test
    void edgeSizeBase() {
        assertEquals(9, g.edgeSize(), "edgeSize didn't return correct size");
    }

    @Test
    void nodeSizeAfterRemoveEdge() {
        g.removeEdge(-1, -101);
        g.removeEdge(6, -101);
        g.removeEdge(3, 0);
        assertEquals(7, g.edgeSize(), "edgeSize didn't update edgeSize after remove");
    }

    @Test
    void nodeSizeAfterAddEdge() {
        g.connect(0, -101, 9);
        g.connect(3, -1, 0);
        assertEquals(11, g.edgeSize(), "edgeSize didn't update edgeSize after add");
    }

    @Test
    void getMCBase() {
        assertEquals(18, g.getMC(), "getMC didn't return correct size");
    }

    @Test
    void getMCAfterRemoveNode() {
        g.removeNode(0);
        g.removeNode(555);
        assertEquals(25, g.getMC(), "getMC didn't update MC after remove node");
    }


    @Test
    void getMCAfterAddNode() {
        g.addNode(10);
        g.addNode(16);
        assertEquals(20, g.getMC(), "getMC didn't update MC after add node");
    }

    @Test
    void getMCAfterAddEdge() {
        g.connect(0, -101, 9);
        g.connect(3, -1, 0);
        assertEquals(20, g.getMC(), "getMC didn't update MC after add Edge");
    }

    @Test
    void getMCAfterRemoveEdge() {
        g.removeEdge(-1, -101);
        g.removeEdge(6, -101);
        g.removeEdge(3, 0);
        assertEquals(20, g.getMC(), "getMC didn't update MC after remove Edge");
    }

    @AfterAll
    public static void AfterAll() {
        System.out.println("Finish succeed");
        long end = new Date().getTime();
        double dt = (end - start) / 1000.0;
        System.out.println("All program runTime: " + dt);
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
package ex1;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class WGraph_DS implements weighted_graph, java.io.Serializable {

    private HashMap<Integer, node_info> vertices;
    private int mc;
    private int edSize;
    private int noSize;

    /**
     * Constructs a graph with not vertices.
     */
    public WGraph_DS() {
        this.mc = 0;
        this.vertices = new HashMap<>();
        this.edSize = 0;
        this.noSize = 0;

    }

    /**
     * Constructs a graph with the same fields
     * as the specified graph object that was received.
     *
     * @param gr
     */
    public WGraph_DS(weighted_graph gr) {
        if (gr != null) {
            this.vertices = new HashMap<>();
            Iterator<node_info> t = gr.getV().iterator();
            while (t.hasNext()) {
                addNode(t.next().getKey());
            }
            for (node_info nodeTemp : gr.getV()) {
                Iterator<node_info> t2 = ((NodeInfo) nodeTemp).getNi().iterator();
                while (t2.hasNext()) {
                    node_info i = new NodeInfo(t2.next());
                    connect(i.getKey(), nodeTemp.getKey(), gr.getEdge(i.getKey(), nodeTemp.getKey()));
                }
            }


            this.edSize = gr.edgeSize();
            this.noSize = gr.nodeSize();
            this.mc = gr.edgeSize() + gr.nodeSize();
        }
    }

    /**
     * @param key - the node_id
     * @return the node of this key, null if none.
     */
    @Override
    public node_info getNode(int key) {
        if (this.vertices.containsKey(key)) {
            return this.vertices.get(key);
        }
        return null;
    }

    @Override
    public boolean hasEdge(int node1, int node2) {
        if (vertices.containsKey(node1) & vertices.containsKey(node2))
            return (((NodeInfo) getNode(node2)).hasNi(node1));
        return false;
    }

    @Override
    public double getEdge(int node1, int node2) {
        if (hasEdge(node1, node2))
            return ((NodeInfo) getNode(node1)).edges.get(node2).weigh;
        return -1;
    }

    /**
     * Add a new node to the graph with the key that was received.
     *
     * @param key
     */
    @Override
    public void addNode(int key) {
        if (!vertices.containsKey(key)) {
            node_info n = new NodeInfo(key);
            this.vertices.put(n.getKey(), n);
            if (vertices.size() == (noSize + 1)) {
                this.noSize++;
                this.mc++;
            }
        }
    }

    /**
     * This function makes an edge between the vertices (node1 and node2)
     * by inserting one node into the neighbor's list of the others node.
     *
     * @param node1
     * @param node2
     */
    @Override
    public void connect(int node1, int node2, double w) {
        if (w >= 0 & vertices.containsKey(node1) & vertices.containsKey(node2) & node1 != node2) {
            if (!hasEdge(node1, node2)) {
                this.edSize++;
            }
            ((NodeInfo) this.vertices.get(node2)).addNi(this.vertices.get(node1), w);
            ((NodeInfo) this.vertices.get(node1)).addNi(this.vertices.get(node2), w);

            this.mc++;

        }
    }

    /**
     * @return a collection with all the nodes in the graph.
     */
    @Override
    public Collection<node_info> getV() {
        if (this.vertices == null) return null;
        return this.vertices.values();
    }

    /**
     * @return a collection with all the Neighbor nodes of the vertex that was received.
     */
    @Override
    public Collection<node_info> getV(int node_id) {
        if (vertices.containsKey(node_id))
            return ((NodeInfo) this.vertices.get(node_id)).getNi();
        return null;
    }

    @Override
    public node_info removeNode(int key) {
        if (!this.vertices.containsKey(key)) return null;
        node_info myNode = getNode(key);
        Iterator<node_info> itr = ((NodeInfo) myNode).getNi().iterator();
        if (itr.hasNext()) {
            node_info t;
            while (itr.hasNext()) {
                t = itr.next();
                if (t.getKey() != key) {
//                    ((NodeInfo) myNode).removeNode(t);
                    ((NodeInfo) t).removeNode(myNode);
                    this.edSize--;
                    this.mc++;
//                    removeEdge(t.getKey(), key);
//                    itr = ((NodeInfo) myNode).getNi().iterator();
                }
//                if (itr.hasNext()) {
//                    t = itr.next();
//                }

            }

//            if (t.getKey() != key) {
//
//                removeEdge(t.getKey(), key);
//                ((NodeInfo) myNode).removeNode(t);
//                ((NodeInfo) t).removeNode(myNode);
//                this.edSize--;
//                this.mc++;
//
//            }

        }

        this.vertices.remove(key);
        this.noSize--;
        this.mc++;
        return myNode;
    }

    @Override
    public void removeEdge(int node1, int node2) {
        if (node1 != node2 & vertices.containsKey(node1) & vertices.containsKey(node2) & hasEdge(node1, node2)) {
            ((NodeInfo) this.vertices.get(node1)).removeNode(this.vertices.get(node2));
            ((NodeInfo) this.vertices.get(node2)).removeNode(this.vertices.get(node1));
            this.edSize--;
            this.mc++;
        }
    }

    /**
     * @return the number of vertices (nodes) in the graph.
     */
    @Override
    public int nodeSize() {
        return this.noSize;
    }

    /**
     * @return the number of edges in the graph.
     */
    @Override
    public int edgeSize() {
        return this.edSize;
    }

    /**
     * @return the Mode Count of the changes made in the graph.
     */
    @Override
    public int getMC() {
        return this.mc;
    }

    @Override
    public String toString() {
        return "{" + vertices +
                ", mc=" + mc +
                ", edSize=" + edSize +
                ", noSize=" + noSize +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof WGraph_DS))
            return false;
        WGraph_DS w = (WGraph_DS) o;
        if (w.edgeSize() != this.edSize | w.nodeSize() != this.noSize)
            return false;

        if (!(w.vertices.equals(this.vertices)))
            return false;

        return true;
    }


    private static class NodeInfo implements node_info, java.io.Serializable {

        private static int id = 0;
        private HashMap<Integer, node_info> myNeighbors;
        private HashMap<Integer, EdgeNode> edges;
        private int key;
        private double tag;
        private String info = "";


        /**
         * Constructs a NodeInfo with minimal fields
         */
        public NodeInfo() {
            this.key = id++;
            this.tag = 0;
            this.info = null;
            this.myNeighbors = new HashMap<>();
            this.edges = new HashMap<>();

        }

        public NodeInfo(int k) {
            this.key = k;
            this.tag = 0;
            this.info = "";
            this.myNeighbors = new HashMap<>();
            this.edges = new HashMap<>();

        }

        /**
         * Constructs a NodeData with the same fields
         * as the specified NodeData object that was received.
         *
         * @param node
         */
        public NodeInfo(node_info node) {
            if (node != null) {
                this.myNeighbors = new HashMap<>();
                this.edges = new HashMap<>();
                this.tag = node.getTag();
                this.info = node.getInfo();
                this.key = node.getKey();

            }
        }

        /**
         * @return the node's key.
         */
        @Override
        public int getKey() {
            return this.key;
        }

        /**
         * @return the node's info.
         */
        @Override
        public String getInfo() {
            return this.info;
        }

        /**
         * Sets the info of the vertex.
         *
         * @param s
         */
        @Override
        public void setInfo(String s) {
            this.info = s;
        }

        /**
         * @return the node's tag.
         */
        @Override
        public double getTag() {
            return this.tag;
        }

        /**
         * Sets the tag of the vertex.
         *
         * @param t
         */
        @Override
        public void setTag(double t) {
            this.tag = t;
        }

        /**
         * @return a collection with all the Neighbor nodes of this vertex.
         */
        public Collection<node_info> getNi() {
            return this.myNeighbors.values();
        }

        /**
         * @return a collection with all the nodes edges of this vertex.
         */
        public Collection<EdgeNode> getEdgeList() {
            return this.edges.values();
        }

        /**
         * @param key
         * @return true if have an edge between the vertices.
         */
        public boolean hasNi(int key) {
            return this.key == key | this.edges.containsKey(key);
        }

        /**
         * This function make edge between this vertex and node_info (t).
         *
         * @param t
         */
        public void addNi(node_info t, double weight) {
            if (!myNeighbors.containsKey(t.getKey()) & t.getKey() != this.key & weight >= 0) {
                this.myNeighbors.put(t.getKey(), t);
                EdgeNode temp = new EdgeNode(key, t.getKey(), weight);
                this.edges.put(t.getKey(), temp);
            } else if (myNeighbors.containsKey(t.getKey()) & t.getKey() != this.key & weight >= 0) {
                edges.get(t.getKey()).setWeigh(weight);
            }
        }

        /**
         * Removes the edge between this vertex and node.
         *
         * @param node
         */
        public void removeNode(node_info node) {
            if (myNeighbors.containsKey(node.getKey())) {
                this.myNeighbors.remove(node.getKey());
                this.edges.remove(node.getKey());
            }
        }

        @Override
        public String toString() {
            return "" + key;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof NodeInfo))
                return false;
            HashMap e = ((NodeInfo) o).edges;
            NodeInfo node = (NodeInfo) o;

            if (this.tag != node.getTag() | !(this.info.equals(node.getInfo())))
                return false;

            if (this.key != node.getKey() | !(e.equals(this.edges)))
                return false;


            return true;

        }

        public static class EdgeNode implements java.io.Serializable {
            int first_key;
            int second_key;
            double weigh;
            double flag;

            public EdgeNode(EdgeNode t) {
                this.weigh = t.weigh;
                this.flag = t.flag;
                this.first_key = t.first_key;
                this.second_key = t.second_key;
            }

            public EdgeNode(int first_key, int second_key, double weigh) {
                this.weigh = weigh;
                this.flag = 0;
                this.first_key = first_key;
                this.second_key = second_key;
            }

            public int getFirst_key() {
                return first_key;
            }

            public int getSecond_key() {
                return second_key;
            }

            public double getWeigh() {
                return weigh;
            }

            public void setWeigh(double weigh) {
                this.weigh = weigh;
            }

            public double getFlag() {
                return flag;
            }

            public void setFlag(double flag) {
                this.flag = flag;
            }

            @Override
            public boolean equals(Object o) {
                if (!(o instanceof EdgeNode))
                    return false;
                EdgeNode t = (EdgeNode) o;
                if (this.weigh == t.weigh & this.flag == t.flag & this.first_key == t.first_key & this.second_key == t.second_key) {
                    return true;
                }
                return false;
            }


        }


    }
}


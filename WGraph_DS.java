package ex1;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * This class implements weighted_graph interface that represents an unidirectional weighted graph.
 * The implementation based on an efficient HashMap representation.
 * The HashMap implement is for high efficiency.
 * Field Summary:
 * mc - count of changes in the WGraph_DS.
 * edSize - count of edges in the WGraph_DS.
 * noSize - count of vertices (nodes) in the WGraph_DS.
 * vertices - collection containing all the vertices (nodes) in the WGraph_DS.
 */
public class WGraph_DS implements weighted_graph, java.io.Serializable {

    private HashMap<Integer, node_info> vertices;
    private int mc;
    private int edSize;
    private int noSize;

    /**
     * Constructs a WGraph_DS with not vertices.
     */
    public WGraph_DS() {
        this.mc = 0;
        this.vertices = new HashMap<>();
        this.edSize = 0;
        this.noSize = 0;

    }

    /**
     * Constructs a WGraph_DS with the same fields
     * as the specified graph object that was received.
     *
     * @param gr
     */
    public WGraph_DS(weighted_graph gr) {
        if (gr == null) return;
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

    /**
     * @param node1
     * @param node2
     * @return true if node1 is a neighbor of node2 and node2 is a neighbor of node1.
     */
    @Override
    public boolean hasEdge(int node1, int node2) {
        if (vertices.containsKey(node1) & vertices.containsKey(node2))
            return (((NodeInfo) getNode(node2)).hasNi(node1));
        return false;
    }

    /**
     * @param node1
     * @param node2
     * @return the weight of the edge between two nodes.
     */
    @Override
    public double getEdge(int node1, int node2) {
        if (hasEdge(node1, node2))
            return ((NodeInfo) getNode(node1)).edges.get(node2).weight;
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
     * by inserting one node into the neighbor's list of the others node,
     * in addition inserting one node into the edges list of the others node.
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
            if (w != getEdge(node1, node2))
                this.mc++;

            ((NodeInfo) this.vertices.get(node2)).addNi(this.vertices.get(node1), w);
            ((NodeInfo) this.vertices.get(node1)).addNi(this.vertices.get(node2), w);

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

    /**
     * The function passes over all the neighbors of the vertex and removes the common edges.
     * Finally delete the vertex from the graph.
     *
     * @param key
     * @return the deleted vertex.
     */
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

    /**
     * This function removes the edge between vertices node1 and node2
     * by deleting one node of the neighbor's list of the other node.
     *
     * @param node1
     * @param node2
     */
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

    /**
     * @return a string representation of the WGraph_DS. In general returns a
     * string that "textually represents" this WGraph_DS.
     * The result is a concise but informative representation
     * that is easy for a person to read.
     */
    @Override
    public String toString() {
        return "{" + vertices +
                ", mc=" + mc +
                ", edSize=" + edSize +
                ", noSize=" + noSize +
                '}';
    }

    /**
     * Indicates whether some other WGraph_DS is "equal to" this one.
     * by examining each element in the WGraph_DS obj.
     *
     * @param o (WGraph_DS)
     * @return true if this WGraph_DS is the same as the WGraph_DS; false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof WGraph_DS))
            return false;
        WGraph_DS w = (WGraph_DS) o;
        if (w.edgeSize() != this.edSize | w.nodeSize() != this.noSize)
            return false;

        return w.vertices.equals(this.vertices);
    }

    /**
     * This class implements node_info interface that represents the set of operations applicable on a
     * node (vertex) in an (undirectional) weighted graph.
     * The vertex's neighbors and the connected edges are implemented by HashMap for high efficiency.
     */
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

        /**
         * Constructs a NodeInfo with the same id that was received.
         *
         * @param k
         */
        public NodeInfo(int k) {
            this.key = k;
            this.tag = 0;
            this.info = "";
            this.myNeighbors = new HashMap<>();
            this.edges = new HashMap<>();

        }

        /**
         * Constructs a NodeInfo with the same fields
         * as the specified NodeInfo object that was received.
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
         * @param key
         * @return true if have an edge between the vertices.
         */
        public boolean hasNi(int key) {
            return this.edges.containsKey(key);
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
                edges.get(t.getKey()).setWeight(weight);
            }
        }

        /**
         * Removes the node and the edges between this vertex and others.
         *
         * @param node
         */
        public void removeNode(node_info node) {
            if (myNeighbors.containsKey(node.getKey())) {
                this.myNeighbors.remove(node.getKey());
                this.edges.remove(node.getKey());
            }
        }

        /**
         * @return a string representation of the NodeInfo. In general returns a
         * string that "textually represents" this NodeInfo.
         * The result is a concise but informative representation
         * that is easy for a person to read.
         */
        @Override
        public String toString() {
            return "" + key;
        }

        /**
         * Indicates whether some other NodeInfo is "equal to" this one.
         * by examining each element in the NodeInfo obj.
         *
         * @param o (NodeInfo)
         * @return true if this NodeInfo is the same as the NodeInfo; false otherwise.
         */
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

        /**
         * This class represents the set of operations applicable on a
         * edge in an (unidirectional) weighted graph.
         * each edge contain the key nodes are connect and the weight of the edge.
         */
        public static class EdgeNode implements java.io.Serializable {
            int first_key;
            int second_key;
            double weight;

            /**
             * Constructs a EdgeNode with receives data.
             */
            public EdgeNode(int first_key, int second_key, double weight) {
                this.weight = weight;
                this.first_key = first_key;
                this.second_key = second_key;
            }

            /**
             * @return the first node's key.
             */
            public int getFirst_key() {
                return first_key;
            }

            /**
             * @return the second node's key.
             */
            public int getSecond_key() {
                return second_key;
            }

            /**
             * @return the weight of the edge.
             */
            public double getWeight() {
                return weight;
            }

            /**
             * Sets the weight of the edge.
             *
             * @param weight
             */
            public void setWeight(double weight) {
                this.weight = weight;
            }

            /**
             * Indicates whether some other edge is "equal to" this one.
             * by examining each element in the edge obj.
             *
             * @param o (edge)
             * @return true if this edge is the same as the edge; false otherwise.
             */
            @Override
            public boolean equals(Object o) {
                if (!(o instanceof EdgeNode))
                    return false;
                EdgeNode t = (EdgeNode) o;
                if (this.weight == t.weight & this.first_key == t.first_key & this.second_key == t.second_key) {
                    return true;
                }
                return false;
            }


        }


    }
}


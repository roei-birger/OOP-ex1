package ex1.src;

import java.io.*;
import java.util.*;


public class WGraph_Algo implements weighted_graph_algorithms, java.io.Serializable {

    private weighted_graph my_g;
    private HashMap<Integer, myWay> path;

    /**
     * Constructs a WGraph_DS with not vertices.
     */
    public WGraph_Algo() {
        this.my_g = new WGraph_DS();
    }

    /**
     * Passes 'my_g' to be the pointer of the weighted_graph (g) that received.
     *
     * @param g
     */
    @Override
    public void init(weighted_graph g) {
        this.my_g = g;
    }

    /**
     * @return the weighted_graph at the WGraph_Algo.init
     */
    @Override
    public weighted_graph getGraph() {

        return this.my_g;
    }

    /**
     * Build a deep copy of the received graph.
     * By calling the WGraph_DS copy constructor.
     *
     * @return the created graph.
     */
    @Override
    public weighted_graph copy() {
        weighted_graph graphToCopy = new WGraph_DS(my_g);
        return graphToCopy;
    }

    /**
     * Resets the tags in the graph.
     *
     * @param g
     */
    public void resetTags(weighted_graph g) {
        for (node_info t : g.getV()) {
            t.setTag(0);

        }
    }

    /**
     * -First the method resets all the vertices in the graph.
     * -If the sum of vertices in the graph are less than 2, return true.
     * -We'll put one random vertex in queue and change his tag to 1.
     * -By loop we will take out the queue each time a different vertex and put in the queue all its neighbors.
     * After the loop we will compare the number of times the loop was executed to
     * the number of vertices in the graph and so we will know if
     * we passed all the vertices and if the graph is connected.
     *
     * @return true if there is a path from every node to each other node.
     */
    @Override
    public boolean isConnected() {
        if (my_g == null) {
            return false;
        }
        resetTags(my_g);
        if (my_g.nodeSize() < 2) return true;
        Queue<node_info> q = new LinkedList<>();
        node_info first = my_g.getV().iterator().next();
        first.setTag(1);
        q.add(first);
        int count = 0;
        node_info temp;
        while (!q.isEmpty()) {
            temp = q.poll();
            if (temp.getTag() == 1) {
                for (node_info i : my_g.getV(temp.getKey())) {
                    if (i.getTag() == 0) {
                        q.add(i);
                        i.setTag(1);
                    }
                }
            }
            temp.setTag(2);
            count++;
        }

        return (my_g.nodeSize() == count);

    }

    /**
     * The method checks the shortest path between two vertices by using the "Dijkstra" method.
     * Each vertex that the method checks is updated in the myWay HASHMAP.
     *
     * @param src  - start node
     * @param dest - end (target) node
     * @return the length of the shortest path between src to dest
     */
    @Override
    public double shortestPathDist(int src, int dest) {
        path = new HashMap<>();
        if (my_g == null) return -1;
        if (my_g.getNode(src) == null | my_g.getNode(dest) == null) return -1;
        PriorityQueue<myWay> q = new PriorityQueue<>();
        if (src == dest) {
            myWay first = new myWay(src);
            first.setShortDis(0);
            path.put(first.getId(), first);
            return 0;
        }
        myWay first = new myWay(src);
        first.setShortDis(0);
        first.setFlag(1);
        q.add(first);
        myWay temp;
        path.put(first.getId(), first);
        while (!q.isEmpty()) {
            temp = q.poll();
            if (temp.getFlag() == 1) {
                if (temp.getId() == dest) {
                    return temp.getShortDis();
                } else {
                    for (node_info i : my_g.getV(temp.getId())) {
                        myWay p;

                        if (!path.containsKey(i.getKey())) {
                            p = new myWay(i.getKey());
                            path.put(p.getId(), p);
                            p.setShortDis(temp.getShortDis() + my_g.getEdge(temp.getId(), p.getId()));
                            p.setParent(temp);
                            q.add(p);
                            p.setFlag(1);
                        } else {
                            p = path.get(i.getKey());
                            double sumE = my_g.getEdge(temp.getId(), p.getId());
                            if (temp.getShortDis() + sumE <= p.getShortDis()) {
                                p.setShortDis(temp.getShortDis() + sumE);
                                p.setParent(temp);
                                q.add(p);
                            }
                        }
                    }
                }
                temp.setFlag(2);
            }
        }
        return -1;
    }

    /**
     * The method calls "shortestPathDist" method on the graph.
     * After that the method used the myWay HASHMAP that the function created.
     * each time inserts the next vertex into the list.
     * The method will be run until the SRC vertex enters the list.
     * Finally we return the created list.
     *
     * @param src  - start node
     * @param dest - end (target) node
     * @return the the shortest path between src to dest - as an ordered List of nodes.
     */
    @Override
    public List<node_info> shortestPath(int src, int dest) {
        double dist = shortestPathDist(src, dest);
        if (dist < 0) return null;
        LinkedList<node_info> finalList = new LinkedList<>();
        myWay tempO = path.get(dest);
        node_info tempN = my_g.getNode(tempO.getId());

        if (dest == src) {
            finalList.addFirst(tempN);
            return finalList;
        }
        finalList.addFirst(tempN);
        while (tempO.getShortDis() > 0) {
            tempO = tempO.getParent();
            tempN = my_g.getNode(tempO.getId());
            finalList.addFirst(tempN);

        }

        return finalList;
    }

    /**
     * Saves this weighted (undirected) graph to the given
     * file name
     *
     * @param file - the file name.
     * @return true - if the file was successfully saved
     */
    @Override
    public boolean save(String file) {
        try {
            FileOutputStream myFile = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream((myFile));

            out.writeObject(this.my_g);

            myFile.close();
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }

    /**
     * This method load a graph to this graph algorithm.
     * the file load underlying graph.
     *
     * @param file - file name.
     * @return true - if the graph was successfully loaded.
     */
    @Override
    public boolean load(String file) {
        try {
            FileInputStream myFile = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(myFile);

            this.my_g = (weighted_graph) in.readObject();
            myFile.close();
            in.close();


        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * @return a string representation of the WGraph_Algo. In general returns a
     * string that "textually represents" this WGraph_Algo.
     * The result is a concise but informative representation
     * that is easy for a person to read.
     */
    @Override
    public String toString() {
        return "WGraph_Algo{" +
                "my_g=" + my_g +
                '}';
    }

    /**
     * This class represents a support object for the "shortestPath" method,
     * at the "shortestPathDist" method אhe graph is tested to find the shortest
     * path between vertices. Each vertex that found in this way is preserved
     * by this object in order to know which vertex was "his parent"
     * on the way and what is the weight of the edge between them.
     */
    public static class myWay implements Comparable<myWay> {
        private int id;
        private double shortDis;
        private myWay parent;
        private int flag;

        /**
         * Constructs a myWay with a basic fields
         */
        public myWay(int id) {
            this.id = id;
            this.shortDis = Double.POSITIVE_INFINITY;
            this.parent = null;
            this.flag = 0;


        }

        /**
         * @return the id.
         */
        public int getId() {
            return this.id;
        }

        /**
         * @return the ShortDis.
         */
        public double getShortDis() {
            return this.shortDis;
        }

        /**
         * Sets the shortDis of the edge.
         *
         * @param shortDis
         */
        public void setShortDis(double shortDis) {
            this.shortDis = shortDis;
        }

        /**
         * @return the parent.
         */
        public myWay getParent() {
            return this.parent;
        }

        /**
         * Sets the parent of the vertex.
         *
         * @param parent
         */
        public void setParent(myWay parent) {
            this.parent = parent;
        }

        /**
         * @return the flag.
         */
        public int getFlag() {
            return this.flag;
        }

        /**
         * Sets the flag of the vertex.
         *
         * @param flag
         */
        public void setFlag(int flag) {
            this.flag = flag;
        }

        /**
         * The method defines a linear order between two sides in a graph for comparing edges.
         *
         * @param o
         * @return
         */
        @Override
        public int compareTo(myWay o) {
            if (this.shortDis < o.getShortDis())
                return -1;
            else if (this.shortDis > o.getShortDis())
                return 1;
            else return 0;
        }

        /**
         * @return a string representation of the myWay. In general returns a
         * string that "textually represents" this myWay.
         * The result is a concise but informative representation
         * that is easy for a person to read.
         */
        @Override
        public String toString() {
            return
                    "" + id;
        }
    }


}

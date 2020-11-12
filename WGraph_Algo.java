package ex1;

import java.util.*;


public class WGraph_Algo implements weighted_graph_algorithms {

    private weighted_graph my_g;
    private HashMap<Integer, myWay> path;

    /**
     * Constructs a WGraph_DS with not vertices.
     */
    public WGraph_Algo() {
        this.my_g = new WGraph_DS();
    }

    @Override
    public void init(weighted_graph g) {
        this.my_g = g;
    }

    @Override
    public weighted_graph getGraph() {

        return this.my_g;
    }

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
            return true;
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


    @Override
    public double shortestPathDist(int src, int dest) {
        path = new HashMap<>();
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


    @Override
    public List<node_info> shortestPath(int src, int dest) {
        double dist = shortestPathDist(src, dest);
        if (dist < 0) return null;
        LinkedList<node_info> finalList = new LinkedList<>();
        myWay tempO = path.get(dest);
        node_info tempN = my_g.getNode(tempO.getId());
        finalList.addFirst(tempN);
        if (dest == src) {
            return finalList;
        }

        while (tempO.getShortDis() > 0) {
            tempO = tempO.getParent();
            tempN = my_g.getNode(tempO.getId());
            finalList.addFirst(tempN);

        }
        return finalList;
    }

    @Override
    public boolean save(String file) {
        return false;
    }

    @Override
    public boolean load(String file) {
        return false;
    }

    public static class myWay implements Comparable<myWay> {
        private int id;
        private double shortDis;
        private myWay parent;
        private int flag;

        public myWay(int id) {
            this.id = id;
            this.shortDis = Double.POSITIVE_INFINITY;
            this.parent = null;
            this.flag = 0;


        }

        public int getId() {
            return this.id;
        }

        public double getShortDis() {
            return this.shortDis;
        }

        public void setShortDis(double shortDis) {
            this.shortDis = shortDis;
        }

        public myWay getParent() {
            return this.parent;
        }

        public void setParent(myWay parent) {
            this.parent = parent;
        }

        public int getFlag() {
            return this.flag;
        }

        public void setFlag(int flag) {
            this.flag = flag;
        }

        @Override
        public int compareTo(myWay o) {
            if (this.shortDis < o.getShortDis())
                return -1;
            else if (this.shortDis > o.getShortDis())
                return 1;
            else return 0;
        }

        @Override
        public String toString() {
            return
                    "" + id;
        }
    }


}

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RG_Graph {
    //total number of vertices
    protected int num_of_vertices;
    //graph
    List<ArrayList<Integer>> graph;
    //dfs instance
    DFS dfs = new DFS();
    //points making up the graph
    Point[] point_arr;
    // create instance of Random class
    Random rand = new Random();
    //r value
    protected double r;

    public List<ArrayList<Integer>> generate(int n, double r) {
        boolean connected = false;

        while (connected == false) {
            genGraph(n, r);
            connected = isConnected();
        }
        return this.graph;
    }

    public void genGraph(int n, double r) {
        this.num_of_vertices = n;
        // Generates n samples in the unit cube.
        gen_points_2D_space(this.num_of_vertices);
        //set r value to expected for connected graph
        this.r = r;
        //generate list of list to hold graph representation
        graph = new ArrayList<>(this.num_of_vertices);
        for (int i = 0; i < this.num_of_vertices; i++) {
            this.graph.add(new ArrayList<>());
        }

        for (int i = 0; i < this.num_of_vertices; i++) {
            for (int j = 0; j < this.num_of_vertices; j++) {
                if (i != j) {
                    if (calc_distance_r(point_arr[i].x, point_arr[i].y, point_arr[j].x, point_arr[j].y) <= this.r) {
                        if (!this.graph.get(i).contains(j)) {
                            addEdge(this.graph, i, j);
                        }
                    }
                }
            }
        }
    }

    //add connection
    private void addEdge(List<ArrayList<Integer>> graph, int i, int j) {
        graph.get(i).add(j);
        graph.get(j).add(i);
    }

    // Check if graph is connected
    public boolean isConnected() {
        //array to indicate if visited
        boolean[] visited = new boolean[num_of_vertices];

        //start the DFS from vertex 0
        dfs.run(0, this.graph, visited);
        //check if all the vertices are visited, if yes then graph is connected
        for (boolean b : visited) {
            if (!b)
                return false;
        }
        return true;
    }

    public double calc_distance_r(double x_1, double y_1, double x_2, double y_2) {
        double distance;
        distance = Math.sqrt(Math.pow((x_1 - x_2), 2) + Math.pow((y_1 - y_2), 2));
        return distance;
    }

    public void gen_points_2D_space(int size) {
        this.point_arr = new Point[size];
        for (int i = 0; i < size; i++) {
            // Generate Random doubles
            double x = rand.nextDouble();
            double y = rand.nextDouble();
            this.point_arr[i] = new Point(x, y);
        }
    }

    public List<ArrayList<Integer>> getGraph() {
        return this.graph;
    }

    public Point[] getPoint_arr() {
        return this.point_arr;
    }

    public static class Point {
        protected double x, y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }
    }

}
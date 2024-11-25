import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;

public class WS_Graph {

    //to hold the graph
    protected List<ArrayList<Integer>> graph;

    //num of vertices
    protected int num_of_vertices;

    //random gen
    protected SplittableRandom rand = new SplittableRandom();

    //to check connectivity
    DFS dfs = new DFS();

    public WS_Graph() {
    }

    public List<ArrayList<Integer>> generate(int n, int k, double beta) {
        boolean connected = false;

        while (connected == false) {
            genGraph(n, k, beta);
            connected = isConnected();
        }
        return this.graph;
    }

    public void genGraph(int n, int k, double beta) {
        //add error handling
        //for 0 <= beta <= 1
        // n > k > ln(n) > 1

        this.num_of_vertices = n;

        //to hold the all node for use in rewiring
        ArrayList<Integer> all_node = new ArrayList<>();

        //generate graph of num_of_vertices
        this.graph = new ArrayList<>();
        for (int i = 0; i < this.num_of_vertices; i++) {
            this.graph.add(new ArrayList<Integer>());
            all_node.add(i);
        }

        //populate graph to form a ring lattice
        int half_k = (int) Math.floor( k / 2);
        for (int i = 0; i < this.num_of_vertices; i++) {
            for (int j = i + 1; j < i + half_k + 1; j++) {
                addEdge(this.graph, i, j % num_of_vertices);
            }
        }


        //to hold the current possible connections
        ArrayList<Integer> current_possible_connections;

        //double for random comparison
        double check_rnd = 0;

//        System.out.println(graph);
        //rewire
        for (int i = 0; i < this.num_of_vertices; i++) {

            //reset possible connections
            current_possible_connections = new ArrayList<>(all_node);

            //filter so that only possible connections remain
            //no multi edges
            for (Integer element : this.graph.get(i)) {
                current_possible_connections.remove(element);
            }
            //no self loops
            current_possible_connections.remove(Integer.valueOf(i));

            if (current_possible_connections.size() != 0) {

                for (int j = i + 1; j < i + half_k + 1; j++) {

                    check_rnd = rand.nextDouble(0, 1);

                    if (check_rnd <= beta) {
                        //get random connection
                        int value = current_possible_connections.get(rand.nextInt(0, current_possible_connections.size()));

                        //add to graph
                        addEdge(this.graph, i, value);

                        //remove from graph
                        removeEdge(this.graph, i, j % num_of_vertices);

                        //add to possible connections
                        current_possible_connections.add(Integer.valueOf(j % num_of_vertices));

                        //remove from possible connections
                        current_possible_connections.remove(Integer.valueOf(value));
                    }
                }
            }
        }
//        System.out.println(this.graph);

    }

    //method to add edges to graph a bit more useful in an adj implementation
    private void addEdge(List<ArrayList<Integer>> graph, int i, int j) {
        graph.get(i).add(j);
        graph.get(j).add(i);
    }

    //method to remove edges to graph a bit more useful in an adj implementation
    private void removeEdge(List<ArrayList<Integer>> graph, int i, int j) {
        graph.get(i).remove(Integer.valueOf(j));
        graph.get(j).remove(Integer.valueOf(i));
    }

    // Check if graph is connected
    public boolean isConnected() {
        //array to indicate if visited
        boolean[] visited = new boolean[num_of_vertices];

        //start the DFS from vertex 0
        dfs.run(0, graph, visited);
        //check if all the vertices are visited, if yes then graph is connected
        for (int i = 0; i < visited.length; i++) {
            if (!visited[i])
                return false;
        }
        return true;
    }
}

//implementation of sequential ER graph generator based on the PreZER algorithm

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ER_Graph {

    // Simple list array to hold values of graph edges - later to be decoded
    protected ArrayList<Integer> Encoded_Graph_Rep;

    //num of vertices
    protected int num_of_vertices;

    //probability
    protected double p;

    // max number of Edges
    protected int max_number_of_edges;

    protected List<ArrayList<Integer>> graph;

    DFS dfs = new DFS();

    //constructor
    public ER_Graph() {
    }

    public List<ArrayList<Integer>> generate(double probability, int number_of_vertices){
        boolean connected = false;

        while (connected == false) {
            genGraph(probability, number_of_vertices);
            connected = isConnected();
        }
        return this.graph;
    }

    public void genGraph(double probability, int number_of_vertices) {
        //initiate
        num_of_vertices = number_of_vertices;

        graph = new ArrayList<>(num_of_vertices);

        p = probability;

        // Max edges
        max_number_of_edges = (num_of_vertices * (num_of_vertices - 1)) / 2;

        //boolean array rep of graph
        Encoded_Graph_Rep = new ArrayList<Integer>();

        //get m value based on n value
        int m = 7;

        //cumulative probability array
        double[] F_array = new double[m + 1];

        //compute the cumulative probability of F[i]
        for (int i = 0; i <= m; i++) {
            F_array[i] = F(i);
        }

        int i = -1;

        //loop
        while (i < max_number_of_edges) {
            //generate a uniform random number alpha E
            double alpha = ThreadLocalRandom.current().nextDouble(0.00000000000000001, 1);

            //define k - this is the skip value
            int k = 0;

            //integrator for second loop
            int j = 0;

            while (j <= m) {
                if (F_array[j] > alpha) {
                    k = j;
                    break;
                }
                j++;
            }
            if (j == (m + 1)) {
                //Compute the skip value k
                k = (int) Math.ceil(Math.log(1 - alpha) / Math.log(1 - probability)) - 1;
            }
            i = i + k + 1;
            Encoded_Graph_Rep.add(i + 1);
        }
        //remove last entry of list
        Encoded_Graph_Rep.remove(Encoded_Graph_Rep.size() - 1);
        convert_to_array_list_format_graph();
    }

    //helper method to get array list rep
    public void convert_to_array_list_format_graph() {

        for (int i = 0; i < num_of_vertices; i++) {
            // list for each vertex
            graph.add(new ArrayList<>());
        }
        int[] edge = new int[2];
        for (int i = 0; i < Encoded_Graph_Rep.size(); i++) {
            edge = decode_undirected_no_self(Encoded_Graph_Rep.get(i));
            addEdge(graph, edge[0], edge[1]);
        }
    }

    //Decoder for undirected with no self loops graph
    public int[] decode_undirected_no_self(int ind) {
        //edge array is edge representation in for of i vertex to j vertex edge - [i, j]
        int[] edge = new int[2];
        int i = (int) Math.ceil((-1 + Math.sqrt((1 + 8 * ind))) / 2);
        int j = (ind - ((i * (i - 1)) / 2)) - 1;
        edge[0] = i;
        edge[1] = j;
        return edge;
    }

    //method to add edges to graph a bit more useful in an adj implementation
    private void addEdge(List<ArrayList<Integer>> graph, int i, int j) {
        graph.get(i).add(j);
        graph.get(j).add(i);
    }

    //get boolean array graph
    public ArrayList<Integer> getEncoded_Graph_Rep() {
        return Encoded_Graph_Rep;
    }

    //get boolean array graph
    public List<ArrayList<Integer>> get_Graph() {
        return graph;
    }

    // Check if graph is connected
    public boolean isConnected() {
        //array to indicate if visited
        boolean[] visited = new boolean[this.num_of_vertices];

        //start the DFS from vertex 0
        dfs.run(0, this.graph, visited);
        //check if all the vertices are visited, if yes then graph is connected
        for (int i = 0; i < visited.length; i++) {
            if (!visited[i])
                return false;
        }
        return true;
    }

    //Formulas
    // Formula to get m value
    private int M() {
        return (int) (Math.pow((1 - p), num_of_vertices) * p);
    }

    // Formula to get
    public double F(int i) {
        return 1 - Math.pow((1 - p), i + 1);
    }

}

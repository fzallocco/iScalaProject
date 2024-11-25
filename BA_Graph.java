import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;

public class BA_Graph {

    //initial network size
    protected int m_0;
    //initial network size
    protected int m;
    //end size of network
    protected int end_size;
    //graph
    List<ArrayList<Integer>> graph;
    //total sum of edges
    protected int sum_edges;

    DFS dfs = new DFS();

    //new linked list for generation use
    public Linked_List_BA linked_list_ba;

    //new random gen instance (good for parallel work bad for threaded)
    protected SplittableRandom rand = new SplittableRandom();

    public List<ArrayList<Integer>> generate(int m_0, int m, int n){
        boolean connected = false;

        while (connected == false) {
            genGraph(m_0, m, n);
            connected = isConnected();
        }
        return this.graph;
    }

    public List<ArrayList<Integer>> genGraph(int m_0, int m, int n) {
        linked_list_ba = new Linked_List_BA();
        sum_edges = 0;

        this.m_0 = m_0;
        this.m = m;
        this.end_size = n;


        //create an initial empty graph
        this.graph = new ArrayList<>();
        for (int i = 0; i < m_0+1; i++) {
            this.graph.add(new ArrayList<Integer>());
        }
        //since first vertex to be added is connected to all existing as m_0 = m
        for (int i = 0; i < m_0; i++) {
            add_edge(m_0, i);
        }

        //populate list with stater values
        int end = graph.size();
        for (int i = 0; i < end; i++) {
            int edge = graph.get(i).size();
            linked_list_ba.append(i, edge);
            sum_edges += edge;
        }


        for (int i = m_0+1; i < end_size; i++) {

            this.graph.add(new ArrayList<Integer>());
            int new_edge_sum = 0;
            int sum_temp = sum_edges;


            int inner_m = 0;
            while (inner_m < m) {

                double rand_prob = rand.nextDouble(0, 1) * sum_temp;
                int[] out = linked_list_ba.link_and_move(rand_prob);
                int linking_vertex = out[0];
                int remove_from_sum_temp = out[1];

                add_edge(linking_vertex, i);

                sum_temp -= remove_from_sum_temp;
                new_edge_sum += 2;

                inner_m++;
            }

            linked_list_ba.append(i, m);

            sum_edges += new_edge_sum;
        }
        return this.graph;
    }

    public List<ArrayList<Integer>> getGraph() {
        return graph;
    }

    public Linked_List_BA getLinked_list_ba() {
        return linked_list_ba;
    }

    public int getSum_edges() {
        return sum_edges;
    }

    //helper method to generate starter graph
    public List<ArrayList<Integer>> starter_graph(int m_0) {
        //Isoperimetric dynamicity & probability for initial graph
        double isoperimetricDynamicity = (Math.log(m_0) / Math.log(2)) / 2;
        double probability = (2 * isoperimetricDynamicity) / m_0;

        //new fast ER graph instance - for initial graph base
        ER_Graph myGen = new ER_Graph();
        return myGen.generate(probability, m_0);
    }

    //add to graph
    public void add_edge(int v_1, int v_2) {
        this.graph.get(v_1).add(v_2);
        this.graph.get(v_2).add(v_1);
    }

    // Check if graph is connected
    public boolean isConnected() {
        //array to indicate if visited
        boolean[] visited = new boolean[end_size];

        //start the DFS from vertex 0
        dfs.run(0, this.graph, visited);
        //check if all the vertices are visited, if yes then graph is connected
        for (int i = 0; i < visited.length; i++) {
            if (!visited[i])
                return false;
        }
        return true;
    }
}

import java.util.ArrayList;
import java.util.List;

public class Star_Graph {
    //total sum of edges
    protected int num_of_vertices;
    //graph
    List<ArrayList<Integer>> graph;

    public List<ArrayList<Integer>> generate(int n) {
        genGraph(n);
        return this.graph;
    }

    public void genGraph(int num_of_vertices) {
        this.num_of_vertices = num_of_vertices;

        //generate list of list to hold graph representation
        graph = new ArrayList<>(this.num_of_vertices);
        for (int i = 0; i < this.num_of_vertices; i++) {
            this.graph.add(new ArrayList<>());
        }

        //Add connections to graph
        for (int i = 1; i < this.num_of_vertices; i++) {
            addEdge(this.graph, 0, i);
        }

    }

    //add connection
    private void addEdge(List<ArrayList<Integer>> graph, int i, int j) {
        graph.get(i).add(j);
        graph.get(j).add(i);
    }
}

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/* References
Shuffle a given array using Fisher–Yates shuffle Algorithm:
https://www.geeksforgeeks.org/shuffle-a-given-array-using-fisher-yates-shuffle-algorithm/
*/
public class Path_Graph {
    //total sum of edges
    protected int num_of_vertices;
    //graph
    List<ArrayList<Integer>> graph;
    // create instance of Random class
    Random rand = new Random();

    public List<ArrayList<Integer>> generate(int n) {
        genGraph(n);
        return this.graph;
    }

    public void genGraph(int num_of_vertices) {
        this.num_of_vertices = num_of_vertices;

        //create and populate path
        int[] path = new int[this.num_of_vertices];
        for (int i = 0; i < this.num_of_vertices; i++) {
            path[i] = i;
        }
        // Start from the last element and swap one by one. We don't
        // need to run for the first element that's why i > 0
        for (int i = this.num_of_vertices - 1; i > 0; i--) {

            // Pick a random index from 0 to i
            int j = rand.nextInt(i + 1);

            // Swap arr[i] with the element at random index
            int temp = path[i];
            path[i] = path[j];
            path[j] = temp;
        }

//        System.out.println(path);

        //generate list of list to hold graph representation
        graph = new ArrayList<>(this.num_of_vertices);
        for (int i = 0; i < this.num_of_vertices; i++) {
            this.graph.add(new ArrayList<>());
        }

        //Add connections to graph
        for (int i = 0; i < this.num_of_vertices; i++) {
            int from_v = path[i];
            int to_v = path[(i+1) % this.num_of_vertices];
            addEdge(this.graph, from_v, to_v);
        }

    }
    //add connection
    private void addEdge(List<ArrayList<Integer>> graph, int i, int j) {
        graph.get(i).add(j);
        graph.get(j).add(i);
    }
}

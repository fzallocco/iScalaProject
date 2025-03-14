import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ISCALATest {
    public static void main(String[] args) {
        // Create an instance of ISCALA
        ISCALA<Integer> iscala = new ISCALA<>();

        // Define the parameters for the ISCALA algorithm
        int l = 5;
        double isoDynamicity = 0.5;
        int num_graphs = 3;

        // Create nodes
        Node<Integer>[] nodes = new Node[10];
        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = new Node<>(i);
        }

        // Create graphs
        HashMap<String, List<ArrayList<Integer>>> graphs = new HashMap<>();
        for (int i = 0; i < num_graphs; i++) {
            List<ArrayList<Integer>> adjList = new ArrayList<>();
            for (int j = 0; j < nodes.length; j++) {
                ArrayList<Integer> neighbors = new ArrayList<>();
                for (int k = 0; k < nodes.length; k++) {
                    if (j != k && Math.random() > 0.5) {
                        neighbors.add(k);
                    }
                }
                adjList.add(neighbors);
            }
            graphs.put("graph_" + i, adjList);
        }

        // Run the ISCALA algorithm
        long[] result = iscala.ISCALA_algo(l, isoDynamicity, nodes, graphs, num_graphs);

        // Print the results
        System.out.println("Target value: " + result[0]);
        System.out.println("Rounds taken: " + result[1]);
    }
}
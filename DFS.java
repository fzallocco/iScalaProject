import java.util.ArrayList;
import java.util.List;

public class DFS {
    //DFS helper method
    public void DFS() {
    }

    public void run(int current, List<ArrayList<Integer>> adjacencyList, boolean[] visited) {
        //set to visited
        visited[current] = true;
        //hold current neighbor
        int neighbor;
        //travel the neighbors
        for (int i = 0; i < adjacencyList.size(); i++) {
            if (adjacencyList.get(current).contains(i)) {
                neighbor = i;
                if (!visited[neighbor]) {
                    //make recursive call from neighbor
                    run(neighbor, adjacencyList, visited);
                }
            }
        }
    }
}

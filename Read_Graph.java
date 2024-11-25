import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Read_Graph {
    //map to hold all graphs
    protected HashMap<String, List<ArrayList<Integer>>> graphs = new HashMap<>();
    //file location relative to java file location
    protected File file = null;

    public Read_Graph() {

    }

    public static void main(String[] args) throws FileNotFoundException {
        Read_Graph rg = new Read_Graph();
        rg.read_graphs(new String[]{"Graphs_Folder"}, "ER_graphs");
//        System.out.println(rg.getGraphs());
    }

    public HashMap<String, List<ArrayList<Integer>>> getGraphs() {
        return graphs;
    }

    public File getFile() {
        return file;
    }

    protected void read_graphs(String[] folder_names, String file_name) throws FileNotFoundException {
        StringBuilder path_name = new StringBuilder();
        for (String i : folder_names) {
            path_name.append(i).append("/");
        }

        file = new File(path_name + "/" + file_name + ".txt");
        String path = file.getAbsolutePath();
        Scanner scanner = new Scanner(file);
        scanner.useDelimiter("[|]");
        // Check if there is another line of input
        String whole_file;
        int graph_number = 0;
        while (scanner.hasNext()) {
            whole_file = scanner.next();
            Scanner graph = new Scanner(whole_file).useDelimiter("[;]");
            graphs.put("graph_" + graph_number, new ArrayList<>());
            int v_number = 0;
            while (graph.hasNext()) {
                String pair = graph.next();
                graphs.get("graph_" + graph_number).add(new ArrayList<>());
                Scanner row = new Scanner(pair).useDelimiter("[:]");
                while (row.hasNext()) {
                    String token = row.next();
                    Scanner single = new Scanner(pair).useDelimiter("[,]");
                    while (single.hasNext()) {
                        int single_item = single.nextInt();
                        graphs.get("graph_" + graph_number).get(v_number).add(single_item);
                    }
                }
                v_number++;
            }
            graph_number++;
        }
    }
}

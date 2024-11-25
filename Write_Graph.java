import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Write_Graph {


    protected String location = "";
    protected File file = null;

    public Write_Graph() {
    }

    public void write(String[] folder_names, String file_name, List<ArrayList<Integer>> graph, boolean append) throws IOException {
        File file = create_file(folder_names, file_name);
        write_graphs_to_file(file, graph, append);
        this.location = file.getAbsolutePath();
        this.file = file;
    }

    //method responsible for creation of new file
    protected File create_file(String[] folder_names, String file_name) {
        StringBuilder path_name = new StringBuilder();

        //Create new folder and file
        for (String i : folder_names) {
            path_name.append(i).append("/");
        }
        File folder = new File(path_name.toString());
        folder.mkdirs();

        //File folder = new File(folder_name);
        File file = new File(path_name + file_name + ".txt");
        return file;
    }

    //Generate and write graphs to file
    protected void write_graphs_to_file(File file, List<ArrayList<Integer>> graph, boolean append) {
        try (
                FileWriter fw = new FileWriter(file, append);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw)
        ) {
            //write  a single graph
            int jSize = graph.size();
            for (int j = 0; j < jSize; j++) {

                int kSize = graph.get(j).size();
                for (int k = 0; k < kSize; k++) {

                    if (k < kSize - 1)
                        out.print(graph.get(j).get(k) + ",");
                    if (k == kSize - 1 && j != jSize - 1)
                        out.print(graph.get(j).get(k) + ";");
                    if (j == jSize - 1 && k == kSize - 1)
                        out.print(graph.get(j).get(k) + "|");

                }
            }
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        }
    }

    //get file and folder
    public File getFile() {
        return this.file;
    }

    //get absolute address
    public String getLocation() {
        return this.location;
    }
}

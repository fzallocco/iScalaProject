import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Run {
    public static <T> void main(String[] args) throws IOException {
        String type_of_graph = args[0];
        if (!type_of_graph.toLowerCase().equals("er") && !type_of_graph.toLowerCase().equals("ba")
                && !type_of_graph.toLowerCase().equals("ws") && !type_of_graph.toLowerCase().equals("rg")
                && !type_of_graph.toLowerCase().equals("path") && !type_of_graph.toLowerCase().equals("star")) {
            System.exit(0);
        }
        int start_n = Integer.parseInt(args[1]);
        int end_n = Integer.parseInt(args[2]);
        int l = Integer.parseInt(args[3]);
        int num_graphs = Integer.parseInt(args[4]);

        int m_0 = 0;
        if (args.length > 5) {
            m_0 = Integer.parseInt(args[5]);
        }

        Write_Graph wgph = new Write_Graph();
        Read_Graph rgph = new Read_Graph();

        ER_Graph er_gen = new ER_Graph();
        BA_Graph ba_gen = new BA_Graph();
        WS_Graph ws_gen = new WS_Graph();
        RG_Graph rg_gen = new RG_Graph();
        Path_Graph path_gen = new Path_Graph();
        Star_Graph star_gen = new Star_Graph();

        ISCALA<?> sim = new ISCALA<>();

        Node[] Nodes;

        HashMap<String, List<ArrayList<Integer>>> graphs;

        List<ArrayList<Integer>> graph;

        long[] output;

        double iso;

        //Input and output folder per set of sims
        long marker = System.currentTimeMillis();
        String input_folder = "Graphs_" + args[0].toUpperCase() + "_" + marker;
        String[] path_input = new String[]{"Input", input_folder};

        String output_folder = "Data_" + args[0].toUpperCase() + "_" + marker;
        String[] path_output = new String[]{"Output", output_folder};
        String file_name;


        for (int n = start_n; n <= end_n; n = n * 2) {
            System.out.println("--------------------------------------------------------");
            //Make new empty Node array
            Nodes = new Node[n];
            //Populate array of Nodes via for loop
            boolean isSupervisor;
            for (int i = 0; i < n; i++) {
                isSupervisor = i < l;
                Nodes[i] = new Node<T>(isSupervisor);
            }

            double v = Math.log(n) / Math.log(2);
            switch (type_of_graph.toLowerCase()) {
                case "er":
                    iso = v / (double)2;
                    double prob = (2 * iso) / (double) n;

                    file_name = "ER_Graph_N_" + n + "_graphs_" + num_graphs + "_" + System.currentTimeMillis();

                    for (int i = 0; i < num_graphs; i++) {
                        graph = er_gen.generate(prob, n);
                        wgph.write(path_input, file_name, graph, true);
                    }
                    break;
                case "ba":
                    iso = (Math.pow(m_0, 0.5) / Math.log(2) * Math.pow(n, (0.25)));

                    file_name = "BA_Graph_N_" + n + "_graphs_" + num_graphs + "_" + System.currentTimeMillis();

                    for (int i = 0; i < num_graphs; i++) {
                        graph = ba_gen.generate(m_0, m_0, n);
                        wgph.write(path_input, file_name, graph, true);
                    }

                    break;
                case "ws":

                    double beta = 0.7;
                    int k = n / 2;
                    iso = (beta * (double) n) / (6.0 * Math.log(n));

                    file_name = "WS_Graph_N_" + n + "_graphs_" + num_graphs + "_" + System.currentTimeMillis();

                    for (int i = 0; i < num_graphs; i++) {
                        graph = ws_gen.generate(n, k, beta);
                        wgph.write(path_input, file_name, graph, true);
                    }
                    break;
                case "rg":

                    iso = 2 * Math.pow((v / (double) n), 0.25);

                    file_name = "RG_Graph_N_" + n + "_graphs_" + num_graphs + "_" + System.currentTimeMillis();

                    for (int i = 0; i < num_graphs; i++) {
                        graph = rg_gen.generate(n, iso);
                        wgph.write(path_input, file_name, graph, true);
                    }
                    break;
                case "path":

                    iso = 2 / (double) n;
                    file_name = "Path_Graph_N_" + n + "_graphs_" + num_graphs + "_" + System.currentTimeMillis();

                    for (int i = 0; i < num_graphs; i++) {
                        graph = path_gen.generate(n);
                        wgph.write(path_input, file_name, graph, true);
                    }
                    break;
                case "star":

                    iso = 1;
                    file_name = "Star_Graph_N_" + n + "_graphs_" + num_graphs + "_" + System.currentTimeMillis();

                    for (int i = 0; i < num_graphs; i++) {
                        graph = star_gen.generate(n);
                        wgph.write(path_input, file_name, graph, true);
                    }
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + type_of_graph.toLowerCase());
            }


            rgph.read_graphs(path_input, file_name);
            graphs = rgph.getGraphs();

            //run ISCALA
            long start = System.currentTimeMillis();
            output = sim.ISCALA_algo(l, iso, Nodes, graphs, num_graphs);
            long time = System.currentTimeMillis() - start;
            System.out.println("----------------------------");
            System.out.println("Input file path: " + wgph.getLocation());
            System.out.println("----------------------------");
            //write output
            writeFile(n, l, iso, output[0], output[1], time, num_graphs, path_output);
        }
    }

    public static void writeFile(int nValue_Input, int lValue_Input, double iValue_Input, long n_Output, long numRounds_Output, double millis, int num_of_graphs, String[] folder_names) {

        String text
                = "Input: " + "\n" + "N value : " + nValue_Input + "\n" + "L value: " + lValue_Input + "\n" + "I value: " + iValue_Input + "\nNumber of graphs: " + num_of_graphs + "\nOutput:" + "\n"
                + "N value: " + n_Output + "\n" + "Number of rounds: " + numRounds_Output + "\n" + "Time taken in milliseconds: " + millis + "\n";

        StringBuilder path_name = new StringBuilder();
        //Create new folder and file
        for (String i : folder_names) path_name.append(i).append("/");
        File folder = new File(path_name.toString());
        folder.mkdirs();

        String file_name = "/n_" + nValue_Input + "-l_" + lValue_Input + "-i_" + iValue_Input + ".txt";

        File file = new File(path_name + file_name);

        try {
            BufferedWriter f_writer
                    = new BufferedWriter(new FileWriter(file));
            f_writer.write(text);
            System.out.print(text);
            f_writer.close();
            System.out.println("----------------------------");
            System.out.println("Output file path: " + file.getAbsolutePath());
            System.out.println("----------------------------");
        } catch (IOException e) {
            System.out.print(e.getMessage());
        }

    }
}

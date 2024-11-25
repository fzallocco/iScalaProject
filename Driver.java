import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Driver {
    public static <T> void main(String[] args) throws IOException {
        //Number of vertices
        int number_of_v = 32;
        //Isoperimetrical dynamicity
        double isoperimetricDynamicity = (Math.log(number_of_v) / Math.log(2)) / 2;
        //probability
        double probability = (2 * isoperimetricDynamicity) / number_of_v;
        //number of graphs
        int num_graphs = 1000000;

        Write_Graph wgf = new Write_Graph();

        ER_Graph er_gen = new ER_Graph();

        //Write graphs
        for (int i = 0; i < num_graphs; i++) {
        List<ArrayList<Integer>> graph_er = er_gen.generate(probability, number_of_v);
        wgf.write(new String[]{"Test_Folder", "ER"}, "Graphs_ER"+ "_N" + number_of_v + "_V" + num_graphs, graph_er, true);
        }
        System.out.println(wgf.getLocation());

        //Read graphs from file
        Read_Graph rg = new Read_Graph();
        rg.read_graphs(new String[]{"Test_Folder", "ER"}, "Graphs_ER"+ "_N"  + number_of_v + "_V" + num_graphs);
        HashMap<String, List<ArrayList<Integer>>> graphs = rg.getGraphs();
//        System.out.println(graphs);

        //Number of vertices
        int number_of_l = 1;

        //Make new empty Node array
        Node<T>[] Nodes = new Node[number_of_v];
        //Populate array of Nodes via for loop
        boolean isSupervisor;
        for (int i = 0; i < number_of_v; i++) {
            isSupervisor = i < number_of_l;
            Nodes[i] = new Node<>(isSupervisor);
        }


        ISCALA sim = new ISCALA<>();


        //time
        Date date = new Date();

        //run ISCALA
        long start = System.currentTimeMillis();

        long[] output = sim.ISCALA_algo(number_of_l, isoperimetricDynamicity, Nodes, graphs, num_graphs);

        long time = System.currentTimeMillis() - start;

        System.out.println("Time: " + time);
        System.out.println("N: " + output[0]);
        System.out.println("Rounds " + output[1]);
    }
}

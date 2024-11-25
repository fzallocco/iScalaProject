import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) throws IOException {
        Write_Graph wgf = new Write_Graph();

        ER_Graph er_gen = new ER_Graph();

        RG_Graph rg_gen = new RG_Graph();

        BA_Graph ba_gen = new BA_Graph();

        Path_Graph path_gen = new Path_Graph();

//        List<ArrayList<Integer>> graph_er = er_gen.generate(0.5, 4);
//        wgf.write(new String[]{"Test_Folder", "ER"}, "Graphs_ER", graph_er, true);
//        System.out.println(graph_er);


//
//        List<ArrayList<Integer>> graph_ba = ba_gen.generate(2,2,4);
//        wgf.write(new String[]{"Test_Folder", "BA"}, "Graphs_BA", graph_ba, true);
//        System.out.println(graph_ba);
//
//        graph_ba = ba_gen.generate(2,2,8);
//        wgf.write(new String[]{"Test_Folder", "BA"}, "Graphs_BA", graph_ba, true);
//        System.out.println(graph_ba);
//
//        WS_Graph ws_gen = new WS_Graph();
//
//        List<ArrayList<Integer>> graph_ws = ws_gen.generate(8,2,1);
//        List<ArrayList<Integer>> graph_ws_1 = ws_gen.generate(8,2,1);
//        List<ArrayList<Integer>> graph_ws_12 = ws_gen.generate(8,2,1);
//        System.out.println(graph_ws);

//        for (int i = 4; i <= 256; i = i*2) {
//            double beta = 0.7;
//            int k = i/2;
//            double out = (beta * k) / (3);
//            System.out.println(i + ": " + out);
//        }

        List<ArrayList<Integer>>  graph_path = path_gen.generate(4);
        System.out.println(graph_path);

    }
}

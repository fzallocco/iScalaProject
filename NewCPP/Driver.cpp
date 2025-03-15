// Driver.cpp
#include <string>
#include <iostream>
#include <vector>
#include <unordered_map>
#include <chrono>
#include <random>
#include "ISCALA.h"
#include "Node.h"
#include "ER_Graph.h"
#include "Read_Graph.h"
#include "Write_Graph.h"
#include "WS_Graph.h"
#include "Star_Graph.h"
#include "RG_Graph.h"
#include "Path_Graph.h"

int main() {
    // Number of vertices
    int number_of_v = 32;
    // Isoperimetrical dynamicity
    double isoperimetricDynamicity = (std::log(number_of_v) / std::log(2)) / 2;
    // Probability
    double probability = (2 * isoperimetricDynamicity) / number_of_v;
    // Number of graphs
    //int num_graphs = 100;
    int num_graphs = 1000000;

    Write_Graph wgf;

    ER_Graph er_gen;
    WS_Graph ws_gen;
    Star_Graph star_gen;
    RG_Graph rg_gen;
    Path_Graph path_gen;

    // Write graphs
    for (int i = 0; i < num_graphs; i++) {
        std::vector<std::vector<int>> graph_er = er_gen.generate(probability, number_of_v);
        wgf.write({"Test_Folder", "ER"}, "Graphs_ER_N" + std::to_string(number_of_v) + "_V" + std::to_string(num_graphs), graph_er, true);
    }
    std::cout << wgf.getLocation() << std::endl;

    // Read graphs from file
    Read_Graph rg;
    rg.read_graphs({"Test_Folder", "ER"}, "Graphs_ER_N" + std::to_string(number_of_v) + "_V" + std::to_string(num_graphs));
    std::unordered_map<std::string, std::vector<std::vector<int>>> graphs = rg.getGraphs();

    // Number of vertices
    int number_of_l = 1;

    // Make new empty Node array
    std::vector<Node<int>> Nodes(number_of_v);
    // Populate array of Nodes via for loop
    for (int i = 0; i < number_of_v; i++) {
        bool isSupervisor = i < number_of_l;
        Nodes[i].setSupervisor(isSupervisor);
    }

    ISCALA<int> sim;

    // Time
    auto start = std::chrono::high_resolution_clock::now();

    // Run ISCALA
    std::vector<long> output = sim.ISCALA_algo(number_of_l, isoperimetricDynamicity, Nodes, graphs, num_graphs);

    auto end = std::chrono::high_resolution_clock::now();
    std::chrono::duration<double> elapsed = end - start;

    std::cout << "Time: " << elapsed.count() << " seconds" << std::endl;
    std::cout << "N: " << output[0] << std::endl;
    std::cout << "Rounds: " << output[1] << std::endl;

    return 0;
}
// Run.cpp
#include <iostream>
#include <vector>
#include <unordered_map>
#include <chrono>
#include "Write_Graph.h"
#include "Read_Graph.h"
#include "ER_Graph.h"
#include "BA_Graph.h"
#include "WS_Graph.h"
#include "RG_Graph.h"
#include "Path_Graph.h"
#include "Star_Graph.h"
#include "ISCALA.h"
#include "Node.h"

void writeFile(int nValue_Input, int lValue_Input, double iValue_Input, long n_Output, long numRounds_Output, double millis, int num_of_graphs, const std::vector<std::string>& folder_names) {
    std::string text = "Input: \nN value: " + std::to_string(nValue_Input) + "\nL value: " + std::to_string(lValue_Input) + "\nI value: " + std::to_string(iValue_Input) + "\nNumber of graphs: " + std::to_string(num_of_graphs) + "\nOutput:\nN value: " + std::to_string(n_Output) + "\nNumber of rounds: " + std::to_string(numRounds_Output) + "\nTime taken in milliseconds: " + std::to_string(millis) + "\n";

    std::string path_name;
    for (const auto& folder : folder_names) {
        path_name += folder + "/";
    }

    std::filesystem::create_directories(path_name);
    std::string file_name = "/n_" + std::to_string(nValue_Input) + "-l_" + std::to_string(lValue_Input) + "-i_" + std::to_string(iValue_Input) + ".txt";
    std::ofstream file(path_name + file_name);

    if (file.is_open()) {
        file << text;
        file.close();
        std::cout << text;
        std::cout << "Output file path: " << path_name + file_name << std::endl;
    } else {
        std::cerr << "Unable to open file: " << path_name + file_name << std::endl;
    }
}

int main(int argc, char* argv[]) {
    if (argc < 5) {
        std::cerr << "Usage: " << argv[0] << " <graph_type> <start_n> <end_n> <l> <num_graphs> [m_0]" << std::endl;
        return 1;
    }

    std::string type_of_graph = argv[1];
    int start_n = std::stoi(argv[2]);
    int end_n = std::stoi(argv[3]);
    int l = std::stoi(argv[4]);
    int num_graphs = std::stoi(argv[5]);
    int m_0 = (argc > 6) ? std::stoi(argv[6]) : 0;

    Write_Graph wgph;
    Read_Graph rgph;

    ER_Graph er_gen;
    BA_Graph ba_gen;
    WS_Graph ws_gen;
    RG_Graph rg_gen;
    Path_Graph path_gen;
    Star_Graph star_gen;

    ISCALA<int> sim;

    std::vector<Node<int>> Nodes;
    std::unordered_map<std::string, std::vector<std::vector<int>>> graphs;
    std::vector<std::vector<int>> graph;
    std::vector<long> output;
    double iso;

    long marker = std::chrono::system_clock::now().time_since_epoch() / std::chrono::milliseconds(1);
    std::string input_folder = "Graphs_" + type_of_graph + "_" + std::to_string(marker);
    std::vector<std::string> path_input = {"Input", input_folder};

    std::string output_folder = "Data_" + type_of_graph + "_" + std::to_string(marker);
    std::vector<std::string> path_output = {"Output", output_folder};
    std::string file_name;

    for (int n = start_n; n <= end_n; n *= 2) {
        Nodes.resize(n);
        for (int i = 0; i < n; i++) {
            Nodes[i].setSupervisor(i < l);
        }

        double v = std::log(n) / std::log(2);
        if (type_of_graph == "er") {
            iso = v / 2.0;
            double prob = (2 * iso) / n;
            file_name = "ER_Graph_N_" + std::to_string(n) + "_graphs_" + std::to_string(num_graphs) + "_" + std::to_string(marker);

            for (int i = 0; i < num_graphs; i++) {
                graph = er_gen.generate(prob, n);
                wgph.write(path_input, file_name, graph, true);
            }
        } else if (type_of_graph == "ba") {
            iso = std::pow(m_0, 0.5) / std::log(2) * std::pow(n, 0.25);
            file_name = "BA_Graph_N_" + std::to_string(n) + "_graphs_" + std::to_string(num_graphs) + "_" + std::to_string(marker);

            for (int i = 0; i < num_graphs; i++) {
                graph = ba_gen.generate(m_0, m_0, n);
                wgph.write(path_input, file_name, graph, true);
            }
        } else if (type_of_graph == "ws") {
            double beta = 0.7;
            int k = n / 2;
            iso = (beta * n) / (6.0 * std::log(n));
            file_name = "WS_Graph_N_" + std::to_string(n) + "_graphs_" + std::to_string(num_graphs) + "_" + std::to_string(marker);

            for (int i = 0; i < num_graphs; i++) {
                graph = ws_gen.generate(n, k, beta);
                wgph.write(path_input, file_name, graph, true);
            }
        } else if (type_of_graph == "rg") {
            iso = 2 * std::pow(v / n, 0.25);
            file_name = "RG_Graph_N_" + std::to_string(n) + "_graphs_" + std::to_string(num_graphs) + "_" + std::to_string(marker);

            for (int i = 0; i < num_graphs; i++) {
                graph = rg_gen.generate(n, iso);
                wgph.write(path_input, file_name, graph, true);
            }
        } else if (type_of_graph == "path") {
            iso = 2.0 / n;
            file_name = "Path_Graph_N_" + std::to_string(n) + "_graphs_" + std::to_string(num_graphs) + "_" + std::to_string(marker);

            for (int i = 0; i < num_graphs; i++) {
                graph = path_gen.generate(n);
                wgph.write(path_input, file_name, graph, true);
            }
        } else if (type_of_graph == "star") {
            iso = 1.0;
            file_name = "Star_Graph_N_" + std::to_string(n) + "_graphs_" + std::to_string(num_graphs) + "_" + std::to_string(marker);

            for (int i = 0; i < num_graphs; i++) {
                graph = star_gen.generate(n);
                wgph.write(path_input, file_name, graph, true);
            }
        } else {
            std::cerr << "Unexpected graph type: " << type_of_graph << std::endl;
            return 1;
        }

        rgph.read_graphs(path_input, file_name);
        graphs = rgph.getGraphs();

        auto start = std::chrono::high_resolution_clock::now();
        output = sim.ISCALA_algo(l, iso, Nodes, graphs, num_graphs);
        auto end = std::chrono::high_resolution_clock::now();
        std::chrono::duration<double> elapsed = end - start;

        writeFile(n, l, iso, output[0], output[1], elapsed.count() * 1000, num_graphs, path_output);
    }

    return 0;
}
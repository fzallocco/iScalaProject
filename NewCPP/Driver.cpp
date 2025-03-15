// Driver.cpp
#include <string>
#include <iostream>
#include <vector>
#include <unordered_map>
#include <chrono>
#include <random>
#include "ISCALA.h"
#include "Node.h"

int main() {
    const int num_nodes = 10;
    const int num_graphs = 5;
    const int l = 5;
    const double isoDynamicity = 0.5;

    std::vector<Node<int>> nodes(num_nodes);
    std::unordered_map<std::string, std::vector<std::vector<int>>> graphs;

    // Initialize nodes
    for (int i = 0; i < num_nodes; i++) {
        nodes[i].setK(1);
        nodes[i].setStatus(ISCALA<int>::PROBING);
        nodes[i].setPotential(0);
        nodes[i].setRho(0);
        nodes[i].setSupervisor(i % 2 == 0); // Set some nodes as supervisors
    }

    // Initialize graphs
    for (int i = 0; i < num_graphs; i++) {
        std::vector<std::vector<int>> adjList(num_nodes);
        for (int j = 0; j < num_nodes; j++) {
            for (int k = 0; k < num_nodes; k++) {
                if (j != k && std::rand() % 2) {
                    adjList[j].push_back(k);
                }
            }
        }
        graphs["graph_" + std::to_string(i)] = adjList;
    }

    ISCALA<int> iscala;

    auto start = std::chrono::high_resolution_clock::now();
    std::vector<long> result = iscala.ISCALA_algo(l, isoDynamicity, nodes, graphs, num_graphs);
    auto end = std::chrono::high_resolution_clock::now();

    std::chrono::duration<double> elapsed = end - start;

    std::cout << "Target value: " << result[0] << std::endl;
    std::cout << "Rounds taken: " << result[1] << std::endl;
    std::cout << "Execution time: " << elapsed.count() << " seconds" << std::endl;

    return 0;
}
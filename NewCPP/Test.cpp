// Test.cpp
#include <iostream>
#include <vector>
#include "Write_Graph.h"
#include "ER_Graph.h"
#include "RG_Graph.h"
#include "BA_Graph.h"
#include "Path_Graph.h"

int main() {
    Write_Graph wgf;

    ER_Graph er_gen;
    RG_Graph rg_gen;
    BA_Graph ba_gen;
    Path_Graph path_gen;

    // Example usage
    std::vector<std::vector<int>> graph_er = er_gen.generate(0.5, 4);
    wgf.write({"Test_Folder", "ER"}, "Graphs_ER", graph_er, true);
    std::cout << "ER Graph: " << graph_er.size() << " vertices" << std::endl;

    std::vector<std::vector<int>> graph_path = path_gen.generate(4);
    std::cout << "Path Graph: " << graph_path.size() << " vertices" << std::endl;

    return 0;
}
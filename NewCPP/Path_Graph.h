#ifndef PATH_GRAPH_H
#define PATH_GRAPH_H

#include <vector>
#include <random>
#include <algorithm>
#include <iomanip>
#include <iostream>
#include <list>
#include <numeric>

class Path_Graph {
public:
    std::vector<std::vector<int>> generate(int n) {
        genGraph(n);
        return graph;
    }

private:
    int num_of_vertices;
    std::vector<std::vector<int>> graph;
    std::mt19937 rand;

    void genGraph(int num_of_vertices) {
        this->num_of_vertices = num_of_vertices;
        graph.clear();
        graph.resize(num_of_vertices);

        std::vector<int> path(num_of_vertices);
        std::iota(path.begin(), path.end(), 0);
        std::shuffle(path.begin(), path.end(), rand);

        for (int i = 0; i < num_of_vertices; i++) {
            addEdge(graph, path[i], path[(i + 1) % num_of_vertices]);
        }
    }

    void addEdge(std::vector<std::vector<int>>& graph, int i, int j) {
        graph[i].push_back(j);
        graph[j].push_back(i);
    }
};

#endif // PATH_GRAPH_H
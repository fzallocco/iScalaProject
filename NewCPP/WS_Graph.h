#ifndef WS_GRAPH_H
#define WS_GRAPH_H

#include <vector>
#include <random>
#include <algorithm>
#include "DFS.h"

class WS_Graph {
public:
    WS_Graph() : num_of_vertices(0), rand(std::random_device{}()) {}

    std::vector<std::vector<int>> generate(int n, int k, double beta) {
        bool connected = false;
        while (!connected) {
            genGraph(n, k, beta);
            connected = isConnected();
        }
        return graph;
    }

private:
    std::vector<std::vector<int>> graph;
    int num_of_vertices;
    std::mt19937 rand;
    DFS dfs;

    void genGraph(int n, int k, double beta) {
        num_of_vertices = n;
        graph.clear();
        graph.resize(num_of_vertices);

        // Populate graph to form a ring lattice
        int half_k = k / 2;
        for (int i = 0; i < num_of_vertices; i++) {
            for (int j = i + 1; j <= i + half_k; j++) {
                addEdge(graph, i, j % num_of_vertices);
            }
        }

        // Rewire edges with probability beta
        std::uniform_real_distribution<> dis(0.0, 1.0);
        for (int i = 0; i < num_of_vertices; i++) {
            for (int j = i + 1; j <= i + half_k; j++) {
                if (dis(rand) <= beta) {
                    int new_j;
                    do {
                        new_j = rand() % num_of_vertices;
                    } while (new_j == i || std::find(graph[i].begin(), graph[i].end(), new_j) != graph[i].end());
                    removeEdge(graph, i, j % num_of_vertices);
                    addEdge(graph, i, new_j);
                }
            }
        }
    }

    void addEdge(std::vector<std::vector<int>>& graph, int i, int j) {
        graph[i].push_back(j);
        graph[j].push_back(i);
    }

    void removeEdge(std::vector<std::vector<int>>& graph, int i, int j) {
        graph[i].erase(std::remove(graph[i].begin(), graph[i].end(), j), graph[i].end());
        graph[j].erase(std::remove(graph[j].begin(), graph[j].end(), i), graph[j].end());
    }

    bool isConnected() {
        std::vector<bool> visited(num_of_vertices, false);
        dfs.run(0, graph, visited);
        return std::all_of(visited.begin(), visited.end(), [](bool v) { return v; });
    }
};

#endif // WS_GRAPH_H
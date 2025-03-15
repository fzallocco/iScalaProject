#ifndef ER_GRAPH_H
#define ER_GRAPH_H

#include <vector>
#include <random>
#include <cmath>
#include "DFS.h"

class ER_Graph {
public:
    ER_Graph() : num_of_vertices(0), p(0), max_number_of_edges(0) {}

    std::vector<std::vector<int>> generate(double probability, int number_of_vertices) {
        bool connected = false;

        while (!connected) {
            genGraph(probability, number_of_vertices);
            connected = isConnected();
        }
        return graph;
    }

    std::vector<int> getEncoded_Graph_Rep() const {
        return Encoded_Graph_Rep;
    }

    std::vector<std::vector<int>> get_Graph() const {
        return graph;
    }

private:
    std::vector<int> Encoded_Graph_Rep;
    int num_of_vertices;
    double p;
    int max_number_of_edges;
    std::vector<std::vector<int>> graph;
    DFS dfs;

    void genGraph(double probability, int number_of_vertices) {
        num_of_vertices = number_of_vertices;
        graph.clear();
        graph.resize(num_of_vertices);
        p = probability;
        max_number_of_edges = (num_of_vertices * (num_of_vertices - 1)) / 2;
        Encoded_Graph_Rep.clear();

        int m = 7;
        std::vector<double> F_array(m + 1);

        for (int i = 0; i <= m; i++) {
            F_array[i] = F(i);
        }

        int i = -1;

        while (i < max_number_of_edges) {
            double alpha = static_cast<double>(rand()) / RAND_MAX;
            int k = 0;
            int j = 0;

            while (j <= m) {
                if (F_array[j] > alpha) {
                    k = j;
                    break;
                }
                j++;
            }
            if (j == (m + 1)) {
                k = static_cast<int>(ceil(log(1 - alpha) / log(1 - probability))) - 1;
            }
            i = i + k + 1;
            Encoded_Graph_Rep.push_back(i + 1);
        }
        Encoded_Graph_Rep.pop_back();
        convert_to_array_list_format_graph();
    }

    void convert_to_array_list_format_graph() {
        for (int i = 0; i < num_of_vertices; i++) {
            graph[i].clear();
        }
        for (int ind : Encoded_Graph_Rep) {
            auto edge = decode_undirected_no_self(ind);
            addEdge(graph, edge[0], edge[1]);
        }
    }

    std::vector<int> decode_undirected_no_self(int ind) {
        std::vector<int> edge(2);
        int i = static_cast<int>(ceil((-1 + sqrt(1 + 8 * ind)) / 2));
        int j = ind - ((i * (i - 1)) / 2) - 1;
        edge[0] = i;
        edge[1] = j;
        return edge;
    }

    void addEdge(std::vector<std::vector<int>>& graph, int i, int j) {
        graph[i].push_back(j);
        graph[j].push_back(i);
    }

    bool isConnected() {
        std::vector<bool> visited(num_of_vertices, false);
        dfs.run(0, graph, visited);
        for (bool v : visited) {
            if (!v) return false;
        }
        return true;
    }

    double F(int i) {
        return 1 - pow(1 - p, i + 1);
    }
};

#endif // ER_GRAPH_H
#ifndef STAR_GRAPH_H
#define STAR_GRAPH_H

#include <vector>

class Star_Graph {
public:
    std::vector<std::vector<int>> generate(int n) {
        genGraph(n);
        return graph;
    }

private:
    int num_of_vertices;
    std::vector<std::vector<int>> graph;

    void genGraph(int num_of_vertices) {
        this->num_of_vertices = num_of_vertices;
        graph.clear();
        graph.resize(num_of_vertices);

        for (int i = 1; i < num_of_vertices; i++) {
            addEdge(graph, 0, i);
        }
    }

    void addEdge(std::vector<std::vector<int>>& graph, int i, int j) {
        graph[i].push_back(j);
        graph[j].push_back(i);
    }
};

#endif // STAR_GRAPH_H
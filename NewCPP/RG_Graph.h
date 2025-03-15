#ifndef RG_GRAPH_H
#define RG_GRAPH_H

#include <vector>
#include <random>
#include <cmath>
#include "DFS.h"

class RG_Graph {
public:
    RG_Graph() : num_of_vertices(0), rand(std::random_device{}()) {}

    std::vector<std::vector<int>> generate(int n, double r) {
        bool connected = false;
        while (!connected) {
            genGraph(n, r);
            connected = isConnected();
        }
        return graph;
    }

private:
    struct Point {
        double x, y;
        Point(double x, double y) : x(x), y(y) {}
    };

    int num_of_vertices;
    double r;
    std::vector<std::vector<int>> graph;
    std::vector<Point> points;
    std::mt19937 rand;
    DFS dfs;

    void genGraph(int n, double r) {
        num_of_vertices = n;
        this->r = r;
        graph.clear();
        graph.resize(num_of_vertices);
        points.clear();

        std::uniform_real_distribution<> dis(0.0, 1.0);
        for (int i = 0; i < num_of_vertices; i++) {
            points.emplace_back(dis(rand), dis(rand));
        }

        for (int i = 0; i < num_of_vertices; i++) {
            for (int j = i + 1; j < num_of_vertices; j++) {
                if (distance(points[i], points[j]) <= r) {
                    addEdge(graph, i, j);
                }
            }
        }
    }

    double distance(const Point& p1, const Point& p2) {
        return std::sqrt(std::pow(p1.x - p2.x, 2) + std::pow(p1.y - p2.y, 2));
    }

    void addEdge(std::vector<std::vector<int>>& graph, int i, int j) {
        graph[i].push_back(j);
        graph[j].push_back(i);
    }

    bool isConnected() {
        std::vector<bool> visited(num_of_vertices, false);
        dfs.run(0, graph, visited);
        return std::all_of(visited.begin(), visited.end(), [](bool v) { return v; });
    }
};

#endif // RG_GRAPH_H
#ifndef BA_GRAPH_H
#define BA_GRAPH_H

#include <vector>
#include <random>
#include "DFS.h"
#include "ER_Graph.h"

class Linked_List_BA {
public:
    void append(int vertex, int edge) {
        list.push_back({vertex, edge});
    }

    std::vector<int> link_and_move(double rand_prob) {
        double sum = 0;
        for (auto& pair : list) {
            sum += pair.second;
            if (sum >= rand_prob) {
                return {pair.first, pair.second};
            }
        }
        return {0, 0}; // Should not reach here
    }

private:
    std::vector<std::pair<int, int>> list;
};

class BA_Graph {
public:
    BA_Graph() : m_0(0), m(0), end_size(0), sum_edges(0), rand(std::random_device{}()) {}

    std::vector<std::vector<int>> generate(int m_0, int m, int n) {
        bool connected = false;

        while (!connected) {
            genGraph(m_0, m, n);
            connected = isConnected();
        }
        return graph;
    }

    std::vector<std::vector<int>> genGraph(int m_0, int m, int n) {
        linked_list_ba = Linked_List_BA();
        sum_edges = 0;

        this->m_0 = m_0;
        this->m = m;
        this->end_size = n;

        // Create an initial empty graph
        graph.clear();
        graph.resize(m_0 + 1);
        // Since first vertex to be added is connected to all existing as m_0 = m
        for (int i = 0; i < m_0; i++) {
            add_edge(m_0, i);
        }

        // Populate list with starter values
        for (int i = 0; i < graph.size(); i++) {
            int edge = graph[i].size();
            linked_list_ba.append(i, edge);
            sum_edges += edge;
        }

        for (int i = m_0 + 1; i < end_size; i++) {
            graph.push_back(std::vector<int>());
            int new_edge_sum = 0;
            int sum_temp = sum_edges;

            int inner_m = 0;
            while (inner_m < m) {
                double rand_prob = rand() * sum_temp;
                std::vector<int> out = linked_list_ba.link_and_move(rand_prob);
                int linking_vertex = out[0];
                int remove_from_sum_temp = out[1];

                add_edge(linking_vertex, i);

                sum_temp -= remove_from_sum_temp;
                new_edge_sum += 2;

                inner_m++;
            }

            linked_list_ba.append(i, m);
            sum_edges += new_edge_sum;
        }
        return graph;
    }

    std::vector<std::vector<int>> getGraph() const {
        return graph;
    }

    Linked_List_BA getLinked_list_ba() const {
        return linked_list_ba;
    }

    int getSum_edges() const {
        return sum_edges;
    }

    std::vector<std::vector<int>> starter_graph(int m_0) {
        double isoperimetricDynamicity = (std::log(m_0) / std::log(2)) / 2;
        double probability = (2 * isoperimetricDynamicity) / m_0;

        ER_Graph myGen;
        return myGen.generate(probability, m_0);
    }

    void add_edge(int v_1, int v_2) {
        graph[v_1].push_back(v_2);
        graph[v_2].push_back(v_1);
    }

    bool isConnected() {
        std::vector<bool> visited(end_size, false);
        dfs.run(0, graph, visited);
        for (bool v : visited) {
            if (!v) return false;
        }
        return true;
    }

private:
    int m_0;
    int m;
    int end_size;
    int sum_edges;
    std::vector<std::vector<int>> graph;
    DFS dfs;
    Linked_List_BA linked_list_ba;
    std::mt19937 rand;
};

#endif // BA_GRAPH_H
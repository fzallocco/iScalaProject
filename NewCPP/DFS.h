#ifndef DFS_H
#define DFS_H

#include <vector>

class DFS {
public:
    DFS() {}

    void run(int current, const std::vector<std::vector<int>>& adjacencyList, std::vector<bool>& visited) {
        visited[current] = true;
        for (int neighbor : adjacencyList[current]) {
            if (!visited[neighbor]) {
                run(neighbor, adjacencyList, visited);
            }
        }
    }
};

#endif // DFS_H
#ifndef WRITE_GRAPH_H
#define WRITE_GRAPH_H

#include <fstream>
#include <vector>
#include <string>
#include <filesystem>

class Write_Graph {
public:
    Write_Graph() {}

    void write(const std::vector<std::string>& folder_names, const std::string& file_name, const std::vector<std::vector<int>>& graph, bool append) {
        std::filesystem::path path_name;
        for (const auto& folder : folder_names) {
            path_name /= folder;
        }
        std::filesystem::create_directories(path_name);

        std::ofstream file(path_name / (file_name + ".txt"), append ? std::ios::app : std::ios::out);
        if (!file.is_open()) {
            throw std::runtime_error("Unable to open file: " + (path_name / (file_name + ".txt")).string());
        }

        for (size_t j = 0; j < graph.size(); ++j) {
            for (size_t k = 0; k < graph[j].size(); ++k) {
                file << graph[j][k];
                if (k < graph[j].size() - 1) {
                    file << ",";
                } else if (j != graph.size() - 1) {
                    file << ";";
                } else {
                    file << "|";
                }
            }
        }
    }

    std::string getLocation() const {
        return location;
    }

private:
    std::string location;
};

#endif // WRITE_GRAPH_H
#ifndef READ_GRAPH_H
#define READ_GRAPH_H

#include <fstream>
#include <sstream>
#include <vector>
#include <unordered_map>
#include <string>

class Read_Graph {
public:
    Read_Graph() {}

    std::unordered_map<std::string, std::vector<std::vector<int>>> getGraphs() const {
        return graphs;
    }

    void read_graphs(const std::vector<std::string>& folder_names, const std::string& file_name) {
        std::string path_name;
        for (const auto& folder : folder_names) {
            path_name += folder + "/";
        }

        std::ifstream file(path_name + file_name + ".txt");
        if (!file.is_open()) {
            throw std::runtime_error("File not found: " + path_name + file_name + ".txt");
        }

        std::string whole_file;
        int graph_number = 0;
        while (std::getline(file, whole_file, '|')) {
            std::istringstream graph_stream(whole_file);
            graphs["graph_" + std::to_string(graph_number)] = std::vector<std::vector<int>>();
            std::string pair;
            int v_number = 0;
            while (std::getline(graph_stream, pair, ';')) {
                graphs["graph_" + std::to_string(graph_number)].emplace_back();
                std::istringstream row_stream(pair);
                std::string token;
                while (std::getline(row_stream, token, ':')) {
                    std::istringstream single_stream(token);
                    int single_item;
                    while (single_stream >> single_item) {
                        graphs["graph_" + std::to_string(graph_number)][v_number].push_back(single_item);
                        if (single_stream.peek() == ',') {
                            single_stream.ignore();
                        }
                    }
                }
                v_number++;
            }
            graph_number++;
        }
    }

private:
    std::unordered_map<std::string, std::vector<std::vector<int>>> graphs;
};

#endif // READ_GRAPH_H
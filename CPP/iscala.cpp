#include <vector>
#include <unordered_map>
#include <random>
#include <cmath>
#include <iostream>
#include <algorithm>

template <typename T>
class Node {
public:
    void swapValues(Node& other);
    void setK(int k);
    int getK() const;
    void setStatus(int status);
    int getStatus() const;
    void setPotential(double potential);
    double getPotential() const;
    void setRho(double rho);
    double getRho() const;
    bool getSupervisor() const;
};

template <typename T>
class ISCALA {
public:
    // status states
    static const int DONE = 0;
    static const int PROBING = 1;
    static const int HIGH = 2;
    static const int LOW = 3;

    // epsilon - ϵ > 0
    double EPSILON = 0.001;

    // swap references
    static void SwapArrays(std::vector<Node<T>>& SwapFrom, std::vector<Node<T>>& SwapTo) {
        int lengthOfArray = SwapFrom.size();
        for (int i = 0; i < lengthOfArray; i++) {
            SwapFrom[i].swapValues(SwapTo[i]);
        }
    }

    // ISCALA algorithm
    // returns [<target value>, <rounds taken>]
    std::vector<long> ISCALA_algo(int l, double isoDynamicity, std::vector<Node<T>>& nodes, std::unordered_map<std::string, std::vector<std::vector<int>>>& graphs, int num_graphs) {
        std::vector<std::vector<int>> adjList;
        int Status_Global;
        int ceiling = -1;
        int floor = -1;
        long rounds = 0;
        int epoch = 0;
        int phases;

        std::vector<Node<T>> Nodes_Main(nodes.size());
        std::vector<Node<T>> Nodes_Utility(nodes.size());

        CopyArray(nodes, Nodes_Utility);

        for (auto& value : Nodes_Utility) {
            value.setK(l + 1);
        }

        CopyArray(Nodes_Utility, Nodes_Main);

        int n = nodes.size();
        int stop_counter_rounds_low = 0;

        do {
            epoch++;
            Status_Global = PROBING;

            for (int i = 0; i < Nodes_Main.size(); i++) {
                Nodes_Utility[i].setStatus(PROBING);

                if (Nodes_Main[i].getSupervisor()) {
                    Nodes_Utility[i].setPotential(0);
                    Nodes_Utility[i].setRho(0);
                } else {
                    Nodes_Utility[i].setPotential(l);
                }
            }

            SwapArrays(Nodes_Utility, Nodes_Main);

            int k = Nodes_Main[0].getK();
            double Kappa = this->Kappa(k);
            double d = this->d(k);
            double P = this->P(l, k);
            double tao = this->Tao(l, k);
            double gamma = this->Gamma(k);

            double sum;
            double numberTobeAddedToSum = std::pow((isoDynamicity / d), 2);

            for (phases = 1; phases <= P; phases++) {
                sum = 0;

                do {
                    stop_counter_rounds_low = 0;
                    int randomNum = std::rand() % num_graphs;
                    adjList = graphs["graph_" + std::to_string(randomNum)];
                    rounds++;
                    sum += numberTobeAddedToSum;

                    std::vector<Node<T>> setNeighbours;
                    int all_received_status;
                    int num_neighbours;

                    for (int current_node = 0; current_node < Nodes_Main.size(); current_node++) {
                        all_received_status = PROBING;
                        setNeighbours.clear();
                        num_neighbours = 0;

                        for (int i = 0; i < adjList[current_node].size(); i++) {
                            setNeighbours.push_back(Nodes_Main[adjList[current_node][i]]);
                            num_neighbours++;
                        }

                        for (auto& setNeighbour : setNeighbours) {
                            if (setNeighbour.getStatus() != PROBING) {
                                all_received_status = setNeighbour.getStatus();
                                break;
                            }
                        }

                        double sum_neighbours_potential = 0;
                        for (auto& setNeighbour : setNeighbours) {
                            sum_neighbours_potential += (setNeighbour.getPotential() / d);
                        }

                        if ((Nodes_Main[current_node].getStatus() == PROBING) && (all_received_status == PROBING) && (num_neighbours <= (d - 1))) {
                            Nodes_Utility[current_node].setPotential(Nodes_Main[current_node].getPotential() + sum_neighbours_potential - ((num_neighbours * Nodes_Main[current_node].getPotential()) / d));
                        } else {
                            Nodes_Utility[current_node].setPotential(l);
                            Nodes_Utility[current_node].setStatus(LOW);
                            Status_Global = LOW;
                            stop_counter_rounds_low++;
                        }
                    }

                    SwapArrays(Nodes_Utility, Nodes_Main);
                } while (sum < Kappa && stop_counter_rounds_low < n);

                if (stop_counter_rounds_low > n - 1) {
                    break;
                }

                for (int i = 0; i < Nodes_Main.size(); i++) {
                    if ((phases == 1) && (Nodes_Main[i].getPotential() > tao)) {
                        Status_Global = LOW;
                        Nodes_Utility[i].setStatus(LOW);
                        Nodes_Utility[i].setPotential(l);
                    }
                    if ((Nodes_Main[i].getSupervisor()) && (Nodes_Main[i].getStatus() == PROBING)) {
                        Nodes_Utility[i].setRho(Nodes_Main[i].getRho() + Nodes_Main[i].getPotential());
                        Nodes_Utility[i].setPotential(0);
                    }
                }

                SwapArrays(Nodes_Utility, Nodes_Main);
            }

            if (stop_counter_rounds_low < n) {
                for (int i = 0; i < Nodes_Main.size(); i++) {
                    if ((Nodes_Main[i].getSupervisor()) && (Nodes_Main[i].getStatus() == PROBING)) {
                        if ((((k - l) * (1 - std::pow(k, (-1 * gamma)))) <= Nodes_Main[i].getRho()) && (((k - l) * (1 + std::pow(k, (-1 * gamma)))) >= Nodes_Main[i].getRho())) {
                            Nodes_Utility[i].setStatus(DONE);
                            Status_Global = DONE;
                        } else if ((((k - l) * (1 - std::pow(k, (-1 * gamma)))) < Nodes_Main[i].getRho())) {
                            Nodes_Utility[i].setStatus(LOW);
                            Status_Global = LOW;
                        } else if ((((k - l) * (1 - std::pow(k, (-1 * gamma)))) > Nodes_Main[i].getRho())) {
                            Nodes_Utility[i].setStatus(HIGH);
                            Status_Global = HIGH;
                        }
                    }
                }
            }

            SwapArrays(Nodes_Utility, Nodes_Main);

            for (int i = 0; i < d; i++) {
                int randomNum = std::rand() % num_graphs;
                adjList = graphs["graph_" + std::to_string(randomNum)];
                rounds++;
                stop_counter_rounds_low = 0;
                int some_received_status;

                for (int node = 0; node < Nodes_Main.size(); node++) {
                    if (!Nodes_Main[node].getSupervisor()) {
                        std::vector<int> neighbours = adjList[node];
                        for (int neighbour : neighbours) {
                            if (Nodes_Main[neighbour].getStatus() != PROBING) {
                                some_received_status = Nodes_Main[neighbour].getStatus();
                                Nodes_Utility[node].setStatus(some_received_status);
                                Status_Global = some_received_status;
                                stop_counter_rounds_low++;
                                break;
                            }
                        }
                    }
                }

                if (stop_counter_rounds_low < n) {
                    break;
                }
            }

            SwapArrays(Nodes_Utility, Nodes_Main);
            std::cout << "K value: " << Nodes_Main[0].getK() << std::endl;

            if (Status_Global != DONE) {
                int newValue;
                if (ceiling != -1 && floor != -1) {
                    if (Status_Global == LOW) {
                        floor = k;
                    }
                    if (Status_Global == HIGH) {
                        ceiling = k;
                    }
                    newValue = floor + ((ceiling - floor) / 2);
                    Nodes_Utility[0].setK(newValue);
                }
                if (ceiling == -1 || floor == -1) {
                    if (Status_Global == LOW) {
                        Nodes_Utility[0].setK(k * 2);
                    }
                    if (Status_Global == HIGH) {
                        ceiling = k;
                        floor = k / 2;
                        newValue = floor + ((ceiling - floor) / 2);
                        Nodes_Utility[0].setK(newValue);
                    }
                }
            }

            SwapArrays(Nodes_Utility, Nodes_Main);
            std::cout << "Number of epochs: " << epoch << std::endl;
            std::cout << "Status_Global " << Status_Global << std::endl;
            std::cout << "Floor " << floor << " Ceiling " << ceiling << std::endl;
            std::cout << "Number of rounds: " << rounds << " K value: " << Nodes_Main[0].getK() << std::endl;
            std::cout << "_________________________________________________________________" << std::endl;
        } while (Status_Global != DONE);

        return {Nodes_Main[0].getK(), rounds};
    }

    double P(int l, int k) {
        double d = this->d(k);
        return std::ceil((4 * d * std::log(d)) / l);
    }

    double d(int k) {
        return (2 * std::pow(k, 1 + EPSILON));
    }

    double Tao(int l, int k) {
        double denominator = std::pow(k, (1 + EPSILON));
        return l * (1 - (l / denominator));
    }

    double Kappa(int k) {
        double d = this->d(k);
        double innerTerm = (2 * std::pow(d, 2.67) + 1);
        double n = std::log(d * innerTerm) / std::log(2);
        return 2 * n;
    }

    double Gamma(int k) {
        double d = this->d(k);
        return (std::log(d) / std::log(k));
    }

    double Delta(int k) {
        double d = this->d(k);
        double delta = std::log(std::pow(d, 2)) / std::log(k);
        return delta + EPSILON;
    }

    double Alpha(int k) {
        double gamma = this->Gamma(k);
        double thirdTerm = std::log(3) / std::log(k);
        return 1 + gamma + thirdTerm;
    }

    double Beta(int k) {
        double d = this->d(k);
        double delta = this->Delta(k);
        double innerTerm = (2 * std::pow(k, delta) + 1);
        double n = d * innerTerm;
        return std::log(n) / std::log(k);
    }

    void CopyArray(const std::vector<Node<T>>& CopyFrom, std::vector<Node<T>>& CopyTo) {
        int lengthOfArray = CopyFrom.size();
        for (int i = 0; i < lengthOfArray; i++) {
            Node<T> temp(CopyFrom[i]);
            CopyTo[i] = temp;
        }
    }
};
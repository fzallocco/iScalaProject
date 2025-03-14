// Node.h
#ifndef NODE_H
#define NODE_H

template <typename T>
class Node {
public:
    Node(bool isSupervisor) : isSupervisor(isSupervisor), potential(0), k(0), status(0), rho(0) {}
    Node(const Node& other) : isSupervisor(other.isSupervisor), potential(other.potential), k(other.k), status(other.status), rho(other.rho) {}

    void swapValues(Node& other) {
        std::swap(status, other.status);
        std::swap(isSupervisor, other.isSupervisor);
        std::swap(rho, other.rho);
        std::swap(k, other.k);
        std::swap(potential, other.potential);
    }

    void setK(int k) { this->k = k; }
    int getK() const { return k; }
    void setStatus(int status) { this->status = status; }
    int getStatus() const { return status; }
    void setPotential(double potential) { this->potential = potential; }
    double getPotential() const { return potential; }
    void setRho(double rho) { this->rho = rho; }
    double getRho() const { return rho; }
    bool getSupervisor() const { return isSupervisor; }
    void setSupervisor(bool supervisor) { this->isSupervisor = supervisor; }

private:
    int k;
    int status;
    double potential;
    double rho;
    bool isSupervisor;
};

#endif // NODE_H
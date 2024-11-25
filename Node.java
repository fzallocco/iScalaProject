//basic node for ISCALA
//current working idea
//node of specific topologies will extend this class

public class Node<T> {

    //isSupervisor refers to supervisor or supervised
    private boolean isSupervisor;

    //Φ - potential of node
    private double potential;

    //initial prediction for k and k
    private int k;

    //status array can be int with values 0 - done, 1 - probing, 2 - low, 3 - high
    private int status;

    //rho
    private double rho;

    //regular constructor
    //for supervisor nodes potential will be 0 to start.
    Node(boolean isSupervisor) {
        this.isSupervisor = isSupervisor;
    }

    //copy constructor
    Node(Node other) {
        this.status = other.status;
        this.isSupervisor = other.isSupervisor;
        this.rho = other.rho;
        this.k = other.k;
        this.potential = other.potential;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int newStatus) {
        this.status = newStatus;
    }

    public int getK() {
        return k;
    }

    public void setK(int newK) {
        this.k = newK;
    }

    public boolean getSupervisor() {
        return isSupervisor;
    }

    public void setSupervisor(boolean isSupervisor) {
        this.isSupervisor = isSupervisor;
    }

    public double getPotential() {
        return potential;
    }

    public void setPotential(double newPotential) {
        this.potential = newPotential;
    }

    public double getRho() {
        return rho;
    }

    public void setRho(double newRho) {
        this.rho = newRho;
    }

    public void swapValues(Node other) {

        other.status = this.status;
        other.isSupervisor = this.isSupervisor;
        other.rho = this.rho;
        other.k = this.k;
        other.potential = this.potential;

//        this.status = other.status;
//        this.isSupervisor = other.isSupervisor;
//        this.rho = other.rho;
//        this.k = other.k;
//        this.potential = other.potential;

    }
}

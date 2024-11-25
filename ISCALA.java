//perhaps something like BFS or DFS for visiting each node?


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ISCALA<T> {

    // status states
    final int DONE = 0;
    final int PROBING = 1;
    final int HIGH = 2;
    final int LOW = 3;

    //epsilon - ϵ > 0
    double EPSILON = 0.001;

    //swap references
    public static void SwapArrays(Node[] SwapFrom, Node[] SwapTo) {
        int lengthOfArray = SwapFrom.length;
        for (int i = 0; i < lengthOfArray; i++) {
            SwapFrom[i].swapValues(SwapTo[i]);
        }
    }

    //Theorem 6.5

    //ISCALA algorithm
    //returns [<target value>, <rounds taken>]
    public long[] ISCALA_algo(int l, double isoDynamicity, Node<?>[] nodes, HashMap<String, List<ArrayList<Integer>>> graphs, int num_graphs) {

        List<ArrayList<Integer>> adjList;
        //variable declarations

        //status can be int with values 0 - done, 1 - probing, 2 - high, 3 - low
        int Status_Global;

        //variables for binary search
        int ceiling = -1;
        int floor = -1;

        // number of rounds
        long rounds = 0;
        //number of epochs
        int epoch = 0;
        // number of phases
        int phases;

        //declare 2 arrays of Node type one for new and one for swap purposes
        Node<?>[] Nodes_Main = new Node[nodes.length];
        Node<?>[] Nodes_Utility = new Node[nodes.length];

        //copy over array
        CopyArray(nodes, Nodes_Utility);


        //set predicted value
        for (Node<?> value : Nodes_Utility) {
            value.setK(l + 1);
        }

        //copy over array
        // CopyArray(From, To);
        CopyArray(Nodes_Utility, Nodes_Main);

        //n
        int n = nodes.length;

        ///early stop
        int stop_counter_rounds_low = 0;

        //repeat - epochs
        do {
            epoch++;

            Status_Global = PROBING;

            int Nodes_Main_Length = Nodes_Main.length;

            for (int i = 0; i < Nodes_Main_Length; i++) {

                // set status to all nodes to PROBING
                Nodes_Utility[i].setStatus(PROBING);

                //set  and  to 0 if supervisor
                if (Nodes_Main[i].getSupervisor()) {
                    Nodes_Utility[i].setPotential(0);
                    Nodes_Utility[i].setRho(0);
                }

                //set Φ to l if supervised
                else {
                    Nodes_Utility[i].setPotential(l);
                }

            }

            //copy over array
            // CopyArray(From, To);
            SwapArrays(Nodes_Utility, Nodes_Main);

            //Variables to be calculated once per epoch
            //Goal - Speed Up
            int k = Nodes_Main[0].getK();
            double Kappa = Kappa(k);
            double d = d(k);
            double P = P(l, k);
            double tao = Tao(l, k);
            double gamma = Gamma(k);

            double sum;

            double numberTobeAddedToSum = Math.pow((isoDynamicity / d), 2);

            for (phases = 1; phases <= P; phases++) {
                //Variables to be calculated once per phase
                //Goal - Speed Up

                sum = 0;

                do {
                    //early stopping mechanism
                    //reset stop counter
                    stop_counter_rounds_low = 0;

                    int randomNum = ThreadLocalRandom.current().nextInt(0, num_graphs);
                    adjList = graphs.get("graph_" + randomNum);

                    rounds++;


                    sum = sum + numberTobeAddedToSum;

                    //broadcast and receive potential Φ and status

                    //create a node array list to hold the neighbours
                    ArrayList<Node> setNeighbours = new ArrayList<>();

                    //create var to hold all_received_status
                    int all_received_status;

                    // int to hold number of neighbours
                    int num_neighbours;

                    for (int current_node = 0; current_node < Nodes_Main_Length; current_node++) {
                        all_received_status = PROBING;
                        setNeighbours.clear();
                        num_neighbours = 0;

                        //size of current adjList
                        int sizeList = adjList.get(current_node).size();
                        for (int i = 0; i < sizeList; i++) {
                            setNeighbours.add(Nodes_Main[adjList.get(current_node).get(i)]);
                            num_neighbours++;
                        }


                        //go over the neighbours that transmitted to the given node and check if all are PROBING
                        int setOfNeighboursSize = setNeighbours.size();

                        for (Node setNeighbour : setNeighbours) {
                            if (setNeighbour.getStatus() != PROBING) {
                                all_received_status = setNeighbour.getStatus();
                                break;
                            }
                        }

                        //calculate sum of neighbours' potentials over d
                        double sum_neighbours_potential = 0;
                        for (Node setNeighbour : setNeighbours) {
                            sum_neighbours_potential += (setNeighbour.getPotential() / d);
                        }

                        if (
                                (Nodes_Main[current_node].getStatus() == PROBING) &&
                                        (all_received_status == PROBING) &&
                                        (num_neighbours <= (d - 1))
                        ) {

                            Nodes_Utility[current_node].setPotential(Nodes_Main[current_node].getPotential() + sum_neighbours_potential -
                                    ((num_neighbours * Nodes_Main[current_node].getPotential()) / d));

                        } else {
                            Nodes_Utility[current_node].setPotential(l);
                            Nodes_Utility[current_node].setStatus(LOW);
                            Status_Global = LOW;
                            stop_counter_rounds_low++;
                        }
                    }

                    //copy over array
                    // CopyArray(From, To);
                    SwapArrays(Nodes_Utility, Nodes_Main);
                }
                //early stopping mechanism
                while (sum < Kappa && stop_counter_rounds_low < n);

                //early stopping mechanism
                if (stop_counter_rounds_low > n - 1) {
                    break;
                }

                //check if counter is less than n

                for (int i = 0; i < Nodes_Main_Length; i++) {

                    if
                    (
                            (phases == 1) && (Nodes_Main[i].getPotential() > tao)
                    ) {
                        Status_Global = LOW;
                        Nodes_Utility[i].setStatus(LOW);
                        Nodes_Utility[i].setPotential(l);
                    }
                    if
                    (
                            (Nodes_Main[i].getSupervisor()) && (Nodes_Main[i].getStatus() == PROBING)
                    ) {
                        Nodes_Utility[i].setRho(Nodes_Main[i].getRho() + Nodes_Main[i].getPotential());
                        Nodes_Utility[i].setPotential(0);
                    }
                }

                //copy over array
                // CopyArray(From, To);
                SwapArrays(Nodes_Utility, Nodes_Main);

            }
            //early stopping mechanism
            if (stop_counter_rounds_low < n) {

                for (int i = 0; i < Nodes_Main_Length; i++) {

                    if ((Nodes_Main[i].getSupervisor()) && (Nodes_Main[i].getStatus() == PROBING)) {
                        if (
                                (((k - l) * (1 - Math.pow(k, (-1 * gamma)))) <= Nodes_Main[i].getRho())
                                        &&
                                        (((k - l) * (1 + Math.pow(k, (-1 * gamma)))) >= Nodes_Main[i].getRho())
                        ) {
                            Nodes_Utility[i].setStatus(DONE);    //k = n
                            Status_Global = DONE;
//                        System.exit(0);
                        } else if ((((k - l) * (1 - Math.pow(k, (-1 * gamma)))) < Nodes_Main[i].getRho())) {
                            Nodes_Utility[i].setStatus(LOW);
                            Status_Global = LOW;
                        } else if ((((k - l) * (1 - Math.pow(k, (-1 * gamma)))) > Nodes_Main[i].getRho())) {
                            Nodes_Utility[i].setStatus(HIGH);
                            Status_Global = HIGH;

                        }
                    }
                }
            }

            //copy over array
            // CopyArray(From, To);
            SwapArrays(Nodes_Utility, Nodes_Main);

            //for d rounds do broadcast and receive - not sure why the for d roundsPerPhase


            for (int i = 0; i < d; i++) {
                // add graph change here and increment rounds
                int randomNum = ThreadLocalRandom.current().nextInt(0, num_graphs);
                adjList = graphs.get("graph_" + randomNum);
                rounds++;

                //here early stopping
                stop_counter_rounds_low = 0;

                // make changes for adjlist benefits and also move supervise check to first position so that we safe time.
                int some_received_status;


//              Go over AlL nodes and check if supervisor if so check for neibour status
                for (int node = 0; node < Nodes_Main_Length; node++) {
                    if (!Nodes_Main[node].getSupervisor()) {
                        ArrayList<Integer> neibours = adjList.get(node);
//                      loop over neighbours and check status
                        for (int neighbours = 0; neighbours < neibours.size(); neighbours++) {
//                          check status of neighbours
                            if (Nodes_Main[neighbours].getStatus() != PROBING) {
                                some_received_status = Nodes_Main[neighbours].getStatus();
                                Nodes_Utility[node].setStatus(some_received_status);
                                Status_Global = some_received_status;
                                stop_counter_rounds_low++;
                                break;
                            }
                        }
                    }
                }

                //early stopping mechanism
                if (stop_counter_rounds_low < n) {
                    break;
                }
            }

            //copy over array
            SwapArrays(Nodes_Utility, Nodes_Main);
            System.out.println("K value: " + Nodes_Main[0].getK());
            //update k with binary search
            if (Status_Global != DONE) {

                int newValue;
                if (ceiling != -1 && floor != -1) {
                    if (Status_Global == LOW) {
                        floor = k;
                    }
                    if (Status_Global == HIGH) {
                        ceiling = k;
//                        System.exit(0);
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

            //copy over array
            // CopyArray(From, To);
            SwapArrays(Nodes_Utility, Nodes_Main);
            System.out.println("Number of epochs: " + epoch);
            System.out.println("Status_Global " + Status_Global);
            System.out.println("Floor " + floor + " Celing " + ceiling);
            System.out.println("Number of rounds: " + rounds + " K value: " + Nodes_Main[0].getK());
            System.out.println("_________________________________________________________________");
            //update the k value of all Nodes in Utility
            //no current need to do so but for good practice(to slower) may iterate through array Nodes_Utility and update all k values
        }
        while (Status_Global != DONE);

        return new long[]{Nodes_Main[0].getK(), rounds};
    }

    //p = ceil[(2 ln(k))/l MAX{ gamma/1/k+1/k^(alpha) , delta/1/d+1/k^(brta)} ]
    // Math tested and ok
    public double P(int l, int k) {
        double d = d(k);

        return Math.ceil((4 * d * Math.log(d)) / (l));
    }

    //d = 2k^(1+ϵ)
    // Math tested and ok
    public double d(int k) {
        return (2 * Math.pow(k, 1 + EPSILON));
    }

    //tao - τ = l(1 - (l/k^(1+ϵ)))
    // Math tested and ok
    public double Tao(int l, int k) {
        double denominator = Math.pow(k, (1 + EPSILON));

        return l * (1 - (l / denominator));
    }

    //kappa
    // Math tested and ok
    public double Kappa(int k) {

        double d = d(k);

        double innerTerm = (2 * Math.pow(d(k), 2.67) + 1);

        double n = Math.log(d(k) * innerTerm) / Math.log(2);
        return 2 * n;

    }

    //Gamma - γ > log base k of (d − 1)
    // Math tested and ok
    public double Gamma(int k) {
        double d = d(k);
        return (Math.log(d) / Math.log(k));
    }

    //Delta - δ > log(base k)((dk^y))
    // Math tested and ok
    public double Delta(int k) {

        double d = d(k);

        double delta = Math.log(Math.pow(d, 2)) / Math.log(k);
        return delta + EPSILON;
    }

    //Alpha - α >= 1 + γ + (Math.log(3) / Math.log(k))
    // Math tested and ok
    public double Alpha(int k) {
        double gamma = Gamma(k);

        double thirdTerm = Math.log(3) / Math.log(k);

        return 1 + gamma + thirdTerm;
    }

    //Beta β ≥ log(base k)(d(2k^δ + 1))
    // Math tested and ok
    public double Beta(int k) {
        double d = d(k);
        double delta = Delta(k);

        double innerTerm = (2 * Math.pow(k, delta) + 1);
        double n = d * innerTerm;

        return Math.log(n) / Math.log(k);
    }

    //method to copy over array
    public void CopyArray(Node[] CopyFrom, Node[] CopyTo) {
        int lengthOfArray = CopyFrom.length;
        for (int i = 0; i < lengthOfArray; i++) {
            Node temp = new Node<T>(CopyFrom[i]);
            CopyTo[i] = temp;
        }
    }

    //under the following conditions
}

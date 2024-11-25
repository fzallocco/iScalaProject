public class Linked_List_BA {

    Node_L head, tail;

    // Add a node at the end of the list
    void append(int new_vertex, int new_edge_count) {
        Node_L new_node = new Node_L(new_vertex, new_edge_count);

        new_node.next = null;

        if (head == null) {
            new_node.prev = null;
            head = new_node;
            tail = head;
            return;
        } else {
            insert_at_tail(new_node);
        }
    }

    void insert_at_tail(Node_L new_node) {
        tail.next = new_node;
        new_node.prev = tail;
        new_node.next = null;
        tail = new_node;
    }

    void remove(Node_L node_to_remove) {
        if (node_to_remove.prev != null){
        node_to_remove.prev.next = node_to_remove.next;
        }
        if (node_to_remove.next != null) {
            node_to_remove.next.prev = node_to_remove.prev;
        }
        if (head == node_to_remove) {
            head = node_to_remove.next;
        }
        if (tail == node_to_remove) {
            tail = node_to_remove.prev;
        }
    }

    Node_L find_vertex(int vertex_value) {
        Node_L node_out = head;
        while (node_out.next != null) {
            if (node_out.vertex == vertex_value){
                break;
            }
            node_out = node_out.next;
        }
        return node_out;
    }

    int[] link_and_move(double prob){
        Node_L node_to_move = head;
        int min = 0, max = 0;
        while (node_to_move.next != null) {
            min = max;
            max += node_to_move.edges_count;
            if (min <= prob && prob < max){
                break;
            }
            node_to_move = node_to_move.next;
        }
        remove(node_to_move);
        int[] out = new int[]{node_to_move.vertex, node_to_move.edges_count};
        node_to_move.edges_count++;
        insert_at_tail(node_to_move);
        return out;
    }

    public void printlist() {
        Node_L node = head;
        Node_L last = null;
        System.out.println("Traversal in forward Direction");
        while (node != null) {
            System.out.print("(" + node.vertex + ", " + node.edges_count + ")");
            last = node;
            node = node.next;
        }
        System.out.println();
    }

    static class Node_L {

        int vertex;
        int edges_count;
        Node_L next;
        Node_L prev;

        // Node
        Node_L(int vertex, int edges_count) {
            this.vertex = vertex;
            this.edges_count = edges_count;
        }

        @Override
        public String toString() {
            return "\nNode_L{" +
                    "vertex=" + vertex +
                    ", edges_count=" + edges_count +
                    '}';
        }
    }
}

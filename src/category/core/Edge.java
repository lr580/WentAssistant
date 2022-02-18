package category.core;

public class Edge {
    public int to = 0;
    public int next = 0;

    public Edge() {
    }

    public Edge(int to, int next) {
        this.to = to;
        this.next = next;
    }
}

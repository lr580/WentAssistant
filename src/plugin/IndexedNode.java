package plugin;

public class IndexedNode implements Comparable<IndexedNode> {
    public int i = 0, v = 0;

    public IndexedNode() {
    }

    public IndexedNode(int i, int v) {
        this.i = i;
        this.v = v;
    }

    @Override
    public int compareTo(IndexedNode o) {
        return v < o.v ? -1 : (v > o.v ? 1 : 0);
    }
}

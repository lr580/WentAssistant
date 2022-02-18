package plugin;

public class IndexedPair extends Pair {
    public int i = 0;

    public IndexedPair() {
    }

    public IndexedPair(int x, int y, int i) {
        super(x, y);
        this.i = i;
    }
}

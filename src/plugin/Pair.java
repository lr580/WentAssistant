package plugin;

public class Pair implements Comparable<Pair> {
    public int x = 0, y = 0;

    public Pair() {
    }

    public Pair(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int compareTo(Pair o) {
        if (x < o.x) {
            return -1;
        } else if (x > o.x) {
            return 1;
        } else {
            return y < o.y ? -1 : (y > o.y ? 1 : 0);
        }
    }
}

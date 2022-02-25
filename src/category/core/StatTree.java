package category.core;

// import java.util.List;

// public class StatTree<T extends Number> extends CatTree {
public class StatTree extends CatTree {
    // public List<NodeD> p = null;

    public StatTree(String src, boolean weighted) {
        super(src, weighted);
        // super.p
    }

    public void cntNode(int u, Double dx) {
        // NodeD nd = p.get(u);
        // nd.w += dx;
        // p.set(u, nd);
    }
}

package category.core;

// public class NodeT<T extends Number> {
public class NodeT<T> {
    public String name = "";
    public T w;// 点权
    public int father = 0;// 父节点

    public NodeT() {
    }

    public NodeT(T wei, int fa, String nam) {
        w = wei;
        father = fa;
        name = nam;
    }

    @Override
    public String toString() {
        return w + " " + father + " " + name;
    }
}

package category.core;

// import java.util.List;

public class Node {
    public String name = "";
    // public int i = 0;// 编号
    public int w = 0;// 点权
    public int father = 0;// 父节点
    // public List<Integer> son = null;// 子节点

    public Node() {
    }

    public Node(int wei, int fa, String nam) {
        // i = idx;
        w = wei;
        father = fa;
        name = nam;
    }

    @Override
    public String toString() {
        return w + " " + father + " " + name;
    }

    // public static void main(String[] args) {
    // String a = "abc";
    // String b = a;
    // a = "def";
    // System.out.println(a + b);
    // }
}

package category.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.HashMap;
import plugin.FileHelper;
import plugin.Pair;

public class CatTree {
    public int n = 0;// 点数
    public int cnt = 0;// 有向边数
    public List<Node> p = null;// 点
    public List<Edge> e = null;// 边(链式前向星)
    public List<Integer> hd = null;// 头结点(链式前向星)
    public int top = 0;// 最大点编号
    private int root = 1;// 根节点编号
    public HashMap<String, Integer> h;// 名字-编号对应表

    private void addedge(int u, int v) {
        ++cnt;
        Edge ed = new Edge(v, hd.get(u));
        e.add(ed);
        hd.set(u, cnt);
    }

    public void build(String src, boolean weighted) {
        Scanner sc = new Scanner(src);
        n = sc.nextInt();
        top = sc.nextInt();
        p = new ArrayList<>();
        e = new ArrayList<>();
        hd = new ArrayList<>();
        h = new HashMap<>();
        e.add(null);
        for (int i = 0; i <= top; ++i) {
            p.add(null);
            hd.add(0);
        }

        int hasW = weighted ? 1 : 0;
        for (int i = 0; i < n; ++i) {
            int idx = sc.nextInt();
            int w = sc.nextInt();
            int fa = sc.nextInt();
            if (fa == 0) {
                root = idx;
            }
            String name = sc.next();
            Node nd = new Node(w * hasW, fa, name);
            p.set(idx, nd);
            addedge(idx, fa);
            addedge(fa, idx);
            h.put(name, idx);
        }
        sc.close();
    }

    public CatTree() {
    }

    public CatTree(String str) {
        build(str, true);
    }

    public CatTree(String str, boolean w) {
        build(str, w);
    }

    public String export() {
        StringBuilder sb = new StringBuilder(n + " " + top + "\n");
        for (int i = 1; i <= top; ++i) {
            Node nd = p.get(i);
            if (nd != null) {
                sb.append(i + " " + nd + "\n");
            }
        }
        return sb.toString();
    }

    public int[] sum = null;
    public StringBuilder res = null;
    public TreeSet<Pair> sons[] = null;

    private int dfs_sum(int u) {
        int cnt = p.get(u).w;
        for (int i = hd.get(u); i != 0; i = e.get(i).next) {
            int v = e.get(i).to;
            if (v == 0 || v == p.get(u).father) {
                continue;
            }
            int nw = dfs_sum(v);
            sons[u].add(new Pair(nw, v));
            cnt += nw;
        }
        sum[u] = cnt;
        return cnt;
    }

    private void dfs_sumorder(int u, int d) {
        Iterator<Pair> it = sons[u].iterator();
        while (it.hasNext()) {
            Pair pr = it.next();
            dfs_sumorder(pr.y, d + 1);
        }
        String s = "";
        for (int i = 0; i < d; ++i) {
            s += "...";
        }
        s += p.get(u).name;
        s += ": " + sum[u] + "\n";
        res.insert(0, s);
    }

    @SuppressWarnings("unchecked")
    public String stat_sum() {// Wrong
        res = new StringBuilder();
        sum = new int[top + 1];
        sons = new TreeSet[top + 1];
        for (int i = 1; i <= top; ++i) {
            sons[i] = new TreeSet<Pair>();
        }
        dfs_sum(root);
        dfs_sumorder(root, 0);
        return res.toString();
    }

    boolean isAncestorOf(int u, int v) {// u是否是v的祖先(或u=v)
        // if (u == v) {
        // return true;
        // }
        // int fa = p.get(v).father;
        // if (fa == 0) {
        // return false;
        // }
        // return isAncestorOf(u, fa); 递归效率低下
        for (; v != 0; v = p.get(v).father) {
            if (u == v) {
                return true;
            }
        }
        return false;
    }

    public int find(String s) {
        try {
            return h.get(s);
        } catch (NullPointerException e) {
            return 0;
        }
    }

    public void addNode(String name, int fa, int w) {
        ++n;
        int idx = ++top;
        Node nd = new Node(w, fa, name);
        p.add(nd);
        hd.add(0);
        addedge(idx, fa);
        addedge(fa, idx);
        h.put(name, idx);
    }

    public void addNode(String name, int fa) {
        addNode(name, fa, 0);
    }

    public void editNode(int idx, String name, int fa, int w) {
        Node nd = new Node(w, fa, name);
        p.set(idx, nd);
    }

    public void deleteNode(int u) {
        // List<Integer> dels = new LinkedList<>();
        for (int i = hd.get(u); i != 0; i = e.get(i).next) {
            int v = e.get(i).to;
            if (v != p.get(u).father) {
                deleteNode(v);
            }
            e.set(i, new Edge(0, e.get(i).next));
            int j = ((i + 1) ^ 1) - 1;
            e.set(j, new Edge(0, e.get(j).next));
        }
        // Iterator<Integer> it = dels.iterator();
        // while (it.hasNext()) {
        // int idx = it.next();
        // e.remove(idx);
        // }
        --n;
        h.remove(p.get(u).name);
        hd.set(u, 0);
        p.set(u, null);
    }

    // public static void main(String[] args) {// 测试用例
    // String tx = FileHelper.read("a.txt");
    // System.out.println(tx);
    // CatTree tr = new CatTree(tx);
    // tr.addNode("未知", 3, 4);
    // tr.addNode("已知", 2, 20);
    // tr.editNode(1, "total", 0, 100);
    // tr.deleteNode(2);
    // tr.deleteNode(3);
    // tr.deleteNode(5);
    // System.out.println(tr.export());
    // System.out.println(tr.stat_sum());
    // System.out.println(tr.isAncestorOf(1, 1));
    // System.out.println(tr.isAncestorOf(1, 5));
    // System.out.println(tr.isAncestorOf(2, 5));
    // System.out.println(tr.isAncestorOf(2, 3));
    // System.out.println(tr.isAncestorOf(3, 5));
    // System.out.println(tr.isAncestorOf(5, 1));
    // System.out.println(tr.find("A类"));
    // System.out.println(tr.find("还好"));
    // }
}
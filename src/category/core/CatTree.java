package category.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.TreeSet;
import plugin.FileHelper;
import plugin.IndexedPair;

public class CatTree {
    public int n = 0;// 点数
    public int cnt = 0;// 有向边数
    public List<Node> p = null;// 点
    public List<Edge> e = null;// 边(链式前向星)
    public List<Integer> hd = null;// 头结点(链式前向星)
    public int top = 0;// 最大点编号
    private int root = 1;// 根节点编号

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
        // System.out.println(n);
        // System.out.println(top);
        p = new ArrayList<>();
        e = new ArrayList<>();
        hd = new ArrayList<>();
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
            // System.out.println(idx);
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

    public TreeSet<IndexedPair> res = null;

    private int dfs_sum(int u, int d) {
        int cnt = p.get(u).w;
        for (int i = hd.get(u); i != 0; i = e.get(i).next) {
            int v = e.get(i).to;
            if (v == p.get(u).father) {
                continue;
            }
            cnt += dfs_sum(v, d + 1);
        }
        res.add(new IndexedPair(d, -cnt, u));
        return cnt;
    }

    public String stat_sum() {// Wrong
        res = new TreeSet<>();
        dfs_sum(root, 0);
        StringBuilder sb = new StringBuilder();
        Iterator<IndexedPair> it = res.iterator();
        while (it.hasNext()) {
            IndexedPair pr = it.next();
            for (int i = 0; i < pr.x; ++i) {
                sb.append("...");
            }
            Node u = p.get(pr.i);
            sb.append(u.name + ": ");
            sb.append(-pr.y);
            sb.append("\n");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String tx = FileHelper.read("a.txt");
        // System.out.println(tx);
        CatTree tr = new CatTree(tx);
        System.out.println(tr.export());
        System.out.println(tr.stat_sum());
    }
}
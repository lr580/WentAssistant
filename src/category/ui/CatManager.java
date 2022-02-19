package category.ui;

import javax.swing.*;
import category.core.CatTree;
import category.core.Node;
import plugin.FsLabel;
import java.awt.*;
import java.awt.event.*;
import ui.Root;
import javax.swing.tree.*;

public class CatManager extends JDialog {
    private static boolean isSpawned = false;// 是否有多个窗体
    private CatTree cata = null;
    private String type = null;
    private int x = 0;
    private DefaultMutableTreeNode root = null;
    private JTextField i_name = new JTextField(6);

    public CatManager(String type, int x, CatTree cata) {
        super(Root.that, "类别管理", false);// true不方便移动
        this.cata = cata;
        this.x = x;
        this.type = type;
        if (isSpawned) {// 防重复
            dispose();
            JOptionPane.showMessageDialog(null, "请先关闭已打开的类别管理器");
            return;
        } else {
            isSpawned = true;
        }

        String rootname = cata.top >= 1 ? cata.p.get(1).name : "总";
        root = new DefaultMutableTreeNode(rootname);
        if (cata.top >= 1) {
            RenderRoot(root, 1);
        }

        Container ct = getContentPane();
        setLayout(new GridLayout(1, 2, 10, 10));
        ct.add(new CatRenderer(cata, root));
        JPanel jp_rf = new JPanel(new GridLayout(1, 1, 10, 10));

        JPanel jp_name = new JPanel(new FlowLayout(0, 8, 10));
        jp_name.add(new FsLabel("类别名字:"));
        jp_name.add(i_name);
        jp_rf.add(jp_name);

        ct.add(jp_rf);

        setSize(600, 450);
        setVisible(true);
    }

    private void makeActionListener() {
        ActionListener ev_add = new ActionListener(){
            public void actionPerformed(ActionEvent e){
                String name = i_name.getText();
                
            }
        };
        i_name.addActionListener(ev_add);
    }

    public void RenderRoot(DefaultMutableTreeNode r, int u) {
        for (int i = cata.hd.get(u); i != 0; i = cata.e.get(i).next) {
            int v = cata.e.get(i).to;
            Node nd = cata.p.get(v);
            if (v == cata.p.get(u).father) {
                continue;
            }
            String name = nd.name;
            DefaultMutableTreeNode p = new DefaultMutableTreeNode(name);
            RenderRoot(p, v);
            r.add(p);
        }
    }
}

package category.ui;

import javax.swing.*;
import category.core.*;
import plugin.FsLabel;
import plugin.SwingHelper;
import java.awt.*;
import java.awt.event.*;
import ui.Root;
import javax.swing.tree.*;
import base.DbCtrl;
import base.DbLoad;
import base.ModLoad;

public class CatManager extends JDialog {
    private static boolean isSpawned = false;// 是否有多个窗体
    private CatTree cata = null;
    private String type = null;
    private int x = 0;
    private DefaultMutableTreeNode root = null;
    private DefaultTreeModel rootm = null;
    private JTextField i_name = new JTextField(6);
    private JButton b_add = new JButton("增加类别到选中");
    private JButton b_update = new JButton("修改选中类别");
    private JButton b_delete = new JButton("删除选中类别");
    private JButton b_save = new JButton("保存");
    private JButton b_undo = new JButton("撤销更改");
    private CatRenderer catr = null;
    private int saved = 1;
    private static String title = "类别管理";

    public CatManager(String type, int x, CatTree cata) {
        // true不方便移动,但为了防止一边修改(特别是删除)类别一边修改项目，所以禁用父窗口，且应当要求解冻后重新加载父窗口跟类别相关的一切信息
        super(Root.that, title, false);
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
        DbCtrl.write_diary("管理类别" + type + "_" + x);
        init_root();

        Container ct = getContentPane();
        setLayout(new BorderLayout());
        catr = new CatRenderer(cata, rootm);
        JScrollPane jsr = new JScrollPane(catr);
        ct.add(BorderLayout.CENTER, jsr);
        JPanel jp_uf = new JPanel(new GridLayout(2, 1, 10, 0));
        JPanel jp_line = new JPanel(new FlowLayout(0, 8, 5));
        JPanel jp_line2 = new JPanel(new FlowLayout(0, 8, 5));
        jp_line.add(new FsLabel("类别名字:"));
        jp_line.add(i_name);
        jp_line.add(b_add);
        jp_line2.add(b_update);
        jp_line2.add(b_delete);
        jp_line.add(b_save);
        // jp_line2.add(b_undo);
        jp_uf.add(jp_line);
        jp_uf.add(jp_line2);

        ct.add(BorderLayout.NORTH, jp_uf);
        makeActionListener();

        setSize(360, 380);
        setVisible(true);

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (saved == 0) {
                    int i = JOptionPane.showConfirmDialog(null, "是否保存当前修改", "提示",
                            JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.INFORMATION_MESSAGE);
                    if (i == JOptionPane.OK_OPTION) {// 保存
                        save();
                    } else if (i == JOptionPane.CLOSED_OPTION) {// 叉掉
                        return;
                    } else if (i == JOptionPane.CANCEL_OPTION) {// 不保存
                        // return;
                    }
                }
                // DbCtrl.save_diary();

                DbLoad.cata = new CatTree(Cata.query(DbLoad.getTypex()));
                // ModLoad.exec();
                ModLoad.evalctrl.eval();

                DbCtrl.write_diary("关闭类别管理");
                isSpawned = false;
                dispose();
            }
        });
    }

    private void init_root() {
        String rootname = cata.top >= 1 ? cata.p.get(1).name : "总";
        root = new DefaultMutableTreeNode(rootname);
        if (cata.top >= 1) {
            RenderRoot(root, 1);
        }
        rootm = new DefaultTreeModel(root);
    }

    private String checkName() {// 获取并检查名字合理性
        String name = i_name.getText();
        if (name.length() == 0) {
            SwingHelper.syso("类别名字不能为空");
            return null;
        }
        if (cata.find(name) != 0) {
            SwingHelper.syso("类别名字不能与已有名字重名");
            return null;
        }
        return name;
    }

    private boolean checkSingleSelect() {// 查询是否单选
        if (catr.getSelectCount() != 1) {
            SwingHelper.syso("能且仅能选中一个类别");
            return false;
        }
        return true;
    }

    private void set_unsaved() {
        saved = 0;
        setTitle(title + "*");
    }

    private void set_saved() {
        saved = 1;
        setTitle(title);
    }

    private ActionListener ev_add = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            String name = checkName();
            if (!checkSingleSelect() || name == null) {
                return;
            }
            int fa = catr.getSingleSelect();
            cata.addNode(name, fa);
            DefaultMutableTreeNode nd_fa = (DefaultMutableTreeNode) catr.getSelectionPath().getLastPathComponent();
            DefaultMutableTreeNode nd_nw = new DefaultMutableTreeNode(name);
            // nd_fa.add(nd_nw);
            rootm.insertNodeInto(nd_nw, nd_fa, nd_fa.getChildCount());
            // SwingHelper.syso(nd_fa.getChildCount());

            DbCtrl.write_diary("以编号为" + fa + "的父节点添加类别" + name);
            set_unsaved();
        }
    };

    private ActionListener ev_update = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            String name = checkName();
            if (!checkSingleSelect() || name == null) {
                return;
            }
            int idx = catr.getSingleSelect();
            Node cnd = cata.p.get(idx);
            cata.editNode(idx, name, cnd.father, cnd.w);
            DefaultMutableTreeNode nd = catr.getSingle();
            nd.setUserObject(name);
            rootm.nodeChanged(nd);

            DbCtrl.write_diary("修改编号为" + idx + "的类别名称为" + name);
            set_unsaved();
        }
    };

    private ActionListener ev_delete = new ActionListener() {
        public void actionPerformed(ActionEvent e) {// 暂时不支持多选
            if (!checkSingleSelect()) {
                return;
            }
            int idx = catr.getSingleSelect();
            cata.deleteNode(idx);
            DefaultMutableTreeNode nd = catr.getSingle();
            rootm.removeNodeFromParent(nd);

            DbCtrl.write_diary("删除编号为" + idx + "的类别");
            set_unsaved();
        }
    };

    private void save() {
        DbCtrl.write_diary("保存类别更改");
        Cata.update(type + "_" + x, cata.export());
        set_saved();
    }

    private ActionListener ev_save = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            save();
        }
    };

    private void undo() {
        cata = new CatTree(Cata.query(type + "_" + x));
        init_root();
        set_saved();
    }

    private ActionListener ev_undo = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            undo();// 暂不支持
        }
    };

    private void makeActionListener() {
        i_name.addActionListener(ev_add);
        b_add.addActionListener(ev_add);
        b_update.addActionListener(ev_update);
        b_delete.addActionListener(ev_delete);
        b_save.addActionListener(ev_save);
        b_undo.addActionListener(ev_undo);
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

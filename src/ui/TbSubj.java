package ui;

import javax.swing.*;
import base.DbCtrl;
import base.DbLoader;
import plugin.FsLabel;
import java.awt.*;
import java.awt.event.*;

public class TbSubj extends JPanel {// tabbar - subject
    private JTextField i_name = new JTextField(20);
    private JTextField i_semester = new JTextField(8);
    private String name, semester;
    private static TbSubj that = null;

    private void get_input() {
        name = i_name.getText();
        semester = i_semester.getText();
    }

    private boolean check_input() {
        if (name.length() == 0) {
            JOptionPane.showMessageDialog(null, "课程名称不能为空");
            return false;
        }
        if (name.length() > 40) {
            JOptionPane.showMessageDialog(null, "课程名称过长");
            return false;
        }
        if (name.length() > 10) {
            JOptionPane.showMessageDialog(null, "学期信息过长");
            return false;
        }
        return true;
    }

    private ActionListener add_sub = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            get_input();
            if (!check_input()) {
                return;
            }
            int id = DbCtrl.add_sub(name, semester);
            String[] row = { Integer.toString(id), name, semester };
            DbTable.that.addRow(row);
        }
    };

    private ActionListener upd_sub = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            get_input();
            if (!check_input()) {
                return;
            }
            if (DbTable.that.getSelectedRowCount() != 1) {
                JOptionPane.showMessageDialog(null, "必须且只能选中一项");
                return;
            }
            int row = DbTable.that.getSelectedRow();
            int id = Integer.valueOf((String) DbTable.that.getValueAt(row, 0));
            DbCtrl.upd_sub(id, name, semester);
            DbTable.updRow(row, new String[] { Integer.toString(id), name, semester });
        }
    };

    private ActionListener del_sub = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (DbTable.that.getSelectedRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "必须选中至少一项");
                return;
            }
            int rows[] = DbTable.that.getSelectedRows();
            for (int i = 0; i < rows.length; ++i) {
                int id = Integer.valueOf((String) DbTable.that.getValueAt(rows[i], 0));
                DbCtrl.del_sub(id);
            }
            DbTable.delSeleRow();
        }
    };

    private ActionListener ecls_input = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            i_name.setText("");
            i_semester.setText("");
        }
    };

    private ActionListener eprintall = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            DbTable.that.render("select * from `subject_" + DbLoader.t_temp + "`");
        }
    };

    private ActionListener esearch = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            get_input();
            if (name.length() == 0) {
                i_name.setText("%");
                name = "%";
            }
            if (semester.length() == 0) {
                i_semester.setText("%");
                semester = "%";
            }
            String cmd = DbCtrl.sea_sub(name, semester);
            DbTable.that.render(cmd);
        }
    };

    public static void upd_input(String[] s) {
        that.i_name.setText(s[1]);
        that.i_semester.setText(s[2]);
    }

    public TbSubj() {
        that = this;
        setLayout(new GridLayout(2, 1, 5, 5));
        JPanel uf = new JPanel(new FlowLayout(1, 8, 5));
        JPanel df = new JPanel(new FlowLayout(1, 8, 5));
        add(uf);
        add(df);

        uf.add(new FsLabel("课程名:"));
        uf.add(i_name);
        uf.add(new FsLabel("学期信息:"));
        uf.add(i_semester);

        JButton b_cls = new JButton("清空输入框");
        b_cls.addActionListener(ecls_input);
        df.add(b_cls);

        JButton b_add = new JButton("添加课程");
        b_add.addActionListener(add_sub);
        df.add(b_add);

        JButton b_upd = new JButton("修改选中课程");
        b_upd.addActionListener(upd_sub);
        df.add(b_upd);

        JButton b_del = new JButton("删除选中课程");
        b_del.addActionListener(del_sub);
        df.add(b_del);

        JButton b_printall = new JButton("显示全部课程");
        b_printall.addActionListener(eprintall);
        df.add(b_printall);

        JButton b_search = new JButton("搜索");
        b_search.addActionListener(esearch);
        df.add(b_search);
        i_name.addActionListener(esearch);
        i_semester.addActionListener(esearch);
    }
}

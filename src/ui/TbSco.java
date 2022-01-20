package ui;

import javax.swing.*;
import base.DbCtrl;
import base.DbLoader;
import plugin.*;
import java.awt.*;
import java.awt.event.*;

public class TbSco extends JPanel { // tabbar - score
    private JTextField i_stu = new JTextField(10);
    private JTextField i_subj = new JTextField(20);
    private JTextField i_value = new JTextField(4);
    private String stu, subj, value;
    private int score, stui, subji;
    private static TbSco that = null;

    private void get_input() {
        stu = i_stu.getText();
        subj = i_subj.getText();
        value = i_value.getText();
    }

    private boolean check_input() {// 检查输入并把非id输入转化为id
        if (stu.length() == 0) {
            JOptionPane.showMessageDialog(null, "学生不能为空");
            return false;
        }
        if (subj.length() == 0) {
            JOptionPane.showMessageDialog(null, "课程不能为空");
            return false;
        }
        if (stu.length() > 20) {
            JOptionPane.showMessageDialog(null, "学生过长");
            return false;
        }
        if (subj.length() > 40) {
            JOptionPane.showMessageDialog(null, "课程过长");
            return false;
        }

        if (!Checker.isRInt(value, 0)) {
            JOptionPane.showMessageDialog(null, "成绩必须是自然数");
            return false;
        } else {
            score = Integer.valueOf(value);
        }

        if (!Checker.isInt(stu)) {
            stui = DbCtrl.stuname2id(stu);
            i_stu.setText(Integer.toString(stui));
        } else {
            stui = DbCtrl.cfm_stuid(Integer.valueOf(stu));
        }
        if (stui == -1) {
            JOptionPane.showMessageDialog(null, "该学生不存在");
            return false;
        }

        if (!Checker.isInt(subj)) {
            subji = DbCtrl.subname2id(subj);
            i_subj.setText(Integer.toString(subji));
        } else {
            subji = DbCtrl.cfm_subid(Integer.valueOf(subj));
        }
        if (subji == -1) {
            JOptionPane.showMessageDialog(null, "该课程不存在");
            return false;
        }
        return true;
    }

    private ActionListener ecls_input = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            i_stu.setText("");
            i_subj.setText("");
            i_value.setText("");
        }
    };

    private ActionListener eprintall = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            DbTable.that.render("select * from `score_" + DbLoader.t_temp + "`");
        }
    };

    public static void upd_input(String[] s) {
        that.i_stu.setText(s[1]);
        that.i_subj.setText(s[2]);
        that.i_value.setText(s[3]);
    }

    private String[] summon_row(int id) {
        return new String[] { Integer.toString(id), Integer.toString(stui), Integer.toString(subji),
                Integer.toString(score) };
    }

    private ActionListener add_sub = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            get_input();
            if (!check_input()) {
                return;
            }
            int id = DbCtrl.add_sco(stui, subji, score);
            String[] row = summon_row(id);
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
            DbCtrl.upd_sco(id, stui, subji, score);
            DbTable.updRow(row, summon_row(id));
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
                DbCtrl.del_sco(id);
            }
            DbTable.delSeleRow();
        }
    };

    private ActionListener esearch = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            get_input();
            if (stu.length() == 0) {
                i_stu.setText("%");
                stu = "%";
            }
            if (subj.length() == 0) {
                i_subj.setText("%");
                subj = "%";
            }
            if (value.length() == 0) {
                i_value.setText("%");
                value = "%";
            }
            String cmd = DbCtrl.sea_sco(stu, subj, value);
            DbTable.that.render(cmd);
        }
    };

    public TbSco() {
        that = this;
        setLayout(new GridLayout(2, 1, 5, 5));
        JPanel uf = new JPanel(new FlowLayout(1, 8, 5));
        JPanel df = new JPanel(new FlowLayout(1, 8, 5));
        add(uf);
        add(df);

        uf.add(new FsLabel("学生:"));
        uf.add(i_stu);
        uf.add(new FsLabel("课程:"));
        uf.add(i_subj);
        uf.add(new FsLabel("成绩:"));
        uf.add(i_value);

        JButton b_cls = new JButton("清空输入框");
        b_cls.addActionListener(ecls_input);
        df.add(b_cls);

        JButton b_add = new JButton("添加成绩");
        b_add.addActionListener(add_sub);
        df.add(b_add);

        JButton b_upd = new JButton("修改选中成绩");
        b_upd.addActionListener(upd_sub);
        df.add(b_upd);

        JButton b_del = new JButton("删除选中成绩");
        b_del.addActionListener(del_sub);
        df.add(b_del);

        JButton b_printall = new JButton("显示全部成绩");
        b_printall.addActionListener(eprintall);
        df.add(b_printall);

        JButton b_search = new JButton("搜索");
        b_search.addActionListener(esearch);
        df.add(b_search);
        i_stu.addActionListener(esearch);
        i_subj.addActionListener(esearch);
        i_value.addActionListener(esearch);
    }
}

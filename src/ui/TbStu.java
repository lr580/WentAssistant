package ui;

import javax.swing.*;
import base.DbCtrl;
import base.DbLoader;
import plugin.FsLabel;
import java.awt.*;
import java.awt.event.*;

public class TbStu extends JPanel {// tabbar - student
    public DbTable jt = null;
    private JTextField i_name = new JTextField(6);
    private JTextField i_number = new JTextField(14);
    private JTextField i_major = new JTextField(8);
    private String name, number, major;
    private static TbStu that = null;

    private void get_input() {
        name = i_name.getText();
        number = i_number.getText();
        major = i_major.getText();
    }

    private boolean check_input() {
        if (name.length() == 0 || number.length() == 0) {
            JOptionPane.showMessageDialog(null, "学号或姓名不能为空");
            return false;
        }
        if (name.length() > 20) {
            JOptionPane.showMessageDialog(null, "姓名过长");
            return false;
        }
        if (number.length() > 20) {
            JOptionPane.showMessageDialog(null, "学号过长");
            return false;
        }
        if (major.length() > 20) {
            JOptionPane.showMessageDialog(null, "专业过长");
            return false;
        }
        return true;
    }

    private ActionListener add_stu = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            get_input();
            if (!check_input()) {
                return;
            }
            int id = DbCtrl.add_stu(name, number, major);
            String[] row = { Integer.toString(id), name, number, major };
            jt.addRow(row);
        }
    };

    private ActionListener upd_stu = new ActionListener() {
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
            DbCtrl.upd_stu(id, name, number, major);
            DbTable.updRow(row, new String[] { Integer.toString(id), name, number, major });
        }
    };

    private ActionListener del_stu = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (DbTable.that.getSelectedRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "必须选中至少一项");
                return;
            }
            int rows[] = DbTable.that.getSelectedRows();
            for (int i = 0; i < rows.length; ++i) {
                int id = Integer.valueOf((String) DbTable.that.getValueAt(rows[i], 0));
                DbCtrl.del_stu(id);
            }
            DbTable.delSeleRow();
        }
    };

    private ActionListener ecls_input = new ActionListener() {// 清空输入
        public void actionPerformed(ActionEvent e) {
            i_name.setText("");
            i_number.setText("");
            i_major.setText("");
        }
    };

    private ActionListener eprintall = new ActionListener() {// 输出所有
        public void actionPerformed(ActionEvent e) {
            jt.render("select * from `student_" + DbLoader.t_temp + "`");
        }
    };

    private ActionListener esearch = new ActionListener() {// 搜索
        public void actionPerformed(ActionEvent e) {
            get_input();
            if (name.length() == 0) {
                i_name.setText("%");
                name = "%";
            }
            if (major.length() == 0) {
                i_major.setText("%");
                major = "%";
            }
            if (number.length() == 0) {
                i_number.setText("%");
                number = "%";
            }
            String cmd = DbCtrl.sea_stu(name, number, major);
            jt.render(cmd);
        }
    };

    public static void upd_input(String[] s) {// 将对应值填入编辑框
        that.i_name.setText(s[1]);
        that.i_number.setText(s[2]);
        that.i_major.setText(s[3]);
    }

    public TbStu(DbTable jt) {
        this.jt = jt;
        that = this;
        setLayout(new GridLayout(2, 1, 5, 5));
        JPanel p_stu_uf = new JPanel(new FlowLayout(1, 8, 5));
        JPanel p_stu_df = new JPanel(new FlowLayout(1, 8, 5));
        add(p_stu_uf);
        add(p_stu_df);

        p_stu_uf.add(new FsLabel("姓名:"));
        p_stu_uf.add(i_name);
        p_stu_uf.add(new FsLabel("学号:"));
        p_stu_uf.add(i_number);
        p_stu_uf.add(new FsLabel("专业:"));
        p_stu_uf.add(i_major);

        JButton b_cls = new JButton("清空输入框");
        b_cls.addActionListener(ecls_input);
        p_stu_df.add(b_cls);

        JButton b_addstu = new JButton("添加学生");
        b_addstu.addActionListener(add_stu);
        p_stu_df.add(b_addstu);

        JButton b_updstu = new JButton("修改选中学生");
        b_updstu.addActionListener(upd_stu);
        p_stu_df.add(b_updstu);

        JButton b_delstu = new JButton("删除选中学生");
        b_delstu.addActionListener(del_stu);
        p_stu_df.add(b_delstu);

        JButton b_printall = new JButton("显示全部学生");
        b_printall.addActionListener(eprintall);
        p_stu_df.add(b_printall);

        JButton b_search = new JButton("搜索");
        b_search.addActionListener(esearch);
        p_stu_df.add(b_search);
        i_name.addActionListener(esearch);
        i_number.addActionListener(esearch);
        i_major.addActionListener(esearch);
    }
}

package ui;

import javax.swing.*;
import base.DbSearch;
import plugin.*;
import java.awt.*;
import java.awt.event.*;

public class TbSearch extends JPanel {
    private JTextField i_min = new JTextField(4);
    private JTextField i_max = new JTextField(4);
    private JTextField i_name = new JTextField(6);
    private JTextField i_subj = new JTextField(6);
    private JTextField i_major = new JTextField(6);
    private JTextField i_semester = new JTextField(6);
    private FsLabel jl_cnt = new FsLabel("");
    private static TbSearch that = null;

    private ActionListener ecls_input = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            i_min.setText("");
            i_max.setText("");
            i_name.setText("");
            i_subj.setText("");
            i_major.setText("");
            i_semester.setText("");
        }
    };

    public static void prtall() {
        DbTable.that.render(DbSearch.sea_all);
        that.jl_cnt.setText("结果数:" + DbTable.that.getRowCount());
    }

    private ActionListener eprintall = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            prtall();
        }
    };

    private String autofill(JTextField jt) {// 获取内容并自动补全空白
        String t = jt.getText();
        if (t.length() == 0) {
            jt.setText("%");
            t = "%";
        }
        return t;
    }

    private ActionListener esea = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            String v_min = i_min.getText();
            if (v_min.length() == 0) {
                v_min = "0";
                i_min.setText("0");
            }
            if (!Checker.isRInt(v_min, 0)) {
                JOptionPane.showMessageDialog(null, "最小值必须是自然数");
                return;
            }
            int min = Integer.valueOf(v_min);

            String v_max = i_max.getText();
            if (v_max.length() == 0) {
                v_max = "100";
                i_max.setText("100");
            }
            if (!Checker.isRInt(v_max, 0)) {
                JOptionPane.showMessageDialog(null, "最大值必须是自然数");
                return;
            }
            int max = Integer.valueOf(v_max);

            if (min > max) {
                JOptionPane.showMessageDialog(null, "最小值不能大于最大值");
                return;
            }

            String stuname = autofill(i_name);
            String major = autofill(i_major);
            String subjname = autofill(i_subj);
            String semester = autofill(i_semester);

            String cmd = DbSearch.search(min, max, stuname, major, subjname, semester);
            DbTable.that.render(cmd);
            that.jl_cnt.setText("结果数:" + DbTable.that.getRowCount());
        }
    };

    private ActionListener estat = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(null,DbSearch.stat());
        }
    };

    public TbSearch() {
        that = this;
        setLayout(new GridLayout(2, 1, 5, 5));
        JPanel uf = new JPanel(new FlowLayout(1, 8, 5));
        JPanel df = new JPanel(new FlowLayout(1, 8, 5));
        add(uf);
        add(df);

        uf.add(new FsLabel("最低分:"));
        uf.add(i_min);
        uf.add(new FsLabel("最高分:"));
        uf.add(i_max);
        uf.add(new FsLabel("学生名:"));
        uf.add(i_name);
        uf.add(new FsLabel("学生专业:"));
        uf.add(i_major);
        uf.add(new FsLabel("课程名:"));
        uf.add(i_subj);
        uf.add(new FsLabel("课程学期:"));
        uf.add(i_semester);

        JButton b_sea = new JButton("搜索");
        df.add(b_sea);
        b_sea.addActionListener(esea);
        i_min.addActionListener(esea);
        i_max.addActionListener(esea);
        i_name.addActionListener(esea);
        i_major.addActionListener(esea);
        i_subj.addActionListener(esea);
        i_semester.addActionListener(esea);

        JButton b_stat = new JButton("统计");
        b_stat.addActionListener(estat);
        df.add(b_stat);

        JButton b_cls = new JButton("清空输入框");
        b_cls.addActionListener(ecls_input);
        df.add(b_cls);

        JButton b_prtall = new JButton("显示全部成绩");
        b_prtall.addActionListener(eprintall);
        df.add(b_prtall);
        df.add(jl_cnt);
    }
}

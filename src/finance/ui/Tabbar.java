package finance.ui;

import javax.swing.*;
import base.ModLoad;
import base.ProcessCmd;
import base.ProcessCtrl;
import finance.core.Load;
import finance.core.Supply;
import java.awt.*;
import java.awt.event.*;
import base.DbLoad;
import ui.DbTable;
import ui.EvSupply;
import ui.InputFiller;
import ui.TbMain;
import plugin.Eval;
import plugin.FsLabel;
import plugin.SwingHelper;

public class Tabbar {
    private static JTextField i_money = new JTextField(4);
    private static JTextField i_date = new JTextField(5);
    public static JComboBox<String> i_type = new JComboBox<>(Load.cat_list);
    private static JTextField i_comment = new JTextField(12);
    private static JTextField i_cmd = new JTextField(25);
    private static JCheckBox i_multi = new JCheckBox("多项录入");
    private static double[] money = null;
    private static int[] date = null;
    private static int[] type = null;
    private static String[] comment = null;
    private static int n = 0;
    // private static ProcessCmd[] cmd = null;

    public static void InitTabbar() {
        TbMain page = TbMain.that;
        page.removeAll();
        page.setLayout(new GridLayout(2, 1, 5, 5));
        JPanel p_uf = new JPanel(new FlowLayout(1, 8, 5));
        JPanel p_df = new JPanel(new FlowLayout(1, 8, 5));
        page.add(p_uf);
        page.add(p_df);

        p_uf.add(new FsLabel("金额:"));
        p_uf.add(i_money);
        p_uf.add(new FsLabel("日期:"));
        p_uf.add(i_date);
        p_uf.add(new FsLabel("类别:"));
        p_uf.add(i_type);
        p_uf.add(new FsLabel("备注:"));
        p_uf.add(i_comment);
        p_uf.add(i_multi);

        JButton b_catactrl = new JButton("类别管理");
        b_catactrl.addActionListener(ev_catactrl);
        p_uf.add(b_catactrl);

        JButton b_add = new JButton("添加");
        p_df.add(b_add);

        JButton b_update = new JButton("编辑");
        p_df.add(b_update);

        JButton b_delete = new JButton("删除");
        p_df.add(b_delete);

        p_df.add(new FsLabel("命令:"));
        p_df.add(i_cmd);

        JButton b_search = new JButton("搜索");
        p_df.add(b_search);

        JButton b_stat = new JButton("统计");
        p_df.add(b_stat);

        b_add.addActionListener(ev_add);
        b_update.addActionListener(ev_update);
        b_delete.addActionListener(ev_delete);
        b_search.addActionListener(ev_search);
        b_stat.addActionListener(ev_stat);
        activate();// 各种其他事件激活
    }

    private static void activate() {
        DbTable.filler = new InputFiller() {
            public void fill(Object[] s) {
                i_money.setText(s[1].toString());
                int idx = Load.cat_list.indexOf(s[2].toString());
                i_type.setSelectedIndex(idx);
                i_date.setText(Integer.toString(Supply.Str2Date(s[3].toString())));
                i_comment.setText(s[4].toString());
            }
        };

        i_date.setText(Integer.toString(Supply.Now2Date()));
    }

    private static ActionListener ev_catactrl = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            EvSupply.openCataCtrl();
            ModLoad.evalctrl.cmd = new Eval() {// 像是在做闭包，强行异步改同步
                public void eval() {
                    Load.update_catlist();
                }
            };
        }
    };

    private static boolean check_input() {// 检查非空,获取输入
        String s_money = i_money.getText();
        String s_date = i_date.getText();
        int s_type = DbLoad.cata.find((String) i_type.getSelectedItem());
        String s_comment = i_comment.getText();
        if (s_type == 0 || s_money.length() == 0 || s_date.length() == 0) {
            return false;
        }
        if (i_multi.isSelected()) {// 多选
            // 稍后实现
        } else {// 单选
            money = new double[1];
            date = new int[1];
            type = new int[1];
            comment = new String[1];
            money[0] = Double.parseDouble(s_money);
            date[0] = Integer.parseInt(s_date);
            type[0] = s_type;
            comment[0] = s_comment;
            n = 1;
        }
        return true;
    }

    private static ActionListener ev_add = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (!check_input()) {
                SwingHelper.syso("金额、日期或类别不能为空");
                return;
            }
            // ProcessCmd[] cmd = new ProcessCmd[n];
            for (int i = 0; i < n; ++i) {
                Object[] from = new Object[5];
                from[1] = money[i];
                from[2] = date[i];
                from[3] = type[i];
                from[4] = comment[i];
                ProcessCmd cmd = new ProcessCmd(1, from);
                ProcessCtrl.push(cmd);
            }
        }
    };

    private static ActionListener ev_update = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
        }
    };

    private static ActionListener ev_delete = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
        }
    };

    private static ActionListener ev_search = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
        }
    };

    private static ActionListener ev_stat = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
        }
    };
}

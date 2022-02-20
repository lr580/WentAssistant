package finance.ui;

import javax.swing.*;

import finance.core.Load;
// import category.core.CatTree;
import plugin.FsLabel;

import java.awt.*;
// import java.util.Vector;

import ui.TbMain;

public class Tabbar {
    private static JTextField i_money = new JTextField(4);
    private static JTextField i_date = new JTextField(5);
    
    private static JComboBox<String> i_type = new JComboBox<>(Load.cat_list);
    private static JTextField i_comment = new JTextField(12);

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

        JButton b_add = new JButton("添加");
        p_df.add(b_add);
    }
}

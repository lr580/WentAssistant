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
    private static JButton b_add = new JButton("添加");
    private static JButton b_update = new JButton("编辑");
    private static JButton b_delete = new JButton("删除");
    private static Object[] tmp = null;// 点击后获取的一行

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
        p_df.add(b_add);
        p_df.add(b_update);
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
                tmp = new Object[5];
                for (int i = 0; i < 5; ++i) {
                    tmp[i] = s[i];
                }
                tmp[2] = Load.cat_list.indexOf(s[2].toString());
                tmp[3] = Supply.Str2Date(s[3].toString());
                i_money.setText(s[1].toString());
                int idx = Load.cat_list.indexOf(s[2].toString());
                i_type.setSelectedIndex(idx);
                i_date.setText(Integer.toString(Supply.Str2Date(s[3].toString())));
                i_comment.setText(s[4].toString());
            }
        };

        i_date.setText(Integer.toString(Supply.Now2Date()));

        // 热键：
        i_money.addActionListener(ev_add);
        i_date.addActionListener(ev_add);
        i_comment.addActionListener(ev_add);
        i_money.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {// 不能用keyTyped获取无输入的键
                // System.out.println(e.getKeyCode() + "_" + e.getKeyChar());
                int code = e.getKeyCode();
                if (code == KeyEvent.VK_UP) {
                    i_cmd.requestFocus();
                } else if (code == KeyEvent.VK_DOWN) {
                    i_date.requestFocus();
                }
                checkShortcuts(e);
            }
        });
        i_date.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                int code = e.getKeyCode();
                if (code == KeyEvent.VK_UP) {
                    i_money.requestFocus();
                } else if (code == KeyEvent.VK_DOWN) {
                    i_type.requestFocus();
                }
                checkShortcuts(e);
            }
        });
        i_type.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();
                int idx = i_type.getSelectedIndex();
                int n = Load.cat_list.size();
                if (code == KeyEvent.VK_UP) {
                    i_date.requestFocus();
                } else if (code == KeyEvent.VK_DOWN) {
                    i_comment.requestFocus();
                } else if (code == KeyEvent.VK_LEFT) {
                    if (idx != 0) {
                        i_type.setSelectedIndex(idx - 1);
                    } else {
                        i_type.setSelectedIndex(n - 1);
                    }
                } else if (code == KeyEvent.VK_RIGHT) {
                    if (idx + 1 < n) {
                        i_type.setSelectedIndex(idx + 1);
                    } else {
                        i_type.setSelectedIndex(0);
                    }
                }
                checkShortcuts(e);
            }
        });
        i_comment.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                int code = e.getKeyCode();
                if (code == KeyEvent.VK_UP) {
                    i_type.requestFocus();
                } else if (code == KeyEvent.VK_DOWN) {
                    i_cmd.requestFocus();
                }
                checkShortcuts(e);
            }
        });
        i_cmd.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                int code = e.getKeyCode();
                if (code == KeyEvent.VK_UP) {
                    i_comment.requestFocus();
                } else if (code == KeyEvent.VK_DOWN) {
                    i_money.requestFocus();
                }
                checkShortcuts(e);
            }
        });
    }

    public static void checkShortcuts(KeyEvent e) {
        // System.out.println("awa");
        if (e.isControlDown()) {// CTRL+
            int code = e.getKeyCode();
            if (code == KeyEvent.VK_I) {
                add();
            } else if (code == KeyEvent.VK_U) {
                update();
            } else if (code == KeyEvent.VK_D) {
                delete();
            }
        }
    }// 未来可以考虑拓展到其他控件，所以public一下

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
            String s_cmd = i_cmd.getText();
            n = Supply.getMultiInput(money, date, type, comment, s_money, s_date, s_type, s_comment, s_cmd);
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

    private static Object[] fetch(int i) {
        Object[] res = new Object[5];
        res[1] = money[i];
        res[2] = type[i];
        res[3] = date[i];
        res[4] = comment[i];
        return res;
    }

    private static void add() {
        if (!check_input()) {
            SwingHelper.syso("金额、日期或类别不能为空");
            return;
        }
        for (int i = 0; i < n; ++i) {
            Object[] from = fetch(i);
            ProcessCmd cmd = new ProcessCmd(1, from);
            ProcessCtrl.push(cmd);
        }
    }

    private static void update() {
        if (!check_input()) {
            SwingHelper.syso("金额、日期或类别不能为空");
            return;
        }
        if (DbTable.that.getSelectedRows().length != 1) {
            SwingHelper.syso("能且仅能编辑选中的一项");
            return;
        }
        Object[] to = fetch(0);
        int idx = DbTable.that.getSelectedRow();
        to[0] = (Integer) DbTable.that.getValueAt(idx, 0);
        Object[] from = DbTable.queryRow(idx);
        Supply.queryModify(from);
        ProcessCmd cmd = new ProcessCmd(2, from, to);
        ProcessCtrl.push(cmd);
    }

    private static void delete() {
        int[] rows = DbTable.that.getSelectedRows();
        for (int i = rows.length - 1; i >= 0; --i) {// 切记倒着来
            Object[] from = DbTable.queryRow(rows[i]);
            Supply.queryModify(from);
            ProcessCmd cmd = new ProcessCmd(3, from);
            ProcessCtrl.push(cmd);
        }
    }

    private static ActionListener ev_add = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            add();
        }
    };

    private static ActionListener ev_update = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            update();
        }
    };

    private static ActionListener ev_delete = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            delete();
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

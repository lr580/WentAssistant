package ui;

import javax.swing.*;

import base.DbCtrl;
import base.PrefManager;
import plugin.FsLabel;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

//不提供输入正确性检验
public class Preference extends JDialog { // 暂不提供翻译功能，因为不需要
    private int n = 0;// 配置项数目
    private FsLabel[] label = null;
    private JTextField[] input = null;
    private Map<Integer, String> h = new HashMap<>();
    private int saved = 1;
    private static String title = "偏好设置";

    private void loadInput() {
        Iterator<Map.Entry<String, String>> it = PrefManager.pref.entrySet().iterator();
        for (int i = 0; i < n; ++i) {
            Map.Entry<String, String> pair = it.next();
            label[i].setText(pair.getKey() + ":");
            input[i].setText(pair.getValue());
        }
    }

    public Preference() {
        super(Root.that, title, false);
        DbCtrl.write_diary("打开偏好设置");
        n = PrefManager.pref.size();
        setLayout(new BorderLayout());
        Container ct = getContentPane();
        JPanel tabbar = new JPanel(new FlowLayout(1, 8, 8));
        JButton b_save = new JButton("保存");
        JButton b_undo = new JButton("撤销");
        JButton b_init = new JButton("初始化");
        tabbar.add(b_save);
        tabbar.add(b_undo);
        tabbar.add(b_init);
        b_save.addActionListener(ev_save);
        b_undo.addActionListener(ev_undo);
        b_init.addActionListener(ev_init);
        ct.add(BorderLayout.SOUTH, tabbar);
        JPanel center = new JPanel(new GridLayout(n, 2, 10, 5));
        label = new FsLabel[n];
        input = new JTextField[n];
        Iterator<Map.Entry<String, String>> it = PrefManager.pref.entrySet().iterator();
        for (int i = 0; i < n; ++i) {
            Map.Entry<String, String> pair = it.next();
            h.put(i, pair.getKey());
            label[i] = new FsLabel();
            input[i] = new JTextField(8);
            input[i].addKeyListener(i_unsaved);
            center.add(label[i]);
            center.add(input[i]);
        }
        loadInput();
        ct.add(BorderLayout.CENTER, center);
        setSize(260, 50 + 35 * n);
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
                        // SwingHelper.syso("x");
                        return;
                    } else if (i == JOptionPane.CANCEL_OPTION) {// 不保存
                        // SwingHelper.syso("nsav");
                        // return;
                    }
                }
                DbCtrl.write_diary("关闭偏好设置");
                dispose();
            }
        });
    }

    private KeyAdapter i_unsaved = new KeyAdapter() {
        public void keyTyped(KeyEvent e) {
            set_unsaved();
        }
    };

    private void set_saved() {
        saved = 1;
        setTitle(title);
    }

    private void set_unsaved() {
        saved = 0;
        setTitle(title + "*");
    }

    private void save() {
        for (int i = 0; i < n; ++i) {
            String s = input[i].getText();
            PrefManager.pref.put(h.get(i), s);
            PrefManager.write_setting();
        }
        DbCtrl.write_diary("保存偏好设置");
        set_saved();
    }

    private ActionListener ev_save = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            save();
        }
    };

    private ActionListener ev_undo = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            PrefManager.load_setting();
            loadInput();
            set_saved();
        }
    };

    private ActionListener ev_init = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            // 不设置危险操作弹窗核实是否初始化(以后如果确实有需要再说)
            PrefManager.init_setting();
            loadInput();
            DbCtrl.write_diary("初始化偏好设置");
            set_saved();
        }
    };
}

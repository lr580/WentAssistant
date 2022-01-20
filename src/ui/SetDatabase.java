package ui;

import javax.swing.*;
import base.*;
import mysql.Link;
import java.awt.event.*;
import java.awt.*;
import plugin.*;

public class SetDatabase extends JDialog implements ActionListener {
    private static final int i_len = 16;
    private JTextField i_ip = new JTextField("127.0.0.1", i_len);
    private JTextField i_port = new JTextField("3306", i_len);
    private JTextField i_db = new JTextField(i_len);
    private JTextField i_user = new JTextField(i_len);
    private JPasswordField i_psw = new JPasswordField(i_len);
    private JTextField i_cfg = new JTextField("serverTimezone=UTC", i_len);

    public SetDatabase(Root frame) {
        super(frame, "设置数据库连接", true);
        Container ct = getContentPane();
        String db_cfg[] = Init.read_db_settings();
        JPanel cct = new JPanel(new GridLayout(6, 2, 5, 5));
        ct.add(cct, BorderLayout.CENTER);

        cct.add(new FsLabel("IP:"));
        cct.add(i_ip);
        cct.add(new FsLabel("端口:"));
        cct.add(i_port);
        cct.add(new FsLabel("数据库名:"));
        cct.add(i_db);
        cct.add(new FsLabel("用户名:"));
        cct.add(i_user);
        cct.add(new FsLabel("密码:"));
        cct.add(i_psw);
        cct.add(new FsLabel("选项(&分割):"));
        cct.add(i_cfg);

        i_ip.setText(db_cfg.length > 1 && db_cfg[1].length() != 0 ? db_cfg[1] : "127.0.0.1");
        i_port.setText(db_cfg.length > 2 && db_cfg[2].length() != 0 ? db_cfg[2] : "3306");
        i_db.setText(db_cfg.length > 3 && db_cfg[3].length() != 0 ? db_cfg[3] : "");
        i_user.setText(db_cfg.length > 4 && db_cfg[4].length() != 0 ? db_cfg[4] : "root");
        i_psw.setText(db_cfg.length > 5 && db_cfg[5].length() != 0 ? db_cfg[5] : "");
        i_cfg.setText(db_cfg.length > 6 && db_cfg[6].length() != 0 ? db_cfg[6] : "serverTimezone=UTC");
        // i_cfg.setSelectionEnd(i_cfg.getText().length());好像没用

        JPanel butts = new JPanel(new FlowLayout(1, 5, 10));
        JButton jb_con = new JButton("连接");
        JButton jb_cls = new JButton("清空");
        butts.add(jb_cls);
        butts.add(jb_con);
        ct.add(butts, BorderLayout.SOUTH);

        jb_con.addActionListener(this);
        jb_cls.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                i_ip.setText("");
                i_port.setText("");
                i_db.setText("");
                i_user.setText("");
                i_psw.setText("");
                i_cfg.setText("");
            }
        });

        setSize(200, 250);
        setResizable(false);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String ip = i_ip.getText();
        String port = i_port.getText();
        String db = i_db.getText();
        String user = i_user.getText();
        String psw = new String(i_psw.getPassword());
        String cfg = i_cfg.getText();
        if (ip.length() == 0) {
            // JOptionPane.showMessageDialog(null, "IP不能为空");
            // return;
            ip = "127.0.0.1";// 自动补全
            i_ip.setText(ip);
        }
        if (port.length() == 0) {
            // JOptionPane.showMessageDialog(null, "端口不能为空");
            // return;
            port = "3306";// 自动补全
            i_port.setText(port);
        }
        if (db.length() == 0) {
            JOptionPane.showMessageDialog(null, "数据库不能为空");
            return;
        }
        if (user.length() == 0) {
            JOptionPane.showMessageDialog(null, "用户名不能为空");
            return;
        }

        Init.update_db_settings(ip, port, db, user, psw, cfg);
        boolean suc = Link.connect(ip, port, db, user, psw, cfg);
        if (suc) {
            DbCtrl.write_diary("设置数据库链接");
            dispose();
        } else {
            JOptionPane.showMessageDialog(null, Link.err_msg);
        }
    }
}

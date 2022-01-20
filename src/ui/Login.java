package ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import base.*;
import plugin.*;

public class Login extends JFrame {
    public boolean suc = false;// 是否登陆成功
    private static final int tot_times = 3;// 可尝试总次数
    private static int chance = tot_times;// 剩余次数

    public Login() {
        check_login();
    }

    private class psw_input_dialog extends JDialog implements ActionListener {
        private JPasswordField jpsw = null;
        private JLabel jl = null;

        private String get_tip_html() {
            return "<html><div style='font-size:14'>请输入密码(剩余机会:" + chance + "):</div></html>";
        }// 可以用SwingHelper，下同

        private String get_retip_html() {
            return "<html><div style='font-size:14'>密码错误，请重新输入密码(剩余机会:" + chance + "):</div></html>";
        }

        public psw_input_dialog(Login frame) {
            super(frame, "提示", true);
            Container ct = getContentPane();
            jl = new JLabel(get_tip_html());
            ct.add(jl, BorderLayout.NORTH);

            JPanel jp = new JPanel(new FlowLayout(1, 10, 10));
            jpsw = new JPasswordField(16);
            jpsw.addActionListener(this);
            jp.add(jpsw);

            JButton jb = new JButton("确认");
            jb.addActionListener(this);
            jp.add(jb);
            ct.add(jp, BorderLayout.CENTER);

            setSize(300, 100);
            setResizable(false);// visible之前
            setVisible(true);

            addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    dispose();
                    System.exit(0);
                }
            });
        }

        public void actionPerformed(ActionEvent e) {
            String input_md5 = new String(jpsw.getPassword());
            input_md5 = PswMD5.password_md5(input_md5);
            if (input_md5.equals(Init.login_md5)) {
                suc = true;
                dispose();
            } else {
                chance--;
                if (chance == 0) {
                    JOptionPane.showMessageDialog(null, "登陆失败!");
                    dispose();
                    System.exit(0);
                }
                jpsw.setText("");
                jl.setText(get_retip_html());
            }
        }
    }

    private void check_login() {
        if (Init.isValidate()) {
            if (Init.login_md5.length() != 0) {// 用户设置了密码
                new psw_input_dialog(this);
            } else {
                suc = true;
            }
        } else {
            JOptionPane.showMessageDialog(null, "文件被破坏，无法检验登录密码! ", "警告", JOptionPane.DEFAULT_OPTION);
        }
    }
}

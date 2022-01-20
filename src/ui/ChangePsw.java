package ui;

import javax.swing.*;
import base.Init;
import java.awt.event.*;
import java.awt.*;
import plugin.*;

public class ChangePsw extends JDialog implements ActionListener {
    private JPasswordField jp_ori = null, jp_new = null, jp_cfm = null;
    private final static int lim_maxlen = 16;// 密码长度最大限制

    public ChangePsw(Root frame) {
        super(frame, "修改密码", true);
        Container ct = getContentPane();
        JPanel cct = new JPanel(new GridLayout(3, 2, 5, 5));
        ct.add(cct, BorderLayout.CENTER);

        FsLabel jl_ori = new FsLabel("原密码(没有留空):");
        cct.add(jl_ori);
        jp_ori = new JPasswordField(16);
        cct.add(jp_ori);
        FsLabel jl_new = new FsLabel("新密码(可设空):");
        cct.add(jl_new);
        jp_new = new JPasswordField(16);
        cct.add(jp_new);
        FsLabel jl_cfm = new FsLabel("再次输入新密码:");
        cct.add(jl_cfm);
        jp_cfm = new JPasswordField(16);
        cct.add(jp_cfm);

        JButton jb = new JButton("提交");
        // JPanel jbp = new JPanel(new FlowLayout(1, 10, 10));
        // jbp.add(jb);
        // ct.add(jbp, BorderLayout.SOUTH);
        ct.add(jb, BorderLayout.SOUTH);

        jp_cfm.addActionListener(this);
        jb.addActionListener(this);

        // jp_ori.addActionListener(new ActionListener() {
        // public void actionPerformed(ActionEvent e) {
        // jp_new.setSelectionEnd(0);
        // }
        // }); 想尝试按下回车让下一个输入框获取焦点失败了

        setSize(235, 160);
        setResizable(false);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String ori = new String(jp_ori.getPassword());
        String nwe = new String(jp_new.getPassword());
        String cfm = new String(jp_cfm.getPassword());

        if (!nwe.equals(cfm)) {
            JOptionPane.showMessageDialog(null, "两次密码输入不一致!");
            return;
        }
        if (!(Init.login_md5.equals(PswMD5.password_md5(ori)) || ori.length() + Init.login_md5.length() == 0)) {// 非原现都是空或相等其一满足
            JOptionPane.showMessageDialog(null, "原密码不正确!");
            return;
        }
        // 不设置字符限制，可以是任意非ASCII字符
        if (nwe.length() > lim_maxlen) {
            JOptionPane.showMessageDialog(null, "新密码过长!");
            return;
        }
        // 不考虑原密码和新密码一致
        Init.change_psw(nwe);
        dispose();
        JOptionPane.showMessageDialog(null, "修改成功!");
    }
}

package ui;

import java.awt.event.*;
import javax.swing.*;

public class RootMenu extends JMenuBar {
    public RootMenu(Root frame) {
        JMenu file = new JMenu("文件");
        add(file);

        JMenuItem save = new JMenuItem("保存");
        save.addActionListener(TbGlobal.e_save);
        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));

        JMenuItem undo = new JMenuItem("撤销");
        undo.addActionListener(TbGlobal.e_undo);
        undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK));

        JMenuItem backup = new JMenuItem("添加到备份");
        backup.addActionListener(TbGlobal.e_backup);
        backup.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, KeyEvent.CTRL_DOWN_MASK));

        JMenuItem frombackup = new JMenuItem("从备份还原");
        frombackup.addActionListener(TbGlobal.e_frombackup);
        frombackup.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));

        JMenuItem delbackup = new JMenuItem("删除选中备份");
        delbackup.addActionListener(TbGlobal.e_delbackup);
        delbackup.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_D, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));

        JMenuItem importall = new JMenuItem("导入数据库");
        importall.addActionListener(TbGlobal.e_importall);
        importall.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK | KeyEvent.ALT_DOWN_MASK));// ctrl+shift+o跟jtextfield冲突

        JMenuItem exportall = new JMenuItem("导出所有数据");
        exportall.addActionListener(TbGlobal.e_exportall);
        exportall.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));

        JMenuItem export = new JMenuItem("导出当前表");
        export.addActionListener(TbGlobal.e_export);
        export.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.SHIFT_DOWN_MASK));

        file.add(save);
        file.add(undo);
        file.addSeparator();
        file.add(backup);
        file.add(frombackup);
        file.add(delbackup);
        file.addSeparator();
        file.add(importall);
        file.add(exportall);
        file.add(export);

        JMenu setting = new JMenu("设置");
        add(setting);

        JMenuItem reset_psw = new JMenuItem("修改登录密码");
        reset_psw.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ChangePsw(frame);
            }
        });
        reset_psw.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK));

        JMenuItem reset_mysql = new JMenuItem("设置数据库连接");
        reset_mysql.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new SetDatabase(frame);
            }
        });
        reset_mysql.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));

        setting.add(reset_mysql);
        setting.add(reset_psw);

        JMenu help = new JMenu("帮助");
        add(help);

        JMenuItem helpme = new JMenuItem("帮助");
        helpme.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "请查看开发文档");
            }
        });
        helpme.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, KeyEvent.CTRL_DOWN_MASK));

        JMenuItem about = new JMenuItem("关于");
        about.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "版本:V1.0");
            }
        });
        about.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));

        help.add(helpme);
        help.addSeparator();
        help.add(about);
    }
}

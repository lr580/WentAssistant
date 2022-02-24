package ui;

import java.awt.event.*;
import javax.swing.*;
import plugin.EvalCtrl;
import plugin.SwingHelper;

public class RootMenu extends JMenuBar {
    public static RootMenu that = null;
    public JMenu switchs = new JMenu("模块切换");
    public ActionListener e_waiting = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            SwingHelper.syso("敬请期待 ovo");
        }
    };
    public static EvalCtrl m_add = new EvalCtrl();
    public static EvalCtrl m_update = new EvalCtrl();
    public static EvalCtrl m_remove = new EvalCtrl();
    private ActionListener e_add = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            m_add.eval_ctn();
        }
    };
    private ActionListener e_update = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            m_update.eval_ctn();
        }
    };
    private ActionListener e_remove = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            m_remove.eval_ctn();
        }
    };

    public RootMenu(Root frame) {
        that = this;
        JMenu file = new JMenu("文件");
        add(file);

        JMenuItem save = new JMenuItem("保存");
        save.addActionListener(TbGlobal.e_save);
        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));

        JMenuItem undo = new JMenuItem("撤销");
        undo.addActionListener(TbGlobal.e_undo);
        undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK));

        JMenuItem redo = new JMenuItem("重做");
        redo.addActionListener(TbGlobal.e_redo);
        redo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK));

        JMenuItem undoall = new JMenuItem("撤销全部");
        undoall.addActionListener(TbGlobal.e_undoall);
        undoall.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));

        JMenuItem add = new JMenuItem("插入项");
        add.addActionListener(e_add);
        add.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, KeyEvent.CTRL_DOWN_MASK));

        JMenuItem update = new JMenuItem("修改项");
        update.addActionListener(e_update);
        update.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, KeyEvent.CTRL_DOWN_MASK));

        JMenuItem remove = new JMenuItem("删除项");
        remove.addActionListener(e_remove);
        remove.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK));

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
        file.add(redo);
        file.add(undoall);
        file.addSeparator();
        file.add(add);
        file.add(update);
        file.add(remove);
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

        JMenuItem preference = new JMenuItem("偏好设置");
        preference.addActionListener(TbGlobal.e_preference);
        preference.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, KeyEvent.CTRL_DOWN_MASK));// 若ctrl+,不知为何用过后会失灵

        // JMenuItem mod_i = new JMenuItem("冰川");
        // mod_i.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I,
        // KeyEvent.CTRL_DOWN_MASK | KeyEvent.ALT_DOWN_MASK));
        // mod_i.addActionListener(e_waiting);

        // JMenuItem mod_d = new JMenuItem("QT频道");
        // mod_d.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D,
        // KeyEvent.CTRL_DOWN_MASK | KeyEvent.ALT_DOWN_MASK));
        // mod_d.addActionListener(e_waiting);

        // JMenuItem mod_t = new JMenuItem("计时任务");
        // mod_t.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T,
        // KeyEvent.CTRL_DOWN_MASK | KeyEvent.ALT_DOWN_MASK));
        // mod_t.addActionListener(e_waiting);

        // JMenuItem mod_m = new JMenuItem("备忘录");
        // mod_m.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M,
        // KeyEvent.CTRL_DOWN_MASK | KeyEvent.ALT_DOWN_MASK));
        // mod_m.addActionListener(e_waiting);

        // switchs.add(mod_i);
        // switchs.add(mod_d);
        // switchs.add(mod_t);
        // switchs.add(mod_m);

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

        JMenuItem del_database = new JMenuItem("删除数据库");
        del_database.addActionListener(TbGlobal.e_delDatabase);
        // del_database.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D,
        // KeyEvent.CTRL_DOWN_MASK | KeyEvent.ALT_DOWN_MASK |
        // KeyEvent.SHIFT_DOWN_MASK));
        // 这么危险的指令就不设置快捷键了，而且快捷键这么长很不美观

        setting.add(preference);
        setting.addSeparator();
        setting.add(switchs);
        setting.addSeparator();
        setting.add(reset_mysql);
        setting.add(reset_psw);
        setting.addSeparator();
        setting.add(del_database);

        JMenu help = new JMenu("帮助");
        add(help);

        JMenuItem helpme = new JMenuItem("帮助");
        helpme.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "请查看使用手册");
            }
        });
        helpme.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, KeyEvent.CTRL_DOWN_MASK));

        JMenuItem about = new JMenuItem("关于");
        about.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "版本:Beta2.0");
            }
        });
        about.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));

        help.add(helpme);
        help.addSeparator();
        help.add(about);
    }
}

package ui;

import java.awt.event.*;
import javax.swing.*;
import base.DbBackup;
import base.DbCtrl;
import base.DbLoad;
import base.ModLoad;
import category.ui.CatManager;
import java.io.File;
import java.awt.*;
import javax.swing.JFileChooser;

public class EvSupply {// 事件监听器提供
    public static void openCataCtrl() {
        new CatManager(ModLoad.nowModule, DbLoad.t_temp, DbLoad.cata);
    }

    public static ActionListener ev_openCataCtrl = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            openCataCtrl();
        }
    };

    public static void set_saved() {
        DbLoad.saved = 1;
        DbLoad.set_info(ModLoad.nowModule + "_saved", 1);
        Root.that.setTitle(Root.title);
    }

    public static ActionListener ev_set_saved = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            set_saved();
        }
    };

    public static void set_unsaved() {
        DbLoad.saved = 0;
        DbLoad.set_info(ModLoad.nowModule + "_saved", 0);
        Root.that.setTitle(Root.title + "*");
    }

    public static ActionListener ev_set_unsaved = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            set_unsaved();
        }
    };

    public static File select_file(Container ct, javax.swing.filechooser.FileFilter filter, boolean isOpen) {
        JFileChooser fc = new JFileChooser();
        if (filter != null) {
            fc.setFileFilter(filter);
        }
        fc.setCurrentDirectory(new File("."));
        int i;
        if (isOpen) {
            i = fc.showOpenDialog(ct);
        } else {
            i = fc.showSaveDialog(ct);
        }
        if (i != JFileChooser.APPROVE_OPTION) {
            return null;
        }
        File res = fc.getSelectedFile();
        if (!isOpen && res.exists()) {
            int j = JOptionPane.showConfirmDialog(null, "文件已存在，是否覆盖?", "提示", JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.INFORMATION_MESSAGE);
            if (j != JOptionPane.OK_OPTION) {
                return null;
            }
        }
        return res;
    }

    public static File select_file(Container ct, javax.swing.filechooser.FileFilter filter) {
        return select_file(ct, filter, true);
    }

    public static void check_abnormal_exit() {// 是否非正常退出
        if (!DbBackup.tempsame()) {// DbLoad.saved == 0 &&
            int i = JOptionPane.showConfirmDialog(null, "上次退出时未保存，是否保存上次的更改?", "提示", JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.INFORMATION_MESSAGE);
            if (i == JOptionPane.OK_OPTION) {
                DbBackup.replace(DbLoad.getTypex(), DbLoad.getTypex(false));
                DbCtrl.write_diary("保存了非正常退出的更改");
            } else if (i == JOptionPane.CANCEL_OPTION) {
                DbBackup.replace(DbLoad.getTypex(false), DbLoad.getTypex());
                DbCtrl.write_diary("撤销了非正常退出的更改");
            } else {
                Root.that.dispose();
                System.exit(0);
            }
            set_saved();
        }
    }
}

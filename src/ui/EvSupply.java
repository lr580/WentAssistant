package ui;

import java.awt.event.*;
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
        Root.that.setTitle(Root.title);
    }

    public static ActionListener ev_set_saved = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            set_saved();
        }
    };

    public static void set_unsaved() {
        DbLoad.saved = 0;
        Root.that.setTitle(Root.title + "*");
    }

    public static ActionListener ev_set_unsaved = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            set_unsaved();
        }
    };

    public static File select_file(Container ct, javax.swing.filechooser.FileFilter filter) {
        JFileChooser fc = new JFileChooser();
        if (filter != null) {
            fc.setFileFilter(filter);
        }
        fc.setCurrentDirectory(new File("."));
        if (fc.showOpenDialog(ct) != JFileChooser.APPROVE_OPTION) {
            return null;
        }
        return fc.getSelectedFile();
    }
}

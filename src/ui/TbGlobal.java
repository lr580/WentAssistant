package ui;

import javax.swing.*;
import javax.swing.filechooser.*;
import base.*;
import java.awt.*;
import java.awt.event.*;
import plugin.*;
import java.io.*;

public class TbGlobal extends JPanel {// tabbar global
    public static DbTable jt = null;
    public static JComboBox<String> jc = new JComboBox<>(DbBackup.backups);
    private static javax.swing.filechooser.FileFilter flits = new FileNameExtensionFilter("数据库文件(.sql)", "sql");
    public static EvalCtrl pro_save = new EvalCtrl() {
    };
    public static EvalCtrl pro_undoall = new EvalCtrl() {
    };
    public static EvalCtrl f_importcata = new EvalCtrl() {
    };
    public static EvalCtrl f_importdata = new EvalCtrl() {
    };

    public static ActionListener e_save = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            pro_save.eval_ctn();
            ProcessCtrl.save();
        }
    };// 可以被菜单栏复用

    public static ActionListener e_undo = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            ProcessCtrl.undo();
        }
    };

    public static ActionListener e_redo = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            ProcessCtrl.redo();
        }
    };

    public static ActionListener e_undoall = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (DbLoad.saved == 0) {
                int i = JOptionPane.showConfirmDialog(null, "这将会不可恢复地丢失尚未保存的全部更改,确认撤销吗", "提示",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.INFORMATION_MESSAGE);
                if (i != JOptionPane.OK_OPTION) {
                    return;
                }
                pro_undoall.eval_ctn();
                ProcessCtrl.undoall();
            }
        }
    };

    public static ActionListener e_backup = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            DbBackup.addbackup();
            jc.setSelectedIndex(jc.getItemCount() - 1);
        }
    };

    public static ActionListener e_frombackup = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            int i = jc.getSelectedIndex();
            if (i == -1) {
                JOptionPane.showMessageDialog(null, "您没有选中备份项");
                return;
            }

            int j = JOptionPane.showConfirmDialog(null, "这将会不可恢复地丢失当前表格,确认还原吗", "提示",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.INFORMATION_MESSAGE);
            if (j != JOptionPane.OK_OPTION) {
                return;
            }

            DbBackup.frombackup(i);
        }
    };

    public static ActionListener e_delbackup = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            int i = jc.getSelectedIndex();
            if (i == -1) {
                JOptionPane.showMessageDialog(null, "您没有选中备份项");
                return;
            }
            int j = JOptionPane.showConfirmDialog(null, "这将会不可恢复地永久删除该备份,确认删除吗", "提示",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.INFORMATION_MESSAGE);
            if (j != JOptionPane.OK_OPTION) {
                return;
            }
            DbBackup.delbackup(i);
            if (jc.getItemCount() > 0) {
                jc.setSelectedIndex(0);
            }
        }
    };

    public static ActionListener e_importall = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            int h = JOptionPane.showConfirmDialog(null, "这将会不可恢复地丢失当前的全部数据,确认导入吗", "警告",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.INFORMATION_MESSAGE);
            if (h != JOptionPane.OK_OPTION) {
                return;
            }

            JFileChooser jfc = new JFileChooser();
            jfc.setFileFilter(flits);
            jfc.setCurrentDirectory(new File("."));
            int i = jfc.showOpenDialog(Root.that.getContentPane());
            if (i != JFileChooser.APPROVE_OPTION) {
                return;
            }
            File f = jfc.getSelectedFile();
            DbIO.importall(f.toString());
            // 未检测sql文件的正确性，如果读取到不符合格式的文件会崩，此时需要手动删除数据库(或删掉infos的main)让程序启动初始化
        }
    };

    public static String get_newpath() {// 获取选择的文件路径(基于.sql筛选器)
        JFileChooser jfc = new JFileChooser();
        jfc.setFileFilter(flits);
        jfc.setCurrentDirectory(new File("."));
        int i = jfc.showSaveDialog(Root.that.getContentPane());
        if (i != JFileChooser.APPROVE_OPTION) {
            return null;
        }
        File f = jfc.getSelectedFile();
        if (f.exists()) {
            int j = JOptionPane.showConfirmDialog(null, "是否覆盖原有文件?", "提示",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.INFORMATION_MESSAGE);
            if (j != JOptionPane.OK_OPTION) {
                return null;
            }
        }
        return f.toString();
    }

    private static String addif_sql(String str) {// 智能补全后缀.sql
        if (null == str) {
            return null;
        }
        if (!str.substring(Math.max(0, str.length() - 4)).toLowerCase().equals(".sql")) {// 没有sql后缀
            str += ".sql";
        }
        return str;
    }

    public static ActionListener e_exportall = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            String path = addif_sql(get_newpath());
            if (null != path) {
                DbIO.exportall(path);
            }
        }
    };

    public static ActionListener e_export = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            String path = addif_sql(get_newpath());
            if (null != path) {
                DbIO.export(path);
            }
        }
    };

    public static ActionListener e_preference = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            new Preference();
        }
    };

    public static void delDatabase() {
        int j = JOptionPane.showConfirmDialog(null, "这将会不可还原地清空整个数据库，确认删除吗?", "警告",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.INFORMATION_MESSAGE);
        if (j != JOptionPane.OK_OPTION) {
            return;
        }

        int i = JOptionPane.showConfirmDialog(null, "再次警告，这将会不可还原地清空整个数据库，是否取消执行命令?", "警告",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.INFORMATION_MESSAGE);
        if (i != JOptionPane.CANCEL_OPTION) {
            return;
        }

        DbBackup.delDatabase();

        // 因为功能不常用，直接让重启即可，没必要真的重载一次相应代码
        SwingHelper.syso("清空成功，请重启本程序");
        DbCtrl.write_diary("清空数据库");
        DbCtrl.save_diary();
        System.exit(0);
    }

    public static ActionListener e_delDatabase = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            delDatabase();
        }
    };

    public static ActionListener e_importcata = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            f_importcata.eval_ctn();
        }
    };

    public static ActionListener e_importdata = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            f_importdata.eval_ctn();
        }
    };

    public TbGlobal(DbTable jt) {
        TbGlobal.jt = jt;
        setLayout(new GridLayout(2, 1, 5, 5));
        JPanel uf = new JPanel(new FlowLayout(1, 5, 5));
        add(uf);

        JButton b_save = new JButton("保存");
        b_save.addActionListener(e_save);
        uf.add(b_save);
        JButton b_undo = new JButton("撤销");
        b_undo.addActionListener(e_undo);
        uf.add(b_undo);
        JButton b_redo = new JButton("重做");
        uf.add(b_redo);
        JButton b_undoall = new JButton("撤销全部");
        uf.add(b_undoall);

        JButton b_importall = new JButton("导入数据库");
        b_importall.addActionListener(e_importall);
        uf.add(b_importall);

        JButton b_exportall = new JButton("导出所有数据");
        b_exportall.addActionListener(e_exportall);
        uf.add(b_exportall);

        JButton b_export = new JButton("导出当前表");
        b_export.addActionListener(e_export);
        uf.add(b_export);

        JButton b_delDatabase = new JButton("清空数据库");
        b_delDatabase.addActionListener(e_delDatabase);
        uf.add(b_delDatabase);

        JPanel df = new JPanel(new FlowLayout(1, 5, 5));
        add(df);

        JButton b_preference = new JButton("设置");
        b_preference.addActionListener(e_preference);
        df.add(b_preference);

        if (jc.getItemCount() > 0) {
            jc.setSelectedIndex(0);
        }
        df.add(new FsLabel("已有备份号:"));
        df.add(jc);

        JButton b_backup = new JButton("添加到备份");
        b_backup.addActionListener(e_backup);
        df.add(b_backup);

        JButton b_frombackup = new JButton("从备份还原");
        b_frombackup.addActionListener(e_frombackup);
        df.add(b_frombackup);

        JButton b_delbackup = new JButton("删除选中备份");
        b_delbackup.addActionListener(e_delbackup);
        df.add(b_delbackup);

        JButton b_importcata = new JButton("导入旧类别");
        b_importcata.addActionListener(e_importcata);
        df.add(b_importcata);

        JButton b_importdata = new JButton("导入旧数据");
        b_importdata.addActionListener(e_importdata);
        df.add(b_importdata);
    }
}

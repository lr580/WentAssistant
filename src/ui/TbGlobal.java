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
    public static JComboBox<String> jc = new JComboBox<>(DbLoader.backups);
    private static javax.swing.filechooser.FileFilter flits = new FileNameExtensionFilter("数据库文件(.sql)", "sql");
    public static ActionListener e_save = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            DbLoader.save();
        }
    };// 可以被菜单栏复用

    public static ActionListener e_undo = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (DbLoader.saved == 0) {
                int i = JOptionPane.showConfirmDialog(null, "这将会不可恢复地丢失尚未保存的全部更改,确认撤销吗", "提示",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.INFORMATION_MESSAGE);
                if (i != JOptionPane.OK_OPTION) {
                    return;
                }
                DbLoader.undo();
            }
        }
    };

    public static ActionListener e_backup = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            DbLoader.addbackup();
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
            if (DbLoader.saved == 0) {
                int j = JOptionPane.showConfirmDialog(null, "这将会不可恢复地丢失尚未保存的全部更改,确认还原吗", "提示",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.INFORMATION_MESSAGE);
                if (j != JOptionPane.OK_OPTION) {
                    return;
                }
            }
            DbLoader.frombackup(i);
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
            DbLoader.delbackup(i);
            if (jc.getItemCount() > 0) {
                jc.setSelectedIndex(0);
            }
        }
    };

    public static ActionListener e_importall = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (DbLoader.saved == 0) {
                int i = JOptionPane.showConfirmDialog(null, "这将会不可恢复地丢失尚未保存的全部更改,确认撤销吗", "提示",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.INFORMATION_MESSAGE);
                if (i != JOptionPane.OK_OPTION) {
                    return;
                }
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
            // 未检测sql文件的正确性，如果读取到不符合格式的文件会崩，此时需要手动删除数据库(或删掉info的main)让程序启动初始化
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

        JButton b_importall = new JButton("导入数据库");
        b_importall.addActionListener(e_importall);
        uf.add(b_importall);

        JButton b_exportall = new JButton("导出所有数据");
        b_exportall.addActionListener(e_exportall);
        uf.add(b_exportall);

        JButton b_export = new JButton("导出当前表");
        b_export.addActionListener(e_export);
        uf.add(b_export);

        JPanel df = new JPanel(new FlowLayout(1, 5, 5));
        add(df);

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
    }
}

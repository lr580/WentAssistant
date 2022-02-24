package ui;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import base.PrefManager;
import mysql.Ctrl;
import plugin.SwingHelper;
import java.awt.Font;
import java.sql.*;
import java.util.*;

public class DbTable extends JTable {
    private DefaultTableModel tm = new DefaultTableModel();
    public final static Map<String, String> h = new HashMap<>();// 列名英译中
    private String last_cmd = "";// 上一次渲染的语句
    public static DbTable that = null;
    public static int table_idx = 0;// 当前表格编号
    public static TableBlender blender = new TableBlender() {
    };
    public static InputFiller filler = new InputFiller() {
    };
    private static int n = 0;// 列数
    private static int ty[] = null;// 各列数据类型
    public static int tb_state = 0; // 表格状态(各模块自定义)
    public static TableUpdater updater = new TableUpdater() {
    }; // 默认不记录日志(因为含重做指令的可能性)

    public DbTable() {
        that = this;
        setModel(tm);
        DefaultTableCellRenderer crr = new DefaultTableCellRenderer();
        crr.setHorizontalAlignment(JLabel.CENTER);
        setDefaultRenderer(Object.class, crr);// 表格居中
        Font ft = new Font("宋体", Font.PLAIN, 16);
        setFont(ft);
        JTableHeader jh = getTableHeader();
        jh.setFont(ft);
        jh.setReorderingAllowed(false);
        setTableHeader(jh);
        setAutoCreateRowSorter(true);

        getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                int rows[] = getSelectedRows();
                if (rows.length == 0) {
                    return;
                }
                int row = rows[rows.length - 1];
                Object s[] = new Object[tm.getColumnCount()];
                for (int i = 0; i < s.length; ++i) {
                    s[i] = getValueAt(row, i);
                }
                if (PrefManager.pref.get("isFillAfterSelect").equals("1")) {
                    filler.fill(s);// 会触发两次，原因未知
                }
            }
        });
    }

    private void render(ResultSet res) {
        try {
            ResultSetMetaData reso = res.getMetaData();
            n = reso.getColumnCount();
            ty = new int[n + 1];
            tm.setColumnCount(0);
            tm.setRowCount(0);
            for (int i = 1; i <= n; ++i) {
                String colname = reso.getColumnName(i);
                tm.addColumn(h.get(colname));
                ty[i] = reso.getColumnType(i);// 不判断直接全部getString也行
            }
            while (res.next()) {
                tm.addRow(blender.blend(res, n, ty));// 根据模块需求动态改变
            }
        } catch (Exception e) {
            Ctrl.raised(e);
        }
    }

    public void render(String res) {
        last_cmd = res;
        render(Ctrl.query(res));
    }

    public void refresh() {
        render(last_cmd);
    }

    public static void fresh() {
        that.refresh();
    }

    public boolean isCellEditable(int row, int col) {
        return false;// 不支持双击编辑表格直接修改数据库
    }

    public void addRow(Object[] row) {
        tm.addRow(row);
    }

    public void setRow(int row, Object[] s) {
        for (int i = 0; i < s.length; ++i) {
            setValueAt(s[i], row, i);
        }
    }

    public static void updRow(int row, Object[] s) {
        that.setRow(row, s);
    }

    public static void delSeleRow() {// 删除所有选中行
        int rows[] = DbTable.that.getSelectedRows();
        // 确保删除后剩余待删除的下标不变,倒序
        for (int i = rows.length - 1; i >= 0; --i) {
            that.tm.removeRow(rows[i]);
        }
    }

    public static void delRow(int idx) {
        that.tm.removeRow(findRow(idx));
    }

    public static int findRow(int idx) {
        for (int i = 0, ie = that.tm.getRowCount(); i < ie; ++i) {
            int v = (Integer) that.tm.getValueAt(i, 0);
            if (v == idx) {
                return i;
            }
        }
        SwingHelper.syso("找不到编号为" + idx + "的列");
        return -1;
    }

    public static void updateRow(int idx, Object[] row) {// 假设已经blend了
        updRow(findRow(idx), row);
    }

    public static Object[] queryRow(int idx) {// 第idx行
        Object[] res = new Object[n];
        for (int i = 0; i < n; ++i) {
            res[i] = that.getValueAt(idx, i);
        }
        return res;
    }
}

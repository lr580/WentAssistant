package ui;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import mysql.Ctrl;
import java.awt.Font;
import java.sql.*;
import java.util.*;

public class DbTable extends JTable {
    private DefaultTableModel tm = new DefaultTableModel();
    public final static Map<String, String> h = new HashMap<>();// 列名英译中
    private String last_cmd = "";// 上一次渲染的语句
    public static DbTable that = null;
    public static int table_idx = 0;// 当前表格编号
    public static TableBlender default_blender = new TableBlender() {
        public Object[] blend(ResultSet res, int n, int[] ty) {
            Object[] row = new Object[n];
            try {
                for (int i = 1; i <= n; ++i) {
                    if (ty[i] == 4) {// int
                        row[i - 1] = (Integer) res.getInt(i);
                    } else if (ty[i] == 3) {// decimal
                        row[i - 1] = (Double) res.getDouble(i);
                    } else {// varchar(ty[i]==12)
                        row[i - 1] = res.getString(i);
                    }
                }
            } catch (Exception e) {
                Ctrl.raised(e);
                return null;
            }
            return row;
        }
    };
    public static TableBlender blender = default_blender;

    public DbTable() {
        // init_h();
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
                // 将当前选中的最下一行填入编辑框(待做)
                for (int i = 0; i < s.length; ++i) {
                    s[i] = getValueAt(row, i);
                }
                // SwingHelper.syso(s);// 调试输出
            }
        });

        // render("select * from " + DbLoad.getTypex());// 初始值
    }

    private void render(ResultSet res) {
        try {
            ResultSetMetaData reso = res.getMetaData();
            int n = reso.getColumnCount();
            int ty[] = new int[n + 1];
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
}

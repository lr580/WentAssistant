package ui;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import base.DbLoader;
import mysql.Ctrl;
import java.awt.Font;
import java.sql.*;
import java.util.*;

public class DbTable extends JTable {
    private DefaultTableModel tm = new DefaultTableModel();
    private final static Map<String, String> h = new HashMap<>();// 列名英译中
    private String last_cmd = "";// 上一次渲染的语句
    public static DbTable that = null;
    public static int table_idx = 0;// 当前表格编号,0学生,1课程,2成绩

    public DbTable() {
        init_h();
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
        // setAutoCreateRowSorter(true); 未来版本考虑实现,需要重构表格数据类型

        getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                int rows[] = getSelectedRows();
                if (rows.length == 0) {
                    return;
                }
                int row = rows[rows.length - 1];
                String s[] = new String[tm.getColumnCount()];
                // 将当前选中的最下一行填入编辑框
                for (int i = 0; i < s.length; ++i) {
                    s[i] = (String) getValueAt(row, i);
                }
                if (table_idx == 0) {
                    TbStu.upd_input(s);
                } else if (table_idx == 1) {
                    TbSubj.upd_input(s);
                } else if (table_idx == 2) {
                    TbSco.upd_input(s);
                }
            }
        });

        render("select * from student_" + DbLoader.t_temp);// 初始值
    }

    private void init_h() {
        h.put("id", "编号");
        h.put("name", "名字");
        h.put("student_id", "学生编号");
        h.put("student_number", "学号");
        h.put("major", "专业");
        h.put("semester", "学期");
        h.put("subject_id", "课程编号");
        h.put("value", "分数");
        h.put("key", "键");
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
                if (n == 5 && (i == 1 || i == 3)) {// 有两个"name"，特判区分
                    tm.addColumn(i == 1 ? "学生名" : "课程名");
                } else {
                    tm.addColumn(h.get(colname));
                }
                ty[i] = reso.getColumnType(i);// 不判断直接全部getString也行
            }
            while (res.next()) {
                String row[] = new String[n];
                for (int i = 1; i <= n; ++i) {
                    if (ty[i] == 4) {// int
                        row[i - 1] = Integer.toString(res.getInt(i));
                    } else {// varchar
                        row[i - 1] = res.getString(i);
                    }
                }
                tm.addRow(row);
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
        return false;// 暂时不支持双击编辑表格直接修改数据库
    }

    public void addRow(String[] row) {
        tm.addRow(row);
    }

    public void setRow(int row, String[] s) {
        for (int i = 0; i < s.length; ++i) {
            setValueAt(s[i], row, i);
        }
    }

    public static void updRow(int row, String[] s) {
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

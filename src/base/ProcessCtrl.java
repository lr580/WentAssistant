package base;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import mysql.Ctrl;

//注：Process系列均未调试
public class ProcessCtrl {// 撤销、重做管理
    public static List<ProcessCmd> s = new ArrayList<>();// 指令列表
    public static String tbname = null;// 数据库名
    public static int top = 0;// 当前栈顶
    public static int n = 0;// 指令列表长度
    public static int m = 0;// 数据域长度
    public static String cname[] = null;// 数据库列名
    public static int ty[] = null;// 数据类型
    // 3int 4decimal 12varchar

    public static void init() {
        tbname = DbLoad.getTypex();
        String cmd0 = "select * from " + tbname;
        ResultSet res0 = Ctrl.query(cmd0);
        try {
            ResultSetMetaData res1 = res0.getMetaData();
            m = res1.getColumnCount();
            ty = new int[m];
            cname = new String[m];
            for (int i = 1; i <= m; ++i) {
                ty[i - 1] = res1.getColumnType(i);
                cname[i - 1] = res1.getColumnName(i);
            }
        } catch (Exception e) {
            Ctrl.raised(e);
        }
        top = 0;
        n = 0;
        s.clear();
    }

    public static String getString(Object[] a, boolean bracket) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0, ie = a.length; i < ie; ++i) {
            sb.append(i == 0 ? "" : ",");
            sb.append(getString(a[i], i));
        }
        if (bracket) {
            sb.append(")");
            sb.insert(0, "(");
        }
        return sb.toString();
    }

    public static String getString(Object[] a) {
        return getString(a, true);
    }

    public static String getString(Object a, int i) {
        if (ty[i] == 12) {
            return "'" + a.toString() + "'";
        }
        return a.toString();
    }

    public static void undo() {
        if (top > 0) {
            --top;
            s.get(top).exec_inv();
        }
    }

    public static void redo() {
        if (top < n) {
            s.get(top).exec();
            ++top;
        }
    }

    public static void push(ProcessCmd cmd) {
        while (n > top) {
            s.remove(n - 1);
            --n;
        }
        cmd.exec();
        s.add(cmd);
        ++n;
        ++top;
    }

    public static void save() {
        tbname = ModLoad.nowModule + "_" + DbLoad.t_main;
        for (int i = 0; i < top; ++i) {
            s.get(i).exec();
        }
        tbname = DbLoad.getTypex();
        n = 0;
        top = 0;
    }

    public static void undoall() {
        for (int i = top - 1; i >= 0; --i) {
            s.get(i).exec_inv();
        }
        top = 0;
    }
}

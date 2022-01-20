package mysql;

import java.sql.*;
import plugin.SwingHelper;

public class Ctrl {
    public static void raised(Exception e) {
        if (Link.con == null) {
            return;
        }
        Link.err_msg = e.getMessage();
        SwingHelper.syso(e.getMessage());
    }

    public static boolean run(String cmd) {
        try {
            Link.cmd.execute(cmd);
            return true;
        } catch (Exception e) {
            raised(e);
            return false;
        }
    }

    public static boolean update(String cmd) {
        try {
            Link.cmd.executeUpdate(cmd);
            return true;
        } catch (Exception e) {
            raised(e);
            return false;
        }
    }

    public static ResultSet query(String cmd) {
        try {
            return Link.cmd.executeQuery(cmd);
        } catch (Exception e) {
            raised(e);
            return null;
        }
    }

    public static boolean exists(ResultSet res, String col, String key) {
        try {
            while (res.next()) {
                String tmp = res.getString(col);
                if (tmp.equals(key)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            raised(e);
            return false;
        }
    }

    public static PreparedStatement pre(String cmd) {
        try {
            return Link.con.prepareStatement(cmd);
        } catch (Exception e) {
            raised(e);
            return null;
        }
    }

    public static int getv(ResultSet res, String col, String key, String value) {
        try {
            while (res.next()) {
                String tmp = res.getString(col);
                if (tmp.equals(key)) {
                    return res.getInt(value);
                }
            }
            return -1;
        } catch (Exception e) {
            raised(e);
            return -1;
        }
    }

    public static int getv(ResultSet res) {// 从单一数据结果获取整数值
        try {
            while (res.next()) {
                return res.getInt(1);
            }
            return -1;
        } catch (Exception e) {
            raised(e);
            return -1;
        }
    }

    public static int getv(String cmd) {
        return getv(query(cmd));
    }
}

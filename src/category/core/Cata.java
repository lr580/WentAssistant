package category.core;

import java.sql.ResultSet;
import mysql.Ctrl;

public class Cata {// 数据库与类别数据的增删改查
    private static boolean add(String typex, String nr) { // 注意 typex 是 type_x 下同
        String cmd = "insert ignore into `category` (`key`, `value`) values ('" + typex + "', '" + nr + "')";
        return Ctrl.update(cmd);
    }

    public static boolean add(String typex) {
        return add(typex, "0 0");
    }

    public static boolean update(String typex, String nr) {
        String cmd = "update `category` set `value` = '" + nr + "' where `key` = '" + typex + "'";
        return Ctrl.update(cmd);
    }

    public static boolean delete(String typex) {
        String cmd = "delete from `category` where `key` = '" + typex + "'";
        return Ctrl.update(cmd);
    }

    public static String query(String typex) {
        String cmd = "select `value` from `category` where `key` = '" + typex + "'";
        ResultSet res = Ctrl.query(cmd);
        try {
            res.next();
            return res.getString("value");
        } catch (Exception e) {
            Ctrl.raised(e);
            return "0 0";
        }
    }
}

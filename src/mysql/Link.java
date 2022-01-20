package mysql;

import java.sql.*;
import base.Init;

public class Link {
    public static int version = 8;// mysql桥版本,5或8,当前版本不支持5
    public static boolean loaded = false;
    public static Connection con = null;
    public static Statement cmd = null;
    public static String err_msg = "";// 连接失败报错信息

    public static boolean load() {
        if (loaded) {
            return true;
        }
        try {
            if (version == 8) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                loaded = true;
            } else if (version == 5) {
                Class.forName("com.mysql.jdbc.Driver");
                loaded = true;
            } else {
                throw new Exception("wrong mysql version");
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void create_database(String ip, String port, String db, String name, String psw) {// 不存在就创建，存在不会覆盖，忽略已存在报错
        String cmd = "mysqladmin -h " + ip + " -u " + name + " -p" + psw + " create " + db;
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (Exception e) {
        }
    }

    public static boolean connect(String ip, String port, String db, String name, String psw, String cfg) {
        if (!load()) {
            return false;
        }
        try {
            if (con != null) {
                con.close();
            }
            create_database(ip, port, db, name, psw);

            String url = "jdbc:mysql://" + ip + ":" + port + "/" + db;
            if (cfg.length() != 0) {
                url += "?" + cfg;
            }

            con = DriverManager.getConnection(url, name, psw);
            cmd = con.createStatement();
            return true;
        } catch (Exception e) {
            err_msg = e.getMessage();
            return false;
        }
    }

    public static boolean connect() {
        String res[] = Init.read_db_settings();
        if (res.length <= 6) {
            err_msg = "配置项/data/setting.txt有误";
            return false;
        }
        return connect(res[1], res[2], res[3], res[4], res[5], res[6]);
    }
}

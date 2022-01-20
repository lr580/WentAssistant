package base;

import mysql.SqlIO;
import ui.DbTable;
import ui.Root;

public class DbIO {
    public static void importall(String path) {
        String[] s = Init.read_db_settings();
        SqlIO.importAll(s[1], s[2], s[3], s[4], s[5], path);
        // Ctrl.run("drop database if exists `" + s[3] + "`;");
        // Ctrl.run("use `" + s[3] + "`;");
        // DbLoader.del_all_table();
        DbLoader.checkinit();
        DbLoader.saved = 1;
        DbLoader.set_info("saved", 1);
        Root.updateTitle();
        try {
            Thread.sleep(1000);// 可能是数据库异步，必须等待片刻加载才生效
        } catch (Exception e) {
            e.printStackTrace();
        }
        DbTable.fresh();
    }

    public static void exportall(String path) {
        String[] s = Init.read_db_settings();
        SqlIO.exportAll(s[1], s[2], s[3], s[4], s[5], path);
    }

    public static void export(String path) {
        String i = Integer.toString(DbLoader.t_temp);
        String t[] = { "student_" + i, "subject_" + i, "score_" + i };
        String[] s = Init.read_db_settings();
        SqlIO.export(s[1], s[2], s[3], s[4], s[5], path, t);
    }
}

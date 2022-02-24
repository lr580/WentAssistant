package mysql;

import java.sql.ResultSet;

public class Extend {// 数据库辅助功能
    public static boolean isSame(String name1, String name2) {
        long v1, v2;
        try {
            // 特别注意resultset不能同时共存，找下一个时上一个会closed
            // 且注意到查询时得到两列(Table Checksum)
            ResultSet res1 = Ctrl.query("checksum table `" + name1 + "`");
            res1.next();
            v1 = res1.getLong("Checksum");
            ResultSet res2 = Ctrl.query("checksum table `" + name2 + "`");
            res2.next();
            v2 = res2.getLong("Checksum");
        } catch (Exception e) {
            Ctrl.raised(e);
            return false;
        }
        return v1 == v2;
    }

    public static boolean overwrite(String from, String to) {
        boolean s1 = Ctrl.run("drop table if exists `" + to + "`");
        if (!s1) {
            return false;
        }
        boolean s2 = Ctrl.run("create table `" + to + "` select * from `" + from + "`");
        return s1 && s2;
    }
}

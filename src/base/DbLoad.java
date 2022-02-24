package base;

import category.core.CatTree;
import category.core.Cata;
import mysql.*;
import java.sql.*;
import java.util.*;

public class DbLoad {
    private static PreparedStatement info_adder = null;
    private static PreparedStatement info_changer = null;
    public static int saved = 1;
    public static int t_main = 0;// 主表
    public static int t_temp = 0;// 临时表
    public static int top = 0;// 最大表编号
    public static int cnt = 0;// 最大记录编号
    public static Map<String, String> table_creator = new HashMap<>();
    public static CatTree cata = null;

    public static void init() {// 检查是否需要初始化，是则自动执行
        String cmd0 = "create table if not exists `infos` (`id` int not null auto_increment, `key` varchar(20) not null, `value` int not null, primary key(`id`), unique(`key`)) engine=InnoDB default charset=utf8;";
        Ctrl.run(cmd0);

        String cmd1 = "create table if not exists `category` (`id` int not null auto_increment, `key` varchar(20) not null, `value` text not null, primary key(`id`), unique(`key`)) engine=InnoDB default charset=utf8;";
        Ctrl.run(cmd1);

        String ccmd1 = "insert ignore into `infos` (`key`, `value`) values (?, ?)";
        info_adder = Ctrl.pre(ccmd1);

        String ccmd2 = "update `infos` set `value`= ? where `key` = ?";
        info_changer = Ctrl.pre(ccmd2);
    }

    public static void add_info(String key, int value) {
        try {
            info_adder.setString(1, key);
            info_adder.setInt(2, value);
            info_adder.executeUpdate();
        } catch (Exception e) {
            Ctrl.raised(e);
        }
    }

    public static void set_info(String key, int value) {
        try {
            info_changer.setInt(1, value);
            info_changer.setString(2, key);
            info_changer.executeUpdate();
        } catch (Exception e) {
            Ctrl.raised(e);
        }
    }

    public static int get_info(String key) {
        String cmd1 = "select `key`, `value` from `infos` where `key`='" + key + "';";
        ResultSet res1 = Ctrl.query(cmd1);
        return Ctrl.getv(res1, "key", key, "value");
    }

    public static void del_info(String key) {
        String cmd = "delete from `infos` where `key` = '" + key + "';";
        Ctrl.update(cmd);
    }

    public static void create_table(String type) {// 表格名为type的表格初始化
        if (-1 != get_info(type + "_main")) {
            return;
        }
        add_info(type + "_1", 0);
        add_info(type + "_2", 0);
        add_info(type + "_top", 2);
        add_info(type + "_saved", 1);
        add_info(type + "_main", 1);
        add_info(type + "_temp", 2);
        Cata.add(type + "_1");
        Cata.add(type + "_2");
        String cmd_crtb = table_creator.get(type);
        for (int i = 1; i <= 2; ++i) {
            String cmd = cmd_crtb;
            cmd = cmd.replace("#", Integer.toString(i));
            Ctrl.run(cmd);
        }
    }

    public static void load_table(String type) {
        t_main = get_info(type + "_main");
        t_temp = get_info(type + "_temp");
        top = get_info(type + "_top");
        saved = get_info(type + "_saved");
        cnt = get_info(getTypex());
        cata = new CatTree(Cata.query(type + "_" + t_temp));
        DbBackup.get_backups();
    }

    public static String getTypex() {
        return ModLoad.nowModule + "_" + t_temp;
    }

    public static String getTypex(boolean isTemp) {
        if (isTemp) {
            return getTypex();
        }
        return ModLoad.nowModule + "_" + t_main;
    }
}

package base;

import mysql.SqlIO;
import ui.EvSupply;
// import ui.DbTable;

public class DbIO {
    public static void importall(String path) {
        String[] s = Init.read_db_settings();
        SqlIO.importAll(s[1], s[2], s[3], s[4], s[5], path);
        try {
            Thread.sleep(1000);// 可能是数据库异步，必须等待片刻加载才生效
        } catch (Exception e) {
            e.printStackTrace();
        }
        // DbLoad.init();
        // DbLoad.load_table(ModLoad.nowModule);
        ModLoad.startModule(); // 包含了processctrl.init
        EvSupply.set_saved();
        // DbTable.that.render("select * from " + DbLoad.getTypex());
    }

    public static void exportall(String path) {
        String[] s = Init.read_db_settings();
        SqlIO.exportAll(s[1], s[2], s[3], s[4], s[5], path);
    }

    public static void export(String path) {
        String t[] = { DbLoad.getTypex() };
        String[] s = Init.read_db_settings();
        SqlIO.export(s[1], s[2], s[3], s[4], s[5], path, t);
    }
}

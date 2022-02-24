package base;

import mysql.Extend;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import category.core.Cata;
import java.sql.ResultSet;
import mysql.Ctrl;
import ui.DbTable;
import ui.EvSupply;
import ui.TbGlobal;

public class DbBackup {// 保存撤销的其他拓展，导入导出，备份还原实现类
    public static Vector<String> backups = new Vector<>();// 备份编号

    public static boolean tempsame() {// 检查主表和临时表是否一致
        return Extend.isSame(DbLoad.getTypex(false), DbLoad.getTypex());
    }

    public static void get_backups() {// 以模块名字筛选可以实现每个模块各自的备份不同
        backups.clear();
        ResultSet res = Ctrl.query("select `key` from infos where `key` like '" + ModLoad.nowModule + "_%'");
        Pattern p = Pattern.compile("\\d+");
        try {
            while (res.next()) {
                String tmp = res.getString(1);
                tmp = tmp.substring(tmp.indexOf("_") + 1);
                Matcher m = p.matcher(tmp);
                if (!m.find()) {
                    continue;
                }
                int v = Integer.valueOf(tmp);
                if (v == DbLoad.t_main || v == DbLoad.t_temp) {
                    continue;
                }
                backups.addElement(tmp);
            }
        } catch (Exception e) {
            Ctrl.raised(e);
        }
        if (backups.size() > 0) {
            TbGlobal.jc.setSelectedIndex(0);
        }
    }

    private static void overset_infos(String from, String to) {
        // infos覆盖和cata覆盖
        Ctrl.run("insert ignore into `infos` (`key`, `value`) values ('" + to + "', 0)");
        int v = Ctrl.getv("select `value` from `infos` where `key` = '" + from + "'");
        Ctrl.run("update `infos` set `value`= " + v + " where `key` = '" + to + "'");

        String nr = Cata.query(from);
        // System.out.println(to);
        Cata.add(to);
        Cata.update(to, nr);

    }

    public static void replace(String from, String to) {
        Extend.overwrite(from, to);
        overset_infos(from, to);
    }

    public static void addbackup() {
        ++DbLoad.top;
        String from = DbLoad.getTypex();
        String to = ModLoad.nowModule + "_" + DbLoad.top;
        // Extend.overwrite(from, to);
        // overset_infos(from, to);
        replace(from, to);
        DbLoad.set_info(ModLoad.nowModule + "_top", DbLoad.top);
        backups.addElement(Integer.toString(DbLoad.top));
        DbCtrl.write_diary("备份当前表为第" + DbLoad.top + "号表");
    }

    public static void frombackup(int i) {
        int v = Integer.valueOf(backups.elementAt(i));
        String from = ModLoad.nowModule + "_" + v;
        String to = DbLoad.getTypex();
        // Extend.overwrite(from, to);
        // overset_infos(from, to);
        ProcessCtrl.save();// 置空
        replace(from, to);
        replace(to, DbLoad.getTypex(false));
        DbTable.fresh();
        EvSupply.set_saved();
        DbCtrl.write_diary("用第" + v + "号备份表覆盖当前表");
    }

    private static void del_table(String typex) {
        Ctrl.run("drop table if exists `" + typex + "`");
        Ctrl.run("delete from `infos` where `key` = '" + typex + "'");
        Cata.delete(typex);
    }

    public static void delbackup(int i) {
        String v = backups.elementAt(i);
        backups.remove(v);
        del_table(ModLoad.nowModule + "_" + v);
        DbCtrl.write_diary("删除第" + v + "号备份表");
        // System.out.println(backups.size());
        if (backups.size() == 0) {
            // TbGlobal.jc.setSelectedItem("");//不行
            TbGlobal.jc.setSelectedIndex(-1);
        }
    }

    // public static void main(String[] args) {
    // String tmp = "fin_3";
    // System.out.println(tmp.substring(tmp.indexOf("_") + 1));
    // }
}

package finance.core;

import java.util.Map;
import java.util.Vector;
import java.sql.ResultSet;
import base.DbLoad;
import finance.ui.Tabbar;
import mysql.Ctrl;
import ui.DbTable;
import ui.TableBlender;

public class Load {
    public static Vector<String> cat_list = new Vector<>();

    public static void update_catlist() {
        DbLoad.cata.getSortedNode(cat_list);
        Tabbar.i_type.setSelectedIndex(0);
    }

    public static void Read() {
        Tabbar.InitTabbar();

        Map<String, String> h_tb = DbTable.h;
        h_tb.clear();
        h_tb.put("id", "编号");
        h_tb.put("value", "金额(元)");
        h_tb.put("type", "类别");
        h_tb.put("date", "日期");
        h_tb.put("comment", "备注");

        update_catlist();

        DbTable.blender = new TableBlender() {
            public Object[] blend(ResultSet res, int n, int[] ty) {
                Object[] row = new Object[n];
                try {
                    row[0] = res.getInt(1);
                    row[1] = res.getDouble(2);
                    row[2] = DbLoad.cata.p.get(res.getInt(3)).name;
                    row[3] = Supply.Date2Str(res.getInt(4));
                    row[4] = res.getString(5);
                    return row;
                } catch (Exception e) {
                    Ctrl.raised(e);
                    return null;
                }
            }
        };
    }
}

package finance.core;

import java.util.Map;
import java.util.Vector;
import base.DbLoad;
import finance.ui.Tabbar;
import ui.DbTable;

public class Load {
    public static Vector<String> cat_list = new Vector<>();

    public static void update_catlist() {
        DbLoad.cata.getSortedNode(cat_list);
        Tabbar.i_type.setSelectedIndex(0);
    }

    public static void Read() {
        Map<String, String> h_tb = DbTable.h;
        h_tb.clear();
        h_tb.put("id", "编号");
        h_tb.put("value", "金额");
        h_tb.put("type", "类别");
        h_tb.put("date", "日期");
        h_tb.put("comment", "备注");

        update_catlist();
    }
}

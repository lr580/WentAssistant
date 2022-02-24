package finance.core;

import java.awt.Container;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import base.DbCtrl;
import base.DbLoad;
import base.ProcessCmd;
import base.ProcessCtrl;
import category.core.CatImport;
import category.core.CatTree;
import category.core.Cata;
import finance.ui.Tabbar;
import plugin.FileHelper;
import plugin.SwingHelper;
import ui.EvSupply;

//这里的操作不设置保存撤销，当未保存状态时不允许操作
//类别强制覆写可能会带来不可逆的影响
//假定输入一定正确
public class OldSupporter {// 旧版本兼容导入
    // private javax.swing.filechooser.FileFilter
    public static void import_cata(Container ct) {
        // 该导入会强制覆盖当前类别(设计时没有考虑实现树合并，虽然不难实现)
        if (DbLoad.saved == 0) {
            SwingHelper.syso("请先保存当前更改再导入");
            return;
        }
        String res = FileHelper.read(EvSupply.select_file(ct, null));
        if (res == null) {
            return;
        }
        res = CatImport.load(res);
        DbLoad.cata = new CatTree(res);
        Cata.update(DbLoad.getTypex(), res);
        // Cata.update(DbLoad.getTypex(false), res);
        EvSupply.set_unsaved();
        DbLoad.cata.getSortedNode(Load.cat_list);
        Tabbar.i_type.setSelectedIndex(0);
        DbCtrl.write_diary("导入了旧版本类别数据");
    }

    public static void import_data(Container ct) {
        if (DbLoad.saved == 0) {
            SwingHelper.syso("请先保存当前更改再导入");
            return;
        }
        String res = FileHelper.read(EvSupply.select_file(ct, null));
        if (res == null) {
            return;
        }
        data_convert(res);
    }

    private static void data_convert(String old) {
        String regex = "\\d+: \\['(-?\\d+.?\\d*)', \\[(\\d+)\\], \\d+, '([^']*)', '(\\d+)', False, 'default.cco', '\\d+'\\]";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(old);
        DbCtrl.write_diary("导入了旧版本账单数据");
        while (m.find()) {
            double money = Double.parseDouble(old.substring(m.start(1), m.end(1)));
            int type = 1 + Integer.parseInt(old.substring(m.start(2), m.end(2)));// 新旧版本的数据不一样
            String comment = old.substring(m.start(3), m.end(3));
            int date = Integer.parseInt(old.substring(m.start(4), m.end(4)));
            Object[] from = new Object[5];
            from[1] = money;
            from[2] = type;
            from[3] = date;
            from[4] = comment;
            ProcessCmd cmd = new ProcessCmd(1, from);
            ProcessCtrl.push(cmd);
        }
    }
}

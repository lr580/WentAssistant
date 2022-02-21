package finance.core;

import java.awt.event.*;
import javax.swing.*;
import base.DbLoad;
import base.ModLoad;
import ui.RootMenu;

public class Init {// 负责模块初始化
    public static String modname = "fin";// 模块名
    public static String modabs = "f";// 模块简称

    public static void InitModule() {
        ModLoad.modnames.add(modname);

        JMenuItem mod_f = new JMenuItem("财政官");
        mod_f.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK | KeyEvent.ALT_DOWN_MASK));
        RootMenu.that.switchs.add(mod_f);

        String cmd_tbcr = "create table if not exists `fin_#` ( `id` int not null auto_increment, `value` decimal(16,2) not null, `type` int not null, `date` int not null, `comment` varchar(100), primary key(`id`)) engine=InnoDB default charset=utf8;";
        DbLoad.table_creator.put(modname, cmd_tbcr);
    }
}

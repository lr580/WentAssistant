package base;

import java.util.*;
import plugin.EvalCtrl;
import ui.DbTable;
import ui.EvSupply;

public class ModLoad {// 各模块加载并挂入当前选择模块
    public static Set<String> modnames = new HashSet<>();
    public static String nowModule = "default";// 事实上用default为nowModule其他地方的函数调用会bug
    public static EvalCtrl evalctrl = new EvalCtrl();

    public static void loadModule() {
        finance.core.Init.InitModule();

        loadDb();
        startModule();
        // loadDb2();
    }

    private static void loadDb() {// 加载完全部模块后的后继事件
        Iterator<String> it = modnames.iterator();
        while (it.hasNext()) {// 必须考虑初始空数据库建表
            DbLoad.create_table(it.next());// 目前唯一调用create_table的地方
        }
    }

    // private static void loadDb2() {// 载入完所选模块后的后继事件
    // // DbLoad.saved = 0;// 调试
    // // EvSupply.check_abnormal_exit();
    // }

    private static void startModule() {
        nowModule = PrefManager.pref.get("nowModule");
        DbLoad.load_table(nowModule);
        EvSupply.check_abnormal_exit();

        if (nowModule.equals("fin")) {
            finance.core.Load.Read();
        }

        if (PrefManager.pref.get("isFirstShow").equals("1")) {
            DbTable.that.render("select * from " + DbLoad.getTypex());
        }
    }
}

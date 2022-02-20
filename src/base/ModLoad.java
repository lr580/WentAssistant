package base;

import java.util.*;
import plugin.EvalCtrl;
import ui.DbTable;

public class ModLoad {// 各模块加载并挂入当前选择模块
    public static Set<String> modnames = new HashSet<>();
    public static String nowModule = "default";
    // public static String hanging_cmd = "";// 将被执行指令悬挂
    public static EvalCtrl evalctrl = new EvalCtrl();

    public static void loadModule() {
        finance.core.Init.InitModule();

        startModule();
    }

    private static void startModule() {
        nowModule = PrefManager.pref.get("nowModule");
        DbLoad.load_table(nowModule);

        if (nowModule.equals("fin")) {
            finance.core.Load.Read();
        }

        DbTable.that.render("select * from " + DbLoad.getTypex());
    }

    // public static void exec() {
    // if (hanging_cmd.length() != 0) {
    // eval
    // }
    // hanging_cmd = "";
    // if (evalctrl != null) {
    // evalctrl.eval();
    // }
    // evalctrl = null;
    // }
}

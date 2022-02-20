package base;

import java.util.*;

public class ModLoad {// 各模块加载并挂入当前选择模块
    public static Set<String> modnames = new HashSet<>();
    public static String nowModule = "default";

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
    }
}

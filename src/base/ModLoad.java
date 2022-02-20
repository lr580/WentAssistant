package base;

import java.util.*;

public class ModLoad {// 各模块加载
    public static Set<String> modnames = new HashSet<>();

    public static void loadModule() {
        finance.core.Init.InitModule();
    }
}

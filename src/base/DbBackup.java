package base;

import mysql.Extend;

public class DbBackup {// 保存撤销的其他拓展，导入导出，备份还原和
    public static boolean tempsame() {// 检查主表和临时表是否一致
        return Extend.isSame(DbLoad.getTypex(false), DbLoad.getTypex());
    }
}

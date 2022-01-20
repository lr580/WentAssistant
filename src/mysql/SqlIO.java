package mysql;

public class SqlIO {
    private static String get_export_head(String ip, String port, String name, String psw) {
        return "cmd /c mysqldump -u" + name + " -p" + psw + " -h" + ip + " -P" + port + " ";
    }

    public static void exportAll(String ip, String port, String db, String name, String psw, String path) {
        String cmd = get_export_head(ip, port, name, psw) + " " + db + " > " + path;
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (Exception e) {
            Ctrl.raised(e);
        }
    }

    public static void export(String ip, String port, String db, String name, String psw, String path, String dt[]) {
        String cmd = get_export_head(ip, port, name, psw) + " " + db;
        for (String i : dt) {
            cmd += " " + i;
        }
        cmd += " >" + path;
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (Exception e) {
            Ctrl.raised(e);
        }
    }

    public static void importAll(String ip, String port, String db, String name, String psw, String path) {
        String cmd = "cmd /c mysql -h " + ip + " -P " + port + " -u " + name + " -p" + psw + " " + db + " < "
                + path;
        // cmd = cmd.replaceAll("\\\\", "\\\\\\\\");
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (Exception e) {
            Ctrl.raised(e);
        }
    }
}

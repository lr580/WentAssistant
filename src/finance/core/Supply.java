package finance.core;

public class Supply {// 库辅助函数
    public static String Date2Str(int x) {
        int day = x % 100;
        x /= 100;
        int month = x % 100;
        x /= 100;
        int year = 2000 + x % 100;
        return year + "年" + month + "月" + day + "日";
    }

    public static int Str2Date(String s) {
        int idx_y = s.indexOf("年");
        int idx_m = s.indexOf("月");
        int idx_d = s.indexOf("日");
        int res = Integer.parseInt(s.substring(idx_m + 1, idx_d));
        res += 100 * Integer.parseInt(s.substring(idx_y + 1, idx_m));
        res += 10000 * (Integer.parseInt(s.substring(0, idx_y)) % 100);
        return res;
    }
}
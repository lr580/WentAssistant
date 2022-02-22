package finance.core;

import java.util.Date;
import base.DbLoad;
import plugin.DateHelper;

import java.text.SimpleDateFormat;

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

    public static int Now2Date() {
        Date now = new Date();
        String res = String.format("%tY%tm%td", now, now, now);
        return Integer.parseInt(res) % (1000000);
        // SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        // System.out.println(sdf.format(new Date()));
    }

    public static SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");

    public static int DateDelta(int now, int dt) {
        try {
            Date no = sdf.parse(Integer.toString(now));
            Date res = DateHelper.timeDelta(no, dt);
            return Integer.parseInt(sdf.format(res));
        } catch (Exception e) {
            return 0;
        }
    }

    // public static Date Date2TD(int x) {
    // int d = x % 100;
    // int m = (x / 100) % 100;
    // int y = 2000 + (x / 10000) % 100;
    // Date res = new Date();
    // }

    // public static int DateDelta(int d, int dt) {

    // }

    public static void queryModify(Object[] obj) {// 对获取到的一列调整为数据库格式
        obj[2] = DbLoad.cata.find((String) obj[2]);
        obj[3] = Str2Date((String) obj[3]);
    }

    public static int getMultiInput(double[] money, int[] date, int[] type, String[] comment, String s_money,
            String s_date, int s_type, String s_comment, String s_cmd) {
        String[] a_money = s_money.split(" ");
        String[] a_date = s_date.split(" ");
        String[] a_type = s_cmd.split(" ");
        String[] a_comment = s_comment.split(" ");

        int n = a_money.length;
        money = new double[n];
        date = new int[n];
        type = new int[n];
        comment = new String[n];

        if (a_date.length >= 2 && a_date.length <= 3 && a_date[1].charAt(0) == '+') {// 周期
            int x = Integer.parseInt(a_date[1]);
            int y = 1;
            if (a_date.length == 3) {
                y = Integer.parseInt(a_date[2]);
            }
        }

        return 0;
    }

    public static void main(String[] args) {
        System.out.println(DateDelta(220222, -1000));
    }
}

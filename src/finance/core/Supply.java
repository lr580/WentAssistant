package finance.core;

import java.util.Date;
import base.DbLoad;
import finance.ui.Tabbar;
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

    public static void queryModify(Object[] obj) {// 对获取到的一列调整为数据库格式
        obj[2] = DbLoad.cata.find((String) obj[2]);
        obj[3] = Str2Date((String) obj[3]);
    }

    public static int getMultiInput(String s_money, String s_date, int s_type, String s_comment, String s_cmd) {
        String[] a_money = s_money.split(" ");
        String[] a_date = s_date.split(" ");
        String[] a_type = s_cmd.split(" ");
        String[] a_comment = s_comment.split(" ");

        int n = a_money.length;
        double[] money = new double[n];
        int[] date = new int[n];
        int[] type = new int[n];
        String[] comment = new String[n];

        for (int i = 0; i < n; ++i) {
            money[i] = Double.parseDouble(a_money[i]);
        }

        int n_date = a_date.length;
        if (a_date.length >= 2 && a_date.length <= 3 && a_date[1].charAt(0) == '+') {// 周期
            int v = Integer.parseInt(a_date[0]);
            int x = Integer.parseInt(a_date[1]);
            int y = 1;
            if (a_date.length == 3) {
                y = Integer.parseInt(a_date[2]);
            }
            for (int i = 0, j = 0; i < n; ++i) {
                date[i] = v;
                if (++j == x) {
                    j = 0;
                    v = DateDelta(v, y);
                }
            }
        } else {
            for (int i = 0, j = 0; i < n; ++i, j = (j + 1) % n_date) {
                date[i] = Integer.parseInt(a_date[j]);
            }
        }

        if (a_type[0].equals("-C")) {
            for (int i = 0, j = 1; i < n; ++i) {
                type[i] = DbLoad.cata.find(a_type[j]);
                if (++j == a_type.length) {
                    j = 1;
                }
            }
        } else {
            for (int i = 0; i < n; ++i) {
                type[i] = s_type;
            }
        }

        int n_comment = a_comment.length;
        String[] b_comment = new String[n_comment];
        int t_comment = 0;
        for (int i = 0; i < n_comment; ++i) {
            if (a_comment[i].length() == 0) {
                continue;
            }
            int tag = a_comment[i].indexOf("|");
            if (tag == -1) {
                comment[i] = a_comment[i].substring(0, a_comment[i].length());
                continue;
            }
            if (tag + 1 == a_comment[i].length()) { // |
                b_comment[t_comment++] = a_comment[i].substring(0, a_comment[i].length() - 1);
            } else { // |x
                int x = Integer.parseInt(a_comment[i].substring(tag + 1));
                comment[x] = a_comment[i].substring(0, tag);
            }
        }
        if (t_comment == 0) {
            t_comment = 1;
            b_comment[0] = "";
        }
        for (int i = 0, j = 0; i < n; ++i) {
            if (comment[i] != null) {// comment[i].length() != 0
                continue;
            }
            comment[i] = b_comment[j];
            if (++j == t_comment) {
                j = 0;
            }
        }

        Tabbar.money = money;
        Tabbar.date = date;
        Tabbar.type = type;
        Tabbar.comment = comment;

        return n;
    }

    // public static void main(String[] args) {
    // System.out.println(DateDelta(220222, -1000));
    // }

    // public static void test(int[] x) {
    // x = new int[1];
    // x[0] = 1;
    // }

    // public static void main(String[] args) {
    // int y[] = null;
    // test(y);
    // System.out.println(y == null);
    // }
}

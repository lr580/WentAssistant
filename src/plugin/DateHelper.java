package plugin;

import java.util.Calendar;
import java.util.Date;

public class DateHelper {
    public static boolean isLeap(int y) { // 未测试
        return y % 4 == 0 && y % 100 != 0 || y % 400 == 0;
    }

    public static int bfmo[] = { 0, 0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334, 365 };

    public static int days(int y, int m, int d) {// 返回当前总天数 //未测试
        int y2 = y - 1;
        int t = 365 * y2 + y2 / 4 - y2 / 100 + y2 / 400 + bfmo[m] + d;
        if (m > 2 && isLeap(y)) {
            ++t;
        }
        return t;
    }

    public static int getWeek(int y, int m, int d) { // [0,6] //未测试
        if (m <= 2) {
            m += 12;
        }
        return (d + 2 * m + 3 * (m + 1) / 5 + y + y / 4 - y / 100 + y / 400) % 7;
    }

    public static int getWeek(Date d) {// [0,6]对应周一到周日
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        return (c.get(Calendar.DAY_OF_WEEK) + 5) % 7;
    }

    public static Date timeDelta(Date d, int dt) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.add(Calendar.DAY_OF_YEAR, dt);
        return now.getTime();
    }

    public static int timeDistance(Date d1, Date d2) {// 输出d1-d2天数
        return (int) ((d1.getTime() - d2.getTime()) / (1000 * 3600 * 24));
    }
}
package plugin;

// import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateHelper {
    public static boolean isLeap(int y) {
        return y % 4 == 0 && y % 100 != 0 || y % 400 == 0;
    }

    public static int bfmo[] = { 0, 0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334, 365 };

    public static int days(int y, int m, int d) {// 返回当前总天数
        int y2 = y - 1;
        int t = 365 * y2 + y2 / 4 - y2 / 100 + y2 / 400 + bfmo[m] + d;
        if (m > 2 && isLeap(y)) {
            ++t;
        }
        return t;
    }

    public static int getWeek(int y, int m, int d) { // [0,6]
        if (m <= 2) {
            m += 12;
        }
        return (d + 2 * m + 3 * (m + 1) / 5 + y + y / 4 - y / 100 + y / 400) % 7;
    }

    public static Date timeDelta(Date d, int dt) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.add(Calendar.DAY_OF_YEAR, dt);
        return now.getTime();
    }

    // public static void main(String[] args) throws Exception {
    // SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
    // Date res = timeDelta(sdf.parse("220222"), -400);
    // System.out.println(sdf.format(res));
    // }
}
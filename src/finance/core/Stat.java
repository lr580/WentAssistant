package finance.core;

import base.DbLoad;
import category.core.CatTree;
import category.core.Cata;
import finance.ui.Tabbar;
import plugin.DateHelper;
import plugin.StringHelper;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

//为了解决历史遗留问题(CatTree没兼容int w和double w),将x.xx扩大100倍强转int
//计算结束后再转回去
public class Stat {// 统计
    private static CatTree cat = null;
    private static int n = 0;// 记录条数
    private static int m = 0;// 时间跨度
    private static int date_mi = 0;// 最早项日期
    private static int date_mx = 0;// 最晚项日期
    private static TreeMap<Integer, Double> months = null;
    private static double sum = 0;
    private static double std = 0;
    private static double avg = 0;
    private static double sum_weekday = 0;
    private static double sum_weekend = 0;
    // private static int n_weekday = 0;
    // private static int n_weekend = 0;
    private static DecimalFormat sdf = new DecimalFormat("0.00");
    public static StringBuilder res = null;// 结果文本

    public static void stat() {
        res = new StringBuilder();

        cat = new CatTree(Cata.query(DbLoad.getTypex()), false);
        n = Tabbar.money.length;
        months = new TreeMap<>();
        sum = sum_weekend = sum_weekday = 0;
        // n_weekday = n_weekend = 0;
        date_mi = 999999;
        date_mx = 0;
        std = avg = 0;
        Calendar cld = Calendar.getInstance();

        for (int i = 0; i < n; ++i) {
            int ty = Tabbar.type[i];
            double ov = Tabbar.money[i];
            int v = (int) Math.round(ov * 100);
            cat.cntNode(ty, v);

            int dt = Tabbar.date[i];
            int yymm = dt / 100;
            Double oriv = months.get(yymm);
            if (oriv == null) {
                oriv = 0.0;
            }
            months.put(yymm, oriv + ov);

            sum += ov;
            Date d = Supply.date2TDate(dt);
            cld.setTime(d);
            int ww = cld.get(Calendar.DAY_OF_WEEK);
            if (ww == Calendar.SUNDAY || ww == Calendar.SATURDAY) {
                sum_weekend += ov;
                // n_weekend++;
            } else {
                sum_weekday += ov;
                // n_weekday++;
            }

            date_mi = Math.min(date_mi, dt);
            date_mx = Math.max(date_mx, dt);
        }

        m = DateHelper.timeDistance(Supply.date2TDate(date_mx), Supply.date2TDate(date_mi)) + 1;
        avg = sum / Math.max(1, n);
        for (int i = 0; i < n; ++i) {
            double ov = Tabbar.money[i];
            std += (ov - avg) * (ov - avg);
        }
        std = Math.sqrt(std / n);

        res.append("项目数: " + n + "\n");
        res.append("天数跨度: " + m + "\n");
        res.append("总金额: " + sdf.format(sum) + "\n");
        res.append("日均金额: " + sdf.format(sum / m) + "\n");
        res.append("金额方差: " + sdf.format(std) + "\n");
        res.append("工作日总金额: " + sdf.format(sum_weekday) + "\n");
        // res.append("工作日均金额: " + sdf.format(sum_weekday / Math.max(1, n_weekday)) +
        // "\n");
        res.append("工作日均金额: " + sdf.format(sum_weekday / m) + "\n");
        res.append("周末的总金额: " + sdf.format(sum_weekend) + "\n");
        // res.append("周末的均金额: " + sdf.format(sum_weekend / Math.max(1, n_weekend)) +
        // "\n");
        res.append("周末的均金额: " + sdf.format(sum_weekend / m) + "\n");
        res.append("\n");

        res.append("按类别统计:\n");
        res.append(cat.stat_sum());
        res.append("\n");

        res.append("按月份统计:\n");
        Iterator<Map.Entry<Integer, Double>> it = months.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, Double> pr = it.next();
            int yymm = pr.getKey();
            int m = yymm % 100;
            int y = yymm / 100 % 100 + 2000;
            String t = y + "年" + StringHelper.intND(m) + "月: " + sdf.format(pr.getValue()) + "元\n";
            res.append(t);
        }
    }
}

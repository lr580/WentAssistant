package finance.core;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import base.DbLoad;
import plugin.DateHelper;
import plugin.Param;
import plugin.StringHelper;
import ui.DbTable;
import ui.TableFilter;
import java.text.SimpleDateFormat;

public class Search {
    private static StringBuilder res = null;// 搜索结果对应的SQL语句指令
    private static int date_begin = 0;
    private static int date_end = 0;
    private static Calendar cld = Calendar.getInstance();
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyMM");
    private static double inf_p = 200000000.00;
    private static double inf_n = -inf_p;
    private static double money_min = inf_n;
    private static double money_max = inf_p;
    private static ArrayList<Integer> types = null;
    private static int cmds = 0;// 数据库条件数
    private static boolean weekdayOnly = false;
    private static boolean weekendOnly = false;
    private static Pattern p_nint = Pattern.compile("[^0-9]+");

    private static int dateComplete(int x, boolean isFirst) {
        Date dy = new Date();
        cld.setTime(dy);
        if (x <= 12) {// 只有月份，补全年份
            int yy = cld.get(Calendar.YEAR) % 100;
            x += 100 * yy;
        } else if (x < 100) {// 只有年份，补全月份
            if (isFirst) {
                x = x * 100 + 1;
            } else {
                x = x * 100 + 12;
            }
        }
        if (x >= 101 && x <= 1231)// 只有月份，补全年份
        {
            int yy = cld.get(Calendar.YEAR) % 100;
            x += 10000 * yy;
        }
        if (x <= 130101) {// 只有月日
            if (isFirst) {
                x = x * 100 + 1;
            } else {
                try {
                    Date d2 = sdf.parse(Integer.toString(x));
                    cld.setTime(d2);
                } catch (Exception e) {
                    return 990101;
                }
                int dd = cld.getActualMaximum(Calendar.DAY_OF_MONTH);
                x = x * 100 + dd;
            }
        }
        return x;
    }

    private static void dateComplete(int x, int y) {// 日期补全
        if (y == 0) {// 第二个参数缺省
            y = x;
        }
        date_begin = dateComplete(x, true);
        date_end = dateComplete(y, false);
    }

    private static void add_connector(int cmds) {
        if (cmds == 0) {
            res.append(" where ");
        } else {
            res.append(" and ");
        }
    }

    private static void moneyComplete(String x, String y) {
        if (x.equals("*")) {
            money_min = inf_n;
        } else {
            money_min = Double.parseDouble(x);
        }
        if (y.equals("*")) {
            money_max = inf_p;
        } else {
            money_max = Double.parseDouble(y);
        }
    }

    private static int typeComplete(String v) {
        int r;
        Matcher m = p_nint.matcher(v);
        if (m.find()) {
            r = DbLoad.cata.find(v);
        } else {
            r = Integer.parseInt(v);
        }
        return r;
    }

    public static void search(String cmd) {
        cmds = 0;
        res = new StringBuilder("select * from " + DbLoad.getTypex());
        types = DbLoad.cata.getSubtree(1);
        int ori_types_len = types.size();
        weekdayOnly = weekendOnly = false;

        String c = StringHelper.trimAll(cmd);

        Iterator<Param> it = StringHelper.getParams(c).iterator();
        while (it.hasNext()) {
            Param p = it.next();
            String k = p.key;
            String[] v = p.value;
            if (k.equals("-t") || k.equals("--time")) {
                if (v.length == 1) {
                    dateComplete(Integer.parseInt(v[0]), 0);
                } else if (v.length == 2) {
                    dateComplete(Integer.parseInt(v[0]), Integer.parseInt(v[1]));
                }

                add_connector(cmds++);
                String t = "date >= " + date_begin + " and date <= " + date_end;
                res.append(t);

            } else if (k.equals("-p") || k.equals("--price")) {
                if (v.length == 1) {
                    moneyComplete(v[0], v[0]);
                } else if (v.length == 2) {
                    moneyComplete(v[0], v[1]);
                }

                add_connector(cmds++);
                String t = "value >= " + money_min + " and value <= " + money_max;
                res.append(t);

            } else if (k.equals("-B")) {
                add_connector(cmds++);
                res.append("(");
                for (int i = 0, ie = v.length; i < ie; ++i) {
                    if (i > 0) {
                        res.append(" or ");
                    }
                    res.append("comment regexp '" + v[i] + "'");
                }
                res.append(")");
            } else if (k.equals("-b")) {
                add_connector(cmds++);
                res.append("(");
                for (int i = 0, ie = v.length; i < ie; ++i) {
                    if (i > 0) {
                        res.append(" or ");
                    }
                    res.append("comment = '" + v[i] + "'");
                }
                res.append(")");
            } else if (k.equals("-c")) {
                for (int i = 0, ie = v.length; i < ie; ++i) {
                    int u = typeComplete(v[i]);
                    ArrayList<Integer> arr = DbLoad.cata.getSubtree(u);
                    types.retainAll(arr);
                }
            } else if (k.equals("-C")) {
                for (int i = 0, ie = v.length; i < ie; ++i) {
                    int u = typeComplete(v[i]);
                    ArrayList<Integer> arr = DbLoad.cata.getSubtree(u);
                    types.removeAll(arr);
                }
            } else if (k.equals("-N")) {
                for (int i = 0, ie = v.length; i < ie; ++i) {
                    int u = typeComplete(v[i]);
                    int idx = types.indexOf(u);
                    if (-1 != idx) {
                        types.remove(idx);
                    }
                }
            } else if (k.equals("-n")) {
                for (int i = 0, ie = v.length; i < ie; ++i) {
                    int u = typeComplete(v[i]);
                    int idx = types.indexOf(u);
                    if (-1 == idx) {
                        types.add(u);
                    }
                }
            } else if (k.equals("-w") || k.equals("-weekday")) {
                weekdayOnly = true;
            } else if (k.equals("-W") || k.equals("-weekend")) {
                weekendOnly = true;
            }
        }

        if (types.size() != ori_types_len && types.size() > 0) {
            add_connector(cmds);
            res.append("type in (");
            for (int i = 0, ie = types.size(); i < ie; ++i) {
                if (i != 0) {
                    res.append(",");
                }
                res.append(types.get(i));
            }
            res.append(")");
        }

        if (weekdayOnly) {
            DbTable.filter = new TableFilter() {
                public boolean isReserve(Object[] row) {
                    Date d = Supply.date2TDate(Supply.Str2Date((String) row[3]));
                    return DateHelper.getWeek(d) < 5;
                }
            };
        }
        if (weekendOnly) {
            DbTable.filter = new TableFilter() {
                public boolean isReserve(Object[] row) {
                    Date d = Supply.date2TDate(Supply.Str2Date((String) row[3]));
                    return DateHelper.getWeek(d) >= 5;
                }
            };
        }
        if (!weekdayOnly && !weekendOnly) {
            DbTable.filter = new TableFilter() {
            };
        }

        DbTable.that.render(res.toString());
    }
}

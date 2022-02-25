package plugin;

import java.awt.Toolkit;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.StringSelection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringHelper {
    public static String trimAll(String s) {// 去除所有\n\t,连续空白字符
        StringBuilder sb = new StringBuilder();
        char prev = 0, now = 0;
        for (int i = 0, ie = s.length(); i < ie; ++i, prev = now) {
            now = s.charAt(i);
            if (!(now == '\n' || now == '\t' || now == ' ')) {
                sb.append(now);
            } else if (now == ' ' && prev != ' ') {
                sb.append(now);
            }
        }
        return sb.toString();
    }

    private static Pattern p_param = Pattern.compile("\\-[a-zA-Z]|\\-\\-[a-zA-Z]+");

    public static boolean isParam(String s) {// 是否是长参数或短参数
        Matcher m = p_param.matcher(s);
        return m.find();
    }

    public static ArrayList<Param> getParams(String[] s) {// 查找参数
        String key = null;
        int last = 0;// 上一个key的位置
        ArrayList<Param> res = new ArrayList<>();
        for (int i = 0, ie = s.length; i <= ie; ++i) {
            if (i == ie || isParam(s[i])) {
                if (key != null) {
                    int rf = i - 1;
                    int lf = last + 1;
                    int len = rf - lf + 1;
                    String[] value = new String[len];
                    for (int j = lf, k = 0; j <= rf; ++j, ++k) {
                        value[k] = s[j];
                    }
                    Param param = new Param(key, value);
                    res.add(param);
                }
                last = i;
                if (i != ie) {
                    key = s[i];
                }
            }
        }
        return res;
    }

    public static ArrayList<Param> getParams(String s) {
        return getParams(s.split(" "));
    }

    public static String intND(int x, int d) {
        String fm = "";
        for (int i = 0; i < d; ++i) {
            fm += '0';
        }
        DecimalFormat mf = new DecimalFormat(fm);
        return mf.format(x);
    }

    public static String intND(int x) {
        return intND(x, 2);
    }

    public static void toClipboard(String s) {
        Transferable res = new StringSelection(s);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(res, null);
    }
}

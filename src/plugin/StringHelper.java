package plugin;

import java.awt.Toolkit;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.StringSelection;
import java.text.DecimalFormat;

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

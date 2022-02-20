package plugin;

import javax.swing.JOptionPane;

public class SwingHelper {
    public static String font_size(String s, int siz) {
        return "<html><div style='font-size=" + siz + "'>" + s + "</div></html>";
    }

    public static void syso(String s) {
        JOptionPane.showMessageDialog(null, s);
    }

    public static void syso(int s) {
        syso(Integer.toString(s));
    }

    public static void syso(Object s) {
        syso(s.toString());
    }

    public static void syso(Object[] s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0, ie = s.length; i < ie; ++i) {
            sb.append((i == 0 ? "" : ",") + s[i]);
        }
        syso(sb.toString());
    }
}

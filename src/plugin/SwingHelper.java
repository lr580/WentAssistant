package plugin;

import javax.swing.JOptionPane;

public class SwingHelper {
    public static String font_size(String s, int siz) {
        return "<html><div style='font-size=" + siz + "'>" + s + "</div></html>";
    }

    public static void syso(String s){
        JOptionPane.showMessageDialog(null, s);
    }
}

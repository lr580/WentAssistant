package plugin;

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

    // public static void main(String[] args) {
    // String tx = FileHelper.read("m.txt");
    // System.out.println(trimAll(tx));
    // }
}

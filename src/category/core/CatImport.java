package category.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
// import plugin.FileHelper;

public class CatImport {
    public static String load(String s) {// 假设格式一定正确
        String regex1 = "cas=\\{(.[^\\}]+)\\}";
        Pattern p1 = Pattern.compile(regex1);
        Matcher m1 = p1.matcher(s);
        String s1 = null;
        if (m1.find()) {
            // System.out.println(m1.start(1));// 第一个子匹配
            // System.out.println(m1.end(1));
            // System.out.println(s.substring(m1.start(1), m1.end(1)));
            s1 = s.substring(m1.start(1), m1.end(1));
        }
        String regex2 = "(\\d+): \\['([^']+)', \\[(\\d*)\\]\\]";
        Pattern p2 = Pattern.compile(regex2);
        Matcher m2 = p2.matcher(s1);
        int n = 1, top = 0;
        StringBuilder res = new StringBuilder("\n1 0 0 总");
        while (m2.find()) {
            int r1 = Integer.parseInt(s1.substring(m2.start(1), m2.end(1))) + 1;
            String r2 = s1.substring(m2.start(2), m2.end(2));
            String t3 = s1.substring(m2.start(3), m2.end(3));
            int r3 = 1;
            ++n;
            top = Math.max(r1, top);
            if (t3.length() != 0) {
                r3 = Integer.parseInt(t3) + 1;
            }
            res.append("\n" + r1 + " 0 " + r3 + " " + r2);
        }
        res.insert(0, n + " " + top);
        return res.toString();
    }

    // public static void main(String[] args) {// 调试或临时使用
    // String tx = FileHelper.read("b.txt");
    // System.out.println(load(tx));
    // System.out.println(tx);
    // load(tx);
    // System.out.println(Integer.parseInt("0"));
    // }
}

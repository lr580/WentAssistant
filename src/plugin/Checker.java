package plugin;

public class Checker {// 对输入字符串进行通用检查
    public static boolean isInt(String s) {
        try {
            Integer.valueOf(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isRInt(String s, int x) {// 是否是 >= x
        if (!isInt(s)) {
            return false;
        }
        return Integer.valueOf(s) >= x;
    }

    public static boolean isPosInt(String s) {// 是否是正整数
        if (!isInt(s)) {
            return false;
        }
        return isRInt(s, 1);
    }

    // public static void main(String[] args) {// 测试用例
    // System.out.println(isInt("2.3"));
    // System.out.println(isPosInt("-9"));
    // System.out.println(isPosInt("0"));
    // System.out.println(isPosInt("580"));
    // System.out.println(isInt("awa"));
    // System.out.println(isInt(""));
    // }
}

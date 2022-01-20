package plugin;

// import sun.misc.BASE64Decoder;
import java.util.Base64;

public class Base64Plugin {// Java8 做法；java8之前用sun.misc.BASE64Decoder;
    public static String get(byte[] key) {
        try {
            return Base64.getUrlEncoder().encodeToString(key);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public static byte[] from(String key) {
        try {
            return Base64.getUrlDecoder().decode(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}// 测试用例见Entrypt.java的main方法
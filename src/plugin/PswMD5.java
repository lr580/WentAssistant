package plugin;

import java.math.BigInteger;
import java.security.MessageDigest;

public class PswMD5 {
    private static final String salt = "Lr5801437580&";// 密码加盐

    public static String encrypt(String data) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(data.getBytes());
            return new BigInteger(1, md5.digest()).toString(16);// 校验码
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String password_md5(String psw) {
        return encrypt(psw + salt);
    }

    // public static void main(String[] args) {// 测试用例
    // String md5_1 = password_md5("123456");// 密码有ASCII就够了
    // String md5_2 = password_md5("123457");
    // String md5_3 = password_md5("123456");
    // System.out.println(md5_1);
    // System.out.println(md5_2);
    // System.out.println(md5_3.equals(md5_1));
    // System.out.println(md5_3.equals(md5_2));
    // }
}

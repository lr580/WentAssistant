package plugin;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Encrypt {
    // NOPadding
    private static final String transormation = "AES/CBC/PKCS5Padding";
    private static final String key = "1437580lr_CZGv1.";// 默认密码
    private static final String alg = "AES";
    private static Cipher cipher = null;
    private static SecretKeySpec keySpec = null;
    private static IvParameterSpec iv = null;
    static {// 初始化
        try {
            cipher = Cipher.getInstance(transormation);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static byte[] fill(String psw) {// 不足16位填充，手写PKCS5Padding
        byte[] b = psw.getBytes();
        // int len = (int) (Math.ceil((b.length + 0) / 16.0)) * 16;
        int len = 16;
        // System.out.println(len);
        byte[] c = new byte[len];
        // System.out.println(b.length);
        int left = (16 - b.length % 16) % 16;// 密码只能是16位长
        int i = 0;
        for (i = 0; i < b.length; ++i) {
            c[i] = b[i];
        }
        for (; i < c.length; ++i) {
            c[i] = (byte) left;
        }
        // System.out.println(c.length);
        return c;
    }

    public static String encode(String ori, String psw) {
        try {
            byte[] key_b = fill(psw);
            keySpec = new SecretKeySpec(key_b, alg);
            iv = new IvParameterSpec(key_b);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
            byte[] res = cipher.doFinal(ori.getBytes());// "utf-8"不可
            return Base64Plugin.get(res);
        } catch (Exception e) {
            // e.printStackTrace();
            return null;
        }
    }

    public static String encode(String ori) {
        return encode(ori, key);
    }

    public static String decode(String ori, String psw) {
        try {
            byte[] key_b = fill(psw);
            keySpec = new SecretKeySpec(key_b, alg);
            iv = new IvParameterSpec(key_b);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
            byte[] res = cipher.doFinal(Base64Plugin.from(ori));
            return new String(res);
        } catch (Exception e) {
            // e.printStackTrace();
            return null;
        }
    }

    public static String decode(String ori) {
        return decode(ori, key);
    }
}

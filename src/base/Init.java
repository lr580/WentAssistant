package base;

import java.io.*;
import java.util.*;
import mysql.Link;
import plugin.*;

/**
 * 为了防止直接删掉data/直接触发初始化来破解登录密码，采用如下策略：
 * 设登录密码的MD5加密存储在user.txt, user.txt文本的AES加密存在validate.txt
 * 如果读取user.txt并AES加密后与validate.txt不符合，直接报错，拒绝运行程序
 * 那么任何试图通过修改/删除user.txt, validate.txt来破解的行为都会拒绝运行程序
 * 特别地，无登录密码时用empty_token变量的AES加密与之检验
 */

public class Init {
    public static String login_md5 = null;// 登录密码
    private static String empty_token = "Empty&";// 空登录密码校验值
    // private static boolean validate = true;// 文件是否有效
    private static File f_dir = new File("data/");
    private static File f_psw = new File("data/user.txt");
    private static File f_vali = new File("data/validate.txt");
    private static File f_set = new File("data/settings.txt");
    public static final int psw_pos = 5;// f_set密码下标

    private static boolean create_if_not_exist() {// 创建文件
        boolean integrity = true;// 是否路径完整
        if (!f_dir.exists()) {
            f_dir.mkdir();
            integrity = false;
        }
        if (!f_psw.exists()) {
            FileHelper.touch(f_psw);
            integrity = false;
        }
        if (!f_vali.exists()) {
            FileHelper.touch(f_vali);
            integrity = false;
        }
        if (!f_set.exists()) {
            FileHelper.touch(f_set);// 这个残缺不影响密码检验
            // init_db_settings();
        }
        return integrity;
    }

    // 是否文件正确
    public static boolean isValidate() {
        boolean integrity = create_if_not_exist();

        login_md5 = FileHelper.read(f_psw);
        String check_std = FileHelper.read(f_vali).trim();
        if (check_std == null) {
            System.out.println("error: check_msg null");
            return false;
        }

        String check_psw = "";
        if (login_md5.length() == 0) {
            check_psw = Encrypt.encode(empty_token);
        } else {
            check_psw = Encrypt.encode(login_md5);
        }

        if (!check_psw.equals(check_std)) {
            return false;
        }
        return integrity;
    }

    // 创建正确登录密码文件
    public static boolean change_psw(String psw) {
        create_if_not_exist();
        if (psw.length() == 0) {
            psw = empty_token;
            FileHelper.write("", f_psw);
            login_md5 = "";
        } else {
            psw = PswMD5.password_md5(psw);
            FileHelper.write(psw, f_psw);
            login_md5 = psw;
        }
        FileHelper.write(Encrypt.encode(psw), f_vali);
        return true;
    }

    public static String[] read_db_settings() {
        String[] res = FileHelper.readlines(f_set);
        if (res.length > psw_pos) {
            res[psw_pos] = Encrypt.decode(res[psw_pos]);
        }
        if (res.length > 6) {
            res[6] = get_cfg(res);
        }
        return res;
    }

    public static boolean is_inited_db() {
        create_if_not_exist();
        return f_set.length() != 0;
    }

    public static void update_db_settings(String ip, String port, String db, String user, String psw, String cfg) {
        List<String> v = new LinkedList<>();
        v.add(Integer.toString(Link.version));
        v.add(ip);
        v.add(port);
        v.add(db);
        v.add(user);
        v.add(Encrypt.encode(psw));
        String sep[] = cfg.split("&");
        for (int i = 0; i < sep.length; ++i) {
            if (sep[i].length() > 0) {
                v.add(sep[i]);
            }
        }
        String res[] = new String[v.size()];
        Iterator<String> it = v.iterator();
        int i = 0;
        while (it.hasNext()) {
            res[i++] = it.next();
        }
        FileHelper.writelines(res, f_set);
    }

    private static String get_cfg(String[] res) {
        StringBuilder sb = new StringBuilder();
        for (int i = 6; i < res.length; ++i) {
            sb.append(res[i]);
            if (i > 6) {
                sb.append('&');
            }
        }
        return sb.toString();
    }

    // public static void main(String[] args) {// 测试用例
    // // change_psw("");
    // // is_inited_db();
    // System.out.println(isValidate());
    // }
}

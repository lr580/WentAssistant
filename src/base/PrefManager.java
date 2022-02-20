package base;

import java.util.TreeMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import plugin.FileHelper;

//假定配置项格式正确且无丢失
public class PrefManager {
    private static String path = "data/preference.txt";
    public static Map<String, String> pref = new TreeMap<>();
    public static String initPref = "nowModule=fin\nisRecordDiary=1\nisEncryptDiary=0\nisClearAfterAdd=1\nisFillAfterSelect=1\n";// 初始配置文件(默认)

    static {
        load_setting();
    }

    public static void load_setting() {// 加载偏好设置
        String s = FileHelper.read(path);
        Pattern p = Pattern.compile("([a-zA-Z0-9_]+)=([^\n]*)");
        Matcher m = p.matcher(s);
        pref.clear();
        while (m.find()) {
            String key = s.substring(m.start(1), m.end(1));
            String value = s.substring(m.start(2), m.end(2));
            // System.out.println(key + "," + value);
            pref.put(key, value);
        }
    }

    public static void write_setting() {
        StringBuilder sb = new StringBuilder();
        Iterator<Map.Entry<String, String>> it = pref.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> pair = it.next();
            sb.append(pair.getKey() + "=" + pair.getValue() + "\n");
        }
        FileHelper.write(sb.toString(), path);
    }

    public static void init_setting() {
        FileHelper.write(PrefManager.initPref, path);
        load_setting();
    }

    public static void main(String[] args) {// 测试
        // load_setting();
        // write_setting();
        init_setting();
    }

    // public static String query(String key) {// 完全没必要
    // return pref.get(key);
    // }

    // public static void update(String key, String value) {
    // pref.put(key, value);
    // }// 不存在时新建存在时覆盖

    // public static void delete(String key) {
    // pref.remove(key);
    // }// 不存在时忽略存在时删除
}

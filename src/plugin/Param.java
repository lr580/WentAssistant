package plugin;

public class Param {
    public String key = null;// 参数
    public String[] value = null;// 值

    public Param(String key, String[] value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder(key);
        for (int i = 0, ie = value.length; i < ie; ++i) {
            res.append(" ");
            res.append(value[i]);
        }
        return res.toString();
    }
}

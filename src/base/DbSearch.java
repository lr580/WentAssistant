package base;

import java.sql.*;
import mysql.Ctrl;
import java.util.*;
import ui.DbTable;
import javax.swing.JTable;

public class DbSearch {
    public static String sea_all = "select a.name, a.major, b.name, b.semester, c.value from student_# a join subject_# b join score_# c on a.id = c.student_id and b.id = c.subject_id";// 搜索全部
    private static String sea = "select a.name, a.major, b.name, b.semester, c.value from student_# a join subject_# b join score_# c on a.id = c.student_id and b.id = c.subject_id and c.value>=? and c.value <=? and a.name like ? and a.major like ? and b.name like ? and b.semester like ?";
    private static PreparedStatement s_sea = null;

    static {
        sea_all = sea_all.replaceAll("#", Integer.toString(DbLoader.t_temp));
        sea = sea.replaceAll("#", Integer.toString(DbLoader.t_temp));
        s_sea = Ctrl.pre(sea);
    }

    private static String cut_colon(String tmp) {// 取第一个冒号后面的子串
        return tmp.substring(tmp.indexOf(":") + 1);
    }

    public static String search(int min, int max, String stuname, String major, String subjname, String semester) {
        try {
            s_sea.setInt(1, min);
            s_sea.setInt(2, max);
            s_sea.setString(3, stuname);
            s_sea.setString(4, major);
            s_sea.setString(5, subjname);
            s_sea.setString(6, semester);
            return cut_colon(s_sea.toString());
        } catch (Exception e) {
            Ctrl.raised(e);
            return null;
        }
    }

    public static String stat() {
        JTable t = DbTable.that;
        int n = t.getRowCount();
        if (n == 0) {
            return "没有搜索结果";
        }
        Set<String> stu = new HashSet<>();
        Set<String> subj = new HashSet<>();
        Set<String> major = new HashSet<>();
        Set<String> seme = new HashSet<>();
        int data[] = new int[n];
        int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
        double avg = 0.0, std = 0.0;// 平均数，标准差
        List<Integer> mins = new LinkedList<>();
        List<Integer> maxs = new LinkedList<>();
        String name[] = new String[n];

        for (int i = 0; i < n; ++i) {
            name[i] = (String) t.getValueAt(i, 0);
            stu.add(name[i]);
            major.add((String) t.getValueAt(i, 1));
            subj.add((String) t.getValueAt(i, 2));
            seme.add((String) t.getValueAt(i, 3));
            data[i] = Integer.valueOf((String) t.getValueAt(i, 4));
            avg += data[i];
            min = Math.min(min, data[i]);
            max = Math.max(max, data[i]);
        }
        avg /= n;
        for (int i = 0; i < n; ++i) {
            std += Math.pow(data[i] - avg, 2);
            if (min == data[i]) {
                mins.add(i);
            }
            if (max == data[i]) {
                maxs.add(i);
            }
        }
        std /= n;
        std = Math.sqrt(std);

        StringBuilder sb = new StringBuilder();
        sb.append("共有" + n + "条结果，它们来自:\n");
        sb.append(stu.size() + "个不同学生\n");
        sb.append(major.size() + "个不同专业\n");
        sb.append(subj.size() + "个不同科目\n");
        sb.append(seme.size() + "个不同学期\n");
        sb.append("最高分是" + max + "\n");
        sb.append("最高分获得者有:");
        Iterator<Integer> it = maxs.iterator();
        for (int i = 0; it.hasNext(); ++i) {
            if (i > 0) {
                sb.append("，");
            }
            sb.append(name[it.next()]);
        }
        sb.append("\n最低分是" + min + "\n");
        sb.append("最低分获得者有:");
        it = mins.iterator();
        for (int i = 0; it.hasNext(); ++i) {
            if (i > 0) {
                sb.append("，");
            }
            sb.append(name[it.next()]);
        }
        sb.append("\n平均分为:");
        sb.append(avg);
        sb.append("\n标准差为:");
        sb.append(std);
        return sb.toString();
    }
}

package base;

import java.sql.*;
import java.util.Date;
import mysql.*;
import plugin.FileHelper;
import ui.Root;
import java.io.File;

public class DbCtrl {
    private static StringBuilder diary = new StringBuilder();// 运行日志
    private static PreparedStatement s_add_stu = null;
    private static PreparedStatement s_add_sub = null;
    private static PreparedStatement s_add_sco = null;
    private static PreparedStatement s_upd_stu = null;
    private static PreparedStatement s_upd_sub = null;
    private static PreparedStatement s_upd_sco = null;
    private static PreparedStatement s_del_stu = null;
    private static PreparedStatement s_del_sub = null;
    private static PreparedStatement s_del_sco = null;
    private static PreparedStatement s_sea_stu = null;
    private static PreparedStatement s_sea_sub = null;
    private static PreparedStatement s_sea_sco = null;
    private static PreparedStatement s_stuname2id = null;
    private static PreparedStatement s_subname2id = null;
    private static File f_dir = new File("log/");
    private static File f_log = new File("log/" + getNow() + ".log");

    static {// 初始化
        s_add_stu = Ctrl.pre(
                "insert into `student_" + DbLoader.t_temp
                        + "` (`id`, `name`, `student_number`, `major`) values (?, ?, ?, ?)");
        s_add_sub = Ctrl.pre(
                "insert into `subject_" + DbLoader.t_temp
                        + "` (`id`, `name`, `semester`) values (?, ?, ?)");
        s_add_sco = Ctrl.pre(
                "insert into `score_" + DbLoader.t_temp
                        + "` (`id`, `student_id`, `subject_id`, `value`) values (?, ?, ?, ?)");

        s_upd_stu = Ctrl.pre(
                "update `student_" + DbLoader.t_temp + "` set `name`= ?, `student_number`=?, `major`=? where `id`=?");
        s_upd_sub = Ctrl.pre(
                "update `subject_" + DbLoader.t_temp + "` set `name`= ?, `semester`=? where `id`=?");
        s_upd_sco = Ctrl.pre(
                "update `score_" + DbLoader.t_temp + "` set `student_id`= ?, `student_id`=?, `value`=? where `id`=?");

        s_del_stu = Ctrl.pre("delete from `student_" + DbLoader.t_temp + "` where `id` = ?");
        s_del_sub = Ctrl.pre("delete from `subject_" + DbLoader.t_temp + "` where `id` = ?");
        s_del_sco = Ctrl.pre("delete from `score_" + DbLoader.t_temp + "` where `id` = ?");

        s_sea_stu = Ctrl.pre("select * from `student_" + DbLoader.t_temp
                + "` where `name` like ? and `student_number` like ? and `major` like ?");
        s_sea_sub = Ctrl.pre("select * from `subject_" + DbLoader.t_temp
                + "` where `name` like ? and `semester` like ?");
        s_sea_sco = Ctrl.pre("select * from `score_" + DbLoader.t_temp
                + "` where `student_id` like ? and `subject_id` like ? and `value` like ?");

        s_stuname2id = Ctrl.pre("select `id` from `student_" + DbLoader.t_temp + "` where `name` = ?");
        s_subname2id = Ctrl.pre("select `id` from `subject_" + DbLoader.t_temp + "` where `name` = ?");
    }

    private static void change() {
        DbLoader.set_info("saved", 0);
        DbLoader.saved = 0;
        Root.updateTitle();
    }

    private static void change(String dia) {
        write_diary(dia);
        change();
    }

    public static void write_diary(String dia) {
        diary.append(getNow(1));
        diary.append(dia);
        diary.append('\n');
    }

    public static void save_diary() {
        write_diary("正常退出程序");
        diary.deleteCharAt(diary.length() - 1);// 删掉最后的换行
        if (!f_dir.exists()) {
            f_dir.mkdir();
        }
        FileHelper.write(diary.toString(), f_log);
    }

    public static int add_stu(String name, String number, String major) {
        try {
            s_add_stu.setInt(1, ++DbLoader.cnt_stu);
            s_add_stu.setString(2, name);
            s_add_stu.setString(3, number);
            s_add_stu.setString(4, major);
            s_add_stu.executeUpdate();
            DbLoader.set_info("a_" + DbLoader.t_temp, DbLoader.cnt_stu);
            change("添加学生" + DbLoader.cnt_stu + " (" + name + ", " + number + ", " + major + ")");
            return DbLoader.cnt_stu;
        } catch (Exception e) {
            Ctrl.raised(e);
            return -1;
        }
    }

    public static int add_sub(String name, String semester) {
        try {
            s_add_sub.setInt(1, ++DbLoader.cnt_sub);
            s_add_sub.setString(2, name);
            s_add_sub.setString(3, semester);
            s_add_sub.executeUpdate();
            DbLoader.set_info("b_" + DbLoader.t_temp, DbLoader.cnt_sub);
            change("添加课程" + DbLoader.cnt_sub + " (" + name + ", " + semester + ")");
            return DbLoader.cnt_sub;
        } catch (Exception e) {
            Ctrl.raised(e);
            return -1;
        }
    }

    public static int add_sco(int stu, int subj, int sco) {
        try {
            s_add_sco.setInt(1, ++DbLoader.cnt_sco);
            s_add_sco.setInt(2, stu);
            s_add_sco.setInt(3, subj);
            s_add_sco.setInt(4, sco);
            s_add_sco.executeUpdate();
            DbLoader.set_info("c_" + DbLoader.t_temp, DbLoader.cnt_sco);
            change("添加成绩" + DbLoader.cnt_sco + " (" + stu + ", " + subj + ", " + sco + ")");
            return DbLoader.cnt_sco;
        } catch (Exception e) {
            Ctrl.raised(e);
            return -1;
        }
    }

    public static void upd_stu(int id, String name, String number, String major) {
        try {
            s_upd_stu.setString(1, name);
            s_upd_stu.setString(2, number);
            s_upd_stu.setString(3, major);
            s_upd_stu.setInt(4, id);
            s_upd_stu.executeUpdate();
            change("修改学生" + id + "为 (" + name + ", " + number + ", " + major + ")");
        } catch (Exception e) {
            Ctrl.raised(e);
            return;
        }
    }

    public static void upd_sub(int id, String name, String semester) {
        try {
            s_upd_sub.setString(1, name);
            s_upd_sub.setString(2, semester);
            s_upd_sub.setInt(3, id);
            s_upd_sub.executeUpdate();
            change("修改课程" + id + "为 (" + name + ", " + semester + ")");
        } catch (Exception e) {
            Ctrl.raised(e);
            return;
        }
    }

    public static void upd_sco(int id, int stu, int subj, int sco) {
        try {
            s_upd_sco.setInt(1, stu);
            s_upd_sco.setInt(2, subj);
            s_upd_sco.setInt(3, sco);
            s_upd_sco.setInt(4, id);
            s_upd_sco.executeUpdate();
            change("修改成绩" + id + "为 (" + stu + ", " + subj + ", " + sco + ")");
        } catch (Exception e) {
            Ctrl.raised(e);
            return;
        }
    }

    public static void del_stu(int id) {
        try {
            s_del_stu.setInt(1, id);
            s_del_stu.executeUpdate();
            Ctrl.update("delete from `score_" + DbLoader.t_temp + "` where `student_id`=" + id);
            change("删除学生" + id);
        } catch (Exception e) {
            Ctrl.raised(e);
            return;
        }
    }

    public static void del_sub(int id) {
        try {
            s_del_sub.setInt(1, id);
            s_del_sub.executeUpdate();
            Ctrl.update("delete from `score_" + DbLoader.t_temp + "` where `subject_id`=" + id);
            change("删除课程" + id);
        } catch (Exception e) {
            Ctrl.raised(e);
            return;
        }
    }

    public static void del_sco(int id) {
        try {
            s_del_sco.setInt(1, id);
            s_del_sco.executeUpdate();
            change("删除成绩" + id);
        } catch (Exception e) {
            Ctrl.raised(e);
            return;
        }
    }

    public static String sea_stu(String name, String number, String major) {
        try {// 返回可执行的sql语句，交给DbTable的render方法执行
            s_sea_stu.setString(1, name);
            s_sea_stu.setString(2, number);
            s_sea_stu.setString(3, major);
            String tmp = s_sea_stu.toString();
            tmp = tmp.substring(tmp.indexOf(":") + 1);
            return tmp;
        } catch (Exception e) {
            Ctrl.raised(e);
            return null;
        }
    }

    public static String sea_sub(String name, String semester) {
        try {
            s_sea_sub.setString(1, name);
            s_sea_sub.setString(2, semester);
            String tmp = s_sea_sub.toString();
            tmp = tmp.substring(tmp.indexOf(":") + 1);
            return tmp;
        } catch (Exception e) {
            Ctrl.raised(e);
            return null;
        }
    }

    public static String sea_sco(String stu, String subj, String sco) {
        try {
            s_sea_sco.setString(1, stu);
            s_sea_sco.setString(2, subj);
            s_sea_sco.setString(3, sco);
            String tmp = s_sea_sco.toString();
            tmp = tmp.substring(tmp.indexOf(":") + 1);
            return tmp;
        } catch (Exception e) {
            Ctrl.raised(e);
            return null;
        }
    }

    public static int stuname2id(String v) {// 根据学生名精确找id，返回id或不存在-1
        try {
            s_stuname2id.setString(1, v);
            ResultSet rres = s_stuname2id.executeQuery();
            return Ctrl.getv(rres);
        } catch (Exception e) {
            Ctrl.raised(e);
            return -1;
        }
    }

    public static int subname2id(String v) {// 根据课程名精确找id，返回id或不存在-1
        try {
            s_subname2id.setString(1, v);
            ResultSet rres = s_subname2id.executeQuery();
            return Ctrl.getv(rres);
        } catch (Exception e) {
            Ctrl.raised(e);
            return -1;
        }
    }

    public static int cfm_stuid(int id) {// 核实该学生id是否在临时表存在
        return Ctrl.getv("select `id` from `student_" + DbLoader.t_temp + "` where `id` = " + id);
    }

    public static int cfm_subid(int id) {// 核实该课程id是否在临时表存在
        return Ctrl.getv("select `id` from `subject_" + DbLoader.t_temp + "` where `id` = " + id);
    }

    private static String getNow() {// 获得当前时间
        Date date = new Date();
        return String.format("%tF-%tH-%tM-%tS", date, date, date, date);// 不能用冒号%tT
    }

    private static String getNow(int i) {// 获得当前时间
        return String.format("[%tT]", new Date());
    }
}

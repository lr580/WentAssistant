package base;

import mysql.Ctrl;

public class ProcessCmd {// 数据库增删改指令
    public int type = 0;// 1增 2删 3改
    public Object[] from = null, to = null;// 操作前数据 操作后数据

    public ProcessCmd(int type, Object[] from) {
        this.type = type;
        this.from = from;
    }

    public ProcessCmd(int type, Object[] from, Object[] to) {
        this(type, from);
        this.to = to;
    }

    private void add() {// 加入
        String cmd = "insert into " + ProcessCtrl.tbname + " values" + ProcessCtrl.getString(from);
        System.out.println(cmd);
        Ctrl.run(cmd);
    }

    private void delete() {// 删除
        Integer idx = (Integer) from[0];
        String cmd = "delete from " + ProcessCtrl.tbname + " where id = " + idx;
        System.out.println(cmd);
        Ctrl.run(cmd);
    }

    private void update(Object[] t) {
        String cmd = "update " + ProcessCtrl.tbname + " set ";
        for (int i = 1; i < ProcessCtrl.m; ++i) {
            cmd += (i == 1 ? "" : ",");
            cmd += "`" + ProcessCtrl.cname[i] + "`=" + ProcessCtrl.getString(t[i], i);
        }
        cmd += " where id = " + ((Integer) t[0]).toString();
        System.out.println(cmd);
        Ctrl.run(cmd);
    }

    public void exec() {// 顺着执行这条指令
        if (type == 1) {
            // Integer cnt = ++DbLoad.cnt;
            Integer cnt = DbLoad.get_info(ProcessCtrl.tbname) + 1;
            if (ProcessCtrl.tbname.equals(DbLoad.getTypex())) {// 临时表
                DbLoad.cnt++;
            }
            DbLoad.set_info(ProcessCtrl.tbname, cnt);
            from[0] = cnt;
            add();
        } else if (type == 2) {
            update(to);
        } else if (type == 3) {
            delete();
        }
    }

    public void exec_inv() {// 逆着执行这条指令
        if (type == 1) {
            System.out.println(ProcessCtrl.tbname + ":" + DbLoad.cnt);
            delete();
            // --DbLoad.cnt;
            Integer cnt = DbLoad.get_info(ProcessCtrl.tbname) - 1;
            if (ProcessCtrl.tbname.equals(DbLoad.getTypex())) {// 临时表
                DbLoad.cnt--;
            }
            DbLoad.set_info(ProcessCtrl.tbname, cnt);
        } else if (type == 2) {
            update(from);
        } else if (type == 3) {
            add();
        }
    }

    @Override
    public String toString() {
        String res = "";
        if (type == 1) {
            res += "插入";
        } else if (type == 2) {
            res += "修改";
        } else if (type == 3) {
            res += "删除";
        }
        res += " " + ProcessCtrl.getString(from, true);
        if (to != null) {
            res += " -> " + ProcessCtrl.getString(to, true);
        }
        return res;
    }
}

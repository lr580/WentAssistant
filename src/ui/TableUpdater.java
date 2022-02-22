package ui;

import base.ProcessCmd;

public interface TableUpdater {// 执行增删改的窗口化表格更新
    default public void update(ProcessCmd cmd, boolean isInv) {
        if (!isInv) {
            eval(cmd);
        } else {
            eval_inv(cmd);
        }
    }

    default public void eval(ProcessCmd cmd) {
        if (cmd.type == 1) {
            DbTable.that.addRow(blend(cmd.from));
        } else if (cmd.type == 2) {
            DbTable.updateRow((Integer) cmd.to[0], blend(cmd.to));
        } else if (cmd.type == 3) {
            DbTable.delRow((Integer) cmd.from[0]);
        }
    }

    default public void eval_inv(ProcessCmd cmd) {
        if (cmd.type == 3) {
            DbTable.that.addRow(blend(cmd.from));
        } else if (cmd.type == 2) {
            DbTable.updateRow((Integer) cmd.from[0], blend(cmd.from));
        } else if (cmd.type == 1) {
            DbTable.delRow((Integer) cmd.from[0]);
        }
    }

    default public Object[] blend(Object[] x) {
        Object[] res = new Object[x.length];
        for (int i = 0; i < res.length; ++i) {
            res[i] = x[i];
        }
        return res;
    }
}

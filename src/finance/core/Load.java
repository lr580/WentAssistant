package finance.core;

import java.util.Map;
import java.util.Vector;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import base.DbCtrl;
import base.DbLoad;
import base.ProcessCmd;
import base.ProcessCtrl;
import category.core.CatTree;
import category.core.Cata;
import category.core.SumBlender;
import finance.ui.Tabbar;
import mysql.Ctrl;
import plugin.Eval;
import ui.DbTable;
import ui.TableBlender;
import ui.TableUpdater;
import ui.TbGlobal;
import ui.TbMain;

public class Load {
    public static Vector<String> cat_list = new Vector<>();

    public static void update_catlist() {
        DbLoad.cata.getSortedNode(cat_list);
        Tabbar.i_type.setSelectedIndex(0);
    }

    public static void Read() {
        Tabbar.InitTabbar();

        Map<String, String> h_tb = DbTable.h;
        h_tb.clear();
        h_tb.put("id", "编号");
        h_tb.put("value", "金额(元)");
        h_tb.put("type", "类别");
        h_tb.put("date", "日期");
        h_tb.put("comment", "备注");

        update_catlist();

        DbTable.blender = new TableBlender() {
            public Object[] blend(ResultSet res, int n, int[] ty) {
                Object[] row = new Object[n];
                try {
                    row[0] = res.getInt(1);
                    row[1] = res.getDouble(2);
                    row[2] = DbLoad.cata.findName(res.getInt(3));
                    row[3] = Supply.Date2Str(res.getInt(4));
                    row[4] = res.getString(5);
                    return row;
                } catch (Exception e) {
                    Ctrl.raised(e);
                    return null;
                }
            }
        };

        DbTable.updater = new TableUpdater() {
            public void update(ProcessCmd cmd, boolean isInv) {
                if (!isInv) {
                    eval(cmd);
                    if (cmd.type == 1) {
                        DbLoad.cata.cntNode((Integer) cmd.from[2], 1);
                        Cata.update(DbLoad.getTypex(), DbLoad.cata.export());
                    } else if (cmd.type == 3) {
                        DbLoad.cata.cntNode((Integer) cmd.from[2], -1);
                        Cata.update(DbLoad.getTypex(), DbLoad.cata.export());
                    }
                } else {
                    eval_inv(cmd);
                    if (cmd.type == 1) {
                        DbLoad.cata.cntNode((Integer) cmd.from[2], -1);
                        Cata.update(DbLoad.getTypex(), DbLoad.cata.export());
                    } else if (cmd.type == 3) {
                        DbLoad.cata.cntNode((Integer) cmd.from[2], 1);
                        Cata.update(DbLoad.getTypex(), DbLoad.cata.export());
                    }
                }
            }

            public Object[] blend(Object[] x) {
                Object[] res = new Object[x.length];
                for (int i = 0; i < res.length; ++i) {
                    res[i] = x[i];
                }
                res[2] = DbLoad.cata.findName((Integer) res[2]);
                res[3] = Supply.Date2Str((Integer) res[3]);
                return res;
            }
        };

        TbGlobal.pro_save.cmd = new Eval() {
            public void eval() {
                Cata.update(DbLoad.getTypex(false), DbLoad.cata.export());
            }
        };

        TbGlobal.pro_undoall.cmd = new Eval() {
            public void eval() {
                CatTree ori = new CatTree(Cata.query(DbLoad.getTypex(false)));
                Cata.update(DbLoad.getTypex(), ori.export());
            }
        };

        CatTree.sumblender = new SumBlender() {
            public String blend_line(int value, int w) {
                if (value == 0) {
                    return "";
                }
                return "...";
            }

            public String blend_value(String name, int value, int w) {
                if (value == 0) {
                    return "";
                }
                DecimalFormat mf = new DecimalFormat("0.00");// 不能放成员,因为是内部类(内部接口)
                double v = value / 100.0;
                double v2 = w / 100.0;
                String sgn = v2 < 0.0 ? "" : "+";
                return name + ": " + mf.format(v) + " (" + sgn + mf.format(v2) + ") " + "\n";
            }
        };

        TbGlobal.f_importcata.cmd = new Eval() {
            public void eval() {
                OldSupporter.import_cata(TbMain.that);
            }
        };

        TbGlobal.f_importdata.cmd = new Eval() {
            public void eval() {
                OldSupporter.import_data(TbMain.that);
            }
        };

        ProcessCtrl.init();

        DbCtrl.write_diary("载入财政官模块");
    }
}

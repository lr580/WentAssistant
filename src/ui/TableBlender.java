package ui;

import java.sql.ResultSet;
import mysql.Ctrl;

public interface TableBlender {
    default public Object[] blend(ResultSet res, int n, int[] ty) {
        Object[] row = new Object[n];
        try {
            for (int i = 1; i <= n; ++i) {
                row[i - 1] = fetch(res, i, ty[i]);
            }
        } catch (Exception e) {
            Ctrl.raised(e);
            return null;
        }
        return row;
    }

    default public Object fetch(ResultSet res, int i, int ty) {
        try {
            if (ty == 4) {// int
                return (Integer) res.getInt(i);
            } else if (ty == 3) {// double
                return (Double) res.getDouble(i);
            } else {// varchar(ty==12)
                return res.getString(i);
            }
        } catch (Exception e) {
            Ctrl.raised(e);
            return null;
        }
    }
}

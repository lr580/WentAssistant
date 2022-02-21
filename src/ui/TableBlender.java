package ui;

import java.sql.ResultSet;

public interface TableBlender {
    // default public Object[] blender(ResultSet res, int n, int[] ty) {
    // }
    public Object[] blend(ResultSet res, int n, int[] ty);
}

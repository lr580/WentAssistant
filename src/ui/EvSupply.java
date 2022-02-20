package ui;

import java.awt.event.*;
import base.DbLoad;
import base.ModLoad;
import category.ui.CatManager;
// import category.core.CatTree;
// import category.core.Cata;

public class EvSupply {// 事件监听器提供
    public static void openCataCtrl() {
        new CatManager(ModLoad.nowModule, DbLoad.t_temp, DbLoad.cata);
    }

    public static ActionListener ev_openCataCtrl = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            openCataCtrl();
        }
    };
}

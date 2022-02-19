package category.ui;

import javax.swing.*;
import javax.swing.tree.*;
import category.core.CatTree;

public class CatRenderer extends JTree {
    private CatTree cata = null;

    public CatRenderer(CatTree cata, DefaultMutableTreeNode root) {
        super(root);
        this.cata = cata;
        // TreeSelectionModel tsm = getSelectionModel();
        // tsm.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        // setSelectionModel(tsm);//没用
    }

    public CatRenderer(CatTree cata, DefaultTreeModel root) {
        super(root);
        this.cata = cata;
    }

    public int getSelectCount() {
        TreePath[] nd = getSelectionPaths();
        if (nd == null) {
            return 0;
        }
        return nd.length;
        // return getPathCount();
        // return getSelectionPaths().getPathCount();
    }

    public int getSingleSelect() {
        String str = (String) (((DefaultMutableTreeNode) getSelectionPath().getLastPathComponent()).getUserObject());
        return cata.h.get(str);
    }

    public DefaultMutableTreeNode getSingle() {
        return (DefaultMutableTreeNode) getSelectionPath().getLastPathComponent();
    }
}

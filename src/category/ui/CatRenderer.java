package category.ui;

import javax.swing.*;
import javax.swing.tree.*;
import category.core.CatTree;

public class CatRenderer extends JTree {
    private CatTree cata = null;

    public CatRenderer(CatTree cata, DefaultMutableTreeNode root) {
        super(root);
        this.cata = cata;
    }
}

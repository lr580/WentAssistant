package ui;

import javax.swing.*;

public class Preference extends JDialog {
    public Preference() {
        super(Root.that, "偏好设置", false);
        setSize(400, 400);
        setVisible(true);
    }
}

package ui;

import java.awt.*;
import javax.swing.*;

public class Page extends JPanel {
    public Page() {
        setLayout(new BorderLayout());// 不设置会出问题! jpanel 不是默认的
        DbTable jt = new DbTable();
        JScrollPane jscr = new JScrollPane(jt);
        add(jscr, BorderLayout.CENTER);
        add(new Tabbar(jt), BorderLayout.NORTH);
    }
}

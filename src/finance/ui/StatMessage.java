package finance.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import finance.core.Stat;
import plugin.FileHelper;
import plugin.StringHelper;
import ui.EvSupply;
import ui.Root;

//结果文本jText可以编辑，方便用户按自己的意愿二次修改(增删改)
public class StatMessage extends JDialog {// 统计结果
    public StatMessage() {
        super(Root.that, "统计结果", false);// 可以多个弹窗
        final Container ct = getContentPane();
        setLayout(new BorderLayout());
        final JTextArea jt = new JTextArea(Stat.res.toString());
        JScrollPane jscr = new JScrollPane(jt);
        ct.add(BorderLayout.CENTER, jscr);
        JPanel top = new JPanel(new FlowLayout(1, 10, 8));
        ct.add(BorderLayout.NORTH, top);

        JButton b_save = new JButton("保存");
        b_save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                javax.swing.filechooser.FileFilter flits = new FileNameExtensionFilter("文本文件(.txt)", "txt");// 没有补全功能
                File desc = EvSupply.select_file(ct, flits, false);
                if (desc != null) {
                    FileHelper.write(jt.getText(), desc);
                } // 直接让用户自由输入后缀名就行了，不判补全.txt了
            }
        });
        top.add(b_save);

        JButton b_clip = new JButton("复制到剪切板");
        b_clip.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                StringHelper.toClipboard(jt.getText());
            }
        });
        top.add(b_clip);

        setSize(400, 400);
        setVisible(true);
    }
}

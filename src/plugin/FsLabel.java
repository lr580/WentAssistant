package plugin;

import javax.swing.JLabel;

public class FsLabel extends JLabel {// font-size customized JLable
    private final static int default_size = 14;

    public FsLabel(String s, int siz) {
        super(SwingHelper.font_size(s, siz));
        // setAlignmentX(JLabel.RIGHT_ALIGNMENT);没效果
    }

    public FsLabel(String s) {
        this(s, default_size);// 不能写FsLabel
    }

    public FsLabel() {
        this("", default_size);
    }
}

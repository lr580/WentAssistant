package plugin;

public class EvalCtrl {
    public Eval cmd = null;

    public void eval() {
        if (cmd != null) {
            cmd.eval();
        }
        cmd = null;
    }

    public void eval_ctn() {
        if (cmd != null) {
            cmd.eval();
        }
    }
}

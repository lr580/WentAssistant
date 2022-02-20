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
    // public static Eval eval = null;
    // public static Eval evals[] = null;

    // public static void exec() {
    // if (eval != null) {
    // eval.eval();
    // }
    // eval = null;
    // }

    // public static void execall() {
    // if (evals != null) {
    // for (int i = 0; i < evals.length; ++i) {
    // if (evals[i] != null) {
    // evals[i].eval();
    // }
    // }
    // }
    // evals = null;
    // }

    // public static void exec(int i) {
    // if (evals != null && evals[i] != null) {
    // evals[i].eval();
    // }
    // evals[i] = null;
    // }
}

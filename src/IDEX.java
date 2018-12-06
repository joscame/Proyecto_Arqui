public class IDEX {

    public static int dir;
    public static int npc;
    public static int a;
    public static int b;
    public static int imm;
    public static int RL;
    public static IR ir;
    public static boolean idBlocked;
    public static boolean exBlocked;
    public static boolean branchInProgress;
    public static boolean exReady;

    static {
        dir = 0;
        npc = 0;
        a = 0;
        b = 0;
        imm = 0;
        RL = 0;
        ir = null;
        idBlocked = false;
        exBlocked = false;
        branchInProgress = false;
        exReady = false;
    }
}

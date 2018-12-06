public class IFID {

    public static int npc;
    public static IR ir;
    public static boolean ifBlocked;
    public static boolean idBlocked;
    public static boolean branchInProgress;
    public static boolean idReady;

    static{
        npc = 0;
        ir = null;
        ifBlocked = false;
        idBlocked = false;
        branchInProgress = false;
        idReady = false;
    }
}

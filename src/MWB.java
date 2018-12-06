public class MWB {

    public static int aluOutput;
    public static int lmd;
    public static IR ir;
    public static boolean memBlocked;
    public static boolean copyToMemory;
    public static boolean wbBlocked;

    static {
        aluOutput = 0;
        lmd = 0;
        ir = null;
        memBlocked = false;
        wbBlocked = false;
    }
}

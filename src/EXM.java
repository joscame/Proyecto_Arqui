public class EXM {

    public static int aluOutput;
    public static int b;
    public static IR ir;
    public static boolean memBlocked;
    public static boolean exBlocked;
    public static boolean copyToMemory;

    static {
        aluOutput = 0;
        b = 0;
        ir = null;
        memBlocked = false;
        exBlocked = false;
        copyToMemory = false;
    }
}

import java.util.ArrayList;

public class RegistersContainer {
    public static int pc;
    public static ArrayList<Integer> registers;
    public static ArrayList<Integer> registersLocks;

    static{
        registers = new ArrayList<>(33);
        registersLocks = new ArrayList<>(33);
        initializeRegistersAndLocks();
        pc = 384;
    }

    private static void initializeRegistersAndLocks(){
        for (int i = 0; i < 33; i++) {
            registers.add(0);
            registersLocks.add(0);
        }
    }
}

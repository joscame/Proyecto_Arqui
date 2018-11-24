import java.util.ArrayList;

public class ID implements Runnable {

    private IFID ifId;
    private IDEX idEx;
    private ArrayList<Integer> registers;
    private ArrayList<Integer> registersLocks;

    public ID (IFID ifId, IDEX idEx, ArrayList<Integer> registers, ArrayList<Integer> registersLocks){
        this.ifId = ifId;
        this.idEx = idEx;
        this.registers = registers;
        this.registersLocks = registersLocks;
    }

    public void run(){
        System.out.println("ID is running");
    }
}

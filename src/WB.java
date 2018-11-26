import java.util.ArrayList;

public class WB implements Runnable {

    private MWB memWb;
    private ArrayList<Integer> registers;
    private ArrayList<Integer> registersLocks;

    public WB (MWB memWb, ArrayList<Integer> registers, ArrayList<Integer> registersLocks){
        this.memWb = memWb;
        this.registers = registers;
        this.registersLocks = registersLocks;
    }

    public void run(){
        System.out.println("WB is running");
    }

    private void readAndProcessInstruction(){
        if ( (!memWb.memBlocked && !memWb.wbBlocked) || !memWb.wbBlocked){ // Refinar condición
            int operationCode = memWb.ir.getOperationCode();
            switch(operationCode){
                case 19: case 71: case 83: case 72: case 56:
                    this.registers.set(this.memWb.ir.getDestinyResgister(), this.memWb.aluOutput);
                    break;

                case 5:
                    this.registers.set(this.memWb.ir.getDestinyResgister(), this.memWb.lmd);
                    break;

                case 51:
                    this.registers.set(this.memWb.ir.getDestinyResgister(), this.memWb.lmd);
                    this.registers.set(32, this.memWb.aluOutput);
                    break;

                case 52:
                    if (this.memWb.copyToMemory){
                        this.registers.set(this.memWb.ir.getSecondSourceRegister(), 0);
                    }
            }
        }
    }
}

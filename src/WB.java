import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;

public class WB extends Thread {

    private MWB memWb;
    private ArrayList<Integer> registers;
    private ArrayList<Integer> registersLocks;

    private CyclicBarrier clockCycleFinishedBarrier;
    private CyclicBarrier checkedConflictsBarrier;
    private CyclicBarrier wbReadyBarrier;

    public WB (MWB memWb, ArrayList<Integer> registers, ArrayList<Integer> registersLocks, CyclicBarrier wbReadyBarrier){
        this.memWb = memWb;
        this.registers = registers;
        this.registersLocks = registersLocks;
        this.wbReadyBarrier = wbReadyBarrier;
    }

    public void run(){
        System.out.println("WB is running");
        readAndProcessInstruction();
        letIdStart();
        finishClockCycle();
        endProcess();

    }

    private void readAndProcessInstruction(){
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

    public void setBarriers(CyclicBarrier clockCycleFinishedBarrier, CyclicBarrier checkedConflictsBarrier){
        this.clockCycleFinishedBarrier = clockCycleFinishedBarrier;
        this.checkedConflictsBarrier = checkedConflictsBarrier;
    }

    private void finishClockCycle(){
        try {
            this.clockCycleFinishedBarrier.await();  // Se queda bloqueado hasta que 5 hilos hagan esta llamada.
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void endProcess(){
        try {
            this.checkedConflictsBarrier.await();  // Se queda bloqueado hasta que 5 hilos hagan esta llamada.
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void letIdStart(){
        try {
            this.wbReadyBarrier.await();  // Se queda bloqueado hasta que ID y WB digan que estan listos.
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

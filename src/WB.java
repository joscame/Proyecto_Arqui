import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;

public class WB extends Thread {

    public WB (){
        
    }

    public void run(){
        System.out.println("WB is running");
        readAndProcessInstruction();
        letIdStart();
        finishClockCycle();
        endProcess();

    }

    private void readAndProcessInstruction(){
        int operationCode = MWB.ir.getOperationCode();
        switch(operationCode){
            case 19: case 71: case 83: case 72: case 56:
                RegistersContainer.registers.set(MWB.ir.getDestinyResgister(), MWB.aluOutput);
                break;

            case 5:
                RegistersContainer.registers.set(MWB.ir.getDestinyResgister(), MWB.lmd);
                break;

            case 51:
                RegistersContainer.registers.set(MWB.ir.getDestinyResgister(), MWB.lmd);
                RegistersContainer.registers.set(32, MWB.aluOutput);
                break;

            case 52:
                if (MWB.copyToMemory){
                    RegistersContainer.registers.set(MWB.ir.getSecondSourceRegister(), 0);
                }
        }
    }

    private void finishClockCycle(){
        try {
            BarriersHandler.clockCycleFinishedBarrier.await();  // Se queda bloqueado hasta que 5 hilos hagan esta llamada.
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void endProcess(){
        try {
            BarriersHandler.checkedConflictsBarrier.await();  // Se queda bloqueado hasta que 5 hilos hagan esta llamada.
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void letIdStart(){
        try {
            BarriersHandler.wbReadyBarrier.await();  // Se queda bloqueado hasta que ID y WB digan que estan listos.
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

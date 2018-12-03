import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;

public class EX extends Thread {

    private IDEX idEx;
    private EXM exMem;
    private Integer pc;
    private ArrayList<Integer> registers;
    private ArrayList<Integer> registersLocks;

    private int tempAluOutput;
    private int tempB;
    private IR tempIr;
    private boolean tempCopyToMemory;
    private int tempPc;
    private boolean tempExBlocked;
    private boolean changePC;

    private CyclicBarrier clockCycleFinishedBarrier;
    private CyclicBarrier checkedConflictsBarrier;


    public EX (IDEX idEx, EXM exMem, Integer pc, ArrayList<Integer> registers, ArrayList<Integer> registersLocks){
        this.idEx = idEx;
        this.exMem = exMem;
        this.pc = pc;
        this.registers = registers;
        this.registersLocks = registersLocks;
    }

    public void run(){
        System.out.println("EX is running");
        if(idEx.idBlocked){
            this.tempExBlocked = true;
        }
        else{
            readAndProcessInstruction();
            prepareTempValues();
        }
        idEx.exReady = false;
        finishClockCycle();
        if(this.exMem.memBlocked) {
            sendResultsToMem();
        }
        else{
            idEx.exBlocked = true;
        }
        idEx.exReady = true;
        endProcess();
    }

    private int getAluOutput() {
        int operationCode = idEx.ir.getOperationCode();
        int result = -1;

        switch (operationCode) {
            case 19: // addi
                result = this.idEx.a + this.idEx.imm;
                break;

            case 71: // add
                result = this.idEx.a + this.idEx.b;
                break;

            case 83: // sub
                result = this.idEx.a - this.idEx.b;
                break;

            case 72: // mul
                result = this.idEx.a * this.idEx.b;
                break;

            case 56: // div
                result = this.idEx.a / this.idEx.b;
                break;

            case 5: case 37: case 51: case 52:// lw & sw & lr & sc
                result = this.idEx.a + this.idEx.imm;
                break;

            case 99: case 100:
                result = this.idEx.imm * 4;
                break;

            case 111: // jal
                result = this.idEx.npc + this.idEx.imm;
                break;

            case 103: // jalr
                result = this.registers.get(idEx.ir.getFirstSourceRegister()) + idEx.imm;
        }

        return result;
    }

    private boolean areRegisterValuesEqual(){
        return idEx.a == idEx.b;
    }

    private void readAndProcessInstruction() {
        int operationCode = idEx.ir.getOperationCode();
        this.changePC = false;

        switch (operationCode) {
            case 999: // FIN
                break;

            case 99: // beq
                if (areRegisterValuesEqual()) {
                    this.tempAluOutput = getAluOutput();
                    this.tempPc = this.tempAluOutput;
                    this.changePC = true;
                }
                break;

            case 100: // bne
                if (!areRegisterValuesEqual()) {
                    this.tempAluOutput = getAluOutput();
                    this.tempPc = this.tempAluOutput;
                    this.changePC = true;
                }
                break;

            case 111: // jal
                this.registers.set(idEx.ir.getDestinyResgister(), this.idEx.npc);
                this.registersLocks.set(idEx.ir.getDestinyResgister(), 0);
                this.tempAluOutput = getAluOutput();
                this.tempPc = this.tempAluOutput;
                this.changePC = true;
                break;

            case 103: // jalr
                this.registers.set(idEx.ir.getDestinyResgister(), this.idEx.npc);
                this.tempAluOutput = getAluOutput();
                this.tempPc = this.tempAluOutput;
                this.changePC = true;
                break;

            case 52: // sc
                this.tempAluOutput = getAluOutput();
                if (idEx.RL != this.tempAluOutput)
                    this.tempCopyToMemory = false;
                this.tempCopyToMemory = false;
                break;

            default:
                this.tempAluOutput = getAluOutput();
                break;
        }
    }

    private void prepareTempValues(){
        this.tempB = this.idEx.b;
        this.tempIr = this.idEx.ir;
    }

    private void sendResultsToMem(){
        this.exMem.aluOutput = this.tempAluOutput;
        this.exMem.b = this.tempB;
        this.exMem.ir = this.tempIr;
        this.exMem.copyToMemory = this.tempCopyToMemory;
        if (this.changePC)
            this.pc = this.tempPc;
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

    private boolean exHasConflicts(){
        return this.tempExBlocked;
    }

    private boolean memHasConflicts(){
        return this.exMem.memBlocked;
    }
}

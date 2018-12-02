import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;

public class ID implements Runnable {

    private IFID ifId;
    private IDEX idEx;
    private ArrayList<Integer> registers;
    private ArrayList<Integer> registersLocks;

    private int tempA;
    private int tempB;
    private int tempImm;
    private IR tempIr;
    private int tempRl;
    private boolean tempIdBlocked;
    private boolean tempBranchInProgress;


    private CyclicBarrier clockCycleFinishedBarrier;
    private CyclicBarrier checkedConflictsBarrier;
    private CyclicBarrier wbReadyBarrier;

    public ID (IFID ifId, IDEX idEx, ArrayList<Integer> registers, ArrayList<Integer> registersLocks, CyclicBarrier wbReadyBarrier){
        this.ifId = ifId;
        this.idEx = idEx;
        this.registers = registers;
        this.registersLocks = registersLocks;
        this.wbReadyBarrier = wbReadyBarrier;
    }

    public void run(){
        System.out.println("ID is running");
        waitForWB();
        if(!this.ifId.branchInProgress && !this.ifId.ifBlocked){
            processInstruction();
        }
        else if(this.ifId.branchInProgress && !this.idEx.branchInProgress){
            this.ifId.branchInProgress = false;
        }
        ifId.idReady = false;
        finishClockCycle();
        while(!idEx.exReady);
        if(!idEx.exBlocked || !idEx.branchInProgress){
            sendResultsToEx();
        }
        else if(idEx.exBlocked){
            this.ifId.idBlocked = true;
        }
        else if(this.tempIdBlocked) {
            this.idEx.idBlocked = true;
        }
        ifId.idReady = true;
        endProcess();
    }


    public void processInstruction(){
        int sourceReg1 = ifId.ir.getFirstSourceRegister();
        int sourceReg2 = ifId.ir.getSecondSourceRegister();
        int imm = ifId.ir.getImmediate();
        IR iR = ifId.ir;
        int operationCode = ifId.ir.getOperationCode();

        if(ifId.ifBlocked == true){
            this.tempIdBlocked = true;
        }else {

            switch (operationCode) {
                case 19: case 5: case 103: //addi 19 - lw 5 - jalr 103
                    if (registersLocks.get(sourceReg1) == 0 && registersLocks.get(ifId.ir.getDestinyResgister())==0) {
                        this.tempA = registers.get(sourceReg1);
                        this.tempImm = imm;
                        this.tempIr = iR;
                        registersLocks.set(ifId.ir.getDestinyResgister(),1);
                        this.tempIdBlocked = false;
                        this.ifId.idBlocked = false;
                        blockIfBranch();
                    }
                    else{
                        this.tempIdBlocked = true;
                        this.ifId.idBlocked = true;
                    }
                    break;
                case 71: case 83: case 72: case 56: //add 71 - sub 83 - mul 72 - div 56
                    if (registersLocks.get(sourceReg1) == 0 && registersLocks.get(sourceReg2) == 0 && registersLocks.get(ifId.ir.getDestinyResgister())==0) {
                        this.tempA = registers.get(sourceReg1);
                        this.tempB = registers.get(sourceReg2);
                        this.tempImm = imm;
                        this.tempIr = iR;
                        registersLocks.set(ifId.ir.getDestinyResgister(),1);
                        this.tempIdBlocked = false;
                        this.ifId.idBlocked = false;
                    }
                    else{
                        this.tempIdBlocked = true;
                        this.ifId.idBlocked = true;
                    }
                    break;
                case 51: //lr
                    if (registersLocks.get(sourceReg1) == 0 && registersLocks.get(ifId.ir.getDestinyResgister())==0 && registersLocks.get(32) == 0) {
                        this.tempA = registers.get(sourceReg1);
                        this.tempImm = imm;
                        this.tempIr = iR;
                        registersLocks.set(registersLocks.get(32),1);
                        this.tempIdBlocked = false;
                        this.ifId.idBlocked = false;
                    }
                    else{
                        this.tempIdBlocked = true;
                        this.ifId.idBlocked = true;
                    }
                    break;
                case 111: //jal
                    if(registersLocks.get(ifId.ir.getDestinyResgister())==0) {
                        this.tempImm = imm;
                        this.tempIr = iR;
                        registersLocks.set(ifId.ir.getDestinyResgister(),1);
                        blockIfBranch();
                    }
                    else{
                        this.tempIdBlocked = true;
                        this.ifId.idBlocked = true;
                    }
                    break;
                case 999: // fin
                    this.tempIr = iR;
                    break;
                case 52://sc
                    if (registersLocks.get(sourceReg1) == 0 && registersLocks.get(sourceReg2) == 0 && registersLocks.get(32) == 0) {
                        this.tempA = registers.get(sourceReg1);
                        this.tempB = registers.get(sourceReg2);
                        this.tempImm = imm;
                        this.tempIr = iR;
                        this.tempRl = registersLocks.get(32);
                        registersLocks.set(sourceReg2,1);
                        this.tempIdBlocked = false;
                        this.ifId.idBlocked = false;

                    }else{
                        this.tempIdBlocked = true;
                        this.ifId.idBlocked = true;
                    }
                    break;
                default: //sw 37 - beq 99 - bne 100
                    if (registersLocks.get(sourceReg1) == 0 && registersLocks.get(sourceReg2) == 0) {
                        this.tempA = sourceReg1;
                        this.tempB = sourceReg2;
                        this.tempImm = imm;
                        this.tempIr = iR;
                        blockIfBranch();
                    }
                    else{
                        this.tempIdBlocked = true;
                        this.ifId.idBlocked = true;
                    }
                    break;
            }

        }
    }

    private void blockIfBranch(){
        int operationCode = ifId.ir.getOperationCode();
        switch (operationCode) {
            case 99: case 100: case 111: case 103: // 99 beq -100 bne -111 jal -103 jalr
                this.ifId.branchInProgress = true;
                this.tempBranchInProgress = true;
                break;
        }
    }

    private void sendResultsToEx(){
        this.idEx.a = this.tempA;
        this.idEx.b = this.tempB;
        this.idEx.imm = this.tempImm;
        this.idEx.ir = this.tempIr;
        this.idEx.RL = this.tempRl;
        this.idEx.idBlocked = this.tempIdBlocked;
        this.idEx.npc = this.ifId.npc;
        this.idEx.branchInProgress = this.tempBranchInProgress;
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
    private void waitForWB(){
        try {
            this.wbReadyBarrier.await();  // Se queda bloqueado hasta que ID y WB digan que estan listos.
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean idHasConflicts(){
        return this.tempIdBlocked;
    }

    private boolean exHasConflicts(){
        return this.idEx.exBlocked;
    }
}

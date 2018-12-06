public class ID extends Thread {

    private int tempNpc;
    private int tempA;
    private int tempB;
    private int tempImm;
    private IR tempIr;
    private int tempRl;
    private boolean tempIdBlocked;
    private boolean tempBranchInProgress;

    public ID (){

    }

    public void run(){
        System.out.println("ID is running");
        waitForWB();
        if(!IFID.branchInProgress && !IFID.ifBlocked){
            processInstruction();
            prepareTempValues();
        }
        else if(IFID.branchInProgress && !IDEX.branchInProgress){
            IFID.branchInProgress = false;
        }
        IFID.idReady = false;
        finishClockCycle();
        while(!IDEX.exReady);
        if(!IDEX.exBlocked || !IDEX.branchInProgress){
            sendResultsToEx();
        }
        else if(IDEX.exBlocked){
            IFID.idBlocked = true;
        }
        else if(this.tempIdBlocked) {
            IDEX.idBlocked = true;
        }
        IFID.idReady = true;
        endProcess();
    }


    public void processInstruction(){
        int sourceReg1 = IFID.ir.getFirstSourceRegister();
        int sourceReg2 = IFID.ir.getSecondSourceRegister();
        int imm = IFID.ir.getImmediate();
        IR iR = IFID.ir;
        int operationCode = IFID.ir.getOperationCode();

        if(IFID.ifBlocked == true){
            this.tempIdBlocked = true;
        }else {

            switch (operationCode) {
                case 19: case 5: case 103: //addi 19 - lw 5 - jalr 103
                    if (RegistersContainer.registersLocks.get(sourceReg1) == 0 && RegistersContainer.registersLocks.get(IFID.ir.getDestinyResgister())==0) {
                        this.tempA = RegistersContainer.registers.get(sourceReg1);
                        this.tempImm = imm;
                        this.tempIr = iR;
                        RegistersContainer.registersLocks.set(IFID.ir.getDestinyResgister(),1);
                        this.tempIdBlocked = false;
                        IFID.idBlocked = false;
                        blockIfBranch();
                    }
                    else{
                        this.tempIdBlocked = true;
                        IFID.idBlocked = true;
                    }
                    break;
                case 71: case 83: case 72: case 56: //add 71 - sub 83 - mul 72 - div 56
                    if (RegistersContainer.registersLocks.get(sourceReg1) == 0 && RegistersContainer.registersLocks.get(sourceReg2) == 0 && RegistersContainer.registersLocks.get(IFID.ir.getDestinyResgister())==0) {
                        this.tempA = RegistersContainer.registers.get(sourceReg1);
                        this.tempB = RegistersContainer.registers.get(sourceReg2);
                        this.tempImm = imm;
                        this.tempIr = iR;
                        RegistersContainer.registersLocks.set(IFID.ir.getDestinyResgister(),1);
                        this.tempIdBlocked = false;
                        IFID.idBlocked = false;
                    }
                    else{
                        this.tempIdBlocked = true;
                        IFID.idBlocked = true;
                    }
                    break;
                case 51: //lr
                    if (RegistersContainer.registersLocks.get(sourceReg1) == 0 && RegistersContainer.registersLocks.get(IFID.ir.getDestinyResgister())==0 && RegistersContainer.registersLocks.get(32) == 0) {
                        this.tempA = RegistersContainer.registers.get(sourceReg1);
                        this.tempImm = imm;
                        this.tempIr = iR;
                        RegistersContainer.registersLocks.set(RegistersContainer.registersLocks.get(32),1);
                        this.tempIdBlocked = false;
                        IFID.idBlocked = false;
                    }
                    else{
                        this.tempIdBlocked = true;
                        IFID.idBlocked = true;
                    }
                    break;
                case 111: //jal
                    if(RegistersContainer.registersLocks.get(IFID.ir.getDestinyResgister())==0) {
                        this.tempImm = imm;
                        this.tempIr = iR;
                        RegistersContainer.registersLocks.set(IFID.ir.getDestinyResgister(),1);
                        blockIfBranch();
                    }
                    else{
                        this.tempIdBlocked = true;
                        IFID.idBlocked = true;
                    }
                    break;
                case 999: // fin
                    this.tempIr = iR;
                    break;
                case 52://sc
                    if (RegistersContainer.registersLocks.get(sourceReg1) == 0 && RegistersContainer.registersLocks.get(sourceReg2) == 0 && RegistersContainer.registersLocks.get(32) == 0) {
                        this.tempA = RegistersContainer.registers.get(sourceReg1);
                        this.tempB = RegistersContainer.registers.get(sourceReg2);
                        this.tempImm = imm;
                        this.tempIr = iR;
                        this.tempRl = RegistersContainer.registersLocks.get(32);
                        RegistersContainer.registersLocks.set(sourceReg2,1);
                        this.tempIdBlocked = false;
                        IFID.idBlocked = false;

                    }else{
                        this.tempIdBlocked = true;
                        IFID.idBlocked = true;
                    }
                    break;
                default: //sw 37 - beq 99 - bne 100
                    if (RegistersContainer.registersLocks.get(sourceReg1) == 0 && RegistersContainer.registersLocks.get(sourceReg2) == 0) {
                        this.tempA = sourceReg1;
                        this.tempB = sourceReg2;
                        this.tempImm = imm;
                        this.tempIr = iR;
                        blockIfBranch();
                    }
                    else{
                        this.tempIdBlocked = true;
                        IFID.idBlocked = true;
                    }
                    break;
            }

        }
    }

    private void blockIfBranch(){
        int operationCode = IFID.ir.getOperationCode();
        switch (operationCode) {
            case 99: case 100: case 111: case 103: // 99 beq -100 bne -111 jal -103 jalr
                IFID.branchInProgress = true;
                this.tempBranchInProgress = true;
                break;
        }
    }

    private void prepareTempValues(){
        this.tempNpc = IFID.npc;
    }

    private void sendResultsToEx(){
        IDEX.a = this.tempA;
        IDEX.b = this.tempB;
        IDEX.imm = this.tempImm;
        IDEX.ir = this.tempIr;
        IDEX.RL = this.tempRl;
        IDEX.idBlocked = this.tempIdBlocked;
        IDEX.npc = this.tempNpc;
        IDEX.branchInProgress = this.tempBranchInProgress;
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
    private void waitForWB(){
        try {
            BarriersHandler.wbReadyBarrier.await();  // Se queda bloqueado hasta que ID y WB digan que estan listos.
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

public class EX extends Thread {

    private int tempAluOutput;
    private int tempB;
    private IR tempIr;
    private boolean tempCopyToMemory;
    private int tempPc;
    private boolean tempExBlocked;
    private boolean changePC;

    public EX (){
        
    }

    public void run(){
        System.out.println("EX is running");
        if(IDEX.idBlocked){
            this.tempExBlocked = true;
        }
        else{
            readAndProcessInstruction();
            prepareTempValues();
        }
        IDEX.exReady = false;
        finishClockCycle();
        if(EXM.memBlocked) {
            sendResultsToMem();
        }
        else{
            IDEX.exBlocked = true;
        }
        IDEX.exReady = true;
        endProcess();
    }

    private int getAluOutput() {
        int operationCode = IDEX.ir.getOperationCode();
        int result = -1;

        switch (operationCode) {
            case 19: // addi
                result = IDEX.a + IDEX.imm;
                break;

            case 71: // add
                result = IDEX.a + IDEX.b;
                break;

            case 83: // sub
                result = IDEX.a - IDEX.b;
                break;

            case 72: // mul
                result = IDEX.a * IDEX.b;
                break;

            case 56: // div
                result = IDEX.a / IDEX.b;
                break;

            case 5: case 37: case 51: case 52:// lw & sw & lr & sc
                result = IDEX.a + IDEX.imm;
                break;

            case 99: case 100:
                result = IDEX.imm * 4;
                break;

            case 111: // jal
                result = IDEX.npc + IDEX.imm;
                break;

            case 103: // jalr
                result = RegistersContainer.registers.get(IDEX.ir.getFirstSourceRegister()) + IDEX.imm;
        }

        return result;
    }

    private boolean areRegisterValuesEqual(){
        return IDEX.a == IDEX.b;
    }

    private void readAndProcessInstruction() {
        int operationCode = IDEX.ir.getOperationCode();
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
                RegistersContainer.registers.set(IDEX.ir.getDestinyResgister(), IDEX.npc);
                RegistersContainer.registersLocks.set(IDEX.ir.getDestinyResgister(), 0);
                this.tempAluOutput = getAluOutput();
                this.tempPc = this.tempAluOutput;
                this.changePC = true;
                break;

            case 103: // jalr
                RegistersContainer.registers.set(IDEX.ir.getDestinyResgister(), IDEX.npc);
                this.tempAluOutput = getAluOutput();
                this.tempPc = this.tempAluOutput;
                this.changePC = true;
                break;

            case 52: // sc
                this.tempAluOutput = getAluOutput();
                if (IDEX.RL != this.tempAluOutput)
                    this.tempCopyToMemory = false;
                this.tempCopyToMemory = false;
                break;

            default:
                this.tempAluOutput = getAluOutput();
                break;
        }
    }

    private void prepareTempValues(){
        this.tempB = IDEX.b;
        this.tempIr = IDEX.ir;
    }

    private void sendResultsToMem(){
        EXM.aluOutput = this.tempAluOutput;
        EXM.b = this.tempB;
        EXM.ir = this.tempIr;
        EXM.copyToMemory = this.tempCopyToMemory;
        if (this.changePC)
            RegistersContainer.pc = this.tempPc;
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
}

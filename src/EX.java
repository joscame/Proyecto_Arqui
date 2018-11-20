public class EX implements Runnable {

    private IDEX idEx;
    private EXM exMem;
    private Integer pc;

    public EX (IDEX idEx, EXM exMem, Integer pc){
        this.idEx = idEx;
        this.exMem = exMem;
        this.pc = pc;
    }

    public void run(){
        System.out.println("EX is running");
    }

    private int getAluOutput() {
        int operationCode = idEx.ir.getOperationCode();
        int result = -1;

        switch (operationCode) {
            case 19: // addi
                result = idEx.a + idEx.imm;
                break;

            case 71: // add
                result = idEx.a + idEx.b;
                break;

            case 83: // sub
                result = idEx.a - idEx.b;
                break;

            case 72: // mul
                result = idEx.a * idEx.b;
                break;

            case 56: // div
                result = idEx.a / idEx.b;
                break;

            case 5: case 37: case 51: case 52:// lw & sw & lr & sc
                result = idEx.a + idEx.imm;
                break;
        }

        return result;
    }

    private boolean areRegisterValuesEqual(){
        return idEx.a == idEx.b;
    }

    private void readInstruction(){
        int operationCode = idEx.ir.getOperationCode();

        switch (operationCode) {
            case 999: // FIN
                break;

        case 99: // beq
            if (areRegisterValuesEqual()){
                this.exMem.aluOutput = getAluOutput();
                this.pc = this.exMem.aluOutput;
            }
            break;

        case 100: // bne
            if (!areRegisterValuesEqual()){
                this.exMem.aluOutput = getAluOutput();
                this.pc = this.exMem.aluOutput;
            }
        }
    }
}

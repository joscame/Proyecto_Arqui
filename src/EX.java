public class EX implements Runnable {

    private IDEX idEx;
    private EXM exMem;

    public EX (IDEX idEx, EXM exMem){
        this.idEx = idEx;
        this.exMem = exMem;
    }

    public void run(){
        System.out.println("EX is running");
    }

    private int processInstruction() {
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

            case 5: case 37:// lw & sw
                result = idEx.a + idEx.imm;
                break;
        }

        return result;
    }
}

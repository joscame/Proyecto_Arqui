import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CyclicBarrier;

public class IF extends Thread {

    public boolean readInstructions;
    private IFID ifId;
    private Integer pc;
    private ArrayList<Integer> instructionsMemory;
    private ArrayList<InstructionsBlock> instructionsCache;
    public int failCounter;
    private boolean tempIfBlocked;
    private IR tempIr;
    private CyclicBarrier clockCycleFinishedBarrier;
    private CyclicBarrier checkedConflictsBarrier;

    public IF (IFID ifId, Integer pc, ArrayList<Integer> instructionsMemory){
        this.readInstructions = false;
        this.ifId = ifId;
        this.pc = pc;
        this.instructionsMemory = instructionsMemory;
        this.instructionsCache = new ArrayList<>(4);
        //llenar blockID con -1
        for(int i=0; i < 4; ++i)
        {
            InstructionsBlock newInstructionsBlock = new InstructionsBlock();
            for (int j = 0; j < 4 ; j++) {
                ArrayList<Integer> instruction = new ArrayList<>(4);
                for (int k = 0; k < 4; k++) {
                    instruction.add(0);
                }
                newInstructionsBlock.words.add(new IR(instruction));
            }
            newInstructionsBlock.blockId = -1;
            instructionsCache.add(newInstructionsBlock);
        }
        this.failCounter = 0;
    }

    public void run(){
        System.out.println("IF is running");
        loadInst();
        increasePc();
        System.out.println("IF: PC = " + this.pc);
        finishClockCycle();
        while(!ifId.idReady);
        if(failCounter == 0 && !this.ifId.idBlocked){ //si no hay fallo y si id no bloqueado
            sendResultsToID();
        }
        else if(ifId.idBlocked = true || this.tempIfBlocked) {
            rewindPc();
            this.ifId.ifBlocked = true;
        }
        endProcess();
    }

    private void rewindPc(){
        this.pc = this.pc-4;
    }
    private void increasePc(){
        this.pc = this.pc+4;
    }

    private void loadInst() {

        int dir = (this.pc) - 384;
        int numBlock = dir / 16;
        int numWord = dir % 16;

        //buscar en cache la instruccion el bloque
        if (instructionsCache.get(numBlock % 4).blockId == numBlock)//si estaba en cache
        {
            tempIr = instructionsCache.get(numBlock % 4).words.get(numWord);
        } else { //fallo de cache
            this.tempIfBlocked = true;
            if (failCounter == 0) {
                failCounter = 48;
            } else if (failCounter == 1) {
                //copiar en cache
                int blockStartPos = numBlock * 16;
                ArrayList<Integer> block = new ArrayList<>();
                InstructionsBlock newBlock = new InstructionsBlock();
                for (int i = blockStartPos, j = 0; i < blockStartPos + 16; i++, j++) {
                    block.add(instructionsMemory.get(i));
                }
                for (int i = 0; i < 16; i = i + 4) {
                    newBlock.words.add(new IR(new ArrayList<>(block.subList(i, i + 3))));
                }
                newBlock.blockId = numBlock;
                instructionsCache.set(numBlock % 4, newBlock);
                failCounter = 0;
                this.tempIfBlocked = false;
            } else {
                --failCounter;
            }
        }
    }
    private void sendResultsToID(){
        this.ifId.ifBlocked = tempIfBlocked;
        this.ifId.ir = tempIr;
        this.ifId.npc = this.pc;
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

    private boolean idHasConflicts(){
        return this.ifId.idBlocked;
    }

    private boolean ifHasConflicts(){
        return this.tempIfBlocked;
    }
}

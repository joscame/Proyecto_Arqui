import java.util.ArrayList;

public class IF extends Thread {
    private ArrayList<InstructionsBlock> instructionsCache;

    public boolean readInstructions;
    public int failCounter;

    private boolean tempIfBlocked;
    private IR tempIr;

    public IF (){
        this.readInstructions = false;
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
        System.out.println("IF: PC = " + RegistersContainer.pc);
        finishClockCycle();
        while(!IFID.idReady);
        if(failCounter == 0 && !IFID.idBlocked){ //si no hay fallo y si id no bloqueado
            sendResultsToID();
        }
        else if(IFID.idBlocked = true || this.tempIfBlocked) {
            rewindPc();
            IFID.ifBlocked = true;
        }
        endProcess();
    }

    private void rewindPc(){
        RegistersContainer.pc = RegistersContainer.pc-4;
    }
    private void increasePc(){
        RegistersContainer.pc = RegistersContainer.pc+4;
    }

    private void loadInst() {

        int dir = (RegistersContainer.pc) - 384;
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
                    block.add(MemoryHandler.instructionsMemory.get(i));
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
        IFID.ifBlocked = tempIfBlocked;
        IFID.ir = tempIr;
        IFID.npc = RegistersContainer.pc;
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

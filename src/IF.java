import java.util.ArrayList;
import java.util.Iterator;

public class IF implements Runnable {

    private IFID ifId;
    private Integer pc;
    private ArrayList<Integer> instructionsMemory;
    private ArrayList<InstructionsBlock> instructionsCache;
    private int failCounter;
    private boolean tempIfBlocked;
    private IR tempIr;

    public IF (IFID ifId, Integer pc, ArrayList<Integer> instructionsMemory){
        this.ifId = ifId;
        this.pc = pc;
        this.instructionsMemory = instructionsMemory;
        this.instructionsCache = new ArrayList<>(4);
        //llenar blockID con -1
        for(int i=0; i < instructionsCache.size(); ++i)
        {
            instructionsCache.get(i).blockId = -1;
        }
        this.failCounter = 0;
    }

    public void run(){
        System.out.println("IF is running");
    }

    private void loadInst() {

        int dir = (this.pc) - 384;
        int numBlock = dir / 16;
        int numWord = dir % 16;

        if(ifId.idBlocked = true)
        {
            this.pc = this.pc-4;
        }

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
                this.pc = this.pc+4;
            } else {
                --failCounter;
            }
        }
    }
    public void sendResultsToID(){
        this.ifId.ifBlocked = tempIfBlocked;
        this.ifId.ir = tempIr;
        this.ifId.npc = this.pc;
    }

}

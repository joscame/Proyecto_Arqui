import java.util.ArrayList;

public class M implements Runnable {

    private static final int SHARED = 0;
    private static final int MODIFIED = 1;
    private static final int BLOCKID = 4;
    private static final int STATUS = 5;

    private EXM exMem;
    private MWB memWb;
    private int[][] dataCache;
    private ArrayList<Integer> dataMemory;

    private int tempAluOutput;
    private int tempLmd;
    private IR tempIr;
    private boolean tempMemBlocked;

    int failCounter;
    boolean modifyDataMemory;

    public M (EXM exMem, MWB memWb, ArrayList<Integer> dataMemory){
        this.exMem = exMem;
        this.memWb = memWb;
        this.dataMemory = dataMemory;
        this.failCounter = 0;
        modifyDataMemory = false;
        this.dataCache = new int[4][6];
        for (int i = 0; i < 4; i++){
            this.dataCache[i][4] = -1;
        }
    }

    public void run(){
        System.out.println("M is running");
        if (this.exMem.exBlocked){
            this.tempMemBlocked = true;
        }
        else{
            int blockNum = this.exMem.aluOutput / 16;
            int cacheBlockNum = blockNum % 4;
            int word = (this.exMem.aluOutput % 16) / 4;
            if(this.dataCache[cacheBlockNum][BLOCKID] == blockNum){ // hit
                int operationCode = this.exMem.ir.getOperationCode();
                switch(operationCode){
                    case 5: case 51: //lw lr
                        this.tempLmd = this.dataCache[cacheBlockNum][word];
                        break;

                    case 37: //sw
                        this.dataCache[cacheBlockNum][word] = this.exMem.b;
                        this.dataCache[cacheBlockNum][STATUS] = MODIFIED;
                        break;

                    case 52: //sc
                        if (this.exMem.copyToMemory){
                            this.dataCache[cacheBlockNum][word] = this.exMem.b;
                            this.dataCache[cacheBlockNum][STATUS] = MODIFIED;
                        }
                        break;
                }
            }
            else{ //Fallo de cache
                if(this.failCounter == 0){
                    if (this.dataCache[cacheBlockNum][STATUS] == MODIFIED){
                        this.failCounter += 48;
                    }
                    this.failCounter += 48;
                } else if (this.failCounter == 1){
                    if (this.dataCache[cacheBlockNum][STATUS] == MODIFIED){
                        int dataMemoryIndex = (this.exMem.aluOutput / 16) * 4;
                        for (int i = dataMemoryIndex, j = 0; i < dataMemoryIndex + 4; i++, ++j) {
                            this.dataMemory.set(i, this.dataCache[cacheBlockNum][j]);
                        }
                    }
                    else{

                    }
                }
            }
        }
    }


}

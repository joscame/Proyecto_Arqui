import java.util.ArrayList;

public class M extends Thread {

    private static final int SHARED = 0;
    private static final int MODIFIED = 1;
    private static final int BLOCKID = 4;
    private static final int STATUS = 5;
    
    private int[][] dataCache;

    private int tempAluOutput;
    private int tempLmd;
    private IR tempIr;
    private boolean tempMemBlocked;
    private boolean tempCopyToMemory;

    public int failCounter;
    private boolean modifyDataMemory;

    public M (){
        this.failCounter = 0;
        modifyDataMemory = false;
        this.dataCache = new int[4][6];
        for (int i = 0; i < 4; i++){
            this.dataCache[i][BLOCKID] = -1;
        }
    }

    public void run(){
        System.out.println("M is running");
        if (EXM.exBlocked){
            this.tempMemBlocked = true;
        }
        else{
            readAndProcessInstruction();
            prepareTempValues();
        }
        finishClockCycle();
        if(failCounter == 0){ //si no hay fallo
            sendResultsToWb();
        }
        endProcess();
    }

    private void readAndProcessInstruction(){
        int blockNum = EXM.aluOutput / 16;
        int cacheBlockNum = blockNum % 4;
        int word = (EXM.aluOutput % 16) / 4;
        if(this.dataCache[cacheBlockNum][BLOCKID] == blockNum){ // hit
            int operationCode = EXM.ir.getOperationCode();
            switch(operationCode){
                case 5: case 51: //lw lr
                    this.tempLmd = this.dataCache[cacheBlockNum][word];
                    break;

                case 37: //sw
                    this.dataCache[cacheBlockNum][word] = EXM.b;
                    this.dataCache[cacheBlockNum][STATUS] = MODIFIED;
                    break;

                case 52: //sc
                    if (EXM.copyToMemory){
                        this.dataCache[cacheBlockNum][word] = EXM.b;
                        this.dataCache[cacheBlockNum][STATUS] = MODIFIED;
                    }
                    break;
            }
            EXM.memBlocked = false;
            this.tempMemBlocked = false;
        }
        else{ //Fallo de cache
            if(this.failCounter == 0){

                if (this.dataCache[cacheBlockNum][STATUS] == MODIFIED){
                    this.failCounter += 48;
                }
                this.failCounter += 48;
                EXM.memBlocked = true;
                this.tempMemBlocked = true;

            } else if (this.failCounter == 1){ // Es el último ciclo de reloj del fallo, se soluciona el fallo

                int dataMemoryIndex = (EXM.aluOutput / 16) * 4;
                if (this.dataCache[cacheBlockNum][STATUS] == MODIFIED){ //Si el bloque está modificado se copia a memoria
                    for (int i = dataMemoryIndex, j = 0; i < dataMemoryIndex + 4; i++, ++j) {
                        MemoryHandler.dataMemory.set(i, this.dataCache[cacheBlockNum][j]);
                    }
                }

                for (int i = dataMemoryIndex, j = 0; i < dataMemoryIndex + 4; i++, ++j) { // Se copia el bloque de memoria a cache
                    this.dataCache[cacheBlockNum][j] = MemoryHandler.dataMemory.get(i);
                }

                // Se actualiza el num de bloque y el estado en el caché
                this.dataCache[cacheBlockNum][BLOCKID] = blockNum;
                this.dataCache[cacheBlockNum][STATUS] = SHARED;

                this.failCounter = 0;
            }
            else{ //Aún hay ciclos de reloj para fallo de caché, restarle al contador
                --this.failCounter;
            }
        }

    }

    private void prepareTempValues(){
        this.tempCopyToMemory = EXM.copyToMemory;
        this.tempIr = EXM.ir;
        this.tempAluOutput = EXM.aluOutput;
    }

    private void sendResultsToWb(){
        MWB.aluOutput = this.tempAluOutput;
        MWB.copyToMemory = this.tempCopyToMemory;
        MWB.ir = this.tempIr;
        MWB.lmd = this.tempLmd;
        MWB.memBlocked = this.tempMemBlocked;
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

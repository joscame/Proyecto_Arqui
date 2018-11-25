import java.util.ArrayList;
import java.util.Iterator;

public class IF implements Runnable {

    private IFID ifId;
    private Integer pc;
    private ArrayList<Integer> instructionsMemory;
    private ArrayList<InstructionsBlock> instructionsCache;
    private int failCounter;
    private int tempIfBlocked;

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

    private void cargarInst(){

        int dir = (this.pc) - 384;
        int numBlock = dir/16;
        int numWord = dir % 16;

        //buscar en cache la instruccion el bloque
        if(instructionsCache.get(numBlock%4).blockId == numBlock)//estaba en cache
        {
            ifId.ir = instructionsCache.get(numBlock%4).words.get(numWord);
        }else{ //fallo de cache
            this.ifId.ifBlocked = true;
            if(failCounter == 0)
            {
                failCounter = 48;
            }
            else if(failCounter == 1)
            {
                //copiar en cache
                failCounter = 0;
            }
            else{
                --failCounter;
            }
        }



        //si esta se carga la instruccion desde el cache de inst al IR de IFID

    }

}

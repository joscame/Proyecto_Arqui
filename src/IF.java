import java.util.ArrayList;
import java.util.Iterator;

public class IF implements Runnable {

    private IFID ifId;
    private Integer pc;
    private ArrayList<Integer> instructionsMemory;
    private ArrayList<InstructionsBlock> instructionsCache = new ArrayList<>(4);

    public IF (IFID ifId, Integer pc, ArrayList<Integer> instructionsMemory){
        this.ifId = ifId;
        this.pc = pc;
        this.instructionsMemory = instructionsMemory;
    }

    public void run(){
        System.out.println("IF is running");
        //llenar blockID con -1
        for(int i=0; i < instructionsCache.size(); ++i)
        {
            instructionsCache.get(i).blockId = -1;
        }
    }

    private void cargarInst(){

        int dir = (this.pc) - 384;
        int numBlock = dir/16;
        int numWord = dir % 16;

        //buscar en cache la instruccion el bloque
        for(int i=0;i<instructionsCache.size(); ++i)
        {
            if(instructionsCache.get(i).blockId == numBlock)//estaba en cache
            {
                ifId.ir = instructionsCache.get(i).words.get(numWord);
            }else{ //fallo de cache
                //instructionsCache.set(numBlock) = instructionsMemory.get(dir);
            }
        }



        //si esta se carga la instruccion desde el cache de inst al IR de IFID
        if(hit == true){}
    }

}

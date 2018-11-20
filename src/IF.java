import java.util.ArrayList;

public class IF implements Runnable {

    private IFID ifId;
    private Integer pc;
    private int[][] instructionsCache;
    private ArrayList<Integer> instructionsMemory;

    public IF (IFID ifId, Integer pc, ArrayList<Integer> instructionsMemory){
        this.ifId = ifId;
        this.pc = pc;
        this.instructionsCache = new int[4][5];
        this.instructionsMemory = instructionsMemory;

    }

    public void run(){
        System.out.println("IF is running");
    }
}

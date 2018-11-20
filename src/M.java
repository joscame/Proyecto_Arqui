import java.util.ArrayList;

public class M implements Runnable {

    private EXM exMem;
    private MWB memWb;
    private int[][] dataCache;
    private ArrayList<Integer> dataMemory;

    public M (EXM exMem, MWB memWb, ArrayList<Integer> dataMemory){
        this.exMem = exMem;
        this.memWb = memWb;
        this.dataCache = new int[4][6];
        this.dataMemory = dataMemory;
    }

    public void run(){
        System.out.println("M is running");
    }
}

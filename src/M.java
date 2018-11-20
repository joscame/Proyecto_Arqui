public class M implements Runnable {

    private EXM exMem;
    private MWB memWb;
    private int[][] dataCache;

    public M (EXM exMem, MWB memWb){
        this.exMem = exMem;
        this.memWb = memWb;
        this.dataCache = new int[4][6];
    }

    public void run(){
        System.out.println("M is running");
    }
}

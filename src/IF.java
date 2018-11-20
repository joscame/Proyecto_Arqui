public class IF implements Runnable {

    private IFID ifId;
    private Integer pc;
    private int[][] instructionsCache;

    public IF (IFID ifId, Integer pc){
        this.ifId = ifId;
        this.pc = pc;
        this.instructionsCache = new int[4][5];
    }

    public void run(){
        System.out.println("IF is running");
    }
}

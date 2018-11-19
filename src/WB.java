public class WB implements Runnable {

    private MWB memWb;

    public WB (MWB memWb){
        this.memWb = memWb;
    }

    public void run(){
        System.out.println("WB is running");
    }
}

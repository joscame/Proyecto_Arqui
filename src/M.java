public class M implements Runnable {

    private EXM exMem;
    private MWB memWb;

    public M (EXM exMem, MWB memWb){
        this.exMem = exMem;
        this.memWb = memWb;
    }

    public void run(){
        System.out.println("M is running");
    }
}

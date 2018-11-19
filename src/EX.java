public class EX implements Runnable {

    private IDEX idEx;
    private EXM exMem;

    public EX (IDEX idEx, EXM exMem){
        this.idEx = idEx;
        this.exMem = exMem;
    }

    public void run(){
        System.out.println("EX is running");
    }
}

public class ID implements Runnable {

    private IFID ifId;
    private IDEX idEx;

    public ID (IFID ifId, IDEX idEx){
        this.ifId = ifId;
        this.idEx = idEx;
    }

    public void run(){
        System.out.println("ID is running");
    }
}

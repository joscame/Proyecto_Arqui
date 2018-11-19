public class IF implements Runnable {

    private IFID ifId;

    public IF (IFID ifId){
        this.ifId = ifId;
    }

    public void run(){
        System.out.println("IF is running");
    }
}

import java.util.ArrayList;

public class Processor {

    /**
     * Declara las variables para valores globales
     */
    private int quantum;

    /**
     * Declara los registros y memorias
     */
    private ArrayList<Integer> instructionsMemory;

    /**
     * Declara los hilos que seran utilizados
     */
    private IF ifThread;
    private ID id;
    private EX ex;
    private M m;
    private WB wb;

    public Processor(int quantum, ArrayList<Integer> instructionsMemory){
        this.quantum = quantum;
        this.instructionsMemory = instructionsMemory;
        this.ifThread = new IF();
        this.id = new ID();
        this.ex = new EX();
        this.m = new M();
        this.wb = new WB();
    }

    public void start (){
        System.out.println("El procesador comienza a leer instrucciones");
    }
}

import java.util.ArrayList;
import java.util.HashMap;

public class Processor {

    /**
     * Declara las variables para valores globales
     */
    private int quantum;

    /**
     * Declara los registros y memorias
     */
    private ArrayList<Integer> instructionsMemory;
    private ArrayList<Integer> dataMemory;
    private ArrayList<Integer> registers;
    private Integer pc;

    /**
     * Declara los hilos (etapas) que seran utilizados
     */
    private IF ifThread;
    private ID id;
    private EX ex;
    private M m;
    private WB wb;

    /**
     * Declara las estructuras que se encuentran entre etapas
     */
    private IFID ifId;
    private IDEX idEx;
    private EXM exMem;
    private MWB memWb;

    public Processor(int quantum, ArrayList<Integer> instructionsMemory){
        this.quantum = quantum;
        this.instructionsMemory = instructionsMemory;
        this.dataMemory = new ArrayList<Integer>(96);
        this.registers = new ArrayList<>(32);
        this.pc = 384;

        this.ifId = new IFID();
        this.idEx = new IDEX();
        this.exMem = new EXM();
        this.memWb = new MWB();

        this.ifThread = new IF(this.ifId, this.pc, this.instructionsMemory);
        this.id = new ID(this.ifId, this.idEx);
        this.ex = new EX(this.idEx, this.exMem, this.pc);
        this.m = new M(this.exMem, this.memWb, this.dataMemory);
        this.wb = new WB(this.memWb);
    }

    public void start (){
        System.out.println("El procesador comienza a leer instrucciones");
    }
}

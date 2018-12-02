import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CyclicBarrier;

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
    private ArrayList<Integer> registersLocks;
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

    /**
     * Crea las barreras que serán utlizadas para la sincronización
     */
    CyclicBarrier clockCycleFinishedBarrier;
    CyclicBarrier checkedConflictsBarrier;
    CyclicBarrier wbReadyBarrier;

    /**
     * Crea la estructura para guardar los contextos
     */
     

    public Processor(int quantum, ArrayList<Integer> instructionsMemory){
        this.quantum = quantum;
        this.instructionsMemory = instructionsMemory;
        this.dataMemory = new ArrayList<Integer>(96);
        this.registers = new ArrayList<>(33);
        this.registersLocks = new ArrayList<>(33);
        this.pc = 384;

        this.clockCycleFinishedBarrier = new CyclicBarrier(6);
        this.checkedConflictsBarrier = new CyclicBarrier(6);
        this.wbReadyBarrier = new CyclicBarrier(2);

        this.ifId = new IFID();
        this.idEx = new IDEX();
        this.exMem = new EXM();
        this.memWb = new MWB();

        this.ifThread = new IF(this.ifId, this.pc, this.instructionsMemory);
        this.ifThread.setBarriers(this.clockCycleFinishedBarrier, this.checkedConflictsBarrier);

        this.id = new ID(this.ifId, this.idEx, this.registers, this.registersLocks, this.wbReadyBarrier);
        this.id.setBarriers(this.clockCycleFinishedBarrier, this.checkedConflictsBarrier);

        this.ex = new EX(this.idEx, this.exMem, this.pc, this.registers, this.registersLocks);
        this.ex.setBarriers(this.clockCycleFinishedBarrier, this.checkedConflictsBarrier);

        this.m = new M(this.exMem, this.memWb, this.dataMemory);
        this.m.setBarriers(this.clockCycleFinishedBarrier, this.checkedConflictsBarrier);

        this.wb = new WB(this.memWb, this.registers, this.registersLocks, this.wbReadyBarrier);
        this.wb.setBarriers(this.clockCycleFinishedBarrier, this.checkedConflictsBarrier);
    }

    public void start (){
        System.out.println("El procesador comienza a leer instrucciones");
    }
}

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

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
     ArrayList<Context> contexts;

    public Processor(int quantum, ArrayList<Integer> instructionsMemory){
        this.quantum = quantum;
        this.instructionsMemory = instructionsMemory;
        this.dataMemory = new ArrayList<Integer>(96);
        initializeDataMemory();
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

        this.contexts = new ArrayList<>();
        this.contexts.add(new Context(0, 384)); //hilillo 0
        this.contexts.add(new Context(1, 416)); //hilillo 1
        this.contexts.add(new Context(2, 484)); //hilillo 2
        this.contexts.add(new Context(3, 580)); //hilillo 3
        this.contexts.add(new Context(4, 688)); //hilillo 4
        this.contexts.add(new Context(5, 784)); //hilillo 5
        this.contexts.add(new Context(6, 880)); //hilillo 6

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

        this.ifThread.start();
        int hilillosCounter = 0;
        int clockCicles = 0;
        while (this.contexts.size() > 0){
            this.pc = this.contexts.get(hilillosCounter % contexts.size()).pc;
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.ifThread.readInstructions = true;

            while (clockCicles <= this.quantum){
                System.out.println("Processor: PC = " + this.pc);
                finishClockCycle();
                if (this.ifThread.failCounter == 0 && this.m.failCounter == 0){ //Si no hay fallos de caché
                    ++clockCicles;
                }
                endProcess();
            }

            clockCicles = 0;
            this.contexts.get(hilillosCounter % contexts.size()).pc = this.pc;
            ++hilillosCounter;
        }
    }

    private void initializeDataMemory(){
        for (int i = 0; i < 96; i++) {
            this.dataMemory.add(1);
        }
    }

    private void finishClockCycle(){
        try {
            this.clockCycleFinishedBarrier.await();  // Se queda bloqueado hasta que 5 hilos hagan esta llamada.
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void endProcess(){
        try {
            this.checkedConflictsBarrier.await();  // Se queda bloqueado hasta que 5 hilos hagan esta llamada.
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

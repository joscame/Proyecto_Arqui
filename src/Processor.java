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
     * Declara los hilos (etapas) que seran utilizados
     */
    private IF ifThread;
    private ID id;
    private EX ex;
    private M m;
    private WB wb;

    /**
     * Crea la estructura para guardar los contextos
     */
     ArrayList<Context> contexts;

    public Processor(int quantum, ArrayList<Integer> instructionsMemory){
        this.quantum = quantum;

        this.contexts = new ArrayList<>();
        this.contexts.add(new Context(0, 384)); //hilillo 0
        this.contexts.add(new Context(1, 416)); //hilillo 1
        this.contexts.add(new Context(2, 484)); //hilillo 2
        this.contexts.add(new Context(3, 580)); //hilillo 3
        this.contexts.add(new Context(4, 688)); //hilillo 4
        this.contexts.add(new Context(5, 784)); //hilillo 5
        this.contexts.add(new Context(6, 880)); //hilillo 6

        this.ifThread = new IF();
        this.id = new ID();
        this.ex = new EX();
        this.m = new M();
        this.wb = new WB();
    }

    public void start (){
        System.out.println("El procesador comienza a leer instrucciones");

        this.ifThread.start();
        int hilillosCounter = 0;
        int clockCicles = 0;
        while (this.contexts.size() > 0){
            RegistersContainer.pc = this.contexts.get(hilillosCounter % this.contexts.size()).pc;
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.ifThread.readInstructions = true;

            while (clockCicles <= this.quantum){
                System.out.println("Processor: PC = " + RegistersContainer.pc);
                finishClockCycle();
                if (this.ifThread.failCounter == 0 && this.m.failCounter == 0){ //Si no hay fallos de caché
                    ++clockCicles;
                }
                endProcess();
            }

            clockCicles = 0;
            this.contexts.get(hilillosCounter % contexts.size()).pc = RegistersContainer.pc;
            ++hilillosCounter;
        }
    }

    private void finishClockCycle(){
        try {
            BarriersHandler.clockCycleFinishedBarrier.await();  // Se queda bloqueado hasta que 5 hilos hagan esta llamada.
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void endProcess(){
        try {
            BarriersHandler.checkedConflictsBarrier.await();  // Se queda bloqueado hasta que 5 hilos hagan esta llamada.
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

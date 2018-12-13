import java.util.concurrent.CyclicBarrier;

public class BarriersHandler {
    public static CyclicBarrier clockCycleFinishedBarrier;
    public static CyclicBarrier checkedConflictsBarrier;
    public static CyclicBarrier wbReadyBarrier;
    public static CyclicBarrier idReadyBarrier;
    public static CyclicBarrier exReadyBarrier;

    static {
        clockCycleFinishedBarrier = new CyclicBarrier(3);
        checkedConflictsBarrier = new CyclicBarrier(3);
        wbReadyBarrier = new CyclicBarrier(2);
        idReadyBarrier = new CyclicBarrier(2);
        exReadyBarrier = new CyclicBarrier(2);
    }
}

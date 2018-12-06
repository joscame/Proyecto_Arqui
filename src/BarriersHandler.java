import java.util.concurrent.CyclicBarrier;

public class BarriersHandler {
    public static CyclicBarrier clockCycleFinishedBarrier;
    public static CyclicBarrier checkedConflictsBarrier;
    public static CyclicBarrier wbReadyBarrier;

    static {
        clockCycleFinishedBarrier = new CyclicBarrier(6);
        checkedConflictsBarrier = new CyclicBarrier(6);
        wbReadyBarrier = new CyclicBarrier(2);
    }
}

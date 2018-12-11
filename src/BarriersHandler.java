import java.util.concurrent.CyclicBarrier;

public class BarriersHandler {
    public static CyclicBarrier clockCycleFinishedBarrier;
    public static CyclicBarrier checkedConflictsBarrier;
    public static CyclicBarrier wbReadyBarrier;

    static {
        clockCycleFinishedBarrier = new CyclicBarrier(3);
        checkedConflictsBarrier = new CyclicBarrier(3);
        wbReadyBarrier = new CyclicBarrier(2);
    }
}

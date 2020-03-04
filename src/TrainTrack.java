import java.util.concurrent.atomic.*;

public class TrainTrack {

    private final String[] slots = {"[..]", "[..]", "[..]", "[..]", "[..]", "[..]", "[..]", "[..]", "[..]",
            "[..]", "[..]", "[..]", "[..]", "[..]", "[..]", "[..]", "[..]", "[..]", "[..]"};

    private final MageeSemaphore slotSem[] = new MageeSemaphore[19];

    ProcessMonitor theTrainActivity;

    AtomicInteger aUsingJunction;
    AtomicInteger bUsingJunction;

    MageeSemaphore aCountSem;
    MageeSemaphore bCountSem;

    private final MageeSemaphore aMutexSem;
    private final MageeSemaphore bMutexSem;

    MageeSemaphore junctionLock;

    public TrainTrack() {
        theTrainActivity = new ProcessMonitor(slots);
        for (int i = 0; i <= 18; i++) {
            slotSem[i] = new MageeSemaphore(1);
        }
        aMutexSem = new MageeSemaphore(1);
        bMutexSem = new MageeSemaphore(1);
        aUsingJunction = new AtomicInteger(0);
        bUsingJunction = new AtomicInteger(0);
        aCountSem = new MageeSemaphore(3);
        bCountSem = new MageeSemaphore(3);
        junctionLock = new MageeSemaphore(1);
    }

    public void trainA_MoveOnToTrack(String trainName) {
        CDS.idleQuietly((int) (Math.random() * 100));
        aCountSem.P();
        slotSem[4].P();
        slotSem[5].P();
        slots[5] = "[" + trainName + "]";
        slotSem[4].V();
        theTrainActivity.addMovedTo(5);
    }

    public void trainB_MoveOnToTrack(String trainName) {

        bCountSem.P();
        CDS.idleQuietly((int) (Math.random() * 100));
        slotSem[13].P();
        slotSem[14].P();
        slots[14] = "[" + trainName + "]";
        slotSem[13].V();
        theTrainActivity.addMovedTo(14);
    }

    public void trainA_MoveFromEntryToJunction() {
        CDS.idleQuietly((int) (Math.random() * 100));
        moveTrainAroundTrack(5,8);
        CDS.idleQuietly((int) (Math.random() * 100));
    }

    public void trainB_MoveFromEntryToJunction() {
        CDS.idleQuietly((int) (Math.random() * 100));
        moveTrainAroundTrack(14, 17);
        CDS.idleQuietly((int) (Math.random() * 100));
    }

    private void moveTrainAroundTrack(int startPosition, int endPosition) {
        int currentPosition = startPosition;
        do {
            slotSem[currentPosition + 1].P();
            slots[currentPosition + 1] = slots[currentPosition];
            slots[currentPosition] = "[..]";
            theTrainActivity.addMovedTo(currentPosition + 1);
            slotSem[currentPosition].V();
            currentPosition++;
        } while (currentPosition < endPosition);
    }

    public void moveTrainThroughJunctionFromATrack() {
        aMutexSem.P();
        if (aUsingJunction.incrementAndGet() == 1)
        {
            junctionLock.P();
        }
        aMutexSem.V();
        slotSem[9].P();
        slotSem[18].P();
        slots[9] = slots[8];
        slots[8] = "[..]";
        slotSem[8].V();
        theTrainActivity.addMovedTo(9);
        CDS.idleQuietly((int) (Math.random() * 10));
        slotSem[0].P();
        slotSem[10].P();
        slots[0] = slots[9];
        slots[9] = "[..]";
        slotSem[9].V();
        slotSem[18].V();
        theTrainActivity.addMovedTo(0);
        slots[10] = slots[0];
        slots[0] = "[..]";
        theTrainActivity.addMovedTo(10);
        CDS.idleQuietly((int) (Math.random() * 10));
        slotSem[11].P();
        slots[11] = slots[10];
        slots[10] = "[..]";
        slotSem[0].V();
        slotSem[10].V();
        theTrainActivity.addMovedTo(11);
        aMutexSem.P();
        if (aUsingJunction.decrementAndGet() == 0)
        {
            junctionLock.V();
        }
        aMutexSem.V();
        CDS.idleQuietly((int) (Math.random() * 10));
    }

    public void moveTrainThroughJunctionFromBTrack() {
        CDS.idleQuietly((int) (Math.random() * 10));
        bMutexSem.P();
        if (bUsingJunction.incrementAndGet() == 1)
        {
            junctionLock.P();
        }
        bMutexSem.V();
        CDS.idleQuietly((int) (Math.random() * 10));
        slotSem[9].P();
        slotSem[18].P();
        slots[18] = slots[17];
        slots[17] = "[..]";
        slotSem[17].V();
        theTrainActivity.addMovedTo(18);
        CDS.idleQuietly((int) (Math.random() * 10));
        slotSem[0].P();
        slotSem[1].P();
        slots[0] = slots[18];
        slots[18] = "[..]";
        slotSem[9].V();
        slotSem[18].V();
        theTrainActivity.addMovedTo(0);
        slots[1] = slots[0];
        slots[0] = "[..]";
        theTrainActivity.addMovedTo(1);
        CDS.idleQuietly((int) (Math.random() * 10));
        slotSem[2].P();
        slots[2] = slots[1];
        slots[1] = "[..]";
        slotSem[0].V();
        slotSem[1].V();
        theTrainActivity.addMovedTo(2);
        bMutexSem.P();
        if (bUsingJunction.decrementAndGet() == 0)
        {
            junctionLock.V();
        }
        bMutexSem.V();
        CDS.idleQuietly((int) (Math.random() * 10));
    }

    public void trainA_moveAroundBToJunction() {
        CDS.idleQuietly((int) (Math.random() * 100));
        moveTrainAroundTrack(11, 17);
        CDS.idleQuietly((int) (Math.random() * 100));
    }

    public void trainB_moveAroundAToJunction() {
        CDS.idleQuietly((int) (Math.random() * 100));
        moveTrainAroundTrack(2,8);
        CDS.idleQuietly((int) (Math.random() * 100));
    }

    public void trainA_MoveAroundToExit() {
        CDS.idleQuietly((int) (Math.random() * 100));
        moveTrainAroundTrack(2,5);
        CDS.idleQuietly((int) (Math.random() * 100));
    }

    public void trainB_MoveAroundToExit() {
        CDS.idleQuietly((int) (Math.random() * 100));
        moveTrainAroundTrack(11,14);
        CDS.idleQuietly((int) (Math.random() * 100));
    }

    public void trainA_MoveOffTrack(String trainName) {
        CDS.idleQuietly((int) (Math.random() * 10));
        theTrainActivity.addMessage("Train " + trainName + " is leaving the A loop at section 5");
        slots[5] = "[..]";
        slotSem[4].V();
        slotSem[5].V();
        CDS.idleQuietly((int) (Math.random() * 10));
        aCountSem.V();
    }

    public void trainB_MoveOffTrack(String trainName) {
        CDS.idleQuietly((int) (Math.random() * 10));
        theTrainActivity.addMessage("Train " + trainName + " is leaving the B loop at section 14");
        slots[14] = "[..]";
        slotSem[13].V();
        slotSem[14].V();
        CDS.idleQuietly((int) (Math.random() * 10));
        bCountSem.V();
    }

}


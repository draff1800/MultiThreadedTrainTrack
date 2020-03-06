public class TrainTrack {

    private final String[] slots = {"[..]", "[..]", "[..]", "[..]", "[..]", "[..]", "[..]", "[..]", "[..]",
            "[..]", "[..]", "[..]", "[..]", "[..]", "[..]", "[..]", "[..]", "[..]", "[..]"};

    private final MageeSemaphore slotSem[] = new MageeSemaphore[19];

    ProcessMonitor theTrainActivity;

    MageeSemaphore aTrainLimitSem;
    MageeSemaphore bTrainLimitSem;

    MageeSemaphore junctionSem;

    public TrainTrack() {
        theTrainActivity = new ProcessMonitor(slots);
        for (int i = 0; i <= 18; i++) {
            slotSem[i] = new MageeSemaphore(1);
        }
        aTrainLimitSem = new MageeSemaphore(4);
        bTrainLimitSem = new MageeSemaphore(4);
        junctionSem = new MageeSemaphore(1);
    }

    public void insertTrainAOnToTrack(String trainName) {
        CDS.idleQuietly((int) (Math.random() * 100));
        aTrainLimitSem.P();
        slotSem[5].P();
        slots[5] = "[" + trainName + "]";
        theTrainActivity.addMovedTo(5);
    }

    public void insertTrainBOnToTrack(String trainName) {
        CDS.idleQuietly((int) (Math.random() * 100));
        bTrainLimitSem.P();
        slotSem[14].P();
        slots[14] = "[" + trainName + "]";
        theTrainActivity.addMovedTo(14);
    }

    public void moveTrainAFromInsertionToJunction() {
        CDS.idleQuietly((int) (Math.random() * 100));
        moveTrainAroundTrack(5,8);
        CDS.idleQuietly((int) (Math.random() * 100));
    }

    public void moveTrainBFromInsertionToJunction() {
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

    public void moveTrainThroughJunctionToBSide() {
        junctionSem.P();
        slotSem[9].P();
        slots[9] = slots[8];
        slots[8] = "[..]";
        slotSem[8].V();
        theTrainActivity.addMovedTo(9);
        CDS.idleQuietly((int) (Math.random() * 10));
        slotSem[0].P();
        slots[0] = slots[9];
        slots[9] = "[..]";
        slotSem[9].V();
        theTrainActivity.addMovedTo(0);
        slotSem[10].P();
        slots[10] = slots[0];
        slots[0] = "[..]";
        slotSem[0].V();
        theTrainActivity.addMovedTo(10);
        CDS.idleQuietly((int) (Math.random() * 10));
        slotSem[11].P();
        slots[11] = slots[10];
        slots[10] = "[..]";
        slotSem[10].V();
        theTrainActivity.addMovedTo(11);
        junctionSem.V();
        CDS.idleQuietly((int) (Math.random() * 10));
    }

    public void moveTrainThroughJunctionToASide() {
        junctionSem.P();
        slotSem[18].P();
        slots[18] = slots[17];
        slots[17] = "[..]";
        slotSem[17].V();
        theTrainActivity.addMovedTo(18);
        CDS.idleQuietly((int) (Math.random() * 10));
        slotSem[0].P();
        slots[0] = slots[18];
        slots[18] = "[..]";
        slotSem[18].V();
        theTrainActivity.addMovedTo(0);
        slotSem[1].P();
        slots[1] = slots[0];
        slots[0] = "[..]";
        slotSem[0].V();
        theTrainActivity.addMovedTo(1);
        CDS.idleQuietly((int) (Math.random() * 10));
        slotSem[2].P();
        slots[2] = slots[1];
        slots[1] = "[..]";
        slotSem[1].V();
        theTrainActivity.addMovedTo(2);
        junctionSem.V();
        CDS.idleQuietly((int) (Math.random() * 10));
    }

    public void moveTrainAAroundBSide() {
        CDS.idleQuietly((int) (Math.random() * 100));
        moveTrainAroundTrack(11, 17);
        CDS.idleQuietly((int) (Math.random() * 100));
    }

    public void moveTrainBAroundASide() {
        CDS.idleQuietly((int) (Math.random() * 100));
        moveTrainAroundTrack(2,8);
        CDS.idleQuietly((int) (Math.random() * 100));
    }

    public void moveTrainAToExit() {
        CDS.idleQuietly((int) (Math.random() * 100));
        moveTrainAroundTrack(2,5);
        CDS.idleQuietly((int) (Math.random() * 100));
    }

    public void moveTrainBToExit() {
        CDS.idleQuietly((int) (Math.random() * 100));
        moveTrainAroundTrack(11,14);
        CDS.idleQuietly((int) (Math.random() * 100));
    }

    public void removeTrainAFromTrack(String trainName) {
        CDS.idleQuietly((int) (Math.random() * 10));
        theTrainActivity.addMessage("Train " + trainName + " is leaving the A loop at section 5");
        slots[5] = "[..]";
        slotSem[5].V();
        CDS.idleQuietly((int) (Math.random() * 10));
        aTrainLimitSem.V();
    }

    public void removeTrainBFromTrack(String trainName) {
        CDS.idleQuietly((int) (Math.random() * 10));
        theTrainActivity.addMessage("Train " + trainName + " is leaving the B loop at section 14");
        slots[14] = "[..]";
        slotSem[14].V();
        CDS.idleQuietly((int) (Math.random() * 10));
        bTrainLimitSem.V();
    }

}


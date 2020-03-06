public class TrainTrack {

    //Array to represent train track slots
    private final String[] slots = {"[..]", "[..]", "[..]", "[..]", "[..]", "[..]", "[..]", "[..]", "[..]",
            "[..]", "[..]", "[..]", "[..]", "[..]", "[..]", "[..]", "[..]", "[..]", "[..]"};

    private final MageeSemaphore slotSem[] = new MageeSemaphore[19]; //Array to hold slot semaphores

    ProcessMonitor processMonitor; //Object to hold train movement messages

    MageeSemaphore trainLimitSem; //Semaphore to limit number of simultaneous trains on track

    MageeSemaphore junctionSem; //Semaphore to lock junction access to one train process at a time

    public TrainTrack() {
        processMonitor = new ProcessMonitor(slots); //Initialise monitor, allowing it to monitor slots array
        //Fill slotSem[] with semaphores for each train track slot
        for (int i = 0; i <= 18; i++) {
            slotSem[i] = new MageeSemaphore(1);
        }
        trainLimitSem = new MageeSemaphore(8); //Limit track to 8 trains at a time
        junctionSem = new MageeSemaphore(1); //Initialise junction semaphore as unlocked
    }

    //Add an A train to the track, and decrement trainLimitSem
    public void insertTrainAOnToTrack(String trainName) {
        CDS.idleQuietly((int) (Math.random() * 100));
        trainLimitSem.P();
        slotSem[5].P(); //Lock slot 5 in order to safely add a train to it
        slots[5] = "[" + trainName + "]";
        processMonitor.addMovedTo(5);
    }

    //Add a B train to the track, and decrement trainLimitSem
    public void insertTrainBOnToTrack(String trainName) {
        CDS.idleQuietly((int) (Math.random() * 100));
        trainLimitSem.P();
        slotSem[14].P(); //Lock slot 14 in order to safely add a train to it
        slots[14] = "[" + trainName + "]";
        processMonitor.addMovedTo(14);
    }

    public void moveTrainAFromInsertionToJunction() {
        CDS.idleQuietly((int) (Math.random() * 100));
        moveTrainAroundTrack(5,9);
        CDS.idleQuietly((int) (Math.random() * 100));
    }

    public void moveTrainBFromInsertionToJunction() {
        CDS.idleQuietly((int) (Math.random() * 100));
        moveTrainAroundTrack(14, 18);
        CDS.idleQuietly((int) (Math.random() * 100));
    }

    //Move train from startPosition to endPosition
    private void moveTrainAroundTrack(int startPosition, int endPosition) {
        int currentPosition = startPosition;
        do {
            slotSem[currentPosition + 1].P();
            slots[currentPosition + 1] = slots[currentPosition];
            slots[currentPosition] = "[..]";
            processMonitor.addMovedTo(currentPosition + 1);
            slotSem[currentPosition].V();
            currentPosition++;
        } while (currentPosition < endPosition);
    }

    //Lock junction, and move train through junction slots until B side is reached
    public void moveTrainThroughJunctionToBSide() {
        junctionSem.P();
        slotSem[0].P();
        slots[0] = slots[9];
        slots[9] = "[..]";
        slotSem[9].V();
        processMonitor.addMovedTo(0);
        slotSem[10].P();
        slots[10] = slots[0];
        slots[0] = "[..]";
        slotSem[0].V();
        processMonitor.addMovedTo(10);
        junctionSem.V();
        CDS.idleQuietly((int) (Math.random() * 10));
    }

    //Lock junction, and move train through junction slots until A side is reached
    public void moveTrainThroughJunctionToASide() {
        junctionSem.P();
        slotSem[0].P();
        slots[0] = slots[18];
        slots[18] = "[..]";
        slotSem[18].V();
        processMonitor.addMovedTo(0);
        slotSem[1].P();
        slots[1] = slots[0];
        slots[0] = "[..]";
        slotSem[0].V();
        processMonitor.addMovedTo(1);
        junctionSem.V();
        CDS.idleQuietly((int) (Math.random() * 10));
    }

    public void moveTrainAAroundBSide() {
        CDS.idleQuietly((int) (Math.random() * 100));
        moveTrainAroundTrack(10, 18);
        CDS.idleQuietly((int) (Math.random() * 100));
    }

    public void moveTrainBAroundASide() {
        CDS.idleQuietly((int) (Math.random() * 100));
        moveTrainAroundTrack(1,9);
        CDS.idleQuietly((int) (Math.random() * 100));
    }

    public void moveTrainAToExit() {
        CDS.idleQuietly((int) (Math.random() * 100));
        moveTrainAroundTrack(1,5);
        CDS.idleQuietly((int) (Math.random() * 100));
    }

    public void moveTrainBToExit() {
        CDS.idleQuietly((int) (Math.random() * 100));
        moveTrainAroundTrack(10,14);
        CDS.idleQuietly((int) (Math.random() * 100));
    }

    //Empty slot 5, release its semaphore and allow one further train to join the track
    public void removeTrainAFromTrack(String trainName) {
        CDS.idleQuietly((int) (Math.random() * 10));
        processMonitor.addMessage("Train " + trainName + " is leaving the A loop at section 5");
        slots[5] = "[..]";
        slotSem[5].V();
        CDS.idleQuietly((int) (Math.random() * 10));
        trainLimitSem.V();
    }

    //Empty slot 14, release its semaphore and allow one further train to join the track
    public void removeTrainBFromTrack(String trainName) {
        CDS.idleQuietly((int) (Math.random() * 10));
        processMonitor.addMessage("Train " + trainName + " is leaving the B loop at section 14");
        slots[14] = "[..]";
        slotSem[14].V();
        CDS.idleQuietly((int) (Math.random() * 10));
        trainLimitSem.V();
    }

}


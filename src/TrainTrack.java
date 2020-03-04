import java.util.concurrent.atomic.*;

public class TrainTrack {

    private final String[] slots = {"[..]", "[..]", "[..]", "[..]", "[..]", "[..]", "[..]", "[..]", "[..]",
            "[..]", "[..]", "[..]", "[..]", "[..]", "[..]", "[..]", "[..]", "[..]", "[..]"};

    // declare array to hold the Binary Semaphores for access to track slots (sections)
    private final MageeSemaphore slotSem[] = new MageeSemaphore[19];

    // reference to train activity record
    ProcessMonitor theTrainActivity;

    // global count of trains on shared track
    AtomicInteger aUsingJunction;
    AtomicInteger bUsingJunction;

    // counting semaphore to limit number of trains on track
    MageeSemaphore aCountSem;
    MageeSemaphore bCountSem;

    // declare  Semaphores for mutually exclusive access to aUsingJunction
    private final MageeSemaphore aMutexSem;
    // declare  Semaphores for mutually exclusive access to bUsingJunction
    private final MageeSemaphore bMutexSem;

    // shared track lock
    MageeSemaphore junctionLock;

    /* Constructor for TrainTrack */
    public TrainTrack() {
        // record the train activity
        theTrainActivity = new ProcessMonitor(slots);
        // create the array of slotSems and set them all free (empty)
        for (int i = 0; i <= 18; i++) {
            slotSem[i] = new MageeSemaphore(1);
        }
        // create  semaphores for mutually exclusive access to global count
        aMutexSem = new MageeSemaphore(1);
        bMutexSem = new MageeSemaphore(1);
        // create global AtomicInteger count variables
        aUsingJunction = new AtomicInteger(0);
        bUsingJunction = new AtomicInteger(0);
        // create  semaphores for limiting number of trains on track
        aCountSem = new MageeSemaphore(3);
        bCountSem = new MageeSemaphore(3);
        // initially shared track is accessible
        junctionLock = new MageeSemaphore(1);
    }  // constructor

    public void trainA_MoveOnToTrack(String trainName) {
        CDS.idleQuietly((int) (Math.random() * 100));
        aCountSem.P(); // limit  number of trains on track to avoid deadlock
        // record the train activity
        slotSem[4].P();// wait for slot 4 to be free
        slotSem[5].P();// wait for slot 5 to be free
        slots[5] = "[" + trainName + "]"; // move train type A on to slot zero
        slotSem[4].V();
        theTrainActivity.addMovedTo(5); // record the train activity
    }// end trainA_movedOnToTrack

    public void trainB_MoveOnToTrack(String trainName) {
        // record the train activity
        bCountSem.P();  // limit  number of trains on track to avoid deadlock
        CDS.idleQuietly((int) (Math.random() * 100));
        slotSem[13].P();// wait for slot 13 to be free
        slotSem[14].P();// wait for slot 13 to be free
        slots[14] = "[" + trainName + "]"; // move train type B on to slot sixteen
        slotSem[13].V();
        theTrainActivity.addMovedTo(14); // record the train activity
        // }// end trainB_movedOnToTrack
    }

    public void trainA_MoveFromEntryToJunction() {
        CDS.idleQuietly((int) (Math.random() * 100));
        moveTrainAroundTrack(5,8);
        CDS.idleQuietly((int) (Math.random() * 100));
    } // end trainA_MoveAroundToJunction

    public void trainB_MoveFromEntryToJunction() {
        CDS.idleQuietly((int) (Math.random() * 100));
        moveTrainAroundTrack(14, 17);
        CDS.idleQuietly((int) (Math.random() * 100));
    } // end trainB_MoveAroundToJunction

    private void moveTrainAroundTrack(int startPosition, int endPosition) {
        int currentPosition = startPosition;
        do {
            slotSem[currentPosition + 1].P(); // wait for the slot ahead to be free
            slots[currentPosition + 1] = slots[currentPosition]; // move train forward
            slots[currentPosition] = "[..]"; //clear the slot the train vacated
            theTrainActivity.addMovedTo(currentPosition + 1); //record the train activity
            slotSem[currentPosition].V(); //signal slot you are leaving
            currentPosition++;
        } while (currentPosition < endPosition);
    }

    public void moveTrainThroughJunctionFromATrack() {
        // wait for the necessary conditions to get access to shared track
        aMutexSem.P(); // obtain mutually exclusive access to global variable aUsingJunction
        if (aUsingJunction.incrementAndGet() == 1)// if first A train joining shared track
        {
            junctionLock.P();  // grab lock to shared track
        }
        aMutexSem.V(); // release mutually exclusive access to global variable aUsingJunction
        slotSem[9].P();
        slotSem[18].P();
        slots[9] = slots[8];
        slots[8] = "[..]";
        slotSem[8].V(); //move from slot[10] to slot[9]
        theTrainActivity.addMovedTo(9);  //record the train activity
        CDS.idleQuietly((int) (Math.random() * 10));
        // move along shared track
        slotSem[0].P();
        slotSem[10].P();
        slots[0] = slots[9];
        slots[9] = "[..]";
        slotSem[9].V(); //move from slot[9] to slot[8]
        slotSem[18].V();
        theTrainActivity.addMovedTo(0); // record the train activity
        slots[10] = slots[0];
        slots[0] = "[..]";
        theTrainActivity.addMovedTo(10); // record the train activity
        CDS.idleQuietly((int) (Math.random() * 10));
        // move off shared track
        slotSem[11].P();
        slots[11] = slots[10];
        slots[10] = "[..]";
        slotSem[0].V();
        slotSem[10].V(); //move from slot[7] to slot[16]
        theTrainActivity.addMovedTo(11); // record the train activity
        // signal conditions when leaving shared track
        aMutexSem.P(); // obtain mutually exclusive access to global variable aUsingJunction
        if (aUsingJunction.decrementAndGet() == 0) // if last A train leaving shared track
        {
            junctionLock.V(); // release lock to shared track
        }
        aMutexSem.V(); // release mutually exclusive access to global variable aUsingJunction
        CDS.idleQuietly((int) (Math.random() * 10));
    }// end   trainA_MoveThroughJunction

    public void moveTrainThroughJunctionFromBTrack() {
        CDS.idleQuietly((int) (Math.random() * 10));
        // wait for the necessary conditions to get access to shared track
        bMutexSem.P(); // obtain mutually exclusive access to global variable bUsingJunction
        if (bUsingJunction.incrementAndGet() == 1)// if first B train joining shared track
        {
            junctionLock.P();  // grab lock to shared track
        }
        bMutexSem.V(); // release mutually exclusive access to global variable bUsingJunction
        CDS.idleQuietly((int) (Math.random() * 10));
        // move on to shared track
        slotSem[9].P();
        slotSem[18].P();
        slots[18] = slots[17];
        slots[17] = "[..]";
        slotSem[17].V(); //move from slot[10] to slot[9]
        theTrainActivity.addMovedTo(18);  //record the train activity
        CDS.idleQuietly((int) (Math.random() * 10));
        // move along shared track
        slotSem[0].P();
        slotSem[1].P();
        slots[0] = slots[18];
        slots[18] = "[..]";
        slotSem[9].V(); //move from slot[9] to slot[8]
        slotSem[18].V();
        theTrainActivity.addMovedTo(0); // record the train activity
        slots[1] = slots[0];
        slots[0] = "[..]";
        theTrainActivity.addMovedTo(1); // record the train activity
        CDS.idleQuietly((int) (Math.random() * 10));
        // move off shared track
        slotSem[2].P();
        slots[2] = slots[1];
        slots[1] = "[..]";
        slotSem[0].V();
        slotSem[1].V(); //move from slot[7] to slot[16]
        theTrainActivity.addMovedTo(2); // record the train activity
        // signal conditions when leaving shared track
        bMutexSem.P(); // obtain mutually exclusive access to global variable aUsingJunction
        if (bUsingJunction.decrementAndGet() == 0) // if last B train leaving shared track
        {
            junctionLock.V(); // release lock to shared track
        }
        bMutexSem.V(); // release mutually exclusive access to global variable aUsingJunction
        CDS.idleQuietly((int) (Math.random() * 10));
    }// end   trainB_MoveThroughJunction

    public void trainA_moveAroundBToJunction() {
        CDS.idleQuietly((int) (Math.random() * 100));
        moveTrainAroundTrack(11, 17);
        CDS.idleQuietly((int) (Math.random() * 100));
    } // end trainA_MoveAroundBToJunction

    public void trainB_moveAroundAToJunction() {
        CDS.idleQuietly((int) (Math.random() * 100));
        moveTrainAroundTrack(2,8);
        CDS.idleQuietly((int) (Math.random() * 100));
    } // end trainB_MoveAroundAToJunction

    public void trainA_MoveAroundToExit() {
        CDS.idleQuietly((int) (Math.random() * 100));
        moveTrainAroundTrack(2,5);
        CDS.idleQuietly((int) (Math.random() * 100));
    } // end trainA_MoveAroundToJunction

    public void trainB_MoveAroundToExit() {
        CDS.idleQuietly((int) (Math.random() * 100));
        moveTrainAroundTrack(11,14);
        CDS.idleQuietly((int) (Math.random() * 100));
    } // end trainA_MoveAroundToJunction

    public void trainA_MoveOffTrack(String trainName) {
        CDS.idleQuietly((int) (Math.random() * 10));
        // record the train activity
        theTrainActivity.addMessage("Train " + trainName + " is leaving the A loop at section 5");
        slots[5] = "[..]"; // move train type A off slot zero
        slotSem[4].V();
        slotSem[5].V();// signal slot 0 to be free
        CDS.idleQuietly((int) (Math.random() * 10));
        aCountSem.V(); // signal space for another A train
    }// end trainA_movedOffTrack

    public void trainB_MoveOffTrack(String trainName) {
        CDS.idleQuietly((int) (Math.random() * 10));
        // record the train activity
        theTrainActivity.addMessage("Train " + trainName + " is leaving the B loop at section 14");
        slots[14] = "[..]"; // move train type A off slot zero
        slotSem[13].V();
        slotSem[14].V();// signal slot 0 to be free
        CDS.idleQuietly((int) (Math.random() * 10));
        bCountSem.V(); // signal space for another B train
    }// end trainB_movedOffTrack

} // end Train track


class MainDriver {

    static final int NUM_OF_A_TRAINS = 10; //Total number of A trains to enter track
    static final int NUM_OF_B_TRAINS = 10; //Total number of B trains to enter track
    static TrainTrack theTrainTrack;

    public static void main(String[] args) {

        theTrainTrack = new TrainTrack();

        System.out.println("STARTED");

        //Create two arrays to hold A and B train processes respectively
        TrainAProcess[] trainAProcess = new TrainAProcess[NUM_OF_A_TRAINS];
        TrainBProcess[] trainBProcess = new TrainBProcess[NUM_OF_B_TRAINS];

        //Fill trainAProcess array with A train processes
        for (int i = 0; i < NUM_OF_A_TRAINS; i++) {
            CDS.idleQuietly((int) (Math.random() * 1000));
            trainAProcess[i] = new TrainAProcess("A" + i, theTrainTrack);
        }
        //Fill trainBProcess array with B train processes
        for (int i = 0; i < NUM_OF_B_TRAINS; i++) {
            CDS.idleQuietly((int) (Math.random() * 1000));
            trainBProcess[i] = new TrainBProcess("B" + i, theTrainTrack);
        }

        //Run all trainAProcess threads
        for (int i = 0; i < NUM_OF_A_TRAINS; i++) {
            trainAProcess[i].start();
        }
        //Run all trainBProcess threads
        for (int i = 0; i < NUM_OF_B_TRAINS; i++) {
            trainBProcess[i].start();
        }

        //Wait for all trainAProcess threads to finish
        for (int i = 0; i < NUM_OF_A_TRAINS; i++) {
            try {
                trainAProcess[i].join();
            } catch (InterruptedException ex) {
            }
        }
        //Wait for all trainAProcess threads to finish
        for (int i = 0; i < NUM_OF_B_TRAINS; i++) {
            try {
                trainBProcess[i].join();
            } catch (InterruptedException ex) {
            }
        }

        theTrainTrack.processMonitor.printActivities(); //Print recorded train processes

        System.out.println("All trains have successfully travelled 5 circuits of their track loop ");
    }

}

class TrainBProcess extends Thread {

    String trainName;
    TrainTrack theTrack;
    public TrainBProcess(String trainName, TrainTrack theTrack) {
        this.trainName = trainName;
        this.theTrack = theTrack;
    }

    @Override
    public void run() { //Move a single trainB from entry to exit
        theTrack.insertTrainBOnToTrack(trainName);
        theTrack.moveTrainBFromInsertionToJunction();
        theTrack.moveTrainThroughJunctionToASide();
        theTrack.moveTrainBAroundASide();
        theTrack.moveTrainThroughJunctionToBSide();
        theTrack.moveTrainBToExit();
        theTrack.removeTrainBFromTrack(trainName);
    }

}

class TrainAProcess extends Thread {

    String trainName;
    TrainTrack theTrack;
    public TrainAProcess(String trainName, TrainTrack theTrack) {
        this.trainName = trainName;
        this.theTrack = theTrack;
    }

    @Override
    public void run() {
        theTrack.trainA_MoveOnToTrack(trainName);
        theTrack.trainA_MoveFromEntryToJunction();
        theTrack.moveTrainThroughJunctionFromATrack();
        theTrack.trainA_moveAroundBToJunction();
        theTrack.moveTrainThroughJunctionFromBTrack();
        theTrack.trainA_MoveAroundToExit();
        theTrack.trainA_MoveOffTrack(trainName);
    }

}



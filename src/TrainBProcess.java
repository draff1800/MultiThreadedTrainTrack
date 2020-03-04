class TrainBProcess extends Thread {
    String trainName;
    TrainTrack theTrack;
    public TrainBProcess(String trainName, TrainTrack theTrack) {
        this.trainName = trainName;
        this.theTrack = theTrack;
    }

    @Override
    public void run() {
        theTrack.trainB_MoveOnToTrack(trainName);
        theTrack.trainB_MoveFromEntryToJunction();
        theTrack.moveTrainThroughJunctionFromBTrack();
        theTrack.trainB_moveAroundAToJunction();
        theTrack.moveTrainThroughJunctionFromATrack();
        theTrack.trainB_MoveAroundToExit();
        theTrack.trainB_MoveOffTrack(trainName);
    }
}

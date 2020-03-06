import java.util.concurrent.CopyOnWriteArrayList;
import java.util.Iterator;

public class ProcessMonitor {

    private final CopyOnWriteArrayList<String> theActivities;

    private final String[] trainTrack;

    public ProcessMonitor(String[] trainTrack) {
        theActivities = new CopyOnWriteArrayList<>();
        this.trainTrack = trainTrack;
    }

    //Add "moved" activity message
    public void addMovedTo(int section) {
        String tempString1 = "Train " + trainTrack[section] + " moving/moved to [" + section + "]";
        theActivities.add(tempString1);
        theActivities.add(trackString());
    }

    //Add custom message
    public void addMessage(String message) {
        String tempString1 = message;
        theActivities.add(tempString1);
    }

    //Print added messages
    public void printActivities() {
        System.out.println("TRAIN TRACK ACTIVITY(Tracks [0..16])");
        Iterator<String> iterator = theActivities.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }

    //Returns an image of the track including trains' current positions
    public String trackString() {
        String trackStateAsString = "       " + trainTrack[5] + "\n"
                + "   " + trainTrack[4] + "    " + trainTrack[6] + "\n"
                + "  " + trainTrack[3] + "      " + trainTrack[7] + "\n"
                + "   " + trainTrack[2] + "    " + trainTrack[8] + "\n"
                + "    " + trainTrack[1] + "  " + trainTrack[9] + "\n"
                + "       " + trainTrack[0] + "\n"
                + "    " + trainTrack[10] + "  " + trainTrack[18] + "\n"
                + "   " + trainTrack[11] + "    " + trainTrack[17] + "\n"
                + "  " + trainTrack[12] + "      " + trainTrack[16] + "\n"
                + "   " + trainTrack[13] + "    " + trainTrack[15] + "\n"
                + "       " + trainTrack[14];

        return (trackStateAsString);
    }

}


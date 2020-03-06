public class CDS {

    //Pause program for set amount of time
    public static void idle(int millisecs) {
        Thread mainThread = Thread.currentThread();
        System.out.println(mainThread.getName() + ": About to sleep");
        try {
            Thread.sleep(millisecs);
        } catch (InterruptedException e) {
        }
        System.out.println(mainThread.getName() + ": Woken up");
    }

    //Pause program for set amount of time (No print messages)
    public static void idleQuietly(int millisecs) {
        try {
            Thread.sleep(millisecs);
        } catch (InterruptedException e) {
        }
    }

}


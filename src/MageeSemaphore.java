import java.util.concurrent.*;

class MageeSemaphore
{
    private Semaphore sem;

    public MageeSemaphore (int initialCount)
    {
        sem = new Semaphore(initialCount);
    }

    public void P() {
        try {
            sem.acquire();
        } catch (InterruptedException ex) {
            System.out.println("Interrupted when waiting");
        }
    }

    public void V() {
        sem.release();
    }

}


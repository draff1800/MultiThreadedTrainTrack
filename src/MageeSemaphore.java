import java.util.concurrent.*;

class MageeSemaphore
{
    private Semaphore sem;

    public MageeSemaphore (int initialCount)
    {
        sem = new Semaphore(initialCount);
    }

    //Lock Semaphore (Decrement initialCount)
    public void P() {
        try {
            sem.acquire();
        } catch (InterruptedException ex) {
            System.out.println("Interrupted when waiting");
        }
    }

    //Lock Semaphore (Increment initialCount)
    public void V() {
        sem.release();
    }

}


package pl.edu.agh.calculationp2p.network.utilities;

import pl.edu.agh.calculationp2p.state.idle.IdleInterrupter;

import java.util.concurrent.Semaphore;

public class DummyInterrupter implements IdleInterrupter {
    Semaphore semaphore = null;
    Semaphore secondSemaphore = null;

    @Override
    public void wake()
    {
        if (semaphore != null)
        {
            semaphore.release();
        }
        if (secondSemaphore != null)
        {
            secondSemaphore.release();
        }
    }

    public void addSemaphore(Semaphore semaphore)
    {
        this.semaphore = semaphore;
    }

    public void addSecondSemaphore(Semaphore semaphore)
    {
        this.secondSemaphore = semaphore;
    }

}

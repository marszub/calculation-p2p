package pl.edu.agh.calculationp2p.state.idle;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Idle implements IdleInterrupter{
    private final Lock lock;
    private final Condition wake;
    private boolean hasWork;

    public Idle(){
        lock = new ReentrantLock();
        wake = lock.newCondition();
        hasWork = false;
    }

    public void sleep() throws InterruptedException {
        try {
            lock.lock();
            while (!hasWork)
                wake.await();
            hasWork = false;
        }finally {
            lock.unlock();
        }
    }

    public void sleep(long time) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        long currentTime = startTime;

        try {
            lock.lock();
            while (!hasWork &&
                    startTime - currentTime + time > 0 &&
                    wake.await(startTime - currentTime + time, TimeUnit.MILLISECONDS)) {
                currentTime = System.currentTimeMillis();
            }
            hasWork = false;
        }finally {
            lock.unlock();
        }
    }

    @Override
    public void wake() {
        if(hasWork)
            return;
        try {
            lock.lock();
            hasWork = true;
            wake.signal();
        }finally {
            lock.unlock();
        }
    }
}

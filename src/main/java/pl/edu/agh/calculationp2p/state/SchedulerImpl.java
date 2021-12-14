package pl.edu.agh.calculationp2p.state;

import pl.edu.agh.calculationp2p.state.request.MethodRequest;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SchedulerImpl implements Runnable, Scheduler {
    BlockingQueue<MethodRequest> activationQueue;
    Servant servant;
    Thread thread;

    public SchedulerImpl(Servant servant){
        activationQueue = new LinkedBlockingQueue<>();
        this.servant = servant;

        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        while(true){
            try {
                dispatch();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    @Override
    public void enqueue(MethodRequest request) throws InterruptedException {
        activationQueue.put(request);
    }

    private void dispatch() throws InterruptedException {
        MethodRequest request = activationQueue.take();
        request.call(servant);
    }
}

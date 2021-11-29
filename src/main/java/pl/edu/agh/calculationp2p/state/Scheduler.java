package pl.edu.agh.calculationp2p.state;

import pl.edu.agh.calculationp2p.state.MethodRequest;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Scheduler implements Runnable {
    BlockingQueue<MethodRequest> activationQueue;
    Servant servant;
    Thread thread;

    public Scheduler(){
        activationQueue = new LinkedBlockingQueue<>();
        servant = new Servant();
        thread = new Thread(this);
    }

    @Override
    public void run() {

    }
}

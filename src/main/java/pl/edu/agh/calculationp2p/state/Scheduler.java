package pl.edu.agh.calculationp2p.state;

import pl.edu.agh.calculationp2p.state.request.MethodRequest;

public interface Scheduler {
    void enqueue(MethodRequest request) throws InterruptedException;
}

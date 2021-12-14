package pl.edu.agh.calculationp2p.state.proxy;

import pl.edu.agh.calculationp2p.state.future.Future;

public interface TaskGiver {
    Future<Integer> getTask();
    Future<Void> observeTask(Integer taskId, Thread thread);
    void finishTask(Integer taskId);
}

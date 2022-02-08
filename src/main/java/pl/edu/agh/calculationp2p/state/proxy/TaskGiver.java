package pl.edu.agh.calculationp2p.state.proxy;

import pl.edu.agh.calculationp2p.calculationTask.TaskResult;
import pl.edu.agh.calculationp2p.state.future.Future;

import java.util.Optional;

public interface TaskGiver {
    // modul obliczen - interesuje go tylko to co liczy
    Future<Optional<Integer>> getTask();
    Future<Void> observeTask(Integer taskId);
    void finishTask(Integer taskId, TaskResult result);
    Future<Boolean> reserveTask(Integer taskID);
}

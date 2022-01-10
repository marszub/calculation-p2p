package pl.edu.agh.calculationp2p.state.proxy;

import pl.edu.agh.calculationp2p.calculation.TaskResult;
import pl.edu.agh.calculationp2p.state.Scheduler;
import pl.edu.agh.calculationp2p.state.future.Future;
import pl.edu.agh.calculationp2p.state.request.FinishTaskRequest;
import pl.edu.agh.calculationp2p.state.request.GetTaskRequest;

import java.util.Optional;

public class TaskGiverImpl implements TaskGiver {
    private Scheduler scheduler;

    public TaskGiverImpl(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public Future<Optional<Integer>> getTask() {
        Future<Optional<Integer>> future = new Future();
        GetTaskRequest request = new GetTaskRequest(future, Thread.currentThread().getId());
        try {
            scheduler.enqueue(request);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return future;
    }

    @Override
    public Future<Void> observeTask(Integer taskId, Thread thread) {
        return null;
    }

    @Override
    public void finishTask(Integer taskId, TaskResult result) {
        FinishTaskRequest request = new FinishTaskRequest(taskId, result);
        try {
            scheduler.enqueue(request);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}

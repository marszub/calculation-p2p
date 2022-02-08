package pl.edu.agh.calculationp2p.state.proxy;

import pl.edu.agh.calculationp2p.calculationTask.TaskResult;
import pl.edu.agh.calculationp2p.state.Scheduler;
import pl.edu.agh.calculationp2p.state.future.Future;
import pl.edu.agh.calculationp2p.state.request.*;

import java.lang.reflect.Method;
import java.util.Optional;

public class TaskGiverImpl implements TaskGiver {
    private Scheduler scheduler;

    public TaskGiverImpl(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public Future<Optional<Integer>> getTask() {
        Future<Optional<Integer>> taskIDFuture = new Future();
        MethodRequest findInteger = new GetTaskRequest(taskIDFuture);
        try {
            scheduler.enqueue(findInteger);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return taskIDFuture;
    }

    @Override
    public Future<Boolean> reserveTask(Integer taskID) {
        Future<Boolean> future = new Future<>();
        MethodRequest reserveRequest = new ReserveRequest(future, taskID);
        try {
            scheduler.enqueue(reserveRequest);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return future;
    }

    @Override
    public Future<Void> observeTask(Integer taskId) {

        Future<Void> flag = new Future<>();
        MethodRequest request = new ObserveTaskRequest(taskId, flag);
        try {
            scheduler.enqueue(request);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return flag;
    }

    @Override
    public void finishTask(Integer taskId, TaskResult result) {
        MethodRequest request = new CalculateRequest(taskId, result);
        try {
            scheduler.enqueue(request);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}

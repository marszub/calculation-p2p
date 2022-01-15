package pl.edu.agh.calculationp2p.state.proxy;

import pl.edu.agh.calculationp2p.calculation.TaskResult;
import pl.edu.agh.calculationp2p.state.Scheduler;
import pl.edu.agh.calculationp2p.state.future.Future;
import pl.edu.agh.calculationp2p.state.request.FinishTaskRequest;
import pl.edu.agh.calculationp2p.state.request.ReserveRequest;
import pl.edu.agh.calculationp2p.state.task.TaskRecord;

public class StateUpdaterImpl implements StateUpdater{
    private Scheduler scheduler;

    public StateUpdaterImpl(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public Future<TaskRecord> reserve(int task, int nodeId) {
        Future<TaskRecord> future = new Future<>();
        ReserveRequest request = new ReserveRequest(future, task, nodeId);
        try {
            scheduler.enqueue(request);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return future;
    }

    @Override
    public Future<TaskRecord> calculate(int task, int nodeId, TaskResult result) {
        Future<TaskRecord> future = new Future<>();
        FinishTaskRequest request = new FinishTaskRequest(future, task, nodeId, result);
        try {
            scheduler.enqueue(request);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return future;
    }
}

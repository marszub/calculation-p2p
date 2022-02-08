package pl.edu.agh.calculationp2p.state.proxy;

import pl.edu.agh.calculationp2p.calculationTask.TaskResult;
import pl.edu.agh.calculationp2p.state.Scheduler;
import pl.edu.agh.calculationp2p.state.future.Future;
import pl.edu.agh.calculationp2p.state.request.*;
import pl.edu.agh.calculationp2p.state.task.TaskRecord;
import pl.edu.agh.calculationp2p.state.Progress;

public class StateUpdaterImpl implements StateUpdater{
    private Scheduler scheduler;

    public StateUpdaterImpl(Scheduler scheduler) {
        this.scheduler = scheduler;
    }


    // TODO: hard update - only reserved by nodeId, this node was disconnected



    @Override
    public Future<TaskRecord> updateTask(TaskRecord taskRecord) {
        Future<TaskRecord> future = new Future<>();
        MethodRequest request = new UpdateTask(future, taskRecord);
        try {
            scheduler.enqueue(request);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return future;
    }

    @Override
    public void initProgress(Progress progress) {
        Future<Progress> future = new Future();
        MethodRequest request = new UpdateProgressRequest(future, progress);
        try {
            scheduler.enqueue(request);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setNodeId(Integer nodeId) {
        MethodRequest request = new SetIdRequest(nodeId);
        try {
            scheduler.enqueue(request);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clearNodeReservations(Integer nodeID) {
        MethodRequest request = new ClearReservationRequest(nodeID);
        try {
            scheduler.enqueue(request);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

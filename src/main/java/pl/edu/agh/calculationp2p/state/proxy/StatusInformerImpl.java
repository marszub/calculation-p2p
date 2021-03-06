package pl.edu.agh.calculationp2p.state.proxy;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import pl.edu.agh.calculationp2p.state.Progress;
import pl.edu.agh.calculationp2p.state.Scheduler;
import pl.edu.agh.calculationp2p.state.future.Future;
import pl.edu.agh.calculationp2p.state.future.Observation;
import pl.edu.agh.calculationp2p.state.idle.IdleInterrupter;
import pl.edu.agh.calculationp2p.state.request.*;
import pl.edu.agh.calculationp2p.state.task.TaskRecord;

import java.util.List;
import java.util.Optional;

public class StatusInformerImpl implements StatusInformer{
    private Scheduler scheduler;

    public StatusInformerImpl(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public Future<List<TaskRecord>> getReservedTasks() {
        Future<List<TaskRecord>> future = new Future();
        MethodRequest getReservedTasksRequest = new GetReservedTasksRequest(future);
        try {
            scheduler.enqueue(getReservedTasksRequest);
        } catch (InterruptedException e) {
            Logger logger = LoggerFactory.getLogger("");
            logger.error(e.getMessage());
        }
        return future;
    }

    @Override
    public Future<Optional<Integer>> getFreeTask() {
        Future<Optional<Integer>> taskIDFuture = new Future();
        MethodRequest findInteger = new GetTaskRequest(taskIDFuture);
        try {
            scheduler.enqueue(findInteger);
        } catch (InterruptedException e) {
            Logger logger = LoggerFactory.getLogger("");
            logger.error(e.getMessage());
        }
        return taskIDFuture;
    }

    @Override
    public void clearNodeReservations(Integer nodeID) {
        MethodRequest request = new ClearReservationRequest(nodeID);
        try {
            scheduler.enqueue(request);
        } catch (InterruptedException e) {
            Logger logger = LoggerFactory.getLogger("");
            logger.error(e.getMessage());
        }
    }

    @Override
    public Future<Progress> getProgress() {
        Future<Progress> future = new Future<>();
        MethodRequest request = new GetProgressRequest(future);
        try {
            scheduler.enqueue(request);
        } catch (InterruptedException e) {
            Logger logger = LoggerFactory.getLogger("");
            logger.error(e.getMessage());
        }
        return future;
    }

    @Override
    public Future<TaskRecord> getTaskProgress(Integer taskID) {
        Future<TaskRecord> future = new Future<>();
        MethodRequest request = new GetTaskProgressRequest(future, taskID);
        try {
            scheduler.enqueue(request);
        } catch (InterruptedException e) {
            Logger logger = LoggerFactory.getLogger("");
            logger.error(e.getMessage());
        }
        return future;
    }

    @Override
    public Future<Observation> observeCalculated(IdleInterrupter interrupter) {
        Future<Observation> future = new Future<>();
        MethodRequest request = new ObserveCalculatedRequest(future, interrupter);
        try {
            scheduler.enqueue(request);
        } catch (InterruptedException e) {
            Logger logger = LoggerFactory.getLogger("");
            logger.error(e.getMessage());
        }
        return future;
    }

    @Override
    public Future<Observation> observeReserved(IdleInterrupter interrupter) {
        Future<Observation> future = new Future<>();
        MethodRequest request = new ObserveReservedRequest(future, interrupter);
        try {
            scheduler.enqueue(request);
        } catch (InterruptedException e) {
            Logger logger = LoggerFactory.getLogger("");
            logger.error(e.getMessage());
        }
        return future;
    }

    @Override
    public void cancelObservation(IdleInterrupter interrupter) {
        MethodRequest request = new CancelObservationRequest(interrupter);
        try {
            scheduler.enqueue(request);
        } catch (InterruptedException e) {
            Logger logger = LoggerFactory.getLogger("");
            logger.error(e.getMessage());
        }
    }
}

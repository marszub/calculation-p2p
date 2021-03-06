package pl.edu.agh.calculationp2p.state.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.edu.agh.calculationp2p.calculationTask.hashBreaking.HashTaskResult;
import pl.edu.agh.calculationp2p.state.Progress;
import pl.edu.agh.calculationp2p.state.Servant;
import pl.edu.agh.calculationp2p.state.ServantImpl;
import pl.edu.agh.calculationp2p.state.future.Future;
import pl.edu.agh.calculationp2p.state.publisher.TaskPublisher;
import pl.edu.agh.calculationp2p.state.publisher.TaskStatePublisher;
import pl.edu.agh.calculationp2p.state.task.TaskRecord;
import pl.edu.agh.calculationp2p.state.task.TaskState;

class UpdateProgressRequestTest {
    @Test
    void call() {
        Integer nodeID = 10;
        Progress progress = new Progress(4);
        TaskPublisher taskPublisher = new TaskPublisher();
        TaskStatePublisher calculatedPublisher = new  TaskStatePublisher(TaskState.Calculated);
        TaskStatePublisher reservedPublisher = new  TaskStatePublisher(TaskState.Reserved);

        TaskRecord taskRecord1 = new TaskRecord(1, TaskState.Free, 10, new HashTaskResult());
        TaskRecord taskRecord2 = new TaskRecord(2, TaskState.Free, 10, new HashTaskResult());
        TaskRecord taskRecord3 = new TaskRecord(3, TaskState.Free, 10, new HashTaskResult());

        progress.update(taskRecord1);
        progress.update(taskRecord2);
        progress.update(taskRecord3);

        Servant servant = new ServantImpl(progress, taskPublisher, reservedPublisher, calculatedPublisher, 2);

        Progress progressUpdated = new Progress(4);

        TaskRecord taskRecord4 = new TaskRecord(3, TaskState.Calculated, 1, new HashTaskResult());
        progressUpdated.update(taskRecord4);

        Future<Progress> future = new Future();
        MethodRequest request = new UpdateProgressRequest(future, progressUpdated);
        request.call(servant);

        Assertions.assertTrue(progress.get(3).getState() == taskRecord4.getState());

    }
}
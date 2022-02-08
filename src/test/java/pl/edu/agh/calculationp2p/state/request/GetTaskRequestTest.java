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

import java.util.Optional;

class GetTaskRequestTest {

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

        Servant servant = new ServantImpl(progress, taskPublisher, reservedPublisher, calculatedPublisher, nodeID);

        Future<Optional<Integer>> future = new Future();

        MethodRequest request = new GetTaskRequest(future, Thread.currentThread().getId());
        request.call(servant);

        Assertions.assertTrue(future.isReady());
        Assertions.assertFalse(future.get().isEmpty());
    }
}
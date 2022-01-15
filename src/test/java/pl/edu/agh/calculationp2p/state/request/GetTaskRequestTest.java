package pl.edu.agh.calculationp2p.state.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.edu.agh.calculationp2p.calculation.TaskResult;
import pl.edu.agh.calculationp2p.calculation.TaskResultImpl;
import pl.edu.agh.calculationp2p.state.Progress;
import pl.edu.agh.calculationp2p.state.Servant;
import pl.edu.agh.calculationp2p.state.ServantImpl;
import pl.edu.agh.calculationp2p.state.future.Future;
import pl.edu.agh.calculationp2p.state.publisher.CalculatedPublisher;
import pl.edu.agh.calculationp2p.state.publisher.ReservedPublisher;
import pl.edu.agh.calculationp2p.state.publisher.TaskPublisher;
import pl.edu.agh.calculationp2p.state.task.TaskRecord;
import pl.edu.agh.calculationp2p.state.task.TaskState;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class GetTaskRequestTest {

    @Test
    void call() {
        Integer nodeID = 10;
        Progress progress = new Progress();
        TaskPublisher taskPublisher = new TaskPublisher();
        CalculatedPublisher calculatedPublisher = new CalculatedPublisher();
        ReservedPublisher reservedPublisher = new ReservedPublisher();

        TaskRecord taskRecord1 = new TaskRecord(1, TaskState.Free, -1, new TaskResultImpl());
        TaskRecord taskRecord2 = new TaskRecord(2, TaskState.Free, -1, new TaskResultImpl());
        TaskRecord taskRecord3 = new TaskRecord(3, TaskState.Free, -1, new TaskResultImpl());

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
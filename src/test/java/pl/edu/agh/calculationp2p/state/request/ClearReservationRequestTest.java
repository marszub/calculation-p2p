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

import static org.junit.jupiter.api.Assertions.*;

class ClearReservationRequestTest {

    @Test
    void call() {
        Integer nodeID = 10;
        Progress progress = new Progress(6);
        TaskPublisher taskPublisher = new TaskPublisher();
        TaskStatePublisher calculatedPublisher = new  TaskStatePublisher(TaskState.Calculated);
        TaskStatePublisher reservedPublisher = new  TaskStatePublisher(TaskState.Reserved);

        TaskRecord taskRecord1 = new TaskRecord(1, TaskState.Reserved, 3, new HashTaskResult());
        TaskRecord taskRecord2 = new TaskRecord(2, TaskState.Free, 3, new HashTaskResult());
        TaskRecord taskRecord3 = new TaskRecord(3, TaskState.Reserved, 3, new HashTaskResult());
        TaskRecord taskRecord4 = new TaskRecord(4, TaskState.Reserved, 1, new HashTaskResult());
        TaskRecord taskRecord5 = new TaskRecord(5, TaskState.Calculated, 3, new HashTaskResult());

        progress.update(taskRecord1);
        progress.update(taskRecord2);
        progress.update(taskRecord3);
        progress.update(taskRecord4);
        progress.update(taskRecord5);

        Servant servant = new ServantImpl(progress, taskPublisher, reservedPublisher, calculatedPublisher, 2);

        Future<TaskRecord> future = new Future<>();
        MethodRequest request = new ClearReservationRequest(3);
        request.call(servant);

        boolean flag = false;
        for (TaskRecord record: progress.getTasks()) {
            if (record.getState() == TaskState.Reserved && record.getOwner() == nodeID){
                flag = true;
            }
        }
        assertFalse(flag);

    }
}
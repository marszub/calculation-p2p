package pl.edu.agh.calculationp2p.state;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.edu.agh.calculationp2p.calculationTask.hashBreaking.HashTaskResult;
import pl.edu.agh.calculationp2p.state.task.TaskRecord;
import pl.edu.agh.calculationp2p.state.task.TaskState;

import java.util.List;

class ProgressTest {

    @Test
    void update() {
        Progress progress = new Progress(2);

        TaskRecord taskRecord1 = new TaskRecord(1, TaskState.Free, 10, new HashTaskResult());
        TaskRecord taskRecord2 = new TaskRecord(1, TaskState.Reserved, 10, new HashTaskResult());

        progress.update(taskRecord1);
        progress.update(taskRecord2);

        Assertions.assertNotEquals(progress.get(taskRecord2.getTaskID()), taskRecord1);
        Assertions.assertEquals(progress.get(taskRecord1.getTaskID()), taskRecord2);
    }

    @Test
    void testClone() {
        Progress progress = new Progress(4);
        Progress cloned = progress.clone();
        Assertions.assertNotEquals(progress, cloned);
    }

    @Test
    void getFreeTasksList() {
        Progress progress = new Progress(4);
        TaskRecord taskRecord1 = new TaskRecord(1, TaskState.Free, 10, new HashTaskResult());
        TaskRecord taskRecord2 = new TaskRecord(2, TaskState.Reserved, 10, new HashTaskResult());
        TaskRecord taskRecord3 = new TaskRecord(3, TaskState.Free, 10, new HashTaskResult());
        progress.update(taskRecord1);
        progress.update(taskRecord2);
        progress.update(taskRecord3);
        List<Integer> list = progress.getFreeTasksList();
        Assertions.assertEquals(3, list.size());
    }


    @Test
    void serialize() {

    }
}
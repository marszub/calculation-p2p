package pl.edu.agh.calculationp2p.state.task;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.edu.agh.calculationp2p.calculationTask.hashBreaking.HashTaskResult;

class TaskRecordTest {

    @Test
    void hasHigherPriority() {
        TaskRecord taskRecord1 = new TaskRecord(1, TaskState.Free, 10, new HashTaskResult());
        TaskRecord taskRecord2 = new TaskRecord(2, TaskState.Reserved, 10, new HashTaskResult());
        TaskRecord taskRecord3 = new TaskRecord(3, TaskState.Calculated, 10, new HashTaskResult());
        TaskRecord taskRecord4 = new TaskRecord(1, TaskState.Free, 1, new HashTaskResult());
        TaskRecord taskRecord5 = new TaskRecord(2, TaskState.Free, 2, new HashTaskResult());
        TaskRecord taskRecord6 = new TaskRecord(3, TaskState.Reserved, 1, new HashTaskResult());
        TaskRecord taskRecord7 = new TaskRecord(3, TaskState.Free, -1, new HashTaskResult());
        TaskRecord taskRecord8 = new TaskRecord(3, TaskState.Free, 1, new HashTaskResult());

        Assertions.assertTrue(taskRecord2.hasHigherPriority(taskRecord1));
        Assertions.assertTrue(taskRecord3.hasHigherPriority(taskRecord1));
        Assertions.assertTrue(taskRecord4.hasHigherPriority(taskRecord5));
        Assertions.assertTrue(taskRecord3.hasHigherPriority(taskRecord5));
        Assertions.assertTrue(taskRecord6.hasHigherPriority(taskRecord2));
        Assertions.assertTrue(taskRecord4.hasHigherPriority(taskRecord5));
        Assertions.assertTrue(taskRecord8.hasHigherPriority(taskRecord7));

        Assertions.assertFalse(taskRecord7.hasHigherPriority(taskRecord8));
        Assertions.assertFalse(taskRecord2.hasHigherPriority(taskRecord3));
        Assertions.assertFalse(taskRecord4.hasHigherPriority(taskRecord6));



    }
}
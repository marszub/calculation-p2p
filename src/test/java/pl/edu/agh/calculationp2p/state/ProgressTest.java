package pl.edu.agh.calculationp2p.state;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.edu.agh.calculationp2p.calculation.utils.TaskResultImpl;
import pl.edu.agh.calculationp2p.state.task.TaskRecord;
import pl.edu.agh.calculationp2p.state.task.TaskState;

import java.util.ArrayList;

class ProgressTest {

    @Test
    void update() {
        Progress progress = new Progress();
        TaskRecord taskRecord1 = new TaskRecord(1, TaskState.Free, 10, new TaskResultImpl());
        TaskRecord taskRecord2 = new TaskRecord(1, TaskState.Reserved, 10, new TaskResultImpl());

        progress.update(taskRecord1);
        progress.update(taskRecord2);

        Assertions.assertNotEquals(progress.get(taskRecord2.getTaskID()), taskRecord1);
        Assertions.assertEquals(progress.get(taskRecord1.getTaskID()), taskRecord2);
    }

    @Test
    void testClone() {
        Progress progress = new Progress();
        Progress cloned = progress.clone();
        Assertions.assertNotEquals(progress, cloned);
    }

    @Test
    void getFreeTasksList() {
        Progress progress = new Progress();
        TaskRecord taskRecord1 = new TaskRecord(1, TaskState.Free, 10, new TaskResultImpl());
        TaskRecord taskRecord2 = new TaskRecord(2, TaskState.Reserved, 10, new TaskResultImpl());
        TaskRecord taskRecord3 = new TaskRecord(3, TaskState.Free, 10, new TaskResultImpl());
        progress.update(taskRecord1);
        progress.update(taskRecord2);
        progress.update(taskRecord3);
        ArrayList<Integer> list = progress.getFreeTasksList();
        Assertions.assertEquals(2, list.size());
    }


    @Test
    void serialize() {

    }
}
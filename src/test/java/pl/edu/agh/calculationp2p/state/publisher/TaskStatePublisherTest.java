package pl.edu.agh.calculationp2p.state.publisher;

import org.junit.jupiter.api.Test;
import pl.edu.agh.calculationp2p.calculationTask.hashBreaking.HashTaskResult;
import pl.edu.agh.calculationp2p.state.future.Future;
import pl.edu.agh.calculationp2p.state.future.Observation;
import pl.edu.agh.calculationp2p.state.idle.Idle;
import pl.edu.agh.calculationp2p.state.idle.IdleInterrupter;
import pl.edu.agh.calculationp2p.state.task.TaskRecord;
import pl.edu.agh.calculationp2p.state.task.TaskState;

import static org.junit.jupiter.api.Assertions.*;

class TaskStatePublisherTest {

    @Test
    void subscribe() {
        TaskStatePublisher publisher = new  TaskStatePublisher(TaskState.Calculated);
        IdleInterrupter interrupter1 = new Idle();
        Future<Observation> future = new Future<>();

        publisher.subscribe(future, interrupter1);
        assertEquals(1, publisher.observers.size());

        publisher.subscribe(future, interrupter1);
        assertEquals(1, publisher.observers.size());

        IdleInterrupter interrupter2 = new Idle();
        publisher.subscribe(future, interrupter2);
        assertEquals(2, publisher.observers.size());
    }

    @Test
    void unsubscribe() {
        TaskStatePublisher publisher = new  TaskStatePublisher(TaskState.Calculated);
        Future<Observation> future = new Future<>();
        IdleInterrupter interrupter1 = new Idle();
        IdleInterrupter interrupter2 = new Idle();
        IdleInterrupter interrupter3 = new Idle();

        publisher.subscribe(future, interrupter1);
        publisher.subscribe(future, interrupter2);
        publisher.subscribe(future, interrupter3);

        publisher.unsubscribe(interrupter2);
        assertEquals(2, publisher.observers.size());

        publisher.unsubscribe(interrupter3);
        assertEquals(1, publisher.observers.size());
    }


    @Test
    void look() {
        TaskStatePublisher publisher = new  TaskStatePublisher(TaskState.Calculated);
        Future<Observation> future1 = new Future<>();
        Future<Observation> future2 = new Future<>();
        Future<Observation> future3 = new Future<>();


        IdleInterrupter interrupter1 = new Idle();
        IdleInterrupter interrupter2 = new Idle();
        IdleInterrupter interrupter3 = new Idle();

        publisher.subscribe(future1, interrupter1);
        publisher.subscribe(future2, interrupter2);
        publisher.subscribe(future3, interrupter3);

        TaskRecord taskRecord1 = new TaskRecord(1, TaskState.Reserved, 1, new HashTaskResult());
        TaskRecord taskRecord2 = new TaskRecord(1, TaskState.Reserved, 1, new HashTaskResult());
        TaskRecord taskRecord3 = new TaskRecord(1, TaskState.Calculated, 2, new HashTaskResult());

        publisher.look(taskRecord1, taskRecord2);
        assertFalse(future1.isReady());
        assertFalse(future2.isReady());
        assertFalse(future3.isReady());

        publisher.look(taskRecord1, taskRecord3);
        assertTrue(future1.isReady());
        assertTrue(future2.isReady());
        assertTrue(future3.isReady());
    }
}
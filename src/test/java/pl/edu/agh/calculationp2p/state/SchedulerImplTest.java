package pl.edu.agh.calculationp2p.state;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.edu.agh.calculationp2p.calculationTask.hashBreaking.HashTaskResult;
import pl.edu.agh.calculationp2p.state.publisher.CalculatedPublisher;
import pl.edu.agh.calculationp2p.state.publisher.ReservedPublisher;
import pl.edu.agh.calculationp2p.state.publisher.TaskPublisher;
import pl.edu.agh.calculationp2p.state.request.MethodRequest;
import pl.edu.agh.calculationp2p.state.task.TaskRecord;
import pl.edu.agh.calculationp2p.state.task.TaskState;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class SchedulerImplTest {
    Scheduler scheduler;
    
    @BeforeEach
    void init(){
        Integer nodeID = 10;
        Progress progress = new Progress();
        TaskPublisher taskPublisher = new TaskPublisher();
        CalculatedPublisher calculatedPublisher = new CalculatedPublisher();
        ReservedPublisher reservedPublisher = new ReservedPublisher();

        TaskRecord taskRecord1 = new TaskRecord(5, TaskState.Free, 10, new HashTaskResult());
        progress.update(taskRecord1);

        Servant servant = new ServantImpl(progress, taskPublisher, reservedPublisher, calculatedPublisher, nodeID);
        scheduler = new SchedulerImpl(servant);
    }

    @Test
    void enqueueRequestIsProcessed() throws InterruptedException {
        AtomicReference<Integer> response = new AtomicReference<>(0);
        scheduler.enqueue(new MethodRequest() {
            @Override
            public void call(Servant servant) {
                response.set(servant.getFreeTasksList().get(0));
            }
        });

        assertDoesNotThrow(() -> TimeUnit.MILLISECONDS.sleep(20));
        assertEquals(5, response.get());
    }
}
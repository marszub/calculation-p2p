package pl.edu.agh.calculationp2p.state;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.edu.agh.calculationp2p.calculation.TaskResult;
import pl.edu.agh.calculationp2p.state.future.Future;
import pl.edu.agh.calculationp2p.state.future.Observation;
import pl.edu.agh.calculationp2p.state.idle.IdleInterrupter;
import pl.edu.agh.calculationp2p.state.request.MethodRequest;
import pl.edu.agh.calculationp2p.state.task.TaskRecord;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class SchedulerImplTest {
    Scheduler scheduler;
    
    @BeforeEach
    void init(){
        //scheduler = new SchedulerImpl(new ServantImpl());
    }

    @Test
    void enqueueRequestIsProcessed() throws InterruptedException {
        AtomicReference<Integer> response = new AtomicReference<>(0);
        scheduler.enqueue(new MethodRequest() {
            @Override
            public void call(Servant servant) {
                response.set(servant.getTask());
            }
        });

        assertDoesNotThrow(() -> TimeUnit.MILLISECONDS.sleep(20));
        assertEquals(5, response.get());
    }
}
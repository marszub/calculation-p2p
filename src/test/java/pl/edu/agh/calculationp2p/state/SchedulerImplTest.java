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
        scheduler = new SchedulerImpl(new Servant() {
            @Override
            public TaskRecord getTaskProgress(Integer taskId) {
                return null;
            }

            @Override
            public Progress getProgress() {
                return null;
            }

            @Override
            public void observeReserved(Future<Observation> observer, IdleInterrupter interrupter) {
            }

            @Override
            public void observeCalculated(Future<Observation> observer, IdleInterrupter interrupter) {
            }

            @Override
            public void updateProgress(Progress progress) {
            }

            @Override
            public Integer getTask() {
                return 5;
            }

            @Override
            public void observeTask(Integer taskId, Future<Void> flag, Thread thread) {

            }

            @Override
            public void finishTask(Integer taskId, TaskResult result) {

            }

            @Override
            public TaskRecord calculate(Integer taskId, Integer nodeId, TaskResult result) {
                return null;
            }

            @Override
            public TaskRecord reserve(Integer taskId, Integer nodeId) {
                return null;
            }
        });
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
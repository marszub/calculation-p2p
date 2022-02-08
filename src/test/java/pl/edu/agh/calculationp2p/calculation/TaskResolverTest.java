package pl.edu.agh.calculationp2p.calculation;

import org.junit.jupiter.api.Test;
import pl.edu.agh.calculationp2p.calculationTask.TaskResult;
import pl.edu.agh.calculationp2p.calculationTask.CalculationTask;
import pl.edu.agh.calculationp2p.calculationTask.hashBreaking.HashBreaker;
import pl.edu.agh.calculationp2p.calculationTask.hashBreaking.HashBreakerInit;
import pl.edu.agh.calculationp2p.state.future.Future;
import pl.edu.agh.calculationp2p.state.proxy.TaskGiver;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TaskResolverTest {

//    @Test
//    void run() {
//
//        HashBreakerInit initVar = new HashBreakerInit(
//                "E0EC043B3F9E198EC09041687E4D4E8D",
//                100L,
//                10L,
//                10,
//                "0123456789".toCharArray()
//        );
//        CalculationTask task = new HashBreaker(initVar);
//        final TaskResult[] resultGlobal = new TaskResult[1];
//        TaskGiver taskGiver = new TaskGiver() {
//            private int flag = 0;
//            @Override
//            public Future<Optional<Integer>> getTask() {
//                int taskId = task.getNumberOfTaskFragments() - 1;
//                Future<Optional<Integer>> future = new Future<>();
//                if(flag==0){
//                    flag = 1;
//                    future.put(Optional.of(taskId));
//                    return future;
//                } else {
//                    return future;
//                }
//            }
//
//            @Override
//            public Future<Void> observeTask(Integer taskId) {
//                return new Future<>();
//            }
//
//            @Override
//            public void finishTask(Integer taskId, TaskResult result) {
//                resultGlobal[0] = result;
//            }
//        };
//        TaskResolver taskResolver = new TaskResolver(taskGiver, task);
//        taskResolver.run();
//        assertTrue(resultGlobal[0].serialize().contains("9999999999"));
//    }
}
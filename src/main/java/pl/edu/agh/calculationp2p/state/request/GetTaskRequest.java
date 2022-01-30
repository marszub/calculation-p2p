package pl.edu.agh.calculationp2p.state.request;

import pl.edu.agh.calculationp2p.state.Servant;
import pl.edu.agh.calculationp2p.state.future.Future;
import pl.edu.agh.calculationp2p.state.task.TaskRecord;
import pl.edu.agh.calculationp2p.state.task.TaskState;

import java.lang.management.ThreadInfo;
import java.util.*;

public class GetTaskRequest implements MethodRequest {
    Future<Optional<Integer>> future;
    long threadID;

    public GetTaskRequest(Future<Optional<Integer>> future, long threadID) {
        this.threadID = threadID;
        this.future = future;
    }

    @Override
    public void call(Servant servant) {
        List<Integer> freeTasks = servant.getFreeTasksList();
        if (!freeTasks.isEmpty()) {
            Random random = new Random();
            int taskId = freeTasks.get(random.nextInt(freeTasks.size()));
            future.put(Optional.of(taskId));
        } else {
            future.put(Optional.empty());
        }
    }
}
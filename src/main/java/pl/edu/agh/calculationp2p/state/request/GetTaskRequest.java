package pl.edu.agh.calculationp2p.state.request;

import pl.edu.agh.calculationp2p.state.Servant;
import pl.edu.agh.calculationp2p.state.future.Future;
import pl.edu.agh.calculationp2p.state.task.TaskRecord;
import pl.edu.agh.calculationp2p.state.task.TaskState;

import java.lang.management.ThreadInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class GetTaskRequest implements MethodRequest {
    Future<Optional<Integer>> future;
    long threadID;

    public GetTaskRequest(Future<Optional<Integer>> future, long threadID) {
        this.threadID = threadID;
        this.future = future;
    }

    @Override
    public void call(Servant servant) {
        ArrayList<Integer> freeTasks = servant.getFreeTasksList();
        if (!freeTasks.isEmpty()) {
            int random = (int) (Math.random() * freeTasks.size());
            future.put(Optional.of(random));

        } else {
            future.put(Optional.empty());
        }
    }
}
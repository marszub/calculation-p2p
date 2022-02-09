package pl.edu.agh.calculationp2p.state.request;

import pl.edu.agh.calculationp2p.state.Servant;
import pl.edu.agh.calculationp2p.state.future.Future;
import pl.edu.agh.calculationp2p.state.task.TaskRecord;
import pl.edu.agh.calculationp2p.state.task.TaskState;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class GetAndReserveRequest implements MethodRequest{
    Future<Optional<Integer>> future;
    public GetAndReserveRequest(Future<Optional<Integer>> future){
        this.future = future;
    }

    @Override
    public void call(Servant servant) {
        List<Integer> freeTasks = servant.getFreeTasksList();
        if (!freeTasks.isEmpty()) {
            Random random = new Random();
            int taskId = freeTasks.get(random.nextInt(freeTasks.size()));
            TaskRecord old = servant.getProgress().get(taskId);
            TaskRecord reserved = new TaskRecord( old.getTaskID(), TaskState.Reserved, servant.getNodeId(), old.getResult());
            servant.getProgress().update(reserved);
            servant.lookAllPublishers(old, reserved);
            future.put(Optional.of(taskId));
        } else {
            future.put(Optional.empty());
        }
    }

}

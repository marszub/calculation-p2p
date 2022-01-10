package pl.edu.agh.calculationp2p.state.request;

import pl.edu.agh.calculationp2p.state.Servant;
import pl.edu.agh.calculationp2p.state.future.Future;
import pl.edu.agh.calculationp2p.state.task.TaskRecord;
import pl.edu.agh.calculationp2p.state.task.TaskState;

import java.util.HashMap;

public class GetTaskProgressRequest implements MethodRequest {
    Future<TaskRecord> future;
    Integer taskID;

    public GetTaskProgressRequest(Future<TaskRecord> future, Integer taskID) {
        this.future = future;
        this.taskID = taskID;
    }

    @Override
    public void call(Servant servant) {
        future.put(servant.getProgress().get(taskID));
    }

}

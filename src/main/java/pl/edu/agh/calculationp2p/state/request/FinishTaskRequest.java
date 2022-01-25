package pl.edu.agh.calculationp2p.state.request;


import pl.edu.agh.calculationp2p.calculationTask.TaskResult;
import pl.edu.agh.calculationp2p.state.Servant;
import pl.edu.agh.calculationp2p.state.future.Future;
import pl.edu.agh.calculationp2p.state.task.TaskRecord;
import pl.edu.agh.calculationp2p.state.task.TaskState;

public class FinishTaskRequest implements MethodRequest {
    Future<TaskRecord> future;
    int taskID;
    int nodeID;
    TaskResult result;

    public FinishTaskRequest(Future<TaskRecord> future, int taskID, int nodeID, TaskResult result) {
        this.future = future;
        this.taskID = taskID;
        this.nodeID = nodeID;
        this.result = result;
    }

    @Override
    public void call(Servant servant) {
        TaskRecord oldTask = servant.getProgress().get(taskID);
        TaskRecord newTask = new TaskRecord(taskID, TaskState.Calculated, nodeID, result);
        if (oldTask.getState() != TaskState.Calculated) {
            servant.getProgress().update(newTask);
        }
        servant.lookAllPublishers(oldTask, servant.getProgress().get(taskID));
        future.put(servant.getProgress().get(taskID));
    }
}


package pl.edu.agh.calculationp2p.state.request;

import pl.edu.agh.calculationp2p.calculation.utils.TaskResult;
import pl.edu.agh.calculationp2p.state.Progress;
import pl.edu.agh.calculationp2p.state.Servant;
import pl.edu.agh.calculationp2p.state.task.TaskRecord;
import pl.edu.agh.calculationp2p.state.task.TaskState;

public class CalculateRequest implements MethodRequest{
    Integer taskId;
    TaskResult result;


    public CalculateRequest(Integer taskId, TaskResult result) {
        this.result =result;
        this.taskId = taskId;
    }

    @Override
    public void call(Servant servant){
        Progress progress = servant.getProgress();
        TaskRecord oldTask = progress.get(taskId);
        TaskRecord newTask = new TaskRecord(taskId, TaskState.Calculated, servant.getNodeId(), result);
        if(newTask.hasHigherPriority(oldTask)) {
            progress.update(newTask);
        }
        servant.lookAllPublishers(oldTask, progress.get(taskId));
    }
}

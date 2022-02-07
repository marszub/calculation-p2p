package pl.edu.agh.calculationp2p.state.request;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import pl.edu.agh.calculationp2p.calculationTask.TaskResult;
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
        Logger logger = LoggerFactory.getLogger(CalculateRequest.class);
        logger.info("Call");
        Progress progress = servant.getProgress();
        TaskRecord oldTask = progress.get(taskId);
        TaskRecord newTask = new TaskRecord(taskId, TaskState.Calculated, servant.getNodeId(), result);
        if(newTask.hasHigherPriority(oldTask)) {
            progress.update(newTask);
        }
        servant.getTaskPublisher().unsubscribe(taskId);
        servant.lookAllPublishers(oldTask, progress.get(taskId));
    }
}

package pl.edu.agh.calculationp2p.state.request;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import pl.edu.agh.calculationp2p.state.Servant;
import pl.edu.agh.calculationp2p.state.future.Future;
import pl.edu.agh.calculationp2p.state.future.Observation;
import pl.edu.agh.calculationp2p.state.task.TaskRecord;
import pl.edu.agh.calculationp2p.state.task.TaskState;

public class ReserveRequest implements MethodRequest{
    Future<TaskRecord> future;
    Integer taskID;
    Integer nodeID;

    public ReserveRequest(Future<TaskRecord> future, Integer taskID, Integer nodeID) {
        this.future = future;
        this.taskID = taskID;
        this.nodeID = nodeID;
    }

    @Override
    public void call(Servant servant) {
        Logger logger = LoggerFactory.getLogger(ReserveRequest.class);
        logger.info("Call");
        TaskRecord oldTask = servant.getProgress().get(taskID);
        TaskRecord newTask = new TaskRecord(taskID, TaskState.Reserved, nodeID, oldTask.getResult());
        if (newTask.hasHigherPriority(oldTask)) {
            servant.getProgress().update(newTask);
        }
        servant.lookAllPublishers(oldTask, servant.getProgress().get(taskID));
    }
}


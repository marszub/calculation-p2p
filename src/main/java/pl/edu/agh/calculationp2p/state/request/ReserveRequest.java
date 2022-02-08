package pl.edu.agh.calculationp2p.state.request;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.core.net.impl.pool.Task;
import pl.edu.agh.calculationp2p.state.Servant;
import pl.edu.agh.calculationp2p.state.future.Future;
import pl.edu.agh.calculationp2p.state.future.Observation;
import pl.edu.agh.calculationp2p.state.task.TaskRecord;
import pl.edu.agh.calculationp2p.state.task.TaskState;

import java.util.Optional;

public class ReserveRequest implements MethodRequest {
    Integer taskID;


    public ReserveRequest(Integer taskID) {
        this.taskID = taskID;
    }

    @Override
    public void call(Servant servant) {
        Logger logger = LoggerFactory.getLogger(ReserveRequest.class);
        logger.info("Call");
        TaskRecord oldTask = servant.getProgress().get(taskID);
        TaskRecord reserved = new TaskRecord(taskID, TaskState.Reserved, servant.getNodeId(), oldTask.getResult());
        if (reserved.hasHigherPriority(oldTask)) {
            logger.info("Call " + oldTask.getTaskID() + " | OLD: "+ oldTask.getOwner() + " | NEW: " + reserved.getOwner() + " | OLD STATE: " + oldTask.getState() + " | NEW STATE: " + reserved.getState());
            servant.getProgress().update(reserved);
            servant.lookAllPublishers(oldTask, servant.getProgress().get(taskID));

        }

    }


}




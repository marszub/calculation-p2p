package pl.edu.agh.calculationp2p.state.request;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import pl.edu.agh.calculationp2p.message.MessageImpl;
import pl.edu.agh.calculationp2p.state.Servant;
import pl.edu.agh.calculationp2p.state.future.Future;
import pl.edu.agh.calculationp2p.state.task.TaskRecord;

public class UpdateTask implements MethodRequest {
    Future<TaskRecord> future;
    TaskRecord newTaskRecord;

    public UpdateTask(Future<TaskRecord> future, TaskRecord taskRecord) {
        this.future = future;
        this.newTaskRecord = taskRecord;
    }

    public void call(Servant servant) {
        Logger logger = LoggerFactory.getLogger(UpdateTask.class);
        Integer oldTaskID = newTaskRecord.getTaskID();
        TaskRecord oldTask = servant.getProgress().get(oldTaskID);
        logger.info("Call | OLD: "+ oldTaskID + " | NEW: " + newTaskRecord.getTaskID());
        if (newTaskRecord.hasHigherPriority(oldTask)) {
            servant.getProgress().update(newTaskRecord);
            servant.lookAllPublishers(oldTask, servant.getProgress().get(oldTaskID));
        }
    }
}

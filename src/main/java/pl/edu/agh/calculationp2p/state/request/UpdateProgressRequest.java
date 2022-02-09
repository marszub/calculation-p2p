package pl.edu.agh.calculationp2p.state.request;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import pl.edu.agh.calculationp2p.state.Progress;
import pl.edu.agh.calculationp2p.state.Servant;
import pl.edu.agh.calculationp2p.state.future.Future;
import pl.edu.agh.calculationp2p.state.task.TaskRecord;

public class UpdateProgressRequest implements MethodRequest{
    Future<Progress> future;
    Progress newProgress;

    public UpdateProgressRequest(Future<Progress> future, Progress newProgress) {
        this.future = future;
        this.newProgress = newProgress;
    }

    @Override
    public void call(Servant servant) {
        // check every priority and get the highest ones
        Progress currentProgress = servant.getProgress();
        for(TaskRecord newRecord: newProgress.getTasks()){
            TaskRecord oldRecord = currentProgress.get(newRecord.getTaskID());
            if (newRecord.hasHigherPriority(oldRecord)){
                currentProgress.update(newRecord);
                servant.lookAllPublishers(oldRecord, servant.getProgress().get(newRecord.getTaskID()));
            }
        }
    }
}

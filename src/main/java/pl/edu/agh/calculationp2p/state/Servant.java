package pl.edu.agh.calculationp2p.state;

import io.vertx.core.net.impl.pool.Task;
import pl.edu.agh.calculationp2p.state.publisher.TaskPublisher;
import pl.edu.agh.calculationp2p.state.publisher.TaskStatePublisher;
import pl.edu.agh.calculationp2p.state.task.TaskRecord;

import java.util.List;

public interface Servant {
    TaskRecord getTaskProgress(Integer taskId);

    Progress getProgress();

    TaskPublisher getTaskPublisher();

    TaskStatePublisher getReservedPublisher();

    TaskStatePublisher getCalculatedPublisher();

    void lookAllPublishers(TaskRecord prev, TaskRecord curr);

    Integer getNodeId();

    void setNodeId(Integer nodeId);

    List<Integer> getFreeTasksList();

    void setProgress(List<TaskRecord> list);
}

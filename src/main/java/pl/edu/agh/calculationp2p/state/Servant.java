package pl.edu.agh.calculationp2p.state;

import pl.edu.agh.calculationp2p.calculation.TaskResult;
import pl.edu.agh.calculationp2p.state.future.Future;
import pl.edu.agh.calculationp2p.state.future.Observation;
import pl.edu.agh.calculationp2p.state.idle.IdleInterrupter;
import pl.edu.agh.calculationp2p.state.publisher.CalculatedPublisher;
import pl.edu.agh.calculationp2p.state.publisher.ReservedPublisher;
import pl.edu.agh.calculationp2p.state.publisher.TaskPublisher;
import pl.edu.agh.calculationp2p.state.task.TaskRecord;

public interface Servant {
    TaskRecord getTaskProgress(Integer taskId);

    Progress getProgress();

    TaskPublisher getTaskPublisher();

    ReservedPublisher getReservedPublisher();

    CalculatedPublisher getCalculatedPublisher();

    void lookAllPublishers(TaskRecord prev, TaskRecord curr);

    Integer getNodeId();

    Integer getTask();
}

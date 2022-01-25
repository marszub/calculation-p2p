package pl.edu.agh.calculationp2p.state;

import pl.edu.agh.calculationp2p.state.publisher.CalculatedPublisher;
import pl.edu.agh.calculationp2p.state.publisher.ReservedPublisher;
import pl.edu.agh.calculationp2p.state.publisher.TaskPublisher;
import pl.edu.agh.calculationp2p.state.task.TaskRecord;

import java.util.ArrayList;

public interface Servant {
    TaskRecord getTaskProgress(Integer taskId);

    Progress getProgress();

    TaskPublisher getTaskPublisher();

    ReservedPublisher getReservedPublisher();

    CalculatedPublisher getCalculatedPublisher();

    void lookAllPublishers(TaskRecord prev, TaskRecord curr);

    Integer getNodeId();

    ArrayList<Integer> getFreeTasksList();
}

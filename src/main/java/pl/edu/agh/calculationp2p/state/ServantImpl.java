package pl.edu.agh.calculationp2p.state;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import pl.edu.agh.calculationp2p.calculation.TaskResolver;
import pl.edu.agh.calculationp2p.message.MessageImpl;
import pl.edu.agh.calculationp2p.state.publisher.CalculatedPublisher;
import pl.edu.agh.calculationp2p.state.publisher.ReservedPublisher;
import pl.edu.agh.calculationp2p.state.publisher.TaskPublisher;
import pl.edu.agh.calculationp2p.state.task.TaskRecord;
import pl.edu.agh.calculationp2p.state.task.TaskState;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ServantImpl implements Servant {
    private final Progress progress;
    private final TaskPublisher taskPublisher;
    private final ReservedPublisher reservedPublisher;
    private final CalculatedPublisher calculatedPublisher;
    private Integer nodeId;

    public ServantImpl(Progress progress, TaskPublisher taskPublisher, ReservedPublisher reservedPublisher, CalculatedPublisher calculatedPublisher, Integer nodeId) {
        this.progress = progress;
        this.taskPublisher = taskPublisher;
        this.reservedPublisher = reservedPublisher;
        this.calculatedPublisher = calculatedPublisher;
        this.nodeId = nodeId; // TODO: set default nodeId // delete nodeID - nie ustawiasz bo go nie masz
    }

    @Override
    public void setNodeId(Integer nodeId){
        int size = progress.size();
        for(int i = 0; i < size; i++) {
            TaskRecord record = progress.get(i);
            if (record.getOwner() == this.nodeId) {
                progress.update(new TaskRecord(record.getTaskID(), record.getState(), nodeId, record.getResult()));
            }
        }
        this.nodeId = nodeId;
    }

    @Override
    public Integer getNodeId() {
        return nodeId;
    }

    @Override
    public List<Integer> getFreeTasksList() {
        return progress.getTasks()
                .stream()
                .filter(e -> e.getState() == TaskState.Free)
                .map(TaskRecord::getTaskID)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public TaskPublisher getTaskPublisher() {
        return taskPublisher;
    }

    @Override
    public ReservedPublisher getReservedPublisher() {
        return reservedPublisher;
    }

    @Override
    public CalculatedPublisher getCalculatedPublisher() {
        return calculatedPublisher;
    }

    @Override
    public TaskRecord getTaskProgress(Integer taskId) {
        return progress.get(taskId);
    }

    @Override
    public Progress getProgress() {
        return progress;
    }

    @Override
    public void lookAllPublishers(TaskRecord prev, TaskRecord curr) { //TODO sprawdz kto wykonal zmiane
        Logger logger = LoggerFactory.getLogger(ServantImpl.class);
        logger.info("Before IF statement");

        if (prev.getState() == TaskState.Reserved && curr.getState() == TaskState.Free) {
            logger.info("In IF TaskPublisher");
            taskPublisher.look(prev, curr);

       }
        if(prev.getState() == TaskState.Reserved && curr.getState() == TaskState.Calculated){
            logger.info("In IF CalculatedPublisher");
            calculatedPublisher.look(prev, curr);
        }
        if(prev.getState() == TaskState.Free && curr.getState() == TaskState.Reserved){
            logger.info("In IF ReservedPublisher");
            reservedPublisher.look(prev, curr);
        }


    }


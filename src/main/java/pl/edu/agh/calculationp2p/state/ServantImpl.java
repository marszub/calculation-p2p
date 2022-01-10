package pl.edu.agh.calculationp2p.state;


import pl.edu.agh.calculationp2p.calculation.TaskResult;
import pl.edu.agh.calculationp2p.state.future.Future;
import pl.edu.agh.calculationp2p.state.future.Observation;
import pl.edu.agh.calculationp2p.state.idle.IdleInterrupter;
import pl.edu.agh.calculationp2p.state.publisher.CalculatedPublisher;
import pl.edu.agh.calculationp2p.state.publisher.ReservedPublisher;
import pl.edu.agh.calculationp2p.state.publisher.TaskPublisher;
import pl.edu.agh.calculationp2p.state.task.TaskRecord;
import pl.edu.agh.calculationp2p.state.task.TaskState;

public class ServantImpl implements Servant {
    private final Progress progress;
    private final TaskPublisher taskPublisher;
    private final ReservedPublisher reservedPublisher;
    private final CalculatedPublisher calculatedPublisher;
    private final Integer nodeId;

    @Override
    public Integer getNodeId() {
        return nodeId;
    }

    @Override
    public Integer getTask() {
       return null;
    }

    ServantImpl(Progress progress, TaskPublisher taskPublisher, ReservedPublisher reservedPublisher, CalculatedPublisher calculatedPublisher, Integer nodeId) {
        this.progress = progress;
        this.taskPublisher = taskPublisher;
        this.reservedPublisher = reservedPublisher;
        this.calculatedPublisher = calculatedPublisher;
        this.nodeId = nodeId;
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
    public void lookAllPublishers(TaskRecord prev, TaskRecord curr){
        taskPublisher.look(prev, curr);
        calculatedPublisher.look(prev, curr);
        reservedPublisher.look(prev, curr);
         // TODO jesli stan reserved -> free nowy request GiveTaskRequest i wywołaj sam siebie lookAll()
        //
    }
}

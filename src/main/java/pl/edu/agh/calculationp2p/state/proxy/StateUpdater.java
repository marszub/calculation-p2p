package pl.edu.agh.calculationp2p.state.proxy;

import pl.edu.agh.calculationp2p.calculation.TaskResult;
import pl.edu.agh.calculationp2p.state.future.Future;
import pl.edu.agh.calculationp2p.state.task.TaskRecord;
import pl.edu.agh.calculationp2p.state.Progress;

public interface StateUpdater {
    Future<TaskRecord> updateTask(TaskRecord taskRecord);
    void initProgress(Progress progress);
    void setNodeId(Integer nodeId);
}

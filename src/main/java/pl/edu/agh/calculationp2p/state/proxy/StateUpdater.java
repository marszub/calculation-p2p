package pl.edu.agh.calculationp2p.state.proxy;

import pl.edu.agh.calculationp2p.calculation.TaskResult;
import pl.edu.agh.calculationp2p.state.future.Future;
import pl.edu.agh.calculationp2p.state.task.TaskRecord;

public interface StateUpdater {
    Future<TaskRecord> reserve(int task, int nodeId);
    Future<TaskRecord> calculate(int task, int nodeId, TaskResult result);

}

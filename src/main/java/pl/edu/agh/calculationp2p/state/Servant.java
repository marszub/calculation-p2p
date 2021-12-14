package pl.edu.agh.calculationp2p.state;

import pl.edu.agh.calculationp2p.calculation.TaskResult;
import pl.edu.agh.calculationp2p.state.future.Future;
import pl.edu.agh.calculationp2p.state.future.Observation;
import pl.edu.agh.calculationp2p.state.idle.IdleInterrupter;
import pl.edu.agh.calculationp2p.state.task.TaskRecord;

public interface Servant {
    TaskRecord getTaskProgress(Integer taskId);

    Progress getProgress();

    void observeReserved(Future<Observation> observer, IdleInterrupter interrupter);

    void observeCalculated(Future<Observation> observer, IdleInterrupter interrupter);

    void updateProgress(Progress progress);

    Integer getTask();

    void observeTask(Integer taskId, Future<Void> flag, Thread thread);

    void finishTask(Integer taskId, TaskResult result);

    TaskRecord calculate(Integer taskId, Integer nodeId, TaskResult result);

    TaskRecord reserve(Integer taskId, Integer nodeId);
}

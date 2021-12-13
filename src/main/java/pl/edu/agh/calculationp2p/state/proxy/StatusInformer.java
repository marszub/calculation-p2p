package pl.edu.agh.calculationp2p.state.proxy;

import pl.edu.agh.calculationp2p.state.Progress;
import pl.edu.agh.calculationp2p.state.future.Future;
import pl.edu.agh.calculationp2p.state.future.Observation;
import pl.edu.agh.calculationp2p.state.idle.IdleInterrupter;
import pl.edu.agh.calculationp2p.state.task.TaskRecord;

public interface StatusInformer {
    Future<Progress> getProgress();
    Future<TaskRecord> getTaskProgress(Integer task);
    Future<Observation> observeCalculated(IdleInterrupter interrupter);
    Future<Observation> observeReserved(IdleInterrupter interrupter);
    void cancelObservation(IdleInterrupter interrupter);
}

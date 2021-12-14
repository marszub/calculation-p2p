package pl.edu.agh.calculationp2p.state.publisher;

import pl.edu.agh.calculationp2p.state.future.Future;
import pl.edu.agh.calculationp2p.state.future.Observation;
import pl.edu.agh.calculationp2p.state.idle.IdleInterrupter;
import pl.edu.agh.calculationp2p.state.task.TaskRecord;

public class ReservedPublisher {
    public ReservedPublisher(){
        throw new UnsupportedOperationException("Will be implemented");
    }

    public void subscribe(Future<Observation> observer, IdleInterrupter interrupter){
        throw new UnsupportedOperationException("Will be implemented");
    }

    public void unsubscribe(IdleInterrupter interrupter){
        throw new UnsupportedOperationException("Will be implemented");
    }

    public void look(TaskRecord previous, TaskRecord current){
        throw new UnsupportedOperationException("Will be implemented");
    }
}

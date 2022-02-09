package pl.edu.agh.calculationp2p.state.publisher;

import pl.edu.agh.calculationp2p.state.future.Future;
import pl.edu.agh.calculationp2p.state.future.Observation;
import pl.edu.agh.calculationp2p.state.idle.IdleInterrupter;
import pl.edu.agh.calculationp2p.state.task.TaskRecord;
import pl.edu.agh.calculationp2p.state.task.TaskState;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskStatePublisher {
    List<Pair<Future<Observation>, IdleInterrupter>> observers;
    TaskState publisherState;

    public TaskStatePublisher(TaskState publisherState) {
        this.observers = new ArrayList<>();
        this.publisherState = publisherState;
    }

    public void subscribe(Future<Observation> observer, IdleInterrupter interrupter) {
        long filtered = observers.stream()
                .filter(p -> p.getR() == interrupter).count();
        if (filtered == 0) {
            Pair<Future<Observation>, IdleInterrupter> pair = new Pair<>(observer, interrupter);
            observers.add(pair);
        }
    }

    public void unsubscribe(IdleInterrupter interrupter) {
        List<Pair<Future<Observation>, IdleInterrupter>> filtered =
                observers.stream().filter(b -> b.getR() == interrupter).collect(Collectors.toList());
        observers.removeAll(filtered);
    }

    public void look(TaskRecord previous, TaskRecord current) {
        if ((previous.getState() != publisherState && current.getState() ==  publisherState )|| (previous.getOwner() != current.getOwner())) {
            for (Pair<Future<Observation>, IdleInterrupter> pair : observers) {
                Future<Observation> oldF = pair.getL();
                Future<Observation> newF = new Future<>();
                Observation observation = new Observation(current, newF);
                oldF.put(observation);
                pair.setL(newF);
                pair.getR().wake();
            }
        }
    }
    public int numberOfObservers(){
        return this.observers.size();
    }
}

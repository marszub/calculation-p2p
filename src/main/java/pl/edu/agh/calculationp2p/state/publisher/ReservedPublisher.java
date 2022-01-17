package pl.edu.agh.calculationp2p.state.publisher;

import pl.edu.agh.calculationp2p.state.future.Future;
import pl.edu.agh.calculationp2p.state.future.Observation;
import pl.edu.agh.calculationp2p.state.idle.IdleInterrupter;
import pl.edu.agh.calculationp2p.state.task.TaskRecord;
import pl.edu.agh.calculationp2p.state.task.TaskState;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReservedPublisher {

    List<Pair<Future<Observation>, IdleInterrupter>> observers;

    public ReservedPublisher() {
        this.observers = new ArrayList<>();
    }

    public void subscribe(Future<Observation> observer, IdleInterrupter interrupter) {
        // check if subscription already exist
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
        // check if are some changes in observed items
        // have to test stream version
        // check if are some changes in observed items
        // anything -> reserved

        if (current.getState() == TaskState.Reserved && !previous.equals(current)
                && previous.getTaskID() == current.getTaskID()) {
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
}

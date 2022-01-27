package pl.edu.agh.calculationp2p.state.publisher;

import pl.edu.agh.calculationp2p.state.future.Future;
import pl.edu.agh.calculationp2p.state.future.Observation;
import pl.edu.agh.calculationp2p.state.idle.IdleInterrupter;
import pl.edu.agh.calculationp2p.state.task.TaskRecord;
import pl.edu.agh.calculationp2p.state.task.TaskState;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// identyczny jak reserved, ten obserwuje zdarzenie policzone

public class CalculatedPublisher {
    // look podglada to co sie dzieje - sprawdzamy czy mozna

    // list of pairs to get everything in one list
    ArrayList<Pair<Future<Observation>, IdleInterrupter>> observers;


    public CalculatedPublisher() {
        observers = new ArrayList<>();
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
        // anything -> calculated
        if (current.getState() == TaskState.Calculated && !previous.equals(current)
                && previous.getTaskID() == current.getTaskID()) {
            for (Pair<Future<Observation>, IdleInterrupter> pair : observers) {
                Future<Observation> oldFuture = pair.getL();
                Future<Observation> newFuture = new Future<>();
                Observation observation = new Observation(current, newFuture);
                oldFuture.put(observation);
                pair.setL(newFuture);
                pair.getR().wake();
            }
        }
    }
    public int numberOfObservers(){
        return this.observers.size();
    }
}

package pl.edu.agh.calculationp2p.state.publisher;

import pl.edu.agh.calculationp2p.state.Progress;
import pl.edu.agh.calculationp2p.state.future.Future;
import pl.edu.agh.calculationp2p.state.future.Observation;
import pl.edu.agh.calculationp2p.state.idle.IdleInterrupter;
import pl.edu.agh.calculationp2p.state.task.TaskRecord;
import pl.edu.agh.calculationp2p.state.publisher.Pair;
import pl.edu.agh.calculationp2p.state.task.TaskState;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class TaskPublisher {
    // lista obserwatorów -> różnica w look interrupt wszystkich zainteresowanych
    List<Pair<Integer, Future<Void>>> observers;

    public TaskPublisher() {
        observers = new LinkedList<>();
    }

    public void subscribe(Integer taskId, Future<Void> flag, Integer nodeID, Progress progress) {
        // subskrybujemy jako wątek obliczeń, obserwuje to co liczę, reagujemy na każdą zmianę
        // future -> pusty, tylko do sygnalizowania
        // List<TaskRecord> tasks = progress.getTasks(); // unused

        // jesli mamy innego właścicela niż my -> od razu powiadom i nie subskrybuj
        if (progress.get(taskId).getOwner() != nodeID) {
            flag.put();
        } else {
            observers.add(new Pair(taskId, flag));
        }
    }

    public void unsubscribe(Integer taskId) {
        List<Pair<Integer, Future<Void>>> toRemove = new LinkedList<>();
        for (Pair<Integer, Future<Void>> pair : observers) {
            if (Objects.equals(pair.getL(), taskId)) {
                toRemove.add(pair);
            }
        }
        observers.removeAll(toRemove);
    }

    public void raiseFlag(Integer taskId) {
        for (Pair<Integer, Future<Void>> pair : observers) {
            if (Objects.equals(pair.getL(), taskId)) {
                pair.getR().put();
            }
        }
    }

    public void look(TaskRecord previous, TaskRecord current) {
        // istotna jest każda obserwacja
        // sprawdzamy stany 2 tasków, reaguj gdy:
        // free -> coś -> nie reaguj
        // zmiana właściciela(if reserved)
        // reserved -> reserved == check priority
        if (!previous.equals(current)) {
            // reserved -> calculated
            if ((previous.getState() == TaskState.Reserved && current.getState() == TaskState.Calculated)) {
                raiseFlag(current.getTaskID());
            } else if (previous.getState() == TaskState.Reserved && current.getState() == TaskState.Reserved) {
                if (previous.getOwner() != current.getOwner() && current.hasHigherPriority(previous)){
                    // zmienił się właściciel i Reserved
                    raiseFlag(current.getTaskID());
                }
            }
        }
    }
}

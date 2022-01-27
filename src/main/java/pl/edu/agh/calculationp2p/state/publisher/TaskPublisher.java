package pl.edu.agh.calculationp2p.state.publisher;

import pl.edu.agh.calculationp2p.state.Progress;
import pl.edu.agh.calculationp2p.state.future.Future;
import pl.edu.agh.calculationp2p.state.future.Observation;
import pl.edu.agh.calculationp2p.state.idle.IdleInterrupter;
import pl.edu.agh.calculationp2p.state.task.TaskRecord;
import pl.edu.agh.calculationp2p.state.publisher.Pair;
import pl.edu.agh.calculationp2p.state.task.TaskState;

import java.util.*;

public class TaskPublisher {

    Map<Integer, Future<Void>> observers;

    public TaskPublisher() {
        observers = new HashMap<>();
    }

    public void subscribe(Integer taskId, Future<Void> flag, Integer nodeID, Progress progress) {
        if (progress.get(taskId).getOwner() != nodeID) {
            flag.put();
        } else {
            if(observers.containsKey(taskId))
                throw new IllegalArgumentException("Task with ID: " + taskId.toString() + " is already subscribed.");
            observers.put(taskId, flag);
        }
    }

    public void unsubscribe(Integer taskId) {
        observers.remove(taskId);
    }

    private void raiseFlag(Integer taskId) {
        if(observers.containsKey(taskId)){
            observers.remove(taskId).put();
        }
    }

    public void look(TaskRecord previous, TaskRecord current) {
        if(previous.getOwner() != current.getOwner())
            raiseFlag(current.getTaskID());
    }
    public int numberOfObservers(){
        return this.observers.size();
    }
}

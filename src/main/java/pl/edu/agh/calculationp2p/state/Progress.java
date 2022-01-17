package pl.edu.agh.calculationp2p.state;

import pl.edu.agh.calculationp2p.state.task.TaskRecord;
import pl.edu.agh.calculationp2p.state.task.TaskState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Collectors;


public class Progress implements Cloneable {
    private HashMap<Integer, TaskRecord> tasks;

    public Progress() {
        tasks = new HashMap<>();
    }

    public void update(TaskRecord taskRecord) {
        int toUpdate = taskRecord.getTaskID();
        tasks.remove(toUpdate);
        tasks.put(toUpdate, taskRecord);
    }

    public Progress clone() {
        try {
            return (Progress) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Integer> getFreeTasksList() {
        return tasks.entrySet()
                .stream()
                .filter(e -> e.getValue().getState() == TaskState.Free)
                .map(Map.Entry::getKey)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public HashMap<Integer, TaskRecord> getTasks() {
        return tasks;
    }

    public TaskRecord get(int taskId) {
        return tasks.get(taskId);
    }

    public String serialize() {
        throw new UnsupportedOperationException("Will be implemented");
    }
}

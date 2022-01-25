package pl.edu.agh.calculationp2p.state;

import pl.edu.agh.calculationp2p.calculation.TaskResult;
import pl.edu.agh.calculationp2p.state.task.TaskRecord;
import pl.edu.agh.calculationp2p.state.task.TaskState;

import java.util.*;
import java.util.stream.Collectors;


public class Progress implements Cloneable {
    private List<TaskRecord> tasks;

    public Progress(Integer taskNum) {
        tasks = new ArrayList<>(taskNum);
        for(int i = 0; i < taskNum; i++){
            tasks.add(new TaskRecord(i, TaskState.Free, -1, null)); // TODO: default result value
        }
    }

    public void update(TaskRecord taskRecord) {
        int toUpdate = taskRecord.getTaskID();
        tasks.set(toUpdate, taskRecord);
    }

    public Progress clone() {
        try {
            return (Progress) super.clone(); // TODO: repair - need deep copy (but not too deep)
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Integer> getFreeTasksList() {
        return tasks
                .stream()
                .filter(e -> e.getState() == TaskState.Free)
                .map(TaskRecord::getTaskID)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public List<TaskRecord> getTasks() {
        return tasks;
    }

    public TaskRecord get(int taskId) {
        return tasks.get(taskId);
    }

    public String serialize() {
        throw new UnsupportedOperationException("Will be implemented");
    }
}

package pl.edu.agh.calculationp2p.state;

import pl.edu.agh.calculationp2p.state.task.TaskRecord;
import pl.edu.agh.calculationp2p.state.task.TaskState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


public class Progress {
    private HashMap<Integer, TaskRecord> tasks;
    public Progress(int taskNum){
        tasks = new HashMap<>();
    }

    public void update(TaskRecord taskRecord){
        int toUpdate = taskRecord.getTaskID();
        tasks.remove(toUpdate);
        tasks.put(toUpdate, taskRecord);
    }

    public Progress clone(){
        // ??
        return this;
    }

    public ArrayList<Integer> getFreeTasksList(){
        ArrayList<Integer> freeTasks = new ArrayList<>();
        for (TaskRecord task: tasks.values()) {
            if(task.getState() == TaskState.Free){
                freeTasks.add(task.getTaskID());
            }
        }
        return freeTasks;
    }

    public HashMap<Integer, TaskRecord> getTasks() {
        return tasks;
    }

    public TaskRecord get(int taskId){
        return tasks.get(taskId);
    }

    public String serialize(){
        throw new UnsupportedOperationException("Will be implemented");
    }
}

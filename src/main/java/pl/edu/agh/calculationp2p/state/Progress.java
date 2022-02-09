package pl.edu.agh.calculationp2p.state;

import pl.edu.agh.calculationp2p.state.task.TaskRecord;
import pl.edu.agh.calculationp2p.state.task.TaskState;

import java.util.*;
import java.util.stream.Collectors;


public class Progress implements Cloneable {
    private List<TaskRecord> tasks;

    public Progress(Integer taskNum) {
        tasks = new ArrayList<>(taskNum);
        for(int i = 0; i < taskNum; i++){
            tasks.add(new TaskRecord(i, TaskState.Free, -1, null)); // TODO: default result value (brzydki null)
        }
    }

    public Progress(List<TaskRecord> list){
        this.tasks = list;
    }

    public void update(TaskRecord taskRecord) {
        int toUpdate = taskRecord.getTaskID();
        tasks.set(toUpdate, taskRecord);
    }

    public Progress clone() {
        try {
            return (Progress) super.clone(); // TODO: repair - need deep copy (but not too deep)   co? robi nam to róznice?
            // pytanie czy skopiuje liste -> do sprawdzenia (lista osobne referencje)
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

    public List<TaskRecord> getReservedTasksList() {
        return tasks
                .stream()
                .filter(e -> e.getState() == TaskState.Reserved)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public List<TaskRecord> getTasks() {
        return tasks;
    }

    public TaskRecord get(int taskId) {
        return tasks.get(taskId);
    }

    public int size(){
        return tasks.size();
    }

    public int countCompleted(){
        return (int) tasks.stream()
                .filter(e -> e.getState() == TaskState.Calculated)
                .count();
    }

    public String serialize() {
        String res = "";
        res = res.concat("[");
        for(int i=0;i<tasks.size();i++){
            if(this.tasks.get(i).getState()==TaskState.Free)
                continue;
            res = res.concat(this.tasks.get(i).serialize());
            if(i< tasks.size()-1)
                res = res.concat(",");
        }
        res = res.concat("]");
        return res;
    }
}

package pl.edu.agh.calculationp2p.message.body;

import pl.edu.agh.calculationp2p.message.process.MessageProcessContext;
import pl.edu.agh.calculationp2p.state.task.TaskRecord;
import pl.edu.agh.calculationp2p.state.task.TaskState;

import java.util.Objects;

public class Confirm implements Body{

    private final int taskId;
    private final TaskState state;
    private final Integer owner;
    private final TaskRecord taskRecord;

    public Confirm(int taskId, TaskState state, Integer owner, TaskRecord taskRecord) {
        this.taskId = taskId;
        this.state = state;
        this.owner = owner;
        this.taskRecord = taskRecord;
    }

    @Override
    public String serializeType() {
        return "\"confirm\"";
    }

    @Override
    public String serializeContent() {
        String result = "";
        result = result.concat("{\"task_id\":");
        result = result.concat(String.valueOf(this.taskId));
        result = result.concat(",\"state\":");
        if(this.state == TaskState.Calculated){
            result = result.concat("\"calculated\"");
        } else if(this.state == TaskState.Free){
            result = result.concat("\"free\"");
        } else if(this.state == TaskState.Reserved){
            result = result.concat("\"reserved\"");
        }
        result = result.concat(",\"owner\":");
        result = result.concat(String.valueOf(this.owner));
        result = result.concat(",\"result\":");
        // TODO: taskRecord toString or serialize
        // result = result.concat(this.taskRecord.toString());
        result = result.concat("}");
        return result;
    }

    @Override
    public void process(int sender, MessageProcessContext context) {
        //TODO
    }

    public int getTaskId() {
        return taskId;
    }

    public TaskState getState() {
        return state;
    }

    public Integer getOwner() {
        return owner;
    }

    public TaskRecord getTaskRecord() {
        return taskRecord;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (getClass() != o.getClass()) {
            return false;
        }
        Confirm message = (Confirm) o;
        //TODO: equals() in taskRecord
        return message.getTaskId() == this.taskId && message.getTaskRecord() == this.taskRecord && Objects.equals(message.getOwner(), this.owner) && message.getState() == this.state;
    }
}

package pl.edu.agh.calculationp2p.message.body;

import pl.edu.agh.calculationp2p.message.process.MessageProcessContext;
import pl.edu.agh.calculationp2p.state.task.TaskRecord;
import pl.edu.agh.calculationp2p.state.task.TaskState;

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
}

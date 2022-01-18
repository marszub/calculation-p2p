package pl.edu.agh.calculationp2p.message.body;

import pl.edu.agh.calculationp2p.message.process.MessageProcessContext;
import pl.edu.agh.calculationp2p.state.task.TaskRecord;
import pl.edu.agh.calculationp2p.state.task.TaskState;

public class Confirm implements Body{

    private final TaskRecord taskRecord;

    public Confirm(TaskRecord taskRecord) {
        this.taskRecord = taskRecord;
    }

    @Override
    public String serializeType() {
        return "\"confirm\"";
    }

    @Override
    public String serializeContent() {
        return taskRecord.serialize();
    }

    @Override
    public void process(int sender, MessageProcessContext context) {
        //TODO: (calculated, reserve) - > update()
        if(taskRecord.getState() == TaskState.Calculated){
            context.getStateUpdater().calculate(taskRecord.getTaskID(), sender, taskRecord.getResult());
        } else {
            context.getStateUpdater().reserve(taskRecord.getTaskID(), sender);
        }
    }

    @Override
    public Body clone() {
        return new Confirm(taskRecord);
    }

    public int getTaskId() {
        return taskRecord.getTaskID();
    }

    public TaskState getState() {
        return taskRecord.getState();
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
        return message.getTaskRecord().equals(this.taskRecord);
    }
}

package pl.edu.agh.calculationp2p.message.body;

import pl.edu.agh.calculationp2p.calculationTask.TaskResult;
import pl.edu.agh.calculationp2p.message.Message;
import pl.edu.agh.calculationp2p.message.MessageImpl;
import pl.edu.agh.calculationp2p.message.process.MessageProcessContext;
import pl.edu.agh.calculationp2p.network.router.Router;
import pl.edu.agh.calculationp2p.state.future.Future;
import pl.edu.agh.calculationp2p.state.task.TaskRecord;

import java.util.Objects;

public class Calculated implements Body{

    private final TaskRecord taskRecord;

    public Calculated(TaskRecord taskRecord){
        this.taskRecord = taskRecord;
    }

    @Override
    public String serializeType() {
        return "\"calculated\"";
    }

    @Override
    public String serializeContent() {
        return taskRecord.serialize();
    }

    @Override
    public void process(int sender, MessageProcessContext context) {
        int myId = context.getRouter().getId();
        Future<TaskRecord> calculateFuture = context.getStateUpdater().updateTask(taskRecord);
        context.getFutureProcessor().addFutureProcess(calculateFuture, () -> {
            Router router = context.getRouter();
            Message confirm = new MessageImpl(myId, sender, new Confirm(calculateFuture.get()));
            router.send(confirm);
        });
    }

    @Override
    public Body clone() {
        return new Calculated(taskRecord);
    }

    public TaskRecord getTaskRecord(){
        return taskRecord;
    }

    public int getTaskId() {
        return taskRecord.getTaskID();
    }

    public TaskResult getTaskResult() {
        return taskRecord.getResult();
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
        Calculated message = (Calculated) o;
        //TODO: equals() in taskResult
        return message.getTaskRecord().equals(this.taskRecord);
        //&& message.getTaskResult() == this.taskResult;
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskRecord.getTaskID(), taskRecord.getState(), taskRecord.getOwner());
    }

}

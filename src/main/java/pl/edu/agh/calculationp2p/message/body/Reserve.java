package pl.edu.agh.calculationp2p.message.body;

import pl.edu.agh.calculationp2p.message.Message;
import pl.edu.agh.calculationp2p.message.MessageImpl;
import pl.edu.agh.calculationp2p.message.process.MessageProcessContext;
import pl.edu.agh.calculationp2p.network.router.Router;
import pl.edu.agh.calculationp2p.state.future.Future;
import pl.edu.agh.calculationp2p.state.task.TaskRecord;

public class Reserve implements Body{

    private final TaskRecord taskRecord;

    public TaskRecord getTaskRecord() {
        return taskRecord;
    }

    public Reserve(TaskRecord taskRecord) {
        this.taskRecord = taskRecord;
    }

    @Override
    public String serializeType() {
        return "\"reserve\"";
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
        return new Reserve(taskRecord);
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
        Reserve message = (Reserve) o;
        return message.getTaskRecord().equals(this.taskRecord);
    }
}

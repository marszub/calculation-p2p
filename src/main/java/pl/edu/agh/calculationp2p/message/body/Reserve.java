package pl.edu.agh.calculationp2p.message.body;

import pl.edu.agh.calculationp2p.message.Message;
import pl.edu.agh.calculationp2p.message.MessageImpl;
import pl.edu.agh.calculationp2p.message.process.MessageProcessContext;
import pl.edu.agh.calculationp2p.network.router.Router;
import pl.edu.agh.calculationp2p.state.future.Future;
import pl.edu.agh.calculationp2p.state.task.TaskRecord;

public class Reserve implements Body{

    private final int taskId;

    public int getTaskId() {
        return taskId;
    }

    public Reserve(int taksId) {
        this.taskId = taksId;
    }

    @Override
    public String serializeType() {
        return "\"reserve\"";
    }

    @Override
    public String serializeContent() {
        return "{\"task_id\":"+ this.taskId +"}";
    }

    @Override
    public void process(int sender, MessageProcessContext context) {
        //TODO: (calculated, reserve) - > update()
        int myId = context.getRouter().getId();
        Future<TaskRecord> calculateFuture = context.getStateUpdater().reserve(taskId, sender);
        context.getFutureProcessor().addFutureProcess(calculateFuture, () -> {
            Router router = context.getRouter();
            Message confirm = new MessageImpl(myId, sender, new Confirm(calculateFuture.get()));
            router.send(confirm);
        });
    }

    @Override
    public Body clone() {
        return new Reserve(this.taskId);
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
        return message.getTaskId() == this.taskId;
    }
}

package pl.edu.agh.calculationp2p.message.body;

import pl.edu.agh.calculationp2p.message.process.MessageProcessContext;

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
        //TODO: implement
        // someone want to reserve some task and now
        // I want to say if he can or not, it means
        // if I already tried to reserve this task
        // I have to compare ids of ours

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

package pl.edu.agh.calculationp2p.message.body;

import pl.edu.agh.calculationp2p.message.process.MessageProcessContext;

public class Reserve implements Body{

    private final int taskId;

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

    }
}

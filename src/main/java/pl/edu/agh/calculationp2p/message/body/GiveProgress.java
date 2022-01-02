package pl.edu.agh.calculationp2p.message.body;

import pl.edu.agh.calculationp2p.message.process.MessageProcessContext;

public class GiveProgress implements Body{
    public GiveProgress() {

    }

    @Override
    public String serializeType() {
        return "\"give_progress\"";
    }

    @Override
    public String serializeContent() {
        return "{\"progress\":}";
    }

    @Override
    public void process(int sender, MessageProcessContext context) {

    }
}

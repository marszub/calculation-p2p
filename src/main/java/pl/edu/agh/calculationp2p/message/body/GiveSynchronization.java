package pl.edu.agh.calculationp2p.message.body;

import pl.edu.agh.calculationp2p.message.process.MessageProcessContext;

public class GiveSynchronization implements Body{
    public GiveSynchronization() {
    }

    @Override
    public String serializeType() {
        return "\"give_synchronization\"";
    }

    @Override
    public String serializeContent() {
        return null;
    }

    @Override
    public void process(int sender, MessageProcessContext context) {

    }
}

package pl.edu.agh.calculationp2p.message.body;

import pl.edu.agh.calculationp2p.message.process.MessageProcessContext;

public class GetSynchronization implements Body{
    public GetSynchronization() {

    }

    @Override
    public String serializeType() {
        return "\"get_synchronization\"";
    }

    @Override
    public String serializeContent() {
        return null;
    }

    @Override
    public void process(int sender, MessageProcessContext context) {

    }
}

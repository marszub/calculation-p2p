package pl.edu.agh.calculationp2p.message.body;

import pl.edu.agh.calculationp2p.message.process.MessageProcessContext;

public class GetProgress implements Body{
    public GetProgress() {

    }

    @Override
    public String serializeType() {
        return null;
    }

    @Override
    public String serializeContent() {
        return null;
    }

    @Override
    public void process(int sender, MessageProcessContext context) {

    }
}

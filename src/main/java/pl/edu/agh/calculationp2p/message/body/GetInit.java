package pl.edu.agh.calculationp2p.message.body;

import pl.edu.agh.calculationp2p.message.process.MessageProcessContext;

public class GetInit implements Body{
    public GetInit() {
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

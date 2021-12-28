package pl.edu.agh.calculationp2p.message.body;

import pl.edu.agh.calculationp2p.message.process.MessageProcessContext;

public class Reserve implements Body{
    public Reserve(int taksId) {
    }

    @Override
    public String serializeType() {
        return "\"reserve\"";
    }

    @Override
    public String serializeContent() {
        return null;
    }

    @Override
    public void process(int sender, MessageProcessContext context) {

    }
}

package pl.edu.agh.calculationp2p.message.body;

import pl.edu.agh.calculationp2p.message.process.MessageProcessContext;

public class Hello implements Body{
    public Hello(String ip) {

    }

    @Override
    public String serializeType() {
        return "\"hello\"";
    }

    @Override
    public String serializeContent() {
        return null;
    }

    @Override
    public void process(int sender, MessageProcessContext context) {

    }
}

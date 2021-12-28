package pl.edu.agh.calculationp2p.message.body;

import pl.edu.agh.calculationp2p.message.process.MessageProcessContext;

public class HeartBeat implements Body{
    public HeartBeat() {
    }

    @Override
    public String serializeType() {
        return "\"heart_beat\"";
    }

    @Override
    public String serializeContent() {
        return null;
    }

    @Override
    public void process(int sender, MessageProcessContext context) {
        context.getNodeRegister().updateNode(sender);
    }
}

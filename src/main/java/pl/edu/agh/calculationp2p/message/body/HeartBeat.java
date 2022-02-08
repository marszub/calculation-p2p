package pl.edu.agh.calculationp2p.message.body;

import pl.edu.agh.calculationp2p.message.process.MessageProcessContext;
import pl.edu.agh.calculationp2p.network.router.NodeRegister;

import java.util.Objects;


public class HeartBeat implements Body{

    @Override
    public String serializeType() {
        return "\"heart_beat\"";
    }

    @Override
    public String serializeContent() {
        return "{}";
    }

    @Override
    public void process(int sender, MessageProcessContext context) {
        NodeRegister nodeRegister = context.getRouter().getNodeRegister();
        if(nodeRegister.getPrivateNodes().contains(sender) || nodeRegister.getPublicNodes().containsKey(sender))
            nodeRegister.updateNode(sender);
    }

    @Override
    public Body clone() {
        return new HeartBeat();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        return getClass() == o.getClass();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this);
    }
}

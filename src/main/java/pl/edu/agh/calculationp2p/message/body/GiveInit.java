package pl.edu.agh.calculationp2p.message.body;

import pl.edu.agh.calculationp2p.message.process.MessageProcessContext;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;

public class GiveInit implements Body{
    public GiveInit(int newId, List<Integer> privateNodes, Map<Integer, InetSocketAddress> publicNodes) {

    }

    @Override
    public String serializeType() {
        return "\"give_init\"";
    }

    @Override
    public String serializeContent() {
        return null;
    }

    @Override
    public void process(int sender, MessageProcessContext context) {

    }
}

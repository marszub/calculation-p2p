package pl.edu.agh.calculationp2p.message.body;

import pl.edu.agh.calculationp2p.message.Message;
import pl.edu.agh.calculationp2p.message.MessageImpl;
import pl.edu.agh.calculationp2p.message.process.MessageProcessContext;

import java.net.InetSocketAddress;
import java.util.*;

public class GetInit implements Body{
    public GetInit() {
    }

    @Override
    public String serializeType() {
        return "\"get_init\"";
    }

    @Override
    public String serializeContent() {
        return "{}";
    }

    @Override
    public void process(int sender, MessageProcessContext context) {

        int myId = context.getRouter().getId();
        List<Integer> privatesNodes = context.getNodeRegister().getPrivateNodes();
        Map<Integer, InetSocketAddress> publicNodes = context.getNodeRegister().getPublicNodes();

        //get new ID
        int newId = 0;
        Set<Integer> idsAlreadyInUse = new HashSet<>(privatesNodes);
        idsAlreadyInUse.addAll(publicNodes.keySet());
        while(idsAlreadyInUse.contains(newId)){
            newId+=1;
        }

        Message newMess = new MessageImpl(myId, sender, new GiveInit(newId, privatesNodes, publicNodes));
        context.getRouter().send(newMess);
    }

    @Override
    public Body clone() {
        return new GetInit();
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
}

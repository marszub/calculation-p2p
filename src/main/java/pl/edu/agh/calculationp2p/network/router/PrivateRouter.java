package pl.edu.agh.calculationp2p.network.router;

import pl.edu.agh.calculationp2p.message.Message;
import pl.edu.agh.calculationp2p.network.connection.ConnectionManager;
import pl.edu.agh.calculationp2p.network.messagequeue.MessageConnectionPair;
import pl.edu.agh.calculationp2p.network.messagequeue.MessageQueueExit;

import java.util.*;

public class PrivateRouter extends RouterImpl
{
    public PrivateRouter(ConnectionManager ConnectionManager, MessageQueueExit MessageQueue, RoutingTable RoutingTable, NodeRegister nodeRegister)
    {
        super(ConnectionManager, MessageQueue, RoutingTable, nodeRegister);
    }

    @Override
    public boolean isPublic() {
        return false;
    }

    @Override
    public void send(Message message)
    {
        int receiverId = message.getReceiver();
        if(receiverId == broadcastId)
        {
            Integer broadcastSentThrough = null;
            if(nodeRegister.getPrivateNodes().size() > 0)
                broadcastSentThrough = super.sendMessageViaMiddleman(message);
            for(Integer id : nodeRegister.getPublicNodes().keySet())
            {
                if(!id.equals(broadcastSentThrough))
                    routingTable.send(id, message.clone(id));
            }
            return;
        }
        if(nodeRegister.getPrivateNodes().contains(receiverId))
        {
            super.sendMessageViaMiddleman(message);
            return;
        }
        routingTable.send(receiverId, message);
        routingTable.resendAll();
    }

    public List<Message> getMessage()
    {
        List<Message> list = new LinkedList<>();
        MessageConnectionPair result = messageQueue.get();
        while(result != null)
        {
            if(result.message().getReceiver() == myId || myId == unknownId)
            {
                list.add(result.message());
            }
            result = messageQueue.get();
        }
        return list;
    }
}

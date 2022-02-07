package pl.edu.agh.calculationp2p.network.router;

import pl.edu.agh.calculationp2p.message.Message;
import pl.edu.agh.calculationp2p.network.connection.ConnectionManager;
import pl.edu.agh.calculationp2p.network.messagequeue.MessageConnectionPair;
import pl.edu.agh.calculationp2p.network.messagequeue.MessageQueueExit;

import java.util.*;

public class PrivateRouter extends RouterImpl
{
    List<Integer> PrivateNodes = new ArrayList<>();

    public PrivateRouter(ConnectionManager ConnectionManager, MessageQueueExit MessageQueue, RoutingTable RoutingTable)
    {
        super(ConnectionManager, MessageQueue, RoutingTable);
    }

    @Override
    public boolean isPublic() {
        return false;
    }

    @Override
    public void createInterface(Integer nodeId) throws InterfaceExistsException
    {
        if(PrivateNodes.contains(nodeId))
            throw new InterfaceExistsException(nodeId);
        PrivateNodes.add(nodeId);
        super.createInterface(nodeId);
    }

    @Override
    public void deleteInterface(Integer nodeId) throws InterfaceDoesNotExistException
    {
        if(PrivateNodes.contains(nodeId))
            PrivateNodes.remove(nodeId);
        else
            super.deleteInterface(nodeId);
    }

    @Override
    public void send(Message message)
    {
        int receiverId = message.getReceiver();
        if(PrivateNodes.contains(receiverId) || receiverId == broadcastId)
        {
            super.sendMessageViaMiddleman(message);
        }
        else
        {
            routingTable.send(receiverId, message);
            routingTable.resendAll();
        }
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

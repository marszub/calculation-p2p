package pl.edu.agh.calculationp2p.network.router;

import pl.edu.agh.calculationp2p.message.Message;
import pl.edu.agh.calculationp2p.network.connection.ConnectionManager;
import pl.edu.agh.calculationp2p.network.messagequeue.MessageQueueExit;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class PublicRouter extends RouterImpl
{
    public PublicRouter(ConnectionManager ConnectionManager, MessageQueueExit MessageQueue, RoutingTable RoutingTable)
    {
        super(ConnectionManager, MessageQueue, RoutingTable);
    }

    @Override
    public void createInterface(int nodeId) throws InterfaceExistsException
    {
        routingTable.addInterface(nodeId);
    }

    @Override
    public void send(Message message)
    {
        int receiverId = 1; //TODO message.getReceiver().
        routingTable.send(receiverId, message);
        routingTable.resendAll();
    }

    @Override
    public List<Message> getMessage()
    {
        throw new UnsupportedOperationException("Will be implemented");
    }
}

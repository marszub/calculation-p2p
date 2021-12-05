package pl.edu.agh.calculationp2p.network.router;

import pl.edu.agh.calculationp2p.message.Message;
import pl.edu.agh.calculationp2p.network.connection.ConnectionManager;
import pl.edu.agh.calculationp2p.network.messagequeue.MessageQueueExit;

import java.util.ArrayList;
import java.util.List;

public class PublicRouter extends Router
{
    public PublicRouter(ConnectionManager ConnectionManager, MessageQueueExit MessageQueue, RoutingTable RoutingTable)
    {
        super(ConnectionManager, MessageQueue, RoutingTable);
    }

    @Override
    public void createInterface(int nodeId)
    {

    }

    @Override
    public void deleteInterface(int nodeId)
    {
    }

    @Override
    public void send(Message message)
    {
    }
}

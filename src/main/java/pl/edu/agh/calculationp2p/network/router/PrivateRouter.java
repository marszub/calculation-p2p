package pl.edu.agh.calculationp2p.network.router;

import pl.edu.agh.calculationp2p.message.Message;
import pl.edu.agh.calculationp2p.network.connection.ConnectionManager;
import pl.edu.agh.calculationp2p.network.messagequeue.MessageQueueExit;

import java.util.ArrayList;
import java.util.List;

public class PrivateRouter extends Router
{
    List<Integer> PrivateNodes = new ArrayList<>();

    public PrivateRouter(ConnectionManager ConnectionManager, MessageQueueExit MessageQueue, RoutingTable RoutingTable)
    {
        super(ConnectionManager, MessageQueue, RoutingTable);
    }

    @Override
    public void createInterface(int nodeId) throws InterfaceExistsException
    {
        if(PrivateNodes.contains(nodeId))
        {
            throw new InterfaceExistsException(nodeId);
        }
        else
        {
            PrivateNodes.add(nodeId);
        }
    }

    @Override
    public void deleteInterface(int nodeId)
    {
        if(PrivateNodes.contains(nodeId))
        {
        }
    }

    @Override
    public void send(Message message)
    {
    }
}

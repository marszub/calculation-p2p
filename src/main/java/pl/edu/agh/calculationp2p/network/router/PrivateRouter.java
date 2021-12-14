package pl.edu.agh.calculationp2p.network.router;

import pl.edu.agh.calculationp2p.message.Message;
import pl.edu.agh.calculationp2p.network.connection.ConnectionManager;
import pl.edu.agh.calculationp2p.network.messagequeue.MessageQueueExit;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class PrivateRouter extends RouterImpl
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
            throw new InterfaceExistsException(nodeId);
        PrivateNodes.add(nodeId);
    }

    @Override
    public void deleteInterface(int nodeId) throws InterfaceDoesNotExistException
    {
        if(PrivateNodes.contains(nodeId))
            PrivateNodes.remove(nodeId);
        else
            super.deleteInterface(nodeId);
    }

    @Override
    public void send(Message message) throws ConnectionLostException
    {
        int receiverId = 1; //TODO message.getReceiver().
        if(PrivateNodes.contains(receiverId))
        {
            Set<Integer> publicNodesSet = staticInterfaces.keySet();
            List<Integer> publicNodesList = new ArrayList<>(List.copyOf(publicNodesSet));
            Random random = new Random();
            while(publicNodesList.size() > 0)
            {
                int id = publicNodesList.get(random.nextInt(publicNodesList.size()));
                if(!routingTable.trySend(id, message))
                    publicNodesList.remove(id);
                else
                {
                    routingTable.resendAll();
                    return;
                }
            }
            throw new ConnectionLostException();
        }
        else
        {
            routingTable.send(receiverId, message);
            routingTable.resendAll();
        }
    }
}

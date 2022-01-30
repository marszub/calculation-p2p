package pl.edu.agh.calculationp2p.network.router;

import pl.edu.agh.calculationp2p.message.Message;
import pl.edu.agh.calculationp2p.network.connection.Connection;
import pl.edu.agh.calculationp2p.network.connection.ConnectionManager;
import pl.edu.agh.calculationp2p.network.messagequeue.MessageConnectionPair;
import pl.edu.agh.calculationp2p.network.messagequeue.MessageQueueExit;

import java.net.InetSocketAddress;
import java.util.*;

public class PublicRouter extends RouterImpl
{
    private final HashMap<Integer, ConnectionTimestampPair> connectionQueue = new HashMap<>();
    private final LinkedList<Connection> unknownNodeConnections = new LinkedList<>();
    final ArrayList<Integer> interfaces = new ArrayList<>();
    final long removeDeadline = 10000;

    public PublicRouter(ConnectionManager ConnectionManager, MessageQueueExit MessageQueue, RoutingTable RoutingTable)
    {
        super(ConnectionManager, MessageQueue, RoutingTable);
    }

    @Override
    public void createInterface(int nodeId) throws InterfaceExistsException
    {
        interfaces.add(nodeId);
        routingTable.addInterface(nodeId);
        if(connectionQueue.containsKey(nodeId))
        {
            routingTable.bind(nodeId, connectionQueue.get(nodeId).connection());
            connectionQueue.remove(nodeId);
        }
    }

    @Override
    public boolean isPublic() {
        return true;
    }

    @Override
    public void createInterface(int nodeId, InetSocketAddress ipAddress)
    {
        interfaces.add(nodeId);
        super.createInterface(nodeId, ipAddress);
    }

    @Override
    public void deleteInterface(int nodeId) throws InterfaceDoesNotExistException
    {
        interfaces.add(nodeId);
        super.deleteInterface(nodeId);
    }

    @Override
    public void send(Message message)
    {
        int receiverId = message.getReceiver();
        switch (receiverId) {
            case broadcastId:
                processMessageToAll(message);
                break;
            case unknownId:
                Connection connection = unknownNodeConnections.pop();
                connection.send(message);
                break;
            default:
                routingTable.send(receiverId, message);
                routingTable.resendAll();
        }
    }

    @Override
    public List<Message> getMessage()
    {
        checkOutdatedConnections();
        List<Message> list = new LinkedList<>();
        MessageConnectionPair result = messageQueue.get();
        while(result != null)
        {
            processResult(result, list);
            result = messageQueue.get();
        }
        return list;
    }

    //##################################################################################################################
    //                                               PRIVATE FUNCTIONS
    //##################################################################################################################

    private void checkOutdatedConnections()
    {
        for (Map.Entry<Integer, ConnectionTimestampPair> entry : connectionQueue.entrySet())
        {
            if(System.currentTimeMillis() - entry.getValue().timestamp() > removeDeadline)
                connectionQueue.remove(entry.getKey()); //delete connections from queue if they are too old
        }
    }

    private void processResult(MessageConnectionPair result, List<Message> list)
    {
        Message message = result.message();
        if(message.getReceiver() == mainServerId && message.getSender() == unknownId)
        {
            list.add(message);
            unknownNodeConnections.add(result.connection());
            return;
        }
        if(myId == unknownId)
        {
            list.add(message);
        }
        else
        {
            if(message.getReceiver() == myId)
            {
                list.add(message); //message to me
            }
            else
            {
                if (message.getReceiver() == broadcastId)
                {
                    list.add(message.clone(myId));
                    processMessageToAll(message); //message to all
                }
                else
                    if(interfaces.contains(message.getReceiver()))
                    {
                        routingTable.send(message.getReceiver(), message); //message to someone else, not me
                    }
            }
        }
        bindConnectionOrAddToQueue(message.getSender(), result.connection());
    }

    private void processMessageToAll(Message message)
    {
        for(int nodeId : interfaces)
        {
            if(nodeId != message.getSender())
            {
                Message newMessage = message.clone(nodeId);
                routingTable.send(nodeId, newMessage);
            }
        }
    }

    private void bindConnectionOrAddToQueue(int id, Connection connection)
    {
        if(routingTable.interfaceListContains(id))
        {
            routingTable.bind(id, connection);
        }
        else
        {
            connectionQueue.put(id, new ConnectionTimestampPair(connection, System.currentTimeMillis()));
        }
    }
}

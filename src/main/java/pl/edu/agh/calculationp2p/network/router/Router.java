package pl.edu.agh.calculationp2p.network.router;

import pl.edu.agh.calculationp2p.message.Message;
import pl.edu.agh.calculationp2p.network.connection.ConnectionManager;
import pl.edu.agh.calculationp2p.network.connection.StaticConnection;
import pl.edu.agh.calculationp2p.network.messagequeue.MessageQueueExit;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public abstract class Router
{
    private final ConnectionManager ConnectionManager;
    private final MessageQueueExit MessageQueue;
    private final RoutingTable RoutingTable;
    private final Map<Integer, StaticConnection> StaticInterfaces = new HashMap<>();

    public Router(ConnectionManager ConnectionManager, MessageQueueExit MessageQueue, pl.edu.agh.calculationp2p.network.router.RoutingTable RoutingTable)
    {
        this.RoutingTable = RoutingTable;
        this.ConnectionManager = ConnectionManager;
        this.MessageQueue = MessageQueue;
    }

    public void createInterface(int nodeId, InetSocketAddress ipAddress)
    {
        StaticConnection newConnection = new StaticConnection(ipAddress);
        StaticInterfaces.put(nodeId, newConnection);
        ConnectionManager.addStaticConnection(newConnection);
        RoutingTable.addInterface(nodeId);
        RoutingTable.bind(nodeId, newConnection);
    }

    public abstract void createInterface(int nodeId);

    public void deleteInterface(int nodeId)
    {
        if(RoutingTable.interfaceListContains(nodeId))
        {
            if(StaticInterfaces.containsKey(nodeId))
            {
                StaticConnection StaticConnection = StaticInterfaces.get(nodeId);
                ConnectionManager.removeStaticConnection(StaticConnection);
                StaticInterfaces.remove(nodeId);
            }
            RoutingTable.removeInterface(nodeId);
        }
    }

    //TODO
    public Message getMessage()
    {
        return MessageQueue.get();
    }

    public abstract void send(Message message);
}

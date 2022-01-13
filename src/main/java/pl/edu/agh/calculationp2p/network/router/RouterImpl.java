package pl.edu.agh.calculationp2p.network.router;

import pl.edu.agh.calculationp2p.network.connection.ConnectionManager;
import pl.edu.agh.calculationp2p.network.connection.StaticConnection;
import pl.edu.agh.calculationp2p.network.messagequeue.MessageQueueExit;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public abstract class RouterImpl implements Router {
    final ConnectionManager connectionManager;
    final MessageQueueExit messageQueue;
    final RoutingTable routingTable;
    final Map<Integer, StaticConnection> staticInterfaces = new HashMap<>();
    int myId = -1;

    public RouterImpl(ConnectionManager connectionManager, MessageQueueExit messageQueue, RoutingTable routingTable)
    {
        this.routingTable = routingTable;
        this.connectionManager = connectionManager;
        connectionManager.start();
        this.messageQueue = messageQueue;
    }

    @Override
    public void createInterface(int nodeId, InetSocketAddress ipAddress)
    {
        StaticConnection newConnection = new StaticConnection(ipAddress);
        staticInterfaces.put(nodeId, newConnection);
        connectionManager.addStaticConnection(newConnection);
        routingTable.addInterface(nodeId);
        routingTable.bind(nodeId, newConnection);
    }

    @Override
    public void deleteInterface(int nodeId) throws InterfaceDoesNotExistException
    {
        if(!routingTable.interfaceListContains(nodeId))
            throw new InterfaceDoesNotExistException(nodeId);
        if(staticInterfaces.containsKey(nodeId))
        {
            StaticConnection StaticConnection = staticInterfaces.get(nodeId);
            connectionManager.removeStaticConnection(StaticConnection);
            staticInterfaces.remove(nodeId);
        }
        routingTable.removeInterface(nodeId);


    }

    public void setId(int id)
    {
        this.myId = id;
    }

    public int getId()
    {
        return myId;
    }

    public void close()
    {
        connectionManager.close();
    }
}

package pl.edu.agh.calculationp2p.network.router;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import pl.edu.agh.calculationp2p.message.Message;
import pl.edu.agh.calculationp2p.network.connection.ConnectionManager;
import pl.edu.agh.calculationp2p.network.connection.StaticConnection;
import pl.edu.agh.calculationp2p.network.messagequeue.MessageQueueExit;

import java.net.InetSocketAddress;
import java.util.*;

public abstract class RouterImpl implements Router {
    protected final ConnectionManager connectionManager;
    protected final MessageQueueExit messageQueue;
    protected final RoutingTable routingTable;
    protected final Map<Integer, StaticConnection> staticInterfaces = new HashMap<>();
    protected int myId = -2;
    protected final int mainServerId = -3;
    protected final int unknownId = -2;
    protected final int broadcastId = -1;

    public RouterImpl(ConnectionManager connectionManager, MessageQueueExit messageQueue, RoutingTable routingTable)
    {
        this.routingTable = routingTable;
        this.connectionManager = connectionManager;
        connectionManager.start();
        this.messageQueue = messageQueue;
    }

    public int getMainServerId() {
        return mainServerId;
    }

    public int getUnknownId() {
        return unknownId;
    }

    public int getBroadcastId() {
        return broadcastId;
    }

    @Override
    public void createInterface(int nodeId){
        Logger logger = LoggerFactory.getLogger(RouterImpl.class);
        logger.debug("New interface: " + String.valueOf(nodeId));
    }

    @Override
    public void createInterface(int nodeId, InetSocketAddress ipAddress)
    {
        Logger logger = LoggerFactory.getLogger(RouterImpl.class);
        logger.debug("New interface: " + String.valueOf(nodeId));
        StaticConnection newConnection = new StaticConnection(ipAddress);
        staticInterfaces.put(nodeId, newConnection);
        connectionManager.addStaticConnection(newConnection);
        routingTable.addInterface(nodeId);
        routingTable.bind(nodeId, newConnection);
    }

    @Override
    public void deleteInterface(int nodeId) throws InterfaceDoesNotExistException
    {
        Logger logger = LoggerFactory.getLogger(RouterImpl.class);
        logger.debug("Deleting interface: " + String.valueOf(nodeId));
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

    @Override
    public void sendHelloMessage(Message message)
    {
        sendMessageViaMiddleman(message);
    }

    void sendMessageViaMiddleman(Message message)
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
    }
}

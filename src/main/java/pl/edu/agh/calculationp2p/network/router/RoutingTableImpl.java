package pl.edu.agh.calculationp2p.network.router;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import pl.edu.agh.calculationp2p.message.Message;
import pl.edu.agh.calculationp2p.network.connection.Connection;

import java.util.*;

public class RoutingTableImpl implements RoutingTable{
    private final Map<Integer, Connection> interfaces = new HashMap<>();
    private final Map<Integer, LinkedList<Message>> messageInterfaceQueue = new HashMap<>();

    public RoutingTableImpl()
    {
    }

    public void addInterface(int id) throws InterfaceExistsException
    {
        if(interfaces.containsKey(id))
            throw new InterfaceExistsException(id);
        interfaces.put(id, null);
        messageInterfaceQueue.put(id, new LinkedList<>());
    }

    public void removeInterface(int id) throws InterfaceDoesNotExistException
    {
        if(!interfaces.containsKey(id))
            throw new InterfaceDoesNotExistException(id);
        interfaces.remove(id);
        messageInterfaceQueue.remove(id);
    }

    public void bind(int id, Connection connection) throws InterfaceDoesNotExistException
    {
        if(!interfaces.containsKey(id))
            throw new InterfaceDoesNotExistException(id);
        interfaces.put(id, connection);
    }

    public void send(int id, Message message) throws InterfaceDoesNotExistException
    {
        if(!interfaces.containsKey(id))
            throw new InterfaceDoesNotExistException(id);
        if (interfaces.get(id) != null)
            sendTroughConnection(id, interfaces.get(id), message);
        else
        {
            addToMessageQueue(id, message);
        }
    }

    public boolean trySend(int id, Message message) throws InterfaceDoesNotExistException
    {
        if(!interfaces.containsKey(id))
            throw new InterfaceDoesNotExistException(id);
        if (interfaces.get(id) != null) {
            boolean result = interfaces.get(id).send(message);
            if(result) {
                Logger logger = LoggerFactory.getLogger("");
                logger.info("Sending message via: " + id + " | " + message.serialize());
            }
            return result;
        }
        return false;
    }

    public void resendAll()
    {
        for (Map.Entry<Integer, LinkedList<Message>> entry : messageInterfaceQueue.entrySet())
        {
            while (entry.getValue().size() > 0)
            {
                Message message = entry.getValue().pop();
                if(!trySend(entry.getKey(), message))
                {
                    addToMessageQueue(entry.getKey(), message);
                    break;
                }
            }
        }
    }

    public boolean interfaceListContains(int id)
    {
        return interfaces.containsKey(id);
    }
    // PRIVATE FUNCTIONS
    //------------------------------------------------------------------------------------------------------------------

    private void sendTroughConnection(int id, Connection connection, Message message)
    {
        if (!connection.send(message))
            addToMessageQueue(id, message);
        else
        {
            Logger logger = LoggerFactory.getLogger("");
            logger.info("Sending message via: " + id + " | " + message.serialize());
        }
    }

    private void addToMessageQueue(int ID, Message message)
    {
        Logger logger = LoggerFactory.getLogger("");
        logger.info("Sending message failed!: " + ID + " | " + message.serialize());
        LinkedList<Message> Queue = messageInterfaceQueue.get(ID);
        Queue.addLast(message);
    }
}

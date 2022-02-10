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

    public void addInterface(int id)
    {
        if(interfaces.containsKey(id))
            return;
        interfaces.put(id, null);
        messageInterfaceQueue.put(id, new LinkedList<>());
    }

    public void removeInterface(int id)
    {
        if(!interfaces.containsKey(id))
            return;
        interfaces.remove(id);
        messageInterfaceQueue.remove(id);
    }

    public void bind(int id, Connection connection)
    {
        if(!interfaces.containsKey(id))
            return;
        interfaces.put(id, connection);
    }

    public void send(int id, Message message)
    {
        if(!interfaces.containsKey(id))
        {
            Logger logger = LoggerFactory.getLogger("");
            logger.error("Impossible to send, no interface : " + id + " | " + message.serialize() + " | " + "interfaces: ", interfaces.toString());
            return;
        }
        if (interfaces.get(id) != null)
            sendTroughConnection(id, interfaces.get(id), message);
        else
        {
            addToMessageQueue(id, message);
        }
    }

    public boolean trySend(int id, Message message)
    {
        if(!interfaces.containsKey(id))
            return false;
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
                Logger logger = LoggerFactory.getLogger("");
                logger.info("Resending message: " + message.serialize());
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
        messageInterfaceQueue.get(ID).addLast(message);
    }
}

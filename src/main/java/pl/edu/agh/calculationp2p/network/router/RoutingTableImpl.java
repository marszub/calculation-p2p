package pl.edu.agh.calculationp2p.network.router;

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
        else
        {
            interfaces.put(id, null);
            messageInterfaceQueue.put(id, new LinkedList<>());
        }
    }

    public void removeInterface(int id) throws InterfaceDoesNotExistException
    {
        if(interfaces.containsKey(id))
        {
            interfaces.remove(id);
            messageInterfaceQueue.remove(id);
        }
        else
            throw new InterfaceDoesNotExistException(id);
    }

    public void bind(int id, Connection connection) throws InterfaceDoesNotExistException
    {
        if(interfaces.containsKey(id))
            interfaces.put(id, connection);
        else
            throw new InterfaceDoesNotExistException(id);
    }

    public void send(int id, Message message) throws InterfaceDoesNotExistException
    {
        if(interfaces.containsKey(id))
        {
            if (interfaces.get(id) != null)
                sendTroughConnection(id, interfaces.get(id), message);
            else
            {
                addToMessageQueue(id, message);
            }
        }
        else
        {
            throw new InterfaceDoesNotExistException(id);
        }
    }

    public boolean trySend(int id, Message message) throws InterfaceDoesNotExistException
    {
        if(interfaces.containsKey(id))
        {
            if (interfaces.get(id) != null)
                return interfaces.get(id).send(message);
        }
        else
        {
            throw new InterfaceDoesNotExistException(id);
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
    }

    private void addToMessageQueue(int ID, Message message)
    {
        LinkedList<Message> Queue = messageInterfaceQueue.get(ID);
        Queue.addLast(message);
    }
}

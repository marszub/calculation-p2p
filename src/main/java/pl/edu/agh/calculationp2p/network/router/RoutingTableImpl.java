package pl.edu.agh.calculationp2p.network.router;

import pl.edu.agh.calculationp2p.message.Message;
import pl.edu.agh.calculationp2p.network.connection.Connection;

import java.util.*;

public class RoutingTableImpl {
    private final Map<Integer, Connection> Interfaces = new HashMap<>();
    private final Map<Integer, List<Message>> MessageInterfaceQueue = new HashMap<>();

    public RoutingTableImpl()
    {
    }

    public void addInterface(int id) throws InterfaceExistsException
    {
        if(Interfaces.containsKey(id))
            throw new InterfaceExistsException(id);
        else
        {
            Interfaces.put(id, null);
            MessageInterfaceQueue.put(id, new ArrayList<>());
        }
    }
    public void removeInterface(int id) throws InterfaceDoesNotExistException
    {
        if(Interfaces.containsKey(id))
        {
            Interfaces.remove(id);
            MessageInterfaceQueue.remove(id);
        }
        else
            throw new InterfaceDoesNotExistException(id);
    }

    public void bind(int id, Connection connection) throws InterfaceDoesNotExistException
    {
        if(Interfaces.containsKey(id))
            Interfaces.put(id, connection);
        else
            throw new InterfaceDoesNotExistException(id);
    }

    public void send(int id, Message message) throws InterfaceDoesNotExistException
    {
        if(Interfaces.containsKey(id))
        {
            if (Interfaces.get(id) != null)
                sendTroughConnection(id, Interfaces.get(id), message);
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
        if(Interfaces.containsKey(id))
        {
            if (Interfaces.get(id) != null)
                return Interfaces.get(id).send(message);
        }
        else
        {
            throw new InterfaceDoesNotExistException(id);
        }
        return false;
    }

    public void resendAll()
    {
        List<Integer> idList= new ArrayList<>();
        List<Message> messageList= new ArrayList<>();

        Set<Integer> KeySet= MessageInterfaceQueue.keySet();
        for (int id : KeySet) {
            messageList.addAll(MessageInterfaceQueue.get(id));
            for (int i = 0; i < MessageInterfaceQueue.get(id).size(); i++) {
                idList.add(id);
            }
            MessageInterfaceQueue.get(id).clear();
        }
        for(int i = 0; i < idList.size(); i++)
        {
            send(idList.get(i), messageList.get(i));
        }
    }

    public boolean interfaceListContains(int id)
    {
        return Interfaces.containsKey(id);
    }

    private void sendTroughConnection(int id, Connection connection, Message message)
    {
        if (!connection.send(message))
            addToMessageQueue(id, message);
    }

    private void addToMessageQueue(int ID, Message message)
    {
        List<Message> Queue = MessageInterfaceQueue.get(ID);
        Queue.add(message);
    }
}

package pl.edu.agh.calculationp2p.network.utilities;

import pl.edu.agh.calculationp2p.message.Message;
import pl.edu.agh.calculationp2p.network.connection.Connection;

import java.nio.channels.ClosedChannelException;
import java.nio.channels.Selector;
import java.util.LinkedList;

public class DummyConnection implements Connection {
    private boolean sendResult;
    private final LinkedList<Message> list = new LinkedList<>();
    private Message lastMessage;

    public DummyConnection(boolean sendResult)
    {
        this.sendResult = sendResult;
    }

    @Override
    public boolean send(Message message) {
        if(sendResult)
        {
            list.addLast(message);
            lastMessage = message;
        }
        boolean tmp = sendResult;
        sendResult = !sendResult;
        return tmp;
    }

    @Override
    public void register(Selector selector) throws ClosedChannelException {
        return;
    }

    @Override
    public void close() {
        return;
    }

    public Message getLastMessage()
    {
        return lastMessage;
    }

    public void setResult(boolean result)
    {
        sendResult = result;
    }

    public LinkedList<Message> getList()
    {
        return list;
    }

    public String[] read()
    {
        return null;
    }
}

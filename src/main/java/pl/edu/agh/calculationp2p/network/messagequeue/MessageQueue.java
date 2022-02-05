package pl.edu.agh.calculationp2p.network.messagequeue;

import java.util.NoSuchElementException;
import java.util.concurrent.LinkedBlockingQueue;

public class MessageQueue implements MessageQueueEntry, MessageQueueExit {
    LinkedBlockingQueue<MessageConnectionPair> messageQueue = new LinkedBlockingQueue<>();

    public MessageQueue()
    {
    }

    @Override
    public void add(MessageConnectionPair pair)
    {
        messageQueue.add(pair);
    }

    @Override
    public MessageConnectionPair get()
    {
        try
        {
            return messageQueue.remove();
        }
        catch (NoSuchElementException exception)
        {
            return null;
        }
    }
}

package pl.edu.agh.calculationp2p.network.utilities;

import pl.edu.agh.calculationp2p.network.messagequeue.MessageConnectionPair;
import pl.edu.agh.calculationp2p.network.messagequeue.MessageQueue;
import pl.edu.agh.calculationp2p.network.messagequeue.MessageQueueEntry;
import pl.edu.agh.calculationp2p.network.messagequeue.MessageQueueExit;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.concurrent.Semaphore;

public class DummyMessageQueue extends MessageQueue {
    LinkedList<MessageConnectionPair> list = new LinkedList<>();
    Semaphore semaphore = null;
    Semaphore secondSemaphore = null;

    public DummyMessageQueue()
    {
    }

    public LinkedList<MessageConnectionPair> getList()
    {
        return list;
    }

    public void addSemaphore(Semaphore semaphore)
    {
        this.semaphore = semaphore;
    }

    public void addSecondSemaphore(Semaphore semaphore)
    {
        this.secondSemaphore = semaphore;
    }

    @Override
    public void add(MessageConnectionPair pair) {
        list.add(pair);
        if(semaphore != null)
        {
            semaphore.release();
        }
        if(secondSemaphore != null)
        {
            secondSemaphore.release();
        }
    }

    @Override
    public MessageConnectionPair get() {
        try
        {
            return list.remove();
        }
        catch (NoSuchElementException exception)
        {
            return null;
        }
    }
}

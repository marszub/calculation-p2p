package pl.edu.agh.calculationp2p.network.messagequeue;
import org.junit.jupiter.api.Test;
import pl.edu.agh.calculationp2p.network.utilities.DummyConnection;
import pl.edu.agh.calculationp2p.network.utilities.DummyMessage;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MessageQueueTest
{
    @Test
    void checkIfMessageAddsItselfCorrectly()
    {
        MessageQueue queue = new MessageQueue();
        MessageConnectionPair pair = new MessageConnectionPair(new DummyMessage(), new DummyConnection(true));
        queue.add(pair);
        assertEquals(pair, queue.get());
    }

    @Test
    void checkIfAllMessagesAddsCorrectly()
    {
        MessageQueue queue = new MessageQueue();
        queue.add(new MessageConnectionPair(new DummyMessage(), new DummyConnection(true)));
        queue.add(new MessageConnectionPair(new DummyMessage(), new DummyConnection(true)));
        queue.add(new MessageConnectionPair(new DummyMessage(), new DummyConnection(true)));
        queue.add(new MessageConnectionPair(new DummyMessage(), new DummyConnection(true)));
        assertEquals(4, getQueueSize(queue));
    }

    @Test
    void checkIfNewQueueIsEmpty()
    {
        MessageQueue queue = new MessageQueue();
        assertNull(queue.get());
    }

    private int getQueueSize(MessageQueue queue)
    {
        int i = 0;
        MessageConnectionPair res = queue.get();
        while(res != null)
        {
            i = i + 1;
            res = queue.get();
        }
        return i;
    }
}

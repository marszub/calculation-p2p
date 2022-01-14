package pl.edu.agh.calculationp2p.network.router;

import org.junit.jupiter.api.Test;
import pl.edu.agh.calculationp2p.network.messagequeue.MessageConnectionPair;
import pl.edu.agh.calculationp2p.network.utilities.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RouterTest {
    @Test
    void checkIfRemovingInterfaceWorksProperlyPublic()
    {
        PublicRouter router = new PublicRouter(new DummyConnectionManager(),
                                               new DummyMessageQueue(),
                                               new RoutingTableImpl());
        router.createInterface(1);
        assertDoesNotThrow(() -> router.deleteInterface(1));
        router.close();
    }

    @Test
    void checkIfRemovingNonExistingInterfaceWorksProperlyPublic()
    {
        PublicRouter router = new PublicRouter(new DummyConnectionManager(),
                                               new DummyMessageQueue(),
                                               new RoutingTableImpl());
        assertThrows(InterfaceDoesNotExistException.class, () -> router.deleteInterface(1));
        router.close();
    }

    @Test
    void checkIfRemovingInterfaceWorksProperlyPrivate()
    {
        PrivateRouter router = new PrivateRouter(new DummyConnectionManager(),
                new DummyMessageQueue(),
                new RoutingTableImpl());
        router.createInterface(1);
        assertDoesNotThrow(() -> router.deleteInterface(1));
        router.close();
    }

    @Test
    void checkIfRemovingNonExistingInterfaceWorksProperlyPrivate()
    {
        PrivateRouter router = new PrivateRouter(new DummyConnectionManager(),
                new DummyMessageQueue(),
                new RoutingTableImpl());
        RoutingTable table = new RoutingTableImpl();
        assertThrows(InterfaceDoesNotExistException.class, () -> router.deleteInterface(1));
        router.close();
    }

    @Test
    void checkIfGetIdWorksProperly()
    {
        PrivateRouter router1 = new PrivateRouter(new DummyConnectionManager(),
                new DummyMessageQueue(),
                new RoutingTableImpl());
        PublicRouter router2 = new PublicRouter(new DummyConnectionManager(),
                new DummyMessageQueue(),
                new RoutingTableImpl());
        router1.setId(1);
        router2.setId(2);
        assertEquals(1, router1.getId());
        assertEquals(2, router2.getId());
        router1.close();
        router2.close();
    }

    @Test
    void checkGettingMessagesPrivateWithoutId()
    {
        DummyMessageQueue queue = new DummyMessageQueue();
        DummyMessage message = new DummyMessage("TEST123");
        message.setReceiver(1);
        queue.add(new MessageConnectionPair(message, new DummyConnection(false)));
        PrivateRouter router = new PrivateRouter(new DummyConnectionManager(), queue, new DummyRoutingTable());
        assertEquals(message, router.getMessage().get(0));
    }

    @Test
    void checkGettingMessagesPrivateWithIdCorrect()
    {
        DummyMessageQueue queue = new DummyMessageQueue();
        DummyMessage message = new DummyMessage("TEST123");
        message.setReceiver(1);
        queue.add(new MessageConnectionPair(message, new DummyConnection(false)));
        PrivateRouter router = new PrivateRouter(new DummyConnectionManager(), queue, new DummyRoutingTable());
        router.setId(1);
        assertEquals(message, router.getMessage().get(0));
    }

    @Test
    void checkGettingMessagesPrivateWithIdIncorrect()
    {
        DummyMessageQueue queue = new DummyMessageQueue();
        DummyMessage message = new DummyMessage("TEST123");
        message.setReceiver(1);
        queue.add(new MessageConnectionPair(message, new DummyConnection(false)));
        PrivateRouter router = new PrivateRouter(new DummyConnectionManager(), queue, new DummyRoutingTable());
        router.setId(2);
        assertEquals(0, router.getMessage().size());
    }

    @Test
    void checkGettingMessagesPublicWithoutId()
    {
        DummyMessageQueue queue = new DummyMessageQueue();
        DummyMessage message = new DummyMessage("TEST123");
        message.setReceiver(1);
        queue.add(new MessageConnectionPair(message, new DummyConnection(false)));
        PublicRouter router = new PublicRouter(new DummyConnectionManager(), queue, new DummyRoutingTable());
        assertEquals(message, router.getMessage().get(0));
    }

    @Test
    void checkGettingMessagesPublicWithIdCorrect()
    {
        DummyMessageQueue queue = new DummyMessageQueue();
        DummyMessage message = new DummyMessage("TEST123");
        message.setReceiver(1);
        queue.add(new MessageConnectionPair(message, new DummyConnection(false)));
        PublicRouter router = new PublicRouter(new DummyConnectionManager(), queue, new DummyRoutingTable());
        router.setId(1);
        assertEquals(message, router.getMessage().get(0));
    }

    @Test
    void checkGettingMessagesPublicAfterBroadCast()
    {
        DummyMessageQueue queue = new DummyMessageQueue();
        DummyMessage message = new DummyMessage("TEST123");
        message.setReceiver(-1);
        queue.add(new MessageConnectionPair(message, new DummyConnection(false)));
        PublicRouter router = new PublicRouter(new DummyConnectionManager(), queue, new DummyRoutingTable());
        router.setId(1);
        DummyMessage result = (DummyMessage) router.getMessage().get(0);
        assertEquals(1, result.getReceiver());
        assertEquals(message.serialize(), result.serialize());
    }
}

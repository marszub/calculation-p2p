package pl.edu.agh.calculationp2p.network.router;

import org.junit.jupiter.api.Test;
import pl.edu.agh.calculationp2p.network.connection.ConnectionManager;
import pl.edu.agh.calculationp2p.network.messagequeue.MessageQueue;
import pl.edu.agh.calculationp2p.network.utilities.DummyInterrupter;
import pl.edu.agh.calculationp2p.network.utilities.DummyMessageParser;
import pl.edu.agh.calculationp2p.network.utilities.DummyMessageQueue;

import java.net.InetSocketAddress;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RouterTest {
    @Test
    void checkIfRemovingInterfaceWorksProperlyPublic()
    {
        DummyMessageQueue messageQueue = new DummyMessageQueue();
        InetSocketAddress ip = new InetSocketAddress("localhost", 50000);
        DummyMessageParser dummyMessageParser = new DummyMessageParser();
        ConnectionManager connectionManager = new ConnectionManager(messageQueue, dummyMessageParser, ip, new DummyInterrupter());
        connectionManager.start();
        RoutingTable table = new RoutingTableImpl();
        PublicRouter router = new PublicRouter(connectionManager, messageQueue, table);
        router.createInterface(1);
        assertDoesNotThrow(() -> router.deleteInterface(1));
        router.close();
    }

    @Test
    void checkIfRemovingNonExistingInterfaceWorksProperlyPublic()
    {
        DummyMessageQueue messageQueue = new DummyMessageQueue();
        InetSocketAddress ip = new InetSocketAddress("localhost", 50000);
        DummyMessageParser dummyMessageParser = new DummyMessageParser();
        ConnectionManager connectionManager = new ConnectionManager(messageQueue, dummyMessageParser, ip, new DummyInterrupter());
        connectionManager.start();
        RoutingTable table = new RoutingTableImpl();
        PublicRouter router = new PublicRouter(connectionManager, messageQueue, table);
        assertThrows(InterfaceDoesNotExistException.class, () -> router.deleteInterface(1));
        router.close();
    }

    @Test
    void checkIfRemovingInterfaceWorksProperlyPrivate()
    {
        DummyMessageQueue messageQueue = new DummyMessageQueue();
        DummyMessageParser dummyMessageParser = new DummyMessageParser();
        ConnectionManager connectionManager = new ConnectionManager(messageQueue, dummyMessageParser, new DummyInterrupter());
        connectionManager.start();
        RoutingTable table = new RoutingTableImpl();
        PrivateRouter router = new PrivateRouter(connectionManager, messageQueue, table);
        router.createInterface(1);
        assertDoesNotThrow(() -> router.deleteInterface(1));
        router.close();
    }

    @Test
    void checkIfRemovingNonExistingInterfaceWorksProperlyPrivate()
    {
        DummyMessageQueue messageQueue = new DummyMessageQueue();
        DummyMessageParser dummyMessageParser = new DummyMessageParser();
        ConnectionManager connectionManager = new ConnectionManager(messageQueue, dummyMessageParser, new DummyInterrupter());
        connectionManager.start();
        RoutingTable table = new RoutingTableImpl();
        PrivateRouter router = new PrivateRouter(connectionManager, messageQueue, table);
        assertThrows(InterfaceDoesNotExistException.class, () -> router.deleteInterface(1));
        router.close();
    }

    @Test
    void checkIfGetIdWorksProperly()
    {
        ConnectionManager connectionManager = new ConnectionManager(new MessageQueue(), new DummyMessageParser(), new DummyInterrupter());
        Router router1 = new PrivateRouter(connectionManager, new MessageQueue(), new RoutingTableImpl());
        Router router2 = new PublicRouter(connectionManager, new MessageQueue(), new RoutingTableImpl());
        router1.setId(1);
        router2.setId(2);
        assertEquals(1, router1.getId());
        assertEquals(2, router2.getId());
        router1.close();
        router2.close();
    }
}

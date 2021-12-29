package pl.edu.agh.calculationp2p.network.router;

import org.junit.jupiter.api.Test;
import pl.edu.agh.calculationp2p.message.Message;
import pl.edu.agh.calculationp2p.network.connection.Connection;
import pl.edu.agh.calculationp2p.network.connection.ConnectionManager;
import pl.edu.agh.calculationp2p.network.messagequeue.MessageQueue;
import pl.edu.agh.calculationp2p.network.utilities.DummyMessage;
import pl.edu.agh.calculationp2p.network.utilities.DummyMessage2;
import pl.edu.agh.calculationp2p.network.utilities.DummyMessageParser;
import pl.edu.agh.calculationp2p.network.utilities.DummyMessageQueue;

import javax.swing.text.StyleContext;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class RouterTest {
    @Test
    void checkIfRemovingInterfaceWorksProperlyPublic()
    {
        DummyMessageQueue messageQueue = new DummyMessageQueue();
        InetSocketAddress ip = new InetSocketAddress("localhost", 13900);
        DummyMessageParser dummyMessageParser = new DummyMessageParser();
        ConnectionManager connectionManager = new ConnectionManager(messageQueue, dummyMessageParser, ip);
        connectionManager.start();
        RoutingTable table = new RoutingTableImpl();
        PublicRouter router = new PublicRouter(connectionManager, messageQueue, table);
        router.createInterface(1);
        assertDoesNotThrow(() -> router.deleteInterface(1));
    }

    @Test
    void checkIfRemovingNonExistingInterfaceWorksProperlyPublic()
    {
        DummyMessageQueue messageQueue = new DummyMessageQueue();
        InetSocketAddress ip = new InetSocketAddress("localhost", 13901);
        DummyMessageParser dummyMessageParser = new DummyMessageParser();
        ConnectionManager connectionManager = new ConnectionManager(messageQueue, dummyMessageParser, ip);
        connectionManager.start();
        RoutingTable table = new RoutingTableImpl();
        PublicRouter router = new PublicRouter(connectionManager, messageQueue, table);
        assertThrows(InterfaceDoesNotExistException.class, () -> router.deleteInterface(1));
    }

    @Test
    void checkIfRemovingInterfaceWorksProperlyPrivate()
    {
        DummyMessageQueue messageQueue = new DummyMessageQueue();
        DummyMessageParser dummyMessageParser = new DummyMessageParser();
        ConnectionManager connectionManager = new ConnectionManager(messageQueue, dummyMessageParser);
        connectionManager.start();
        RoutingTable table = new RoutingTableImpl();
        PrivateRouter router = new PrivateRouter(connectionManager, messageQueue, table);
        router.createInterface(1);
        assertDoesNotThrow(() -> router.deleteInterface(1));
    }

    @Test
    void checkIfRemovingNonExistingInterfaceWorksProperlyPrivate()
    {
        DummyMessageQueue messageQueue = new DummyMessageQueue();
        DummyMessageParser dummyMessageParser = new DummyMessageParser();
        ConnectionManager connectionManager = new ConnectionManager(messageQueue, dummyMessageParser);
        connectionManager.start();
        RoutingTable table = new RoutingTableImpl();
        PrivateRouter router = new PrivateRouter(connectionManager, messageQueue, table);
        assertThrows(InterfaceDoesNotExistException.class, () -> router.deleteInterface(1));
    }

    @Test
    void checkIfRemovingStaticInterfaceDoesNotReturnError() throws InterruptedException {
        Prepare3RoutersTest variables = prepareEnvironment(13902);
        Router router = variables.router1();
        assertDoesNotThrow(() -> router.deleteInterface(2));
        assertThrows(InterfaceDoesNotExistException.class, () -> router.deleteInterface(2));
    }

    @Test
    void checkIfGetIdWorksProperly()
    {
        ConnectionManager connectionManager = new ConnectionManager(new MessageQueue(), new DummyMessageParser());
        Router router1 = new PrivateRouter(connectionManager, new MessageQueue(), new RoutingTableImpl());
        Router router2 = new PublicRouter(connectionManager, new MessageQueue(), new RoutingTableImpl());
        router1.setId(1);
        router2.setId(2);
        assertEquals(1, router1.getId());
        assertEquals(2, router2.getId());
    }

    @Test
    void checkIfMessagesSendFromPublicRouterToPublicRouter() throws InterruptedException {
        DummyMessageQueue messageQueue1 = new DummyMessageQueue();
        DummyMessageQueue messageQueue2 = new DummyMessageQueue();

        Semaphore semaphore = new Semaphore(1);
        semaphore.acquire();
        messageQueue2.addSemaphore(semaphore);

        InetSocketAddress ip1 = new InetSocketAddress("localhost", 14001);
        InetSocketAddress ip2 = new InetSocketAddress("localhost", 14002);
        DummyMessageParser dummyMessageParser = new DummyMessageParser();
        ConnectionManager connectionManager1 = new ConnectionManager(messageQueue1, dummyMessageParser, ip1);
        ConnectionManager connectionManager2 = new ConnectionManager(messageQueue2, dummyMessageParser, ip2);
        connectionManager1.start();
        connectionManager2.start();

        RoutingTable table1 = new RoutingTableImpl();
        RoutingTable table2 = new RoutingTableImpl();

        PublicRouter publicRouter1 = new PublicRouter(connectionManager1, messageQueue1, table1);
        PublicRouter publicRouter2 = new PublicRouter(connectionManager2, messageQueue2, table2);
        publicRouter1.setId(1);
        publicRouter2.setId(2);

        publicRouter1.createInterface(2, ip2);
        DummyMessage message = new DummyMessage("TEST");
        message.setSender(1);
        message.setReceiver(2);
        dummyMessageParser.addParse(message.serialize(), message);
        publicRouter1.send(message);
        semaphore.tryAcquire(100, TimeUnit.MILLISECONDS);
        assertEquals(message, publicRouter2.getMessage().get(0));
    }

    @Test
    void checkIfMessagesSendFromPrivateRouterToPublicRouter() throws InterruptedException {
        DummyMessageQueue messageQueue1 = new DummyMessageQueue();
        DummyMessageQueue messageQueue2 = new DummyMessageQueue();

        Semaphore semaphore = new Semaphore(1);
        semaphore.acquire();
        messageQueue2.addSemaphore(semaphore);

        InetSocketAddress ip2 = new InetSocketAddress("localhost", 14003);
        DummyMessageParser dummyMessageParser = new DummyMessageParser();
        ConnectionManager connectionManager1 = new ConnectionManager(messageQueue1, dummyMessageParser);
        ConnectionManager connectionManager2 = new ConnectionManager(messageQueue2, dummyMessageParser, ip2);
        connectionManager1.start();
        connectionManager2.start();
        RoutingTable table1 = new RoutingTableImpl();
        RoutingTable table2 = new RoutingTableImpl();

        PrivateRouter Router1 = new PrivateRouter(connectionManager1, messageQueue1, table1);
        PublicRouter Router2 = new PublicRouter(connectionManager2, messageQueue2, table2);
        Router1.setId(1);
        Router2.setId(2);

        Router1.createInterface(2, ip2);
        DummyMessage message = new DummyMessage("TEST");
        message.setSender(1);
        message.setReceiver(2);
        dummyMessageParser.addParse(message.serialize(), message);
        Router1.send(message);
        semaphore.tryAcquire(100, TimeUnit.MILLISECONDS);
        assertEquals(message, Router2.getMessage().get(0));
    }

    @Test
    void checkIfMessagesSendFromPublicRouterToPrivateRouter() throws InterruptedException {
        DummyMessageQueue messageQueue1 = new DummyMessageQueue();
        DummyMessageQueue messageQueue2 = new DummyMessageQueue();

        Semaphore semaphore = new Semaphore(1);
        Semaphore semaphore2 = new Semaphore(1);
        semaphore.acquire();
        messageQueue2.addSemaphore(semaphore);
        semaphore2.acquire();
        messageQueue1.addSemaphore(semaphore2);

        InetSocketAddress ip2 = new InetSocketAddress("localhost", 14004);
        DummyMessageParser dummyMessageParser = new DummyMessageParser();
        ConnectionManager connectionManager1 = new ConnectionManager(messageQueue1, dummyMessageParser);
        ConnectionManager connectionManager2 = new ConnectionManager(messageQueue2, dummyMessageParser, ip2);
        connectionManager1.start();
        connectionManager2.start();

        RoutingTable table1 = new RoutingTableImpl();
        RoutingTable table2 = new RoutingTableImpl();
        PrivateRouter Router1 = new PrivateRouter(connectionManager1, messageQueue1, table1);
        PublicRouter Router2 = new PublicRouter(connectionManager2, messageQueue2, table2);
        Router1.setId(1);
        Router2.setId(2);

        Router1.createInterface(2, ip2);
        DummyMessage message = new DummyMessage("TEST");
        message.setSender(1);
        message.setReceiver(2);
        dummyMessageParser.addParse(message.serialize(), message);
        Router1.send(message);
        semaphore.tryAcquire(100, TimeUnit.MILLISECONDS);
        Router2.getMessage();

        Router2.createInterface(1);
        DummyMessage message2 = new DummyMessage("SECONDMESSAGE");
        message2.setSender(2);
        message2.setReceiver(1);
        dummyMessageParser.addParse(message2.serialize(), message2);
        Router2.send(message2);
        semaphore2.tryAcquire(100, TimeUnit.MILLISECONDS);
        assertEquals(message2, Router1.getMessage().get(0));
    }

    @Test
    void checkIfMessagesSentFromPrivateRouterToPrivateRouterViaPublicRouter() throws InterruptedException {
        Prepare3RoutersTest variables = prepareEnvironment(14005);
        Semaphore semaphore2 = variables.semaphore2();
        Semaphore semaphore3 = variables.semaphore3();
        Router Router1 = variables.router1();
        Router Router2 = variables.router2();
        Router Router3 = variables.router3();
        DummyMessageParser dummyMessageParser = (DummyMessageParser) variables.messageParser();

        DummyMessage message = new DummyMessage("TESTFINAL");
        message.setSender(1);
        message.setReceiver(3);
        dummyMessageParser.addParse(message.serialize(), message);
        Router1.send(message);
        semaphore2.acquire();
        semaphore2.tryAcquire(100, TimeUnit.MILLISECONDS);
        Router2.getMessage();
        semaphore3.acquire();
        semaphore3.tryAcquire(100, TimeUnit.MILLISECONDS);
        assertEquals(message, Router3.getMessage().get(0));
    }

    @Test
    void checkIfMessagesSentFromPrivateToAllArrivesToAll() throws InterruptedException {
        Prepare3RoutersTest variables = prepareEnvironment(14006);
        Semaphore semaphore2 = variables.semaphore2();
        Semaphore semaphore3 = variables.semaphore3();
        Router Router1 = variables.router1();
        Router Router2 = variables.router2();
        Router Router3 = variables.router3();
        DummyMessageParser dummyMessageParser = (DummyMessageParser) variables.messageParser();

        DummyMessage2 message = new DummyMessage2("TESTMSGTOALL");
        message.setSender(1);
        message.setReceiver(-1);
        dummyMessageParser.addParse(message.serialize(), message);
        DummyMessage2 message2 = (DummyMessage2) message.clone(2);
        dummyMessageParser.addParse(message2.serialize(), message2);
        DummyMessage2 message3 = (DummyMessage2) message.clone(3);
        dummyMessageParser.addParse(message3.serialize(), message3);
        Router1.send(message);
        semaphore2.acquire();
        semaphore2.tryAcquire(100, TimeUnit.MILLISECONDS);
        assertEquals(message2.serialize(), Router2.getMessage().get(0).serialize());
        semaphore3.acquire();
        semaphore3.tryAcquire(100, TimeUnit.MILLISECONDS);
        assertEquals(message3, Router3.getMessage().get(0));
    }

    @Test
    void checkIfMessagesSentFromPublicToAllArrivesToAll() throws InterruptedException {
        Prepare3RoutersTest variables = prepareEnvironment(14007);
        Semaphore semaphore1 = variables.semaphore1();
        Semaphore semaphore3 = variables.semaphore3();
        Router Router1 = variables.router1();
        Router Router2 = variables.router2();
        Router Router3 = variables.router3();
        DummyMessageParser dummyMessageParser = (DummyMessageParser) variables.messageParser();

        semaphore3.acquire();
        semaphore1.acquire();
        DummyMessage2 message = new DummyMessage2("TESTMSGTOALL");
        message.setSender(2);
        message.setReceiver(-1);
        dummyMessageParser.addParse(message.serialize(), message);
        DummyMessage2 message1 = (DummyMessage2) message.clone(1);
        dummyMessageParser.addParse(message1.serialize(), message1);
        DummyMessage2 message3 = (DummyMessage2) message.clone(3);
        dummyMessageParser.addParse(message3.serialize(), message3);
        Router2.send(message);
        semaphore3.tryAcquire(100, TimeUnit.MILLISECONDS);
        assertEquals(message3, Router3.getMessage().get(0));
        semaphore1.tryAcquire(100, TimeUnit.MILLISECONDS);
        assertEquals(message1, Router1.getMessage().get(0));
    }

    @Test
    void ultimateRouterTestWith4Routers2Public2PrivateMessageToAllPrivate() throws InterruptedException {
        Prepare4RoutersTest variables = prepareEnvironmentFour(14010, 14011);
        Router Router1 = variables.router1();
        Router Router2 = variables.router2();
        Router Router3 = variables.router3();
        Router Router4 = variables.router4();

        Semaphore semaphore2 = variables.semaphore2();
        Semaphore semaphore3 = variables.semaphore3();
        Semaphore semaphore4 = variables.semaphore4();
        Semaphore semaphore5 = variables.semaphore5();

        DummyMessageParser dummyMessageParser = (DummyMessageParser) variables.messageParser();

        DummyMessage2 message1 = new DummyMessage2("TESTMESSAGETOALL");
        message1.setReceiver(-1);
        message1.setSender(1);
        dummyMessageParser.addParse(message1.serialize(), message1);
        DummyMessage2 message2 = (DummyMessage2) message1.clone(2);
        dummyMessageParser.addParse(message2.serialize(), message2);
        DummyMessage2 message3 = (DummyMessage2) message1.clone(3);
        dummyMessageParser.addParse(message3.serialize(), message3);
        DummyMessage2 message4 = (DummyMessage2) message1.clone(4);
        dummyMessageParser.addParse(message4.serialize(), message4);

        Router1.send(message1);
        semaphore5.acquire();
        semaphore2.acquire();
        semaphore3.acquire();
        semaphore4.acquire();
        semaphore5.tryAcquire(100, TimeUnit.MILLISECONDS);
        List<Message> list2 = Router2.getMessage();
        List<Message> list4 = Router4.getMessage();
        if(list2.size() > 0)
        {
            if(list4.size() > 0)
            {
                assertEquals(message4.serialize(), list4.get(0).serialize());
                semaphore3.tryAcquire(100, TimeUnit.MILLISECONDS);
                assertEquals(message3, Router3.getMessage().get(0));
            }
            else
            {
                assertEquals(message2.serialize(), list2.get(0).serialize());
                semaphore4.tryAcquire(100, TimeUnit.MILLISECONDS);
                semaphore3.tryAcquire(100, TimeUnit.MILLISECONDS);
                assertEquals(message3, Router3.getMessage().get(0));
                assertEquals(message4, Router4.getMessage().get(0));
            }
        }
        else
        {
            assertEquals(message4.serialize(), list4.get(0).serialize());
            semaphore2.tryAcquire(100, TimeUnit.MILLISECONDS);
            semaphore3.tryAcquire(100, TimeUnit.MILLISECONDS);
            assertEquals(message3, Router3.getMessage().get(0));
            assertEquals(message2, Router2.getMessage().get(0));
        }
    }

    @Test
    void ultimateRouterTestWith4Routers2Public2PrivateMessageToAllPublic() throws InterruptedException {
        Prepare4RoutersTest variables = prepareEnvironmentFour(14008, 14009);
        Router Router1 = variables.router1();
        Router Router2 = variables.router2();
        Router Router3 = variables.router3();
        Router Router4 = variables.router4();

        Semaphore semaphore1 = variables.semaphore1();
        Semaphore semaphore3 = variables.semaphore3();
        Semaphore semaphore4 = variables.semaphore4();

        DummyMessageParser dummyMessageParser = (DummyMessageParser) variables.messageParser();

        DummyMessage2 message2 = new DummyMessage2("TESTMESSAGETOALL");
        message2.setReceiver(-1);
        message2.setSender(2);
        dummyMessageParser.addParse(message2.serialize(), message2);
        DummyMessage2 message1 = (DummyMessage2) message2.clone(1);
        dummyMessageParser.addParse(message1.serialize(), message1);
        DummyMessage2 message3 = (DummyMessage2) message2.clone(3);
        dummyMessageParser.addParse(message3.serialize(), message3);
        DummyMessage2 message4 = (DummyMessage2) message2.clone(4);
        dummyMessageParser.addParse(message4.serialize(), message4);

        Router2.send(message2);
        semaphore1.acquire();
        semaphore3.acquire();
        semaphore4.acquire();
        semaphore1.tryAcquire(100, TimeUnit.MILLISECONDS);
        semaphore3.tryAcquire(100, TimeUnit.MILLISECONDS);
        semaphore4.tryAcquire(100, TimeUnit.MILLISECONDS);
        assertEquals(message1, Router1.getMessage().get(0));
        assertEquals(message3, Router3.getMessage().get(0));
        assertEquals(message4, Router4.getMessage().get(0));
    }

    private Prepare3RoutersTest prepareEnvironment(int port) throws InterruptedException {
        DummyMessageQueue messageQueue1 = new DummyMessageQueue();
        DummyMessageQueue messageQueue2 = new DummyMessageQueue();
        DummyMessageQueue messageQueue3 = new DummyMessageQueue();

        Semaphore semaphore1 = new Semaphore(1);
        Semaphore semaphore2 = new Semaphore(1);
        Semaphore semaphore3 = new Semaphore(1);
        messageQueue1.addSemaphore(semaphore2);
        messageQueue2.addSemaphore(semaphore2);
        messageQueue3.addSemaphore(semaphore3);

        InetSocketAddress ip2 = new InetSocketAddress("localhost", port);
        DummyMessageParser dummyMessageParser = new DummyMessageParser();
        ConnectionManager connectionManager1 = new ConnectionManager(messageQueue1, dummyMessageParser);
        ConnectionManager connectionManager2 = new ConnectionManager(messageQueue2, dummyMessageParser, ip2);
        ConnectionManager connectionManager3 = new ConnectionManager(messageQueue3, dummyMessageParser);
        connectionManager1.start();
        connectionManager2.start();
        connectionManager3.start();

        RoutingTable table1 = new RoutingTableImpl();
        RoutingTable table2 = new RoutingTableImpl();
        RoutingTable table3 = new RoutingTableImpl();

        PrivateRouter Router1 = new PrivateRouter(connectionManager1, messageQueue1, table1);
        PublicRouter Router2 = new PublicRouter(connectionManager2, messageQueue2, table2);
        PrivateRouter Router3 = new PrivateRouter(connectionManager3, messageQueue3, table3);
        Router1.setId(1);
        Router2.setId(2);
        Router3.setId(3);

        Router1.createInterface(2, ip2);
        Router3.createInterface(2, ip2);
        DummyMessage welcome_message1 = new DummyMessage("TEST");
        welcome_message1.setSender(1);
        welcome_message1.setReceiver(2);
        dummyMessageParser.addParse(welcome_message1.serialize(), welcome_message1);
        Router1.send(welcome_message1);
        DummyMessage welcome_message2 = new DummyMessage("test2");
        welcome_message2.setReceiver(2);
        welcome_message2.setSender(3);
        dummyMessageParser.addParse(welcome_message2.serialize(), welcome_message2);
        Router3.send(welcome_message2);
        semaphore2.acquire();
        semaphore2.tryAcquire(1000, TimeUnit.MILLISECONDS);
        if(Router2.getMessage().size() == 1) {
            semaphore2.tryAcquire(1000, TimeUnit.MILLISECONDS);
            Router2.getMessage();
        }

        Router2.createInterface(1);
        Router2.createInterface(3);
        Router1.createInterface(3);
        Router3.createInterface(1);
        return new Prepare3RoutersTest(Router1, Router2, Router3, semaphore1, semaphore2, semaphore3, dummyMessageParser, messageQueue2);
    }

    private Prepare4RoutersTest prepareEnvironmentFour(int port2, int port4) throws InterruptedException {
        Prepare3RoutersTest variables = prepareEnvironment(port2);
        DummyMessageParser dummyMessageParser = (DummyMessageParser) variables.messageParser();
        Router Router1 = variables.router1();
        Router Router2 = variables.router2();
        Router Router3 = variables.router3();
        Semaphore semaphore1 = variables.semaphore1();
        Semaphore semaphore2 = variables.semaphore2();
        Semaphore semaphore3 = variables.semaphore3();
        DummyMessageQueue messageQueue2 = (DummyMessageQueue) variables.messageQueue();

        DummyMessageQueue messageQueue4 = new DummyMessageQueue();
        Semaphore semaphore4 = new Semaphore(1);
        Semaphore semaphore5 = new Semaphore(1);
        messageQueue4.addSemaphore(semaphore4);
        messageQueue4.addSecondSemaphore(semaphore5);
        messageQueue2.addSecondSemaphore(semaphore5);
        InetSocketAddress ip4 = new InetSocketAddress("localhost", port4);
        ConnectionManager connectionManager4 = new ConnectionManager(messageQueue4, dummyMessageParser, ip4);
        connectionManager4.start();
        RoutingTable table4 = new RoutingTableImpl();
        PublicRouter Router4 = new PublicRouter(connectionManager4, messageQueue4, table4);
        Router4.setId(4);

        Router1.createInterface(4, ip4);
        Router2.createInterface(4, ip4);
        Router3.createInterface(4, ip4);
        DummyMessage welcome_message1 = new DummyMessage("TEST14");
        welcome_message1.setSender(1);
        welcome_message1.setReceiver(4);
        dummyMessageParser.addParse(welcome_message1.serialize(), welcome_message1);
        Router1.send(welcome_message1);
        DummyMessage welcome_message2 = new DummyMessage("test34");
        welcome_message2.setReceiver(4);
        welcome_message2.setSender(3);
        dummyMessageParser.addParse(welcome_message2.serialize(), welcome_message2);
        Router3.send(welcome_message2);
        DummyMessage welcome_message3 = new DummyMessage("test24");
        welcome_message3.setReceiver(4);
        welcome_message3.setSender(2);
        dummyMessageParser.addParse(welcome_message3.serialize(), welcome_message3);
        Router2.send(welcome_message3);
        semaphore4.acquire();
        int size = 0;
        while(size < 3)
        {
            semaphore4.tryAcquire(1000, TimeUnit.MILLISECONDS);
            size += Router4.getMessage().size();
        }
        Router4.createInterface(1);
        Router4.createInterface(2);
        Router4.createInterface(3);
        semaphore1.drainPermits();
        semaphore2.drainPermits();
        semaphore3.drainPermits();
        semaphore4.drainPermits();
        semaphore5.drainPermits();

        semaphore1.release();
        semaphore2.release();
        semaphore3.release();
        semaphore4.release();
        semaphore5.release();

        return new Prepare4RoutersTest(Router1, Router2, Router3, Router4, semaphore1, semaphore2, semaphore3,
                semaphore4, semaphore5, dummyMessageParser);
    }
}

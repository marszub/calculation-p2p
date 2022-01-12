package pl.edu.agh.calculationp2p.network;

import org.junit.jupiter.api.Test;
import pl.edu.agh.calculationp2p.message.Message;
import pl.edu.agh.calculationp2p.network.connection.ConnectionManager;
import pl.edu.agh.calculationp2p.network.messagequeue.MessageQueue;
import pl.edu.agh.calculationp2p.network.router.*;
import pl.edu.agh.calculationp2p.network.utilities.*;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NetworkIntegrationTests {

    @Test
    void checkIfMessagesSendFromPublicRouterToPublicRouter() throws InterruptedException {
        MessageQueue messageQueue1 = new MessageQueue();
        MessageQueue messageQueue2 = new MessageQueue();

        Semaphore semaphore = new Semaphore(1);
        DummyInterrupter interrupter1 = new DummyInterrupter();
        DummyInterrupter interrupter2 = new DummyInterrupter();
        interrupter2.addSemaphore(semaphore);

        InetSocketAddress ip1 = new InetSocketAddress("localhost", 50000);
        InetSocketAddress ip2 = new InetSocketAddress("localhost", 50001);
        DummyMessageParser dummyMessageParser = new DummyMessageParser();
        ConnectionManager connectionManager1 = new ConnectionManager(messageQueue1, dummyMessageParser, ip1, interrupter1);
        ConnectionManager connectionManager2 = new ConnectionManager(messageQueue2, dummyMessageParser, ip2, interrupter2);
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
        semaphore.acquire();
        semaphore.tryAcquire(1000, TimeUnit.MILLISECONDS);
        assertEquals(message, publicRouter2.getMessage().get(0));
        publicRouter1.close();
        publicRouter2.close();
    }

    @Test
    void checkIfMessagesSendFromPrivateRouterToPublicRouter() throws InterruptedException {
        MessageQueue messageQueue1 = new MessageQueue();
        MessageQueue messageQueue2 = new MessageQueue();

        Semaphore semaphore = new Semaphore(1);
        semaphore.acquire();
        DummyInterrupter interrupter1 = new DummyInterrupter();
        DummyInterrupter interrupter2= new DummyInterrupter();

        interrupter2.addSemaphore(semaphore);

        InetSocketAddress ip2 = new InetSocketAddress("localhost", 50000);
        DummyMessageParser dummyMessageParser = new DummyMessageParser();
        ConnectionManager connectionManager1 = new ConnectionManager(messageQueue1, dummyMessageParser, interrupter1);
        ConnectionManager connectionManager2 = new ConnectionManager(messageQueue2, dummyMessageParser, ip2,interrupter2);
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
        semaphore.tryAcquire(1000, TimeUnit.MILLISECONDS);
        assertEquals(message, Router2.getMessage().get(0));
        Router1.close();
        Router2.close();
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

        InetSocketAddress ip2 = new InetSocketAddress("localhost", 50000);
        DummyMessageParser dummyMessageParser = new DummyMessageParser();
        ConnectionManager connectionManager1 = new ConnectionManager(messageQueue1, dummyMessageParser, new DummyInterrupter());
        ConnectionManager connectionManager2 = new ConnectionManager(messageQueue2, dummyMessageParser, ip2, new DummyInterrupter());
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
        semaphore.tryAcquire(1000, TimeUnit.MILLISECONDS);
        Router2.getMessage();

        Router2.createInterface(1);
        DummyMessage message2 = new DummyMessage("SECONDMESSAGE");
        message2.setSender(2);
        message2.setReceiver(1);
        dummyMessageParser.addParse(message2.serialize(), message2);
        Router2.send(message2);
        semaphore2.tryAcquire(1000, TimeUnit.MILLISECONDS);
        assertEquals(message2, Router1.getMessage().get(0));
        Router1.close();
        Router2.close();
    }

    @Test
    void checkIfMessagesSentFromPrivateRouterToPrivateRouterViaPublicRouter() throws InterruptedException {
        Prepare3RoutersTest variables = prepareEnvironment(50000);
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
        semaphore2.tryAcquire(1000, TimeUnit.MILLISECONDS);
        Router2.getMessage();
        semaphore3.acquire();
        semaphore3.tryAcquire(1000, TimeUnit.MILLISECONDS);
        assertEquals(message, Router3.getMessage().get(0));
        Router1.close();
        Router2.close();
        Router3.close();
    }

    @Test
    void checkIfMessagesSentFromPrivateToAllArrivesToAll() throws InterruptedException {
        Prepare3RoutersTest variables = prepareEnvironment(50000);
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
        semaphore2.tryAcquire(1000, TimeUnit.MILLISECONDS);
        assertEquals(message2.serialize(), Router2.getMessage().get(0).serialize());
        semaphore3.acquire();
        semaphore3.tryAcquire(1000, TimeUnit.MILLISECONDS);
        assertEquals(message3, Router3.getMessage().get(0));
        Router1.close();
        Router2.close();
        Router3.close();
    }

    @Test
    void checkIfMessagesSentFromPublicToAllArrivesToAll() throws InterruptedException {
        Prepare3RoutersTest variables = prepareEnvironment(50000);
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
        semaphore3.tryAcquire(1000, TimeUnit.MILLISECONDS);
        assertEquals(message3, Router3.getMessage().get(0));
        semaphore1.tryAcquire(1000, TimeUnit.MILLISECONDS);
        assertEquals(message1, Router1.getMessage().get(0));
        Router1.close();
        Router2.close();
        Router3.close();
    }

    @Test
    void ultimateRouterTestWith4Routers2Public2PrivateMessageToAllPrivate() throws InterruptedException {
        Prepare4RoutersTest variables = prepareEnvironmentFour(50000, 50001);
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
        semaphore5.tryAcquire(1000, TimeUnit.MILLISECONDS);
        List<Message> list2 = Router2.getMessage();
        List<Message> list4 = Router4.getMessage();
        if(list2.size() > 0)
        {
            if(list4.size() > 0)
            {
                assertEquals(message4.serialize(), list4.get(0).serialize());
                semaphore3.tryAcquire(1000, TimeUnit.MILLISECONDS);
                assertEquals(message3, Router3.getMessage().get(0));
            }
            else
            {
                assertEquals(message2.serialize(), list2.get(0).serialize());
                semaphore4.tryAcquire(1000, TimeUnit.MILLISECONDS);
                semaphore3.tryAcquire(1000, TimeUnit.MILLISECONDS);
                assertEquals(message3, Router3.getMessage().get(0));
                assertEquals(message4, Router4.getMessage().get(0));
            }
        }
        else
        {
            assertEquals(message4.serialize(), list4.get(0).serialize());
            semaphore2.tryAcquire(1000, TimeUnit.MILLISECONDS);
            semaphore3.tryAcquire(1000, TimeUnit.MILLISECONDS);
            assertEquals(message3, Router3.getMessage().get(0));
            assertEquals(message2, Router2.getMessage().get(0));
        }
        Router1.close();
        Router2.close();
        Router3.close();
        Router4.close();
    }

    @Test
    void ultimateRouterTestWith4Routers2Public2PrivateMessageToAllPublic() throws InterruptedException {
        Prepare4RoutersTest variables = prepareEnvironmentFour(50000, 50001);
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
        semaphore1.tryAcquire(1000, TimeUnit.MILLISECONDS);
        semaphore3.tryAcquire(1000, TimeUnit.MILLISECONDS);
        semaphore4.tryAcquire(1000, TimeUnit.MILLISECONDS);
        assertEquals(message1, Router1.getMessage().get(0));
        assertEquals(message3, Router3.getMessage().get(0));
        assertEquals(message4, Router4.getMessage().get(0));
        Router1.close();
        Router2.close();
        Router3.close();
        Router4.close();
    }

    private Prepare3RoutersTest prepareEnvironment(int port) throws InterruptedException {
        MessageQueue messageQueue1 = new MessageQueue();
        MessageQueue messageQueue2 = new MessageQueue();
        MessageQueue messageQueue3 = new MessageQueue();

        Semaphore semaphore1 = new Semaphore(1);
        Semaphore semaphore2 = new Semaphore(1);
        Semaphore semaphore3 = new Semaphore(1);
        DummyInterrupter idleInterrupter1 = new DummyInterrupter();
        DummyInterrupter idleInterrupter2 = new DummyInterrupter();
        DummyInterrupter idleInterrupter3 = new DummyInterrupter();
        idleInterrupter1.addSemaphore(semaphore1);
        idleInterrupter2.addSemaphore(semaphore2);
        idleInterrupter3.addSemaphore(semaphore3);

        InetSocketAddress ip2 = new InetSocketAddress("localhost", port);
        DummyMessageParser dummyMessageParser = new DummyMessageParser();
        ConnectionManager connectionManager1 = new ConnectionManager(messageQueue1, dummyMessageParser, idleInterrupter1);
        ConnectionManager connectionManager2 = new ConnectionManager(messageQueue2, dummyMessageParser, ip2, idleInterrupter2);
        ConnectionManager connectionManager3 = new ConnectionManager(messageQueue3, dummyMessageParser, idleInterrupter3);
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
        semaphore1.drainPermits();
        semaphore2.drainPermits();
        semaphore3.drainPermits();

        semaphore1.release();
        semaphore2.release();
        semaphore3.release();
        return new Prepare3RoutersTest(Router1, Router2, Router3, semaphore1, semaphore2, semaphore3, dummyMessageParser, idleInterrupter2);
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
        DummyInterrupter idleInterrupter2 = (DummyInterrupter) variables.interrupter();

        MessageQueue messageQueue4 = new MessageQueue();
        DummyInterrupter idleInterrupter4 = new DummyInterrupter();
        Semaphore semaphore4 = new Semaphore(1);
        Semaphore semaphore5 = new Semaphore(1);
        idleInterrupter4.addSemaphore(semaphore4);
        idleInterrupter4.addSecondSemaphore(semaphore5);
        idleInterrupter2.addSecondSemaphore(semaphore5);
        InetSocketAddress ip4 = new InetSocketAddress("localhost", port4);
        ConnectionManager connectionManager4 = new ConnectionManager(messageQueue4, dummyMessageParser, ip4, idleInterrupter4);
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

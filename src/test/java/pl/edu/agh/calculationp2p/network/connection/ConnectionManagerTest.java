package pl.edu.agh.calculationp2p.network.connection;

import org.junit.jupiter.api.Test;
import pl.edu.agh.calculationp2p.message.Message;
import pl.edu.agh.calculationp2p.network.messagequeue.MessageConnectionPair;
import pl.edu.agh.calculationp2p.network.utilities.DummyInterrupter;
import pl.edu.agh.calculationp2p.network.utilities.DummyMessage;
import pl.edu.agh.calculationp2p.network.utilities.DummyMessageParser;
import pl.edu.agh.calculationp2p.network.utilities.DummyMessageQueue;

import java.net.InetSocketAddress;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;


class ConnectionManagerTest {
    @Test
    void checkIfConnectionManagerReceivesMessages()
    {
        DummyMessageQueue queue = new DummyMessageQueue();
        DummyMessageParser messageParser = new DummyMessageParser();
        ConnectionManager connectionManager = new ConnectionManager(
                queue,
                messageParser,
                new InetSocketAddress("localhost", 49152),
                new DummyInterrupter()
        );
        Semaphore semaphore = new Semaphore(1);
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        queue.addSemaphore(semaphore);
        connectionManager.start();
        DummyMessage message = new DummyMessage();
        message.setText("TEST1");
        messageParser.addParse(message.serialize(), message);
        sendMessageToServer(new InetSocketAddress("localhost", 49152), message);
        try {
            semaphore.tryAcquire(100, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(message, queue.getList().pop().message());
    }

    @Test
    void sendMultipleMessages() throws InterruptedException {
        DummyMessageQueue queue = new DummyMessageQueue();
        DummyMessageParser messageParser = new DummyMessageParser();
        ConnectionManager connectionManager = new ConnectionManager(
                queue,
                messageParser,
                new InetSocketAddress("localhost", 49153),
                new DummyInterrupter()
        );
        Semaphore semaphore = new Semaphore(1);
        semaphore.acquire();
        queue.addSemaphore(semaphore);
        connectionManager.start();
        DummyMessage message = new DummyMessage();
        message.setText("TEST1");
        messageParser.addParse(message.serialize(), message);
        sendMessageToServer(new InetSocketAddress("localhost", 49153), message);
        semaphore.tryAcquire(100, TimeUnit.MILLISECONDS);
        assertEquals(message, queue.getList().pop().message());
        sendMessageToServer(new InetSocketAddress("localhost", 49153), message);
        semaphore.tryAcquire(100, TimeUnit.MILLISECONDS);
        assertEquals(message, queue.getList().pop().message());
    }

    @Test
    void checkIfPrivateConnectionManagerReceivesMessage() throws InterruptedException {
        DummyMessageQueue queue = new DummyMessageQueue();
        DummyMessageQueue queue2 = new DummyMessageQueue();
        DummyMessageParser messageParser = new DummyMessageParser();
        InetSocketAddress ip = new InetSocketAddress("localhost", 49154);
        ConnectionManager connectionManager1 = new ConnectionManager(queue,messageParser, ip, new DummyInterrupter());
        ConnectionManager connectionManager2 = new ConnectionManager(queue2, messageParser, new DummyInterrupter());
        Semaphore semaphore = new Semaphore(1);
        Semaphore semaphore2 = new Semaphore(1);
        semaphore.acquire();
        semaphore2.acquire();
        queue.addSemaphore(semaphore);
        queue2.addSemaphore(semaphore2);
        connectionManager1.start();
        connectionManager2.start();
        DummyMessage message = new DummyMessage();
        message.setText("TEST1");
        messageParser.addParse(message.serialize(), message);
        StaticConnection connection = new StaticConnection(ip);
        connectionManager2.addStaticConnection(connection);
        connection.send(message);
        semaphore.tryAcquire(100, TimeUnit.MILLISECONDS);
        MessageConnectionPair pair = queue.getList().pop();
        assertEquals(message, pair.message());
        DummyMessage message2 = new DummyMessage();
        message2.setText("TEST2");
        messageParser.addParse(message2.serialize(), message2);
        pair.connection().send(message2);
        semaphore2.tryAcquire(100, TimeUnit.MILLISECONDS);
        assertEquals(message2, queue2.getList().pop().message());
    }

    private void sendMessageToServer(InetSocketAddress ip, Message message)
    {
        StaticConnection serverConnection = new StaticConnection(ip);
        serverConnection.send(message);
    }
}

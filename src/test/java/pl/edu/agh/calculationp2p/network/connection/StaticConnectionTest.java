package pl.edu.agh.calculationp2p.network.connection;

import org.junit.jupiter.api.Test;
import pl.edu.agh.calculationp2p.message.Message;
import pl.edu.agh.calculationp2p.network.utilities.DummyMessage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.*;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

public class StaticConnectionTest
{
    @Test
    void testIfConnectionWillKeepItselfAliveAfterDisconnectRead()
    {
        DummyMessage message = new DummyMessage("TESTMESSAGE");
        InetSocketAddress ip1 = new InetSocketAddress("localhost", 49000);
        InetSocketAddress ip2 = new InetSocketAddress("localhost", 49001);
        Selector selector1 = createServer(ip1);
        Selector selector2 = createServer(ip2);
        StaticConnection connection = new StaticConnection(ip2);
        try {
            connection.register(selector1);
        } catch (ClosedChannelException e) {
            e.printStackTrace();
        }
        connection.send(message);
        DynamicConnection dynamicConnection = addNewConnection(selector2);
        dynamicConnection.close();
        getMessage(selector1);
        dynamicConnection = addNewConnection(selector2);
        dynamicConnection.send(message);
        assertEquals(message.serialize(), getMessage(selector1)[0]);
    }

    @Test
    void checkIfStaticConnectionReadsProperly() throws ClosedChannelException
    {
        DummyMessage message = new DummyMessage("TESTMESSAGE");
        DummyMessage message2 = new DummyMessage("TESTMESSAGE2");
        InetSocketAddress ip1 = new InetSocketAddress("localhost", 50004);
        InetSocketAddress ip2 = new InetSocketAddress("localhost", 50005);
        Selector selector1 = createServer(ip1);
        Selector selector2 = createServer(ip2);
        StaticConnection connection = new StaticConnection(ip2);
        connection.send(message);
        DynamicConnection dynamicConnection = addNewConnection(selector2);
        connection.register(selector1);
        dynamicConnection.send(message2);
        assertEquals(message2.serialize(), getMessage(selector1)[0]);
    }

    private void sendMessageToServer(InetSocketAddress ip, Message message)
    {
        StaticConnection serverConnection = new StaticConnection(ip);
        serverConnection.send(message);
    }

    private Selector createServer(InetSocketAddress ip)
    {
        Selector selector = null;
        ServerSocketChannel serverSocketChannel = null;
        try {
            selector = Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try
        {
            serverSocketChannel = ServerSocketChannel.open();
            ServerSocket serverSocket = serverSocketChannel.socket();
            serverSocket.bind(ip);
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT, serverSocketChannel);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return selector;
    }


    private DynamicConnection addNewConnection(Selector selector)
    {
        try {
            selector.select();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
        while (keys.hasNext()) {
            SelectionKey key = keys.next();
            keys.remove();
            assertTrue(key.isAcceptable());
            ServerSocketChannel server = (ServerSocketChannel) key.channel();
            try {
                SocketChannel socket = server.accept();
                socket.configureBlocking(false);
                DynamicConnection connection = new DynamicConnection(socket);
                connection.register(selector);
                return connection;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private String[] getMessage(Selector selector) {
        try {
            selector.select();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
        while (keys.hasNext()) {
            SelectionKey key = keys.next();
            keys.remove();
            assertTrue(key.isReadable());
            Connection connection = (Connection) key.attachment();
            try {
                return connection.read();
            } catch (ConnectionLostException e) {
                e.printStackTrace();
            }
        }
        return new String[0];
    }
}
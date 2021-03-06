package pl.edu.agh.calculationp2p.network.connection;

import org.junit.jupiter.api.Test;
import pl.edu.agh.calculationp2p.network.utilities.DummyMessage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StaticConnectionTest
{

    @Test
    void checkIfStaticConnectionReadsProperly() throws IOException
    {
        DummyMessage message = new DummyMessage("TESTMESSAGE");
        DummyMessage message2 = new DummyMessage("TESTMESSAGE2");
        InetSocketAddress ip1 = new InetSocketAddress("localhost", 50000);
        InetSocketAddress ip2 = new InetSocketAddress("localhost", 50001);
        SelectorServerPair result1 = createServer(ip1);
        SelectorServerPair result2 = createServer(ip2);
        Selector selector1 = result1.selector();
        Selector selector2 = result2.selector();
        StaticConnection connection = new StaticConnection(ip2);
        connection.send(message);
        DynamicConnection dynamicConnection = addNewConnection(selector2);
        connection.register(selector1);
        dynamicConnection.send(message2);
        assertEquals(message2.serialize(), getMessage(selector1).get(0));
        selector1.close();
        selector2.close();
        connection.close();
        dynamicConnection.close();
        result1.server().close();
        result2.server().close();
    }

    private SelectorServerPair createServer(InetSocketAddress ip)
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new SelectorServerPair(selector, serverSocketChannel);
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

    private List<String> getMessage(Selector selector) {
        try {
            selector.select();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
        List messages = new LinkedList<>();
        while (keys.hasNext()) {
            SelectionKey key = keys.next();
            keys.remove();
            assertTrue(key.isReadable());
            Connection connection = (Connection) key.attachment();
            try {
                connection.read(messages);
                return messages;
            } catch (ConnectionLostException e) {
                e.printStackTrace();
            }
        }
        return messages;
    }
}

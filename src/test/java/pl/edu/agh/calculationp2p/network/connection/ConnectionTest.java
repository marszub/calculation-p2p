package pl.edu.agh.calculationp2p.network.connection;

import org.junit.jupiter.api.Test;
import pl.edu.agh.calculationp2p.message.Message;
import pl.edu.agh.calculationp2p.network.utilities.DummyMessage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConnectionTest {

    @Test
    void checkIfMessageSendsProperly() throws IOException {
        InetSocketAddress ip = new InetSocketAddress("localhost", 48000);
        SelectorServerPair result = createServer(ip);
        Selector selector = result.selector();
        DummyMessage msg = new DummyMessage();
        msg.setText("TEXT");
        sendMessageToServer(ip, msg);
        DynamicConnection conn = addNewConnection(selector);
        assertEquals(msg.serialize(), getMessage(selector)[0]);
        conn.close();
        selector.close();
        result.server().close();
    }

    @Test
    void checkIfMultipleMessagesSendsProperly() throws IOException {
        InetSocketAddress ip = new InetSocketAddress("localhost", 48001);
        SelectorServerPair result = createServer(ip);
        Selector selector = result.selector();
        DummyMessage msg = new DummyMessage();
        msg.setText("TEXT");
        StaticConnection serverConnection = new StaticConnection(ip);
        DynamicConnection conn = addNewConnection(selector);
        serverConnection.send(msg);
        serverConnection.send(msg);
        String[] tab = getMessage(selector);
        assertEquals(msg.serialize(), tab[0]);
        assertEquals(msg.serialize(), tab[1]);
        conn.close();
        selector.close();
        result.server().close();
    }

    private void sendMessageToServer(InetSocketAddress ip, Message message)
    {
        StaticConnection serverConnection = new StaticConnection(ip);
        serverConnection.send(message);
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
        } catch (IOException e)
        {
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
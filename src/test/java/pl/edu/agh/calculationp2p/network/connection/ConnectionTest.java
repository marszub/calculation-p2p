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
    void checkIfMessageSendsProperly()
    {
        InetSocketAddress ip = new InetSocketAddress("localhost", 49151);
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
        Message msg = new DummyMessage();

        new Thread(){
            public void run(){
                sendMessageToServer(ip, msg);
            }
        }.start();

        SocketChannel socket = null;
        ServerSocketChannel server = null;
        try {
            assert selector != null;
            selector.select();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
        while (keys.hasNext()) {
            SelectionKey key = keys.next();
            keys.remove();
            assertTrue(key.isAcceptable());
            server = (ServerSocketChannel) key.channel();
            try {
                assertEquals(server, serverSocketChannel);
                socket = server.accept();
                socket.configureBlocking(false);
                DynamicConnection connection = new DynamicConnection(socket);
                connection.register(selector, SelectionKey.OP_READ);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            selector.select();
        } catch (IOException e) {
            e.printStackTrace();
        }
        keys = selector.selectedKeys().iterator();
        while (keys.hasNext()) {
            SelectionKey key = keys.next();
            keys.remove();
            assertTrue(key.isReadable());
            Connection connection = (Connection) key.attachment();
            try {
                assertEquals(msg.serialize(), connection.read());
            } catch (ConnectionLostException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendMessageToServer(InetSocketAddress ip, Message message)
    {
        StaticConnection serverConnection = new StaticConnection(ip);
        serverConnection.send(message);
    }
}
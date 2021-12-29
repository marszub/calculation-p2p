package pl.edu.agh.calculationp2p.network.connection;

import pl.edu.agh.calculationp2p.message.MessageParser;
import pl.edu.agh.calculationp2p.network.messagequeue.MessageConnectionPair;
import pl.edu.agh.calculationp2p.network.messagequeue.MessageQueueEntry;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ConnectionManager extends Thread {
    private final MessageQueueEntry messageQueueEntry;
    private final List<Connection> incomingConnections = new ArrayList<>();
    private final List<Connection> outgoingConnections = new ArrayList<>();
    private Selector selector;
    private final MessageParser messageParser;

    public ConnectionManager(MessageQueueEntry messageQueueEntry, MessageParser messageParser, InetSocketAddress localListeningAddress) {
        this.messageParser = messageParser;
        this.messageQueueEntry = messageQueueEntry;
        createServerSelector();
        createServerSocket(localListeningAddress);
    }

    public ConnectionManager(MessageQueueEntry messageQueueEntry, MessageParser messageParser) {
        this.messageParser = messageParser;
        this.messageQueueEntry = messageQueueEntry;
        createServerSelector();
    }

    public void addStaticConnection(StaticConnection staticConnection) {
        try {
            staticConnection.register(selector, SelectionKey.OP_READ);
            selector.wakeup();
        } catch (ClosedChannelException e) {
            e.printStackTrace();
        }
        if(!outgoingConnections.contains(staticConnection)){
            outgoingConnections.add(staticConnection);
        }
    }

    public void removeStaticConnection(StaticConnection staticConnection) {
        this.outgoingConnections.remove(staticConnection);
        staticConnection.disconnect();
    }

    public void run() {
        while (true) {
            try {
                selector.select();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
            while (keys.hasNext())
            {
                SelectionKey key = keys.next();
                keys.remove();
                decideKeyPath(key);
            }
        }
    }

    //##################################################################################################################
    //                                                 PRIVATE FUNCTIONS
    //##################################################################################################################

    private void createServerSelector()
    {
        try {
            this.selector = Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createServerSocket(InetSocketAddress localListeningAddress)
    {
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            ServerSocket serverSocket = serverSocketChannel.socket();
            serverSocket.bind(localListeningAddress);
            serverSocketChannel.configureBlocking(false);
            Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT, serverSocketChannel);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void decideKeyPath(SelectionKey key)
    {
        if (key.isAcceptable()) {
            handleNewConnection(key);
        } else if (key.isReadable()) {
            Connection connection = (Connection) key.attachment();
            try {
                String message = connection.read();
                messageQueueEntry.add(new MessageConnectionPair(messageParser.parse(message), connection));
            } catch (ConnectionLostException e) {
                incomingConnections.remove(connection);
            }
        }
    }

    private void handleNewConnection(SelectionKey key) {
        ServerSocketChannel server = (ServerSocketChannel) key.channel();
        try {
            SocketChannel connection = server.accept();
            connection.configureBlocking(false);
            DynamicConnection dynamicConnection = new DynamicConnection(connection);
            dynamicConnection.register(selector, SelectionKey.OP_READ);
            incomingConnections.add(dynamicConnection);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 }
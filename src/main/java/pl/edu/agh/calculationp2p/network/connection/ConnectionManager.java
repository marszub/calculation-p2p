package pl.edu.agh.calculationp2p.network.connection;


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
    private final List<DynamicConnection> incomingConnections = new ArrayList<>();
    private final List<StaticConnection> outgoingConnections = new ArrayList<>();
    private Selector incomingConnectionOrMessage;

    public ConnectionManager(MessageQueueEntry messageQueueEntry, InetSocketAddress localListeningAddress) {
        this.messageQueueEntry = messageQueueEntry;
        try {
            this.incomingConnectionOrMessage = Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            ServerSocket serverSocket = serverSocketChannel.socket();
            serverSocket.bind(localListeningAddress);
            serverSocketChannel.configureBlocking(false);
            Selector.open();
            serverSocketChannel.register(incomingConnectionOrMessage, SelectionKey.OP_ACCEPT, serverSocketChannel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addStaticConnection(StaticConnection staticConnection) {
        try {
            staticConnection.register(incomingConnectionOrMessage, SelectionKey.OP_READ);
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
                incomingConnectionOrMessage.select();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Iterator<SelectionKey> keys = incomingConnectionOrMessage.selectedKeys().iterator();
            while (keys.hasNext()) {
                SelectionKey key = keys.next();
                keys.remove();
                if (key.isAcceptable()) {
                    handleNewConnection(key);
                } else if (key.isReadable()) {
                    Connection connection = (Connection) key.attachment();
                    try {
                        //TODO INSERT TO QUEUE, after message parser
                        connection.read();
                    }catch(ConnectionLostException e)
                    {
                        if(outgoingConnections.contains(connection)) {
                            StaticConnection staticConnection = (StaticConnection) connection;
                            staticConnection.reconnect();
                        }
                        else
                        {
                            incomingConnections.remove(connection);
                        }
                    }
                }
            }
        }
    }

    private void handleNewConnection(SelectionKey key) {
        ServerSocketChannel server = (ServerSocketChannel) key.channel();
        try {
            SocketChannel connection = server.accept();
            connection.configureBlocking(false);
            DynamicConnection dynamicConnection = new DynamicConnection(connection);
            dynamicConnection.register(incomingConnectionOrMessage, SelectionKey.OP_READ);
            incomingConnections.add(dynamicConnection);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 }
package pl.edu.agh.calculationp2p.network.connection;

import pl.edu.agh.calculationp2p.message.MessageParser;
import pl.edu.agh.calculationp2p.network.messagequeue.MessageConnectionPair;
import pl.edu.agh.calculationp2p.network.messagequeue.MessageQueueEntry;
import pl.edu.agh.calculationp2p.state.idle.IdleInterrupter;

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
    private final IdleInterrupter interrupter;
    private boolean endRunning = false;
    private ServerSocketChannel server = null;

    public ConnectionManager(MessageQueueEntry messageQueueEntry, MessageParser messageParser,
                             InetSocketAddress localListeningAddress, IdleInterrupter interrupter) {
        this.interrupter = interrupter;
        this.messageParser = messageParser;
        this.messageQueueEntry = messageQueueEntry;
        createServerSelector();
        createServerSocket(localListeningAddress);
    }

    public ConnectionManager(MessageQueueEntry messageQueueEntry, MessageParser messageParser, IdleInterrupter interrupter) {
        this.interrupter = interrupter;
        this.messageParser = messageParser;
        this.messageQueueEntry = messageQueueEntry;
        createServerSelector();
    }

    public void addStaticConnection(StaticConnection staticConnection) {
        try {
            staticConnection.register(selector);
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

    public void run()
    {
        while (true)
        {
            try {
                if(!endRunning) {
                    selector.select();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(endRunning)
            {
                clear();
                return;
            }
            Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
            boolean messageRead = false;
            while (keys.hasNext())
            {
                SelectionKey key = keys.next();
                keys.remove();
                boolean result = decideKeyPath(key);
                if(result)
                {
                    messageRead = true;
                }
            }
            if(messageRead)
            {
                interrupter.wake();
            }
        }
    }

    private void clear()
    {
        try
        {
            selector.close();
            for(Connection connection : incomingConnections)
                connection.close();
            for(Connection connection : outgoingConnections)
                connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close()
    {
        endRunning = true;
        selector.wakeup();
        if(server != null)
        {
            try
            {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
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
            server = ServerSocketChannel.open();
            ServerSocket serverSocket = server.socket();
            serverSocket.bind(localListeningAddress);
            server.configureBlocking(false);
            server.register(selector, SelectionKey.OP_ACCEPT, server);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private boolean decideKeyPath(SelectionKey key)
    {
        if (key.isAcceptable())
        {
            handleNewConnection(key);
            return false;
        }
        if (key.isReadable())
        {
            Connection connection = (Connection) key.attachment();
            try {
                String[] messages = connection.read();
                for(String message : messages)
                {
                    messageQueueEntry.add(new MessageConnectionPair(messageParser.parse(message), connection));
                }
                return true;
            } catch (ConnectionLostException e) {
                connection.close();
                incomingConnections.remove(connection);
            }
        }
        return false;
    }

    private void handleNewConnection(SelectionKey key) {
        ServerSocketChannel server = (ServerSocketChannel) key.channel();
        try {
            SocketChannel connection = server.accept();
            DynamicConnection dynamicConnection = new DynamicConnection(connection);
            dynamicConnection.register(selector);
            incomingConnections.add(dynamicConnection);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 }

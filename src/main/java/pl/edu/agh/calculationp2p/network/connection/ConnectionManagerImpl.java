package pl.edu.agh.calculationp2p.network.connection;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import pl.edu.agh.calculationp2p.message.Message;
import pl.edu.agh.calculationp2p.message.MessageParser;
import pl.edu.agh.calculationp2p.network.messagequeue.MessageConnectionPair;
import pl.edu.agh.calculationp2p.network.messagequeue.MessageQueueEntry;
import pl.edu.agh.calculationp2p.state.idle.IdleInterrupter;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ConnectionManagerImpl extends Thread implements ConnectionManager {
    private final MessageQueueEntry messageQueueEntry;
    private final List<Connection> incomingConnections = new ArrayList<>();
    private final List<Connection> outgoingConnections = new ArrayList<>();
    private Selector selector;
    private final MessageParser messageParser;
    private final IdleInterrupter interrupter;
    private boolean endRunning = false;
    private ServerSocketChannel server = null;

    public ConnectionManagerImpl(MessageQueueEntry messageQueueEntry, MessageParser messageParser,
                                 InetSocketAddress localListeningAddress, IdleInterrupter interrupter) {
        this.interrupter = interrupter;
        this.messageParser = messageParser;
        this.messageQueueEntry = messageQueueEntry;
        createServerSelector();
        createServerSocket(localListeningAddress);
    }

    public ConnectionManagerImpl(MessageQueueEntry messageQueueEntry, MessageParser messageParser, IdleInterrupter interrupter) {
        this.interrupter = interrupter;
        this.messageParser = messageParser;
        this.messageQueueEntry = messageQueueEntry;
        createServerSelector();
    }

    @Override
    public void addStaticConnection(StaticConnection staticConnection) {
        try {
            staticConnection.register(selector);
            selector.wakeup();
        } catch (ClosedChannelException e) {
            Logger logger = LoggerFactory.getLogger("");
            logger.error(e.getMessage());
        }
        if(!outgoingConnections.contains(staticConnection)){
            outgoingConnections.add(staticConnection);
        }
    }

    @Override
    public void removeStaticConnection(StaticConnection staticConnection) {
        this.outgoingConnections.remove(staticConnection);
        staticConnection.close();
    }

    @Override
    public void run()
    {
        while (true)
        {
            try {
                if(!endRunning) {
                    selector.select();
                }
            } catch (IOException e) {
                Logger logger = LoggerFactory.getLogger("");
                logger.error(e.getMessage());
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
                Logger logger = LoggerFactory.getLogger(ConnectionManager.class);
                logger.debug("ConnectionManager waked up somebody!");
                interrupter.wake();
            }
        }
    }

    private void clear()
    {
        try
        {
            selector.close();
            if(server!=null)
                server.close();
            for(Connection connection : incomingConnections)
                connection.close();
            for(Connection connection : outgoingConnections)
                connection.close();
        } catch (IOException e) {
            Logger logger = LoggerFactory.getLogger("");
            logger.error(e.getMessage());
        }
    }

    @Override
    public void close() {
        endRunning = true;
        selector.wakeup();
        try {
            this.join();
        } catch (InterruptedException e) {
            Logger logger = LoggerFactory.getLogger("");
            logger.error(e.getMessage());
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
            Logger logger = LoggerFactory.getLogger("");
            logger.error(e.getMessage());
        }
    }

    private void createServerSocket(InetSocketAddress localListeningAddress)
    {
        try {
            server = ServerSocketChannel.open();
            server.bind(localListeningAddress);
            server.configureBlocking(false);
            server.register(selector, SelectionKey.OP_ACCEPT, server);
        } catch (IOException e) {
            Logger logger = LoggerFactory.getLogger("");
            logger.error(e.getMessage());
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
            List<String> messages = new LinkedList();
            try {
                connection.read(messages);
            } catch (ConnectionLostException e) {
                connection.close();
                incomingConnections.remove(connection);
            }
            finally
            {
                for(String message : messages)
                {
                    if(!message.equals("")) {
                        Logger logger = LoggerFactory.getLogger("");
                        logger.info("Received message: " + message);
                        Message parsedMessage = messageParser.parse(message);
                        if (parsedMessage != null) {
                            messageQueueEntry.add(new MessageConnectionPair(parsedMessage, connection));
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    private void handleNewConnection(SelectionKey key) {
        ServerSocketChannel server = (ServerSocketChannel) key.channel();
        try {
            Logger logger = LoggerFactory.getLogger(ConnectionManager.class);
            SocketChannel connection = server.accept();
            if(connection != null)
            {
                logger.debug("New not-null connection " + connection.getRemoteAddress().toString());
                DynamicConnection dynamicConnection = new DynamicConnection(connection);
                dynamicConnection.register(selector);
                incomingConnections.add(dynamicConnection);
                return;
            }
            logger.debug("New null connection!");
        } catch (IOException e) {
            Logger logger = LoggerFactory.getLogger("");
            logger.error(e.getMessage());
        }
    }
 }

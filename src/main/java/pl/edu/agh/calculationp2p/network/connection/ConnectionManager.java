package pl.edu.agh.calculationp2p.network.connection;


import pl.edu.agh.calculationp2p.network.messagequeue.MessageQueueEntry;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ConnectionManager extends Thread {

    // TODO czy 2 listy Static/Dynamic? ArrayListy?

    private final MessageQueueEntry messageQueueEntry;
    private final List<DynamicConnection> incomingConnections = new ArrayList<>();
    private final List<StaticConnection> outgoingConnections = new ArrayList<>();
    private Selector incomingConnectionOrMessage;
    private ServerSocketChannel serverSocketChannel;

    private final int someSize = 1024;
    boolean canBind;

    public ConnectionManager(MessageQueueEntry messageQueueEntry, boolean canBind) {
        this.messageQueueEntry = messageQueueEntry;
        try {
            this.incomingConnectionOrMessage = Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.canBind = canBind;
    }

    public void addStaticConnection(StaticConnection staticConnection) {
        try {
            SocketChannel socketChannel = SocketChannel.open(staticConnection.getIpAddress());
            removeFromSelector(socketChannel);
            socketChannel.configureBlocking(false);
            SelectionKey key = socketChannel.register(incomingConnectionOrMessage, SelectionKey.OP_READ | SelectionKey.OP_WRITE, null);
            staticConnection.setSelectionKey(key);
            if(!outgoingConnections.contains(staticConnection)){
                this.outgoingConnections.add(staticConnection);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void removeFromSelector(SocketChannel socketChannel) {
        if (socketChannel.isRegistered()) {
            SelectionKey key = socketChannel.keyFor(incomingConnectionOrMessage);
            key.cancel(); // Requests that the registration of this key's channel with its selector be cancelled.
        }
    }


    public void removeStaticConnection(StaticConnection staticConnection) {
        // TODO: wyrejestrowac z selector
        this.outgoingConnections.remove(staticConnection);
        try {
            staticConnection.getSocketChannel().finishConnect(); //Finishes the process of connecting a socket channel.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        try {
            serverSocketChannel = ServerSocketChannel.open(); // Opens a server-socket channel.
            ServerSocket serverSocket = serverSocketChannel.socket(); // Retrieves a server socket associated with this channel.
            serverSocket.bind(new InetSocketAddress(addressIp, port)); //Binds the channel's socket to a local address and configures the socket to listen for connections.
            serverSocketChannel.configureBlocking(false);
            Selector.open(); // incomingConnectionOrMessage.open() -> IntelliJ cleanup
            //int ops = socket.validOps(); //Returns an operation set identifying this channel's supported operations. here: SelectionKey.OP_ACCEPT
            serverSocketChannel.register(incomingConnectionOrMessage, SelectionKey.OP_ACCEPT, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        if (canBind) {
            init();
        }
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

                if (key.isAcceptable()) { // accept connection
                    System.out.println("NEW PEER");
                    addDynamicConnection(key);
                } else if (key.isReadable()) {
                    System.out.println("READING");
                    try {
                        handleRead(key);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    private void handleRead(SelectionKey key) throws IOException {
        // TODO: write msg to queue
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(someSize);
        channel.read(buffer);
        String msg = new String(buffer.array()).trim();
        System.out.println(msg);
        channel.close();
    }


    private void addDynamicConnection(SelectionKey key) {
        ServerSocketChannel server = (ServerSocketChannel) key.channel();
        SocketChannel channel = null;
        try {
            // accepts incoming connection and binds it with the selector
            channel = server.accept();
            channel.configureBlocking(false);
            channel.register(this.incomingConnectionOrMessage, SelectionKey.OP_READ, ByteBuffer.allocate(this.someSize));

            // get random word
            // send random word
            // hash word
            // f00k your mother if hash are different

        } catch (IOException e) {
            e.printStackTrace();
            incomingConnections.add(new DynamicConnection(channel));
        }
    }

}

// ServersocketChannel -> for listening
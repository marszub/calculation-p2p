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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ConnectionManager extends Thread {

    // TODO czy 2 listy Static/Dynamic? ArrayListy?

    private MessageQueueEntry messageQueueEntry;
    private List<DynamicConnection> incomingConnections = new ArrayList<>();
    private List<StaticConnection> outgoingConnections = new ArrayList<>();
    private Selector incomingConnectionOrMessage;
    private final int someSize = 1024;
    boolean canBind;

    public ConnectionManager(MessageQueueEntry messageQueueEntry, boolean canBind){
        this.messageQueueEntry = messageQueueEntry;
        try {
            this.incomingConnectionOrMessage = Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.canBind = canBind;

    }

    public void addStaticConnection(StaticConnection staticConnection){

        try {

            SocketChannel client = SocketChannel.open(staticConnection.getIpAddress());

            if(client.isRegistered()){
                SelectionKey key = client.keyFor(incomingConnectionOrMessage);
                key.cancel(); // Requests that the registration of this key's channel with its selector be cancelled.
            }

            client.configureBlocking(false);
            client.register(incomingConnectionOrMessage, SelectionKey.OP_READ, null);
            this.outgoingConnections.add(staticConnection);
            staticConnection.setSocketChannel(client);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void removeStaticConnection(StaticConnection staticConnection){
        // TODO: wyrejestrowac z selector
        this.outgoingConnections.remove(staticConnection);
    }

    private void init(){
        // TODO: pobrać z pliku conf czy jestem publiczny czy prywatny
        //boolean bind_flag = true;
        //if (bind_flag) {
        //    addStaticConnection(
        //            new StaticConnection(new InetSocketAddress("localhost", 2137)));
        //}
        ServerSocketChannel socket = null;
        try {
            socket = ServerSocketChannel.open(); // Opens a server-socket channel.
            ServerSocket serverSocket = socket.socket(); // Retrieves a server socket associated with this channel.
            serverSocket.bind(new InetSocketAddress("localhost", 49153));
            //Binds the channel's socket to a local address and configures the socket to listen for connections.
            socket.configureBlocking(false);
            //int ops = socket.validOps(); //Returns an operation set identifying this channel's supported operations. here: SelectionKey.OP_ACCEPT
            socket.register(incomingConnectionOrMessage, SelectionKey.OP_ACCEPT, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run(){
        if(canBind){
            init();
        }
        while(true){
            try {
                incomingConnectionOrMessage.select();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Iterator<SelectionKey> keys = incomingConnectionOrMessage.selectedKeys().iterator();
            while (keys.hasNext()){
                SelectionKey key = keys.next();
                keys.remove();
                //

                if(key.isAcceptable()){ // accept connection
                    addDynamicConnection(key);
                } else if(key.isReadable()){
                    handleRead(key);
                } else if(key.isWritable()){
                        /*
                    SocketChannel channel = (SocketChannel) key.channel();
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    buffer.flip();
                    try {
                        channel.write(buffer);
                        if(buffer.hasRemaining()){
                            buffer.compact();
                        } else {
                            buffer.clear();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                     */
                    key.interestOps(SelectionKey.OP_WRITE);


                }
            }
        }
    }

    private void handleRead(SelectionKey key) {
        // TODO: write msg to queue
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = (ByteBuffer) key.attachment();
        try {
            channel.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        key.interestOps(SelectionKey.OP_WRITE);
        String msg = String.valueOf(StandardCharsets.UTF_8.decode(buffer));
        System.out.println(msg);
    }

    private void addDynamicConnection(SelectionKey key) {
        ServerSocketChannel server = (ServerSocketChannel) key.channel();
        SocketChannel channel = null;
        try {
            // accepts incoming connection and binds it with the selector
            channel = server.accept();
            channel.configureBlocking(false);
            channel.register(this.incomingConnectionOrMessage, SelectionKey.OP_READ, ByteBuffer.allocate(this.someSize));
            incomingConnections.add(new DynamicConnection(channel));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

// ServersocketChannel -> for listening
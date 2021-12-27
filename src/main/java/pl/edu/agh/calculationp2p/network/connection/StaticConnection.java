package pl.edu.agh.calculationp2p.network.connection;

import pl.edu.agh.calculationp2p.message.Message;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class StaticConnection extends ConnectionImpl {

    private final InetSocketAddress ipAddress;
    private Selector selector;
    private int event;

    public StaticConnection(InetSocketAddress ipAddress){
        this.ipAddress = ipAddress;
        try
        {
            socketChannel = SocketChannel.open();
            socketChannel.connect(ipAddress);
            socketChannel.configureBlocking(false);
        } catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public boolean send(Message message) {
        if(!super.send(message))
        {
            reconnect();
            return false;
        }
        return true;
    }

    @Override
    public void register(Selector selector, int event) throws ClosedChannelException {
        this.selector = selector;
        this.event = event;
        super.register(selector, event);
    }

    public void disconnect()
    {
        try {
            socketChannel.finishConnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reconnect(){
        try {
            socketChannel = SocketChannel.open(ipAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socketChannel.register(selector, event);
        } catch (ClosedChannelException e) {
            e.printStackTrace();
        }
    }
}

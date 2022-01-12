package pl.edu.agh.calculationp2p.network.connection;

import pl.edu.agh.calculationp2p.message.Message;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SocketChannel;

public class StaticConnection extends ConnectionImpl {

    private final InetSocketAddress ipAddress;

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
            try {
                super.register(selector);
            } catch (ClosedChannelException ignored) {
            }
            return false;
        }
        return true;
    }

    public void disconnect()
    {
        try {
            socketChannel.finishConnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String[] read() {
        try {
            return super.read();
        } catch (ConnectionLostException e) {
            close();
            reconnect();
            try {
                super.register(selector);
            } catch (ClosedChannelException ignored) {
            }
        }
        return new String[0];
    }

    public void reconnect(){
        try {
            socketChannel = SocketChannel.open(ipAddress);
            socketChannel.configureBlocking(false);
        } catch (IOException e) {
        }
    }
}

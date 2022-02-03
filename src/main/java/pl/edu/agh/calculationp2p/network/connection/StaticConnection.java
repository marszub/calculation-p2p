package pl.edu.agh.calculationp2p.network.connection;

import pl.edu.agh.calculationp2p.message.Message;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SocketChannel;
import java.util.List;

public class StaticConnection extends ConnectionImpl {

    private final InetSocketAddress ipAddress;
    String connectionAddress = "";

    public StaticConnection(InetSocketAddress ipAddress){
        this.ipAddress = ipAddress;
        try
        {
            socketChannel = SocketChannel.open();
            socketChannel.connect(ipAddress);
            socketChannel.configureBlocking(false);
            socketChannel.setOption(StandardSocketOptions.SO_KEEPALIVE, Boolean.TRUE);
            connectionAddress = socketChannel.getRemoteAddress().toString();
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

    @Override
    public String getRemoteAddress() {
        return connectionAddress;
    }

    public void disconnect()
    {
        try {
            socketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void read(List messages) {
        try {
            super.read(messages);
        } catch (ConnectionLostException e) {
            close();
            reconnect();
            try {
                super.register(selector);
            } catch (ClosedChannelException ignored) {
            }
        }
    }

    public void reconnect(){
        try {
            socketChannel = SocketChannel.open(ipAddress);
            socketChannel.configureBlocking(false);
        } catch (IOException e) {
        }
    }
}

package pl.edu.agh.calculationp2p.network.connection;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
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
        Logger logger = LoggerFactory.getLogger(StaticConnection.class);
        logger.debug("Establishing new connection with: " + ipAddress.toString());
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
            Logger logger1 = LoggerFactory.getLogger("");
            logger1.error(e.getMessage());
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
    public String getRemoteAddress() {
        return connectionAddress;
    }

    public void read(List messages) {
        try {
            super.read(messages);
        } catch (ConnectionLostException e) {
            close();
            reconnect();
        }
    }

    public void reconnect(){
        try {
            socketChannel.configureBlocking(false);
            socketChannel = SocketChannel.open(ipAddress);
            try {
                super.register(selector);
            } catch (ClosedChannelException ignored) {
            }
        } catch (IOException e) {
            Logger logger = LoggerFactory.getLogger("");
            logger.error(e.getMessage());
        }
    }
}

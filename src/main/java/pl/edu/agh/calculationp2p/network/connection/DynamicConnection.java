package pl.edu.agh.calculationp2p.network.connection;


import java.io.IOException;
import java.net.StandardSocketOptions;
import java.nio.channels.SocketChannel;


public class DynamicConnection extends ConnectionImpl {
    String connectionAddress = "";

    protected DynamicConnection(SocketChannel socket){
        socketChannel = socket;
        try {
            socketChannel.configureBlocking(false);
            socketChannel.setOption(StandardSocketOptions.SO_KEEPALIVE, Boolean.TRUE);
            connectionAddress = socketChannel.getRemoteAddress().toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getRemoteAddress() {
        return connectionAddress;
    }
}

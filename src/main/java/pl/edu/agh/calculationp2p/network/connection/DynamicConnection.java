package pl.edu.agh.calculationp2p.network.connection;


import java.io.IOException;
import java.nio.channels.SocketChannel;


public class DynamicConnection extends ConnectionImpl {

    protected DynamicConnection(SocketChannel socket){
        socketChannel = socket;
        try {
            socketChannel.configureBlocking(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

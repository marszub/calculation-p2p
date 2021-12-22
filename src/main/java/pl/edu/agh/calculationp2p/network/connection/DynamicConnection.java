package pl.edu.agh.calculationp2p.network.connection;


import java.nio.channels.SocketChannel;


public class DynamicConnection extends ConnectionImpl {

    protected DynamicConnection(SocketChannel socket){
        socketChannel = socket;
    }
}

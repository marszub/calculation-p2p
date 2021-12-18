package pl.edu.agh.calculationp2p.network.connection;


import java.nio.channels.SocketChannel;


public class DynamicConnection extends ConnectionImpl {
    private SocketChannel socket;

    protected DynamicConnection(SocketChannel socket){
        this.socket = socket;
    }
}

package pl.edu.agh.calculationp2p.network.connection;

import pl.edu.agh.calculationp2p.network.message.Message;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;


// Reprezentuje połączenie nawiązane przez inny węzeł.
public class DynamicConnection extends Connection {

    private SocketChannel socket;

    protected DynamicConnection(SocketChannel socket){
        this.socket = socket;
    }


    @Override
    public boolean send(Message message) {
        return false;
    }
}

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

    public boolean send(Message message){
        // TODO : ogarnąć bytebuffer
        //ByteBuffer byteBuffer = new ByteBuffer();
        //try {
        //    socket.write(byteBuffer);
        //    return true;
        //} catch (IOException e) {
        //    e.printStackTrace();
        //    return false;
        //}
        return true;
    }

    @Override
    public void subscribe(Selector selector, int event) {

    }

}

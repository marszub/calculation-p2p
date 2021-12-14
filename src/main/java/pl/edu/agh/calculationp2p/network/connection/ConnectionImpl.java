package pl.edu.agh.calculationp2p.network.connection;


import pl.edu.agh.calculationp2p.message.Message;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;


public abstract class ConnectionImpl implements Connection {
    SocketChannel socketChannel;

    protected void unsafeSend(Message message) throws IOException {
        String data = message.serialize();
        ByteBuffer buff = ByteBuffer.allocate(2048);
        buff.clear();
        buff.put(data.getBytes());
        buff.flip();
        socketChannel.write(buff);
    }

    @Override
    public void subscribe(Selector selector, int event) throws ClosedChannelException {
        socketChannel.register(selector, event, this);
    }

    @Override
    public void close(){

    }

}

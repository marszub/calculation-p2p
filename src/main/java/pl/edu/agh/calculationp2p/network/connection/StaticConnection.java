package pl.edu.agh.calculationp2p.network.connection;

import pl.edu.agh.calculationp2p.message.Message;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class StaticConnection {

    private InetSocketAddress ipAddress;
    private SocketChannel socketChannel;

    public StaticConnection(InetSocketAddress ipAddress) {
        this.ipAddress = ipAddress;
    }


    public boolean send(Message message) {
        try {
            String data = message.getValue();
            ByteBuffer buff = ByteBuffer.allocate(2048);
            buff.clear();
            buff.put(data.getBytes());
            buff.flip();
            ((SocketChannel)this.selectionKey.channel()).write(buff); // call me boss (▀̿Ĺ̯▀̿ ̿)
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public InetSocketAddress getIpAddress() {
        return this.ipAddress;
    }

    public void setSelectionKey(SelectionKey selectionKey) {
        this.selectionKey = selectionKey;
    }
}

package pl.edu.agh.calculationp2p.network.connection;

import pl.edu.agh.calculationp2p.network.message.Message;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class StaticConnection {

    private InetSocketAddress ipAddress;
    private SocketChannel socketChannel;

    public StaticConnection(InetSocketAddress ipAddress) {
        this.ipAddress = ipAddress;
    }




    public boolean send(Message message) {
        // TODO napisz wysyłanie wiadomości ? do kogo kurwa XD do chuja Twego

        try {
            this.socketChannel.write(ByteBuffer.wrap(message.getValue().getBytes(StandardCharsets.UTF_8)));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    protected void reconnect(){
        // TODO Ma możliwość odnowienia połączenia, gdy zostanie urwane.

    }

    public InetSocketAddress getIpAddress(){
        return this.ipAddress;
    }

    public void setSocketChannel(SocketChannel socketChannel){
        this.socketChannel = socketChannel;
    }
}

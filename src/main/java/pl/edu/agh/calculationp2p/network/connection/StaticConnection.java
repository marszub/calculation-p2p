package pl.edu.agh.calculationp2p.network.connection;

import pl.edu.agh.calculationp2p.message.Message;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class StaticConnection extends ConnectionImpl {

    private final InetSocketAddress ipAddress;
    private Selector selector;
    private int event;

    public StaticConnection(InetSocketAddress ipAddress){
        this.ipAddress = ipAddress;
        try {
            socketChannel = SocketChannel.open(ipAddress);
        } catch (IOException e) {
            e.printStackTrace();

            //TODO: make sure that socketChannel is not null
        }
    }

    @Override
    public boolean send(Message message) {
        try {
            unsafeSend(message);
            return true;
        } catch(ClosedChannelException e){
            reconnect();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public void subscribe(Selector selector, int event) throws ClosedChannelException {
        this.selector = selector;
        this.event = event;
        super.subscribe(selector, event);
    }

    public InetSocketAddress getIpAddress() {
        return this.ipAddress;
    }

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }

    private void reconnect(){
        try {
            socketChannel = SocketChannel.open(ipAddress);
        } catch (IOException e) {
            e.printStackTrace();
            //TODO: make sure that socketChannel is not null
        }

        try {
            socketChannel.register(selector, event);
        } catch (ClosedChannelException e) {
            e.printStackTrace();

            //TODO: handle exception
        }
    }
}

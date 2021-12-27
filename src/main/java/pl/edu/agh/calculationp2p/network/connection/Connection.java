package pl.edu.agh.calculationp2p.network.connection;

import pl.edu.agh.calculationp2p.message.Message;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.Selector;

public interface Connection {
    boolean send(Message message);

    void register(Selector selector, int event) throws ClosedChannelException;

    void close();

//    Message read();
    String read() throws ConnectionLostException;
}

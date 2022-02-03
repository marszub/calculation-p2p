package pl.edu.agh.calculationp2p.network.connection;

import pl.edu.agh.calculationp2p.message.Message;

import java.nio.channels.ClosedChannelException;
import java.nio.channels.Selector;
import java.util.List;

public interface Connection {
    boolean send(Message message);

    void register(Selector selector) throws ClosedChannelException;

    void close();

    void read(List<String> messages) throws ConnectionLostException;
}

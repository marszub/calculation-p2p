package pl.edu.agh.calculationp2p.network.router;

import pl.edu.agh.calculationp2p.message.Message;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Optional;

public interface Router {
    void createInterface(int nodeId, InetSocketAddress ipAddress);

    void createInterface(int nodeId);

    void deleteInterface(int nodeId) throws interfaceExistsException;

    List<Message> getMessage();

    void send(Message message);
}

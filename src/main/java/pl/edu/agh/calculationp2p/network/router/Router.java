package pl.edu.agh.calculationp2p.network.router;

import pl.edu.agh.calculationp2p.message.Message;

import java.net.InetSocketAddress;
import java.util.List;

public interface Router {

    boolean isPublic();

    int getMainServerId();

    int getUnknownId();

    int getBroadcastId();

    void createInterface(Integer nodeId);

    void createInterface(Integer nodeId, InetSocketAddress ipAddress);

    void connectToInterface(Integer nodeId, InetSocketAddress ipAddress);

    void deleteInterface(Integer nodeId);

    List<Message> getMessage();

    void send(Message message);

    void setId(int id);

    int getId();

    void close();

    void sendHelloMessage(Message message);

    NodeRegister getNodeRegister();
}

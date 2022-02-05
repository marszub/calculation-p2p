package pl.edu.agh.calculationp2p.network.router;

import pl.edu.agh.calculationp2p.message.Message;
import pl.edu.agh.calculationp2p.network.connection.Connection;

public interface RoutingTable {
    void addInterface(int id);
    void removeInterface(int id);
    void bind(int id, Connection connection);
    void send(int id, Message message);
    boolean trySend(int id, Message message);
    void resendAll();
    boolean interfaceListContains(int id);
}

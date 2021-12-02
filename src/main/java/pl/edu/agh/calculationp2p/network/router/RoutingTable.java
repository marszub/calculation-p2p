package pl.edu.agh.calculationp2p.network.router;

import pl.edu.agh.calculationp2p.message.Message;
import pl.edu.agh.calculationp2p.network.connection.Connection;
import pl.edu.agh.calculationp2p.network.connection.StaticConnection;

public interface RoutingTable {
    public void addInterface(int id);
    public void removeInterface(int id);
    public void bind(int id, Connection connection);
    public void send(int id, Message message);
    public boolean trySend(int id, Message message);
    public void resendAll();
    public boolean interfaceListContains(int id);
}

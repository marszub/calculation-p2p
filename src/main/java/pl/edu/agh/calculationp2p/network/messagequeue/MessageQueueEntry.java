package pl.edu.agh.calculationp2p.network.messagequeue;


import pl.edu.agh.calculationp2p.network.connection.Connection;
import pl.edu.agh.calculationp2p.message.Message;

public interface MessageQueueEntry {
    void add(Message message, Connection connection);
}

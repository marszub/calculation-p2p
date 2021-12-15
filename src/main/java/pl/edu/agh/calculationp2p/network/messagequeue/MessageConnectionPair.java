package pl.edu.agh.calculationp2p.network.messagequeue;

import pl.edu.agh.calculationp2p.message.Message;
import pl.edu.agh.calculationp2p.network.connection.Connection;

public record MessageConnectionPair(Message message, Connection connection) {
}

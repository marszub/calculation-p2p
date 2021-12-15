package pl.edu.agh.calculationp2p.network.messagequeue;



import pl.edu.agh.calculationp2p.network.connection.Connection;
import pl.edu.agh.calculationp2p.message.Message;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class MessageQueue implements MessageQueueEntry, MessageQueueExit {
    // TODO: coo tu ma byc
    Collection<Connection> queue = Collections.synchronizedCollection(new ArrayList<>());

    public MessageQueue(){

    }

    @Override
    public void add(Message msg, Connection connection) {
        //TODO dodaje parę (Message, Connection) do kolejki
        //this.queue.add()
    }

    @Override
    public MessageConnectionPair get() {
        throw new UnsupportedOperationException("Will be implemented");
        //TODO pobiera parę (Message, Connection) z kolejki
    }

}

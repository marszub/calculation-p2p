package pl.edu.agh.calculationp2p.network.connection;
import pl.edu.agh.calculationp2p.message.Message;
import pl.edu.agh.calculationp2p.network.messagequeue.MessageQueue;
import pl.edu.agh.calculationp2p.network.messagequeue.MessageQueueEntry;
import pl.edu.agh.calculationp2p.network.messagequeue.MessageQueueExit;


import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;

class ConnectionManagerTest {

    @Test
    void addStaticConnection() {


    }

    @Test
    void removeStaticConnection() {
    }

    @Test
    void run() {

        /*** test wymaga odpalonej drugiego programu na lokalnej maszynie */
        MessageQueue messageQueue = new MessageQueue();
        MessageQueueEntry messageQueueEntry = messageQueue;
        MessageQueueExit messageQueueExit = messageQueue;

        ConnectionManager connectionManager = new ConnectionManager(messageQueueEntry, false);

        InetSocketAddress inetSocketAddress = new InetSocketAddress("localhost",49153);
        StaticConnection staticConnection = new StaticConnection(inetSocketAddress);

        connectionManager.addStaticConnection(staticConnection);
        connectionManager.start();
        staticConnection.send(new Message("chalo swiat0"));
        staticConnection.send(new Message("chalo swiat1"));
        staticConnection.send(new Message("chalo swiat2"));
        staticConnection.send(new Message("chalo swiat3"));
        staticConnection.send(new Message("chalo swiat4"));



        try {
            Thread.sleep(500000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //messageQueueExit.get();

    }

    @Test
    void run1(){
        MessageQueue messageQueue = new MessageQueue();
        MessageQueueEntry messageQueueEntry = messageQueue;
        MessageQueueExit messageQueueExit = messageQueue;

        ConnectionManager connectionManager = new ConnectionManager(messageQueueEntry, false);

        connectionManager.start();



        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
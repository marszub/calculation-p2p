package pl.edu.agh.calculationp2p.network.connection;

import pl.edu.agh.calculationp2p.network.message.Message;
import pl.edu.agh.calculationp2p.network.messagequeue.MessageQueue;
import pl.edu.agh.calculationp2p.network.messagequeue.MessageQueueEntry;
import pl.edu.agh.calculationp2p.network.messagequeue.MessageQueueExit;

import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) {

        MessageQueue messageQueue1 = new MessageQueue();
        MessageQueueEntry messageQueueEntry1 = messageQueue1;
        MessageQueueExit messageQueueExit1 = messageQueue1;

        ConnectionManager connectionManager1 = new ConnectionManager(messageQueueEntry1, true);

        connectionManager1.start();

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
